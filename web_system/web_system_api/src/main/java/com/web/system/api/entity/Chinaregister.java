package com.web.system.api.entity;

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
 * @since 2020-12-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Chinaregister implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("id")
    private Integer id;

    /**
     * 注册证编号
     */
    @TableField("rno")
    private String rno;

    /**
     * 注册人名称
     */
    @TableField("rname")
    private String rname;

    /**
     * 注册人住所
     */
    @TableField("raddress")
    private String raddress;

    /**
     * 生产地址
     */
    @TableField("paddress")
    private String paddress;

    /**
     * 代理人名称
     */
    @TableField("aname")
    private String aname;

    /**
     * 代理人住所
     */
    @TableField("aaddress")
    private String aaddress;

    /**
     * 产品名称
     */
    @TableField("mname")
    private String mname;

    /**
     * 型号、规格
     */
    @TableField("fmsp")
    private String fmsp;

    /**
     * 结构及组成
     */
    @TableField("structure")
    private String structure;

    /**
     * 适用范围
     */
    @TableField("srange")
    private String srange;

    /**
     * 其他内容
     */
    @TableField("others")
    private String others;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 批准日期
     */
    @TableField("pdate")
    private String pdate;

    /**
     * 有效期至
     */
    @TableField("udate")
    private String udate;

    /**
     * 附件
     */
    @TableField("attach")
    private String attach;

    /**
     * 产品标准
     */
    @TableField("mstandard")
    private String mstandard;

    /**
     * 变更日期
     */
    @TableField("mdate")
    private String mdate;

    /**
     * 邮编
     */
    @TableField("postno")
    private String postno;

    /**
     * 主要组成成分（体外诊断试剂）
     */
    @TableField("maincomps")
    private String maincomps;

    /**
     * 预期用途（体外诊断试剂）
     */
    @TableField("puse")
    private String puse;

    /**
     * 产品储存条件及有效期（体外诊断试剂）
     */
    @TableField("storagecon")
    private String storagecon;

    /**
     * 审批部门
     */
    @TableField("pdept")
    private String pdept;

    /**
     * 变更情况
     */
    @TableField("mcontents")
    private String mcontents;


}
