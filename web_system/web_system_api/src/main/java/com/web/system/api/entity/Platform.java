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
 * 平台信息表
 * </p>
 *
 * @author sunhua
 * @since 2020-12-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ts_platform")
public class Platform implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 平台id
     */
    @TableId("platform_id")
    private String platformId;

    /**
     * 平台名称
     */
    @TableField("name")
    private String name;

    /**
     * 平台地址
     */
    @TableField("url")
    private String url;

    /**
     * 平台类型
     */
    @TableField("platform_type")
    private Integer platformType;

    /**
     * LOGO(图片地址)
     */
    @TableField("logo")
    private String logo;

    /**
     * 背景图(图片地址)
     */
    @TableField("background")
    private String background;

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
     * 更新时间
     */
    @TableField("update_date")
    private LocalDateTime updateDate;

    /**
     * 修改人id
     */
    @TableField("update_user_id")
    private String updateUserId;

    /**
     * 说明
     */
    @TableField("remark")
    private String remark;

    /**
     * 平台编号
     */
    @TableField("platform_no")
    private String platformNo;


}
