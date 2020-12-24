package com.web.system.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 字典信息表
 * </p>
 *
 * @author sunhua
 * @since 2020-12-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ts_static_info")
public class StaticInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 字典分类id
     */
    @TableId("static_id")
    private String staticId;

    /**
     * 机构id
     */
    @TableField("org_id")
    private String orgId;

    /**
     * 字典类型
     */
    @TableField("static_type")
    private String staticType;

    /**
     * 字典分类编码
     */
    @TableField("code")
    private String code;

    /**
     * 字典分类名
     */
    @TableField("name")
    private String name;

    /**
     * 创建人id
     */
    @TableField("create_user_id")
    private String createUserId;

    /**
     * 创建时间
     */
    @TableField("create_date")
    private LocalDateTime createDate;

    /**
     * 修改人id
     */
    @TableField("update_user_id")
    private String updateUserId;

    /**
     * 修改时间
     */
    @TableField("update_date")
    private LocalDateTime updateDate;

    /**
     * 排序
     */
    @TableField("fsort")
    private Integer fsort;

    /**
     * 说明
     */
    @TableField("remark")
    private String remark;


}
