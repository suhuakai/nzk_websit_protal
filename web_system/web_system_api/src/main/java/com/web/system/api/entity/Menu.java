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
 * 菜单表
 * </p>
 *
 * @author sunhua
 * @since 2020-12-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ts_menu")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单编码（唯一）
     */
    @TableId("menu_code")
    private String menuCode;

    /**
     * 上级菜单编码
     */
    @TableField("parent_code")
    private String parentCode;

    /**
     * 菜单层级
1 一级菜单 
2 二级菜单
     */
    @TableField("level")
    private Integer level;

    /**
     * 菜单名称
     */
    @TableField("menu_name")
    private String menuName;

    /**
     * 路径
     */
    @TableField("url")
    private String url;

    /**
     * 排序
     */
    @TableField("fsort")
    private Integer fsort;

    /**
     * 状态
1 启用
0 停用
     */
    @TableField("fstate")
    private Integer fstate;

    /**
     * 说明
     */
    @TableField("remark")
    private String remark;


}
