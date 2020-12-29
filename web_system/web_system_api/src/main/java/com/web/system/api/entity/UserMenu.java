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
 * @since 2020-12-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ts_user_menu")
public class UserMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId("user_id")
    private String userId;

    /**
     * 菜单id
     */
    @TableField("menu_id")
    private String menuId;

    /**
     * 创建时间
     */
    @TableField("create_date")
    private LocalDateTime createDate;

    /**
     * 创建人
     */
    @TableField("create_user_name")
    private String createUserName;


}
