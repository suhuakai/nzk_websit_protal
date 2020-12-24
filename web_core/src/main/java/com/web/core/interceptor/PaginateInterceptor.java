package com.web.core.interceptor;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.web.core.entity.Pager;
import com.web.core.exception.ValidationException;
import com.web.core.util.LocalAssert;
import com.web.core.constant.SystemConst;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * mybatis对oracle分页处理公共处理类（持续完善）
 * @author
 * @version 1.0
 */
@Intercepts({
		@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class PaginateInterceptor implements Interceptor {
	private static final Logger logger = LoggerFactory.getLogger(PaginateInterceptor.class);
	private static final ObjectFactory defaultObjectFactory = new DefaultObjectFactory();
	private static final ObjectWrapperFactory defaultObjectWrapperFactory = new DefaultObjectWrapperFactory();
	public static final ReflectorFactory defaultReflectorFactory = new DefaultReflectorFactory();
	/**
	 * 需要拦截的sqlMapperId(正则匹配)
	 */
	private static String targetSqlIdRegex = "com\\.web\\..+";
	/**
	 * 数据库类型
	 */
	private String dbType;

	/**
	 * 前置检查
	 * @throws Exception
	 */
	void preCheck() throws Exception {
		LocalAssert.notBlank(this.dbType, "PaginateInterceptor分页处理器，必须指定数据库类型！如 Mysql、Oracke（不区分大小写）。");
		LocalAssert.isIncludeIgnoreCase(this.dbType, new String[]{"Oracle", "Mysql"}, "当前只支持Mysql、Oracke两种数据库的分页！");
	}

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		//前置检查
		preCheck();

		/*StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
		MetaObject metaObject = MetaObject.forObject(statementHandler, defaultObjectFactory, defaultObjectWrapperFactory, defaultReflectorFactory);
		// 分离代理对象链(由于目标类可能被多个拦截器拦截，从而形成多次代理，通过下面的两次循环可以分离出最原始的的目标类)
		while (metaObject.hasGetter("h")) {
			Object object = metaObject.getValue("h");
			metaObject = MetaObject.forObject(object, defaultObjectFactory, defaultObjectWrapperFactory, defaultReflectorFactory);
		}
		// 分离最后一个代理对象的目标类
		while (metaObject.hasGetter("target")) {
			Object object = metaObject.getValue("target");
			metaObject = MetaObject.forObject(object, defaultObjectFactory, defaultObjectWrapperFactory, defaultReflectorFactory);
		}*/

		StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
		MetaObject metaObject = SystemMetaObject.forObject(statementHandler);

		Configuration configuration = (Configuration) metaObject.getValue("delegate.configuration");
		MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");

		// 只重写需要分页的sql语句。通过MappedStatement的ID匹配，默认重写以Page结尾的MappedStatement的sql
		String maperMethodId = mappedStatement.getId();
		if (maperMethodId.matches(targetSqlIdRegex)) {
			BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
			Object parameterObject = boundSql.getParameterObject();
			// 如果方法有入参，才进行有关分页的分析与处理
			if (parameterObject != null) {
				if (parameterObject instanceof Pager) {
					Pager page = (Pager) parameterObject;
					/** 分页处理 */
					pagitionProcess(invocation, metaObject, boundSql, mappedStatement, page);
				} else if (parameterObject instanceof Map) {
					Map<String, Object> multiParams = (Map<String, Object>) parameterObject;
					/**查询入参集，如果有分页对象，则返回，没有就返回null*/
					Pager page = lookupPagerOfMuitiParams(multiParams);
					/** 分页处理 */
					pagitionProcess(invocation, metaObject, boundSql, mappedStatement, page);
				}
			}
		}
		//将执行权交给下一个拦截器
		return invocation.proceed();
	}

	/**
	 * 分页处理
	 * @param invocation
	 * @param metaStatementHandler
	 * @param boundSql
	 * @param mappedStatement
	 * @param pager
	 * @return void
	 * @throws ValidationException
	 * @throws SQLException
	 */
	private void pagitionProcess(Invocation invocation, MetaObject metaStatementHandler, BoundSql boundSql, MappedStatement mappedStatement, Pager pager)
			throws ValidationException, SQLException {
		/** 如果没有分页信息，则不做分页处理 */
		if (pager == null) {
			return;
		}
		// 如果有分页信息，检查分页必要的属性性：开始行索引与结束行索引
		if (!pager.isDoneEval()) {
			logger.debug("pageNum=" + pager.getPageNum());
			logger.debug("pageSize=" + pager.getPageSize());
			throw new ValidationException("请检查分页信息是否指定“pageNum”及“pageSize”属性值！");
		}
		Map<String, Object> params = pager.getConditiions();
		String sql = boundSql.getSql();
		//将排序中实体字段转成数据库字段
		if (params != null && params.get("orderMark") != null && params.get("orderField") != null) {
			String resultMapName = (String) params.get("resultMapName");
			String orderField = (String) params.get("orderField");

			if (StringUtils.isNotBlank(resultMapName)) {
				Configuration configuration = (Configuration) metaStatementHandler.getValue("delegate.configuration");
				ResultMap resultMap = configuration.getResultMap(resultMapName);
				List<ResultMapping> mapping = resultMap.getResultMappings();
				boolean flag = true;
				for (ResultMapping m : mapping) {
					if (m.getProperty().equals(orderField)) {
						sql = sql.replace("[orderField]", m.getColumn());
						flag = false;
						break;
					}
				}
				if (flag) {
					sql = sql.replace("[orderField]", orderField);
				}
			} else {
				sql = sql.replace("[orderField]", orderField);
			}
		}

		//logger.debug("boundSql.sql={}", sql);
		Connection connection = (Connection) invocation.getArgs()[0];
		/** 设定总记录数  */
		if (pager.isAutoCount()) {
			pager.setCount(getPagerTotal(sql, connection, mappedStatement, boundSql, pager));
			pager.setTotalPage(pager.getTotalPage());//计算总页数
		}
		/** 自动分页  */
		if (pager.isAutoPagable()) {
			// 自动生成分页sql
			if ("Oracle".equalsIgnoreCase(dbType)) {
				sql = buildPageSqlForOracle(sql, pager);
			} else if ("Mysql".equalsIgnoreCase(dbType)) {
				sql = buildPageSqlForMysql(sql, pager);
			}
			// 采用物理分页后，就不需要mybatis的内存分页了，所以重置下面的两个参数
			metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
			metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);
		}
		//处理工作完成以后，更新目标sql语句
		metaStatementHandler.setValue("delegate.boundSql.sql", sql);
	}

	/**
	 * 查询入参集，如果有分页对象，则返回，没有就返回null <br/>
	 * @param multiParams
	 * @return Pager
	 * @throws ValidationException
	 */
	private Pager lookupPagerOfMuitiParams(Map multiParams) throws ValidationException {
		Collection<?> multiParamVals = multiParams.values();
		Pager pager = null;
		for (Object param : multiParamVals) {
			if (param instanceof Pager) {
				pager = (Pager) param;
				break;
			}
		}
		//如果mapper.java的方法入参里没有Pager信息，则查看是否指定起始行与截止行，并封装Pager信息
		if (pager == null) {
			// 开始行索引
			Object originStartIndex = null;
			if (multiParams.containsKey("START_INDEX")) {
				originStartIndex = multiParams.get("START_INDEX");
			}
			// 结束行索引
			Object originEndIndex = null;
			if (multiParams.containsKey("END_INDEX")) {
				originEndIndex = multiParams.get("END_INDEX");
			}

			if (originStartIndex != null && originEndIndex == null) {
				throw new ValidationException("分页参数‘endIndex’必需指定！");
			} else if (originStartIndex == null && originEndIndex != null) {
				throw new ValidationException("分页参数‘startIndex’必需指定！");
			}
			// 如果设置分页参数，‘开始行索引’与‘结束行索引’两个都要有值
			if (originStartIndex != null && originEndIndex != null) {
				pager = new Pager(true, false);
				if (!(originStartIndex instanceof Number) && !(originStartIndex instanceof String)) {
					throw new ValidationException("分页参数‘startIndex’的值类型必需是Integer或者String！");
				} else {
					String originStartIndexStr = originStartIndex.toString();
					if (!originStartIndexStr.matches("\\d+")) {
						throw new ValidationException("分页：“开始行索引”必须是整数！");
					}
					pager.setStartIndex(Integer.parseInt(originStartIndexStr));// startIndex: 开始行索引
				}
				if (!(originEndIndex instanceof Number) && !(originEndIndex instanceof String)) {
					throw new ValidationException("分页参数‘endIndex’的值类型必需是Integer或者String！");
				} else {
					String originEndIndexStr = originEndIndex.toString();
					if (!originEndIndexStr.matches("\\d+")) {
						throw new ValidationException("分页：“结束行索引”必须是整数！");
					}
					pager.setEndIndex(Integer.parseInt(originEndIndexStr));// endIndex: 结束行索引
				}
				pager.setDoneEval(Boolean.TRUE);// 已经计算出“开始行索引”和“结束行索引”
			}
		}
		return pager;
	}

	/**
	 * 从数据库里查询总的记录数并计算总页数，回写进分页参数
	 * @param sql
	 * @param connection
	 * @param mappedStatement
	 * @param boundSql
	 * @param pager
	 * @throws SQLException
	 */
	private int getPagerTotal(String sql, Connection connection, MappedStatement mappedStatement, BoundSql boundSql, Pager pager) throws SQLException {
		//总记录数
		String countSql = "SELECT COUNT(*) FROM (" + sql + ") T_";
		PreparedStatement countStmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			countStmt = connection.prepareStatement(countSql);
			BoundSql countSQL = new BoundSql(mappedStatement.getConfiguration(), countSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
			setParameters(countStmt, mappedStatement, countSQL, boundSql.getParameterObject());
			rs = countStmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);//总记录数量
				logger.debug("查询总记录数量: " + count);
			}
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (countStmt != null) {
					countStmt.close();
				}
			} catch (SQLException e) {
				logger.error("关闭数据库连接资源时错误提示(ResultSet、PreparedStatement)", e);
			}
		}
		return count;
	}

	/**
	 * 对SQL参数(?)设值
	 * @param ps
	 * @param mappedStatement
	 * @param boundSql
	 * @param parameterObject
	 * @throws SQLException
	 */
	private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql, Object parameterObject) throws SQLException {
		ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
		parameterHandler.setParameters(ps);
	}

	/**
	 * 建立分页sql语句（Oracle数据库）
	 * @param sql
	 * @param pager
	 * @return String
	 */
	public String buildPageSqlForOracle(String sql, Pager pager) {
		StringBuilder pageSql = new StringBuilder(100);
		Integer startIndex = pager.getStartIndex();// 开始行索引
		Integer endIndex = pager.getEndIndex();// 结束行索引
		Assert.notNull(startIndex, "必需指定‘开始行索引’！");
		Assert.notNull(endIndex, "必需指定‘结束行索引’！");

		switch (pager.getPaginateMode()) {
			case SystemConst.PaginateMode.MODE_1:
				pageSql.append("SELECT TTT_.* FROM ( SELECT TT_.*, ROWNUM RN FROM(");
				pageSql.append(sql);
				pageSql.append(") TT_ WHERE ROWNUM <= ").append(endIndex)
						.append(") TTT_ WHERE RN >= ").append(startIndex);
				break;
			default:
				pageSql.append("SELECT TTT_.* FROM ( SELECT TT_.*, ROWNUM RN FROM(");
				pageSql.append(sql);
				pageSql.append(") TT_ ) TTT_ WHERE RN >= ").append(startIndex);
				pageSql.append(" AND RN <= ").append(endIndex);
				break;
		}
		return pageSql.toString();
	}

	/**
	 * 建立分页sql语句（Mysql数据库）
	 * @param sql
	 * @param pager
	 * @return String
	 */
	public String buildPageSqlForMysql(String sql, Pager pager) {
		StringBuilder pageSql = new StringBuilder(100);
		Integer startIndex = pager.getStartIndex();// 开始行索引
		Integer endIndex = pager.getEndIndex();// 结束行索引
		Assert.notNull(startIndex, "必需指定‘开始行索引’！");
		Assert.notNull(endIndex, "必需指定‘结束行索引’！");
		pageSql.append(sql).append(" limit ").append(pager.getStartIndex() - 1).append(", ").append(pager.getPageSize());
		return pageSql.toString();
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	@Override
	public Object plugin(Object target) {
		/**
		 * 当目标类是StatementHandler类型时，
		 * 才包装目标类，
		 * 否者直接返回目标本身,减少目标被代理的次数
		 * */
		if (target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	@Override
	public void setProperties(Properties properties) {
	}
}