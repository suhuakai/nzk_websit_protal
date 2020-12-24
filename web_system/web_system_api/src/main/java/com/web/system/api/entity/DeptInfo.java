package com.web.system.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 科室管理表
 * </p>
 *
 * @author sunhua
 * @since 2020-12-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ts_dept_info")
public class DeptInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 部门id
     */
    @TableId("dept_id")
    private String deptId;

    /**
     * 部门编码
     */
    @TableField("dept_code")
    private String deptCode;

    /**
     * 机构id
     */
    @TableField("org_id")
    private String orgId;

    /**
     * 平台id
     */
    @TableField("platform_id")
    private String platformId;

    /**
     * 状态 1启用 0 停用
     */
    @TableField("fstate")
    private Integer fstate;

    /**
     * 部门简码
     */
    @TableField("dept_fqun")
    private String deptFqun;

    /**
     * 部门名称
     */
    @TableField("dept_name")
    private String deptName;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 备用
     */
    @TableField("type")
    private Integer type;


}
