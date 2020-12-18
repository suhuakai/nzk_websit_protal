package com.web.system.api.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author sunhua
 * @since 2020-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ts_user_info")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主id
     */
    @TableId("user_id")
    private String userId;

    /**
     * 电话
     */
    @TableField("phone")
    private String phone;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 用户名
     */
    @TableField("name")
    private String name;

    /**
     * 账户
     */
    @TableField("login_no")
    private String loginNo;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 机构id
     */
    @TableField("organization_id")
    private String organizationId;

    /**
     * 部门id
     */
    @TableField("dept_id")
    private String deptId;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 创建时间
     */
    @TableField("create_date")
    private LocalDateTime createDate;

    /**
     * 创建人id
     */
    @TableField("create_user_id")
    private String createUserId;

    /**
     * 是否停用 0停用  1启用
     */
    @TableField("fstate")
    private Integer fstate;

    /**
     * 是否管理员 0否 1是
     */
    @TableField("status")
    private Integer status;

    /**
     * 头像
     */
    @TableField("head_url")
    private String headUrl;


}
