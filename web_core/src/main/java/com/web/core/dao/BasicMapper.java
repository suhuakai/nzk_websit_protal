package com.web.core.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.type.JdbcType;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 数据库与实体对象（ORM）基本操作
 * @author
 * @version 1.0
 */
public interface BasicMapper {

	/**
	 * 单表:插入操作
	 * @param tableName 表名
	 * @param columnNames 字段名列表
	 * @param valueMappings 属性值映射信息列表
	 * @return int          插入记录数量
	 * @author
	 * 	 */
	int insert(@Param("tableName") String tableName,
               @Param("columnNames") List<String> columnNames,
               @Param("valueMappings") List<FieldMapping> valueMappings,
               @Param("excludeFieldNames") String... excludeFieldNames);

	/**
	 * 单表:更新操作
	 * @param tableName 表名
	 * @param valueMappings 属性值映射信息列表
	 * @param conditions 条件列表
	 * @return int                    更新记录数量
	 * @author
	 * 	 */
	int update(@Param("tableName") String tableName,
               @Param("valueMappings") List<FieldMapping> valueMappings,
               @Param("conditions") List<FieldMapping> conditions,
               @Param("excludeFieldNames") String... excludeFieldNames);

	/**
	 * 单表:删除操作
	 * @param tableName 表名
	 * @param conditions 条件列表
	 * @return int                    删除记录数量
	 * @author
	 * 	 */
	int delete(@Param("tableName") String tableName, @Param("conditions") List<FieldMapping> conditions);

	/**
	 * 单表:查询操作
	 * @param tableName 表名
	 * @param columnNames 字段名列表
	 * @param conditions 条件列表
	 * @return List
	 * @author
	 * 	 */
	List<Map<String, Object>> query(@Param("tableName") String tableName,
                                    @Param("columnNames") List<String> columnNames,
                                    @Param("conditions") List<FieldMapping> conditions);

	/**
	 * 实体对象（ORM）元信息
	 * @author
	 * @author
	 * @version 1.0
	 * 	 */
	class EntityMeta implements Serializable {
		/**
		 * 对象类型
		 */
		private Class<?> entityClazz;
		/**
		 * 对象类名
		 */
		private String entityName;
		/**
		 * 表名
		 */
		private String tableName = null;
		/**
		 * 对象关系映射标识（id）
		 */
		private String resultMapId = null;
		/**
		 * 对象关系映射信息
		 */
		private ResultMap resultMap = null;
		/**
		 * 字段属性映射列表
		 */
		private List<ResultMapping> resultMappings = null;
		/**
		 * 主键属性映射列表
		 */
		private List<ResultMapping> idResultMappings = null;

		public Class<?> getEntityClazz() {
			return entityClazz;
		}

		public void setEntityClazz(Class<?> entityClazz) {
			this.entityClazz = entityClazz;
		}

		public String getEntityName() {
			return entityName;
		}

		public void setEntityName(String entityName) {
			this.entityName = entityName;
		}

		public String getTableName() {
			return tableName;
		}

		public void setTableName(String tableName) {
			this.tableName = tableName;
		}

		public String getResultMapId() {
			return resultMapId;
		}

		public void setResultMapId(String resultMapId) {
			this.resultMapId = resultMapId;
		}

		public ResultMap getResultMap() {
			return resultMap;
		}

		public void setResultMap(ResultMap resultMap) {
			this.resultMap = resultMap;
		}

		public List<ResultMapping> getResultMappings() {
			return resultMappings;
		}

		public void setResultMappings(List<ResultMapping> resultMappings) {
			this.resultMappings = resultMappings;
		}

		public List<ResultMapping> getIdResultMappings() {
			return idResultMappings;
		}

		public void setIdResultMappings(List<ResultMapping> idResultMappings) {
			this.idResultMappings = idResultMappings;
		}

	}

	/**
	 * 字段属性映射信息
	 * @author
	 * @version 1.0
	 */
	class FieldMapping {
		private String column;
		private String property;
		private Object value;
		private Class<?> javaType;
		private JdbcType jdbcType;

		private String jdbcTypeName;

		public String getColumn() {
			return column;
		}

		public void setColumn(String column) {
			this.column = column;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

		public String getProperty() {
			return property;
		}

		public void setProperty(String property) {
			this.property = property;
		}

		public Class<?> getJavaType() {
			return javaType;
		}

		public void setJavaType(Class<?> javaType) {
			this.javaType = javaType;
		}

		public JdbcType getJdbcType() {
			return jdbcType;
		}

		public void setJdbcType(JdbcType jdbcType) {
			this.jdbcType = jdbcType;
		}

		public String getJdbcTypeName() {
			return jdbcTypeName;
		}

		public void setJdbcTypeName(String jdbcTypeName) {
			this.jdbcTypeName = jdbcTypeName;
		}
	}

	/**
	 * Insert操作元信息
	 * @author
	 * 	 */
	class InsertMeta implements Serializable {
		/**
		 * 表名
		 */
		private String tableName;
		/**
		 * 字段名列表
		 */
		private List<String> columnNames;
		/**
		 * 字段值列表
		 */
		private List<FieldMapping> valueMappings;

		public InsertMeta(String tableName, List<String> columnNames, List<FieldMapping> valueMappings) {
			this.tableName = tableName;
			this.columnNames = columnNames;
			this.valueMappings = valueMappings;
		}

		public String getTableName() {
			return tableName;
		}

		public List<String> getColumnNames() {
			return columnNames;
		}

		public List<FieldMapping> getValueMappings() {
			return valueMappings;
		}
	}

	/**
	 * Merge操作元信息
	 * @author
	 * 	 */
	class MergeMeta implements Serializable {
		/**
		 * 表名
		 */
		private String tableName;
		/**
		 * 字段值列表
		 */
		private List<FieldMapping> valueMappings;
		/**
		 * 主健字段列表
		 */
		private List<String> idColumnNames;
		/**
		 * 更新的字段列表
		 */
		private List<String> updateColumnNames;

		public MergeMeta(String tableName, List<FieldMapping> valueMappings, List<String> idColumnNames, List<String> updateColumnNames) {
			this.tableName = tableName;
			this.idColumnNames = idColumnNames;
			this.valueMappings = valueMappings;
			this.updateColumnNames = updateColumnNames;
		}

		public String getTableName() {
			return tableName;
		}

		public List<FieldMapping> getValueMappings() {
			return valueMappings;
		}

		public List<String> getIdColumnNames() {
			return idColumnNames;
		}

		public List<String> getUpdateColumnNames() {
			return updateColumnNames;
		}
	}

}
