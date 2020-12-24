package com.web.common.InterfaceLog.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @ClassName InterfaceLog
 * @DescriPtion TODO
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tl_interface_log")
public class InterfaceLog {


	/**
	 * 主键
	 */
	@TableId("id")
	private String id;

	/**
	 * 创建时间
	 */
	@TableId("create_date")
	private Date createDate;

	/**
	 * 创建人
	 */
	@TableId("create_user_id")
	private String createUserId;

	/**
	 * 更新时间
	 */
	@TableId("update_date")
	private Date updateDate;

	/**
	 * 更新人
	 */
	@TableId("update_user_id")
	private String updateUserId;

	/**
	 * 删除标识
	 */
	@TableId("del_flag")
	private Integer delFlag;

	/**
	 * 接口请求时间
	 */
	@TableId("request_time")
	private Date requestTime;
	/**
	 * 请求方法
	 */
	@TableId("request_method")
	private String requestMethod;
	/**
	 * 接口请求参数
	 */
	@TableId("request_param")
	private String requestParam;
	/**
	 * 接口请求状态
	 */
	@TableId("result_code")
	private String resultCode;
	/**
	 * 接口请求结果内容
	 */
	@TableId("result_content")
	private String resultContent;
	/**
	 * 异常信息
	 */
	@TableId("exception")
	private String exception;
	/**
	 *是否处理 0--已处理 1--未处理
	 */
	@TableId("is_handle")
	private Integer isHandle;
}
