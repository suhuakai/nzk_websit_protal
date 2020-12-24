package com.web.system.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 机构信息表
 * </p>
 *
 * @author sunhua
 * @since 2020-12-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ts_org_info")
public class OrgInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 机构id
     */
    @TableId("org_id")
    private String orgId;

    /**
     * 机构名称
     */
    @TableField("org_name")
    private String orgName;

    /**
     * 机构简称（中文简码）
     */
    @TableField("org_fqun")
    private String orgFqun;

    /**
     * 机构编号
     */
    @TableField("org_no")
    private Integer orgNo;

    /**
     * 机构别名
     */
    @TableField("org_alias")
    private String orgAlias;

    /**
     * 信用代码(组织机构代码)
     */
    @TableField("credit_code")
    private String creditCode;

    /**
     * 联系人姓名（法人）
     */
    @TableField("contact_name")
    private String contactName;

    /**
     * 联系人手机号
     */
    @TableField("contact_mobile")
    private String contactMobile;

    /**
     * 省
     */
    @TableField("province")
    private String province;

    /**
     * 市
     */
    @TableField("city")
    private String city;

    /**
     * 区
     */
    @TableField("district")
    private String district;

    /**
     * 住所（公司地址）
     */
    @TableField("comp_residence")
    private String compResidence;

    /**
     * 详细地址


     */
    @TableField("tf_address")
    private String tfAddress;

    /**
     * 开户银行
     */
    @TableField("deposit_bank_addr")
    private String depositBankAddr;

    /**
     * 对公银行账号
     */
    @TableField("deposit_bank_account")
    private String depositBankAccount;

    /**
     * 注册资本
     */
    @TableField("registered_capital")
    private BigDecimal registeredCapital;

    /**
     * 注册资本币种
     */
    @TableField("registered_capital_currency")
    private String registeredCapitalCurrency;

    /**
     * 企业类型（编码）
     */
    @TableField("company_type_code")
    private String companyTypeCode;

    /**
     * 企业类型（名称）
     */
    @TableField("company_type_name")
    private String companyTypeName;

    /**
     * 是否为三证合一 0否 1是
     */
    @TableField("is_cert_type")
    private String isCertType;

    /**
     * 成立日期
     */
    @TableField("establish_date")
    private LocalDate establishDate;

    /**
     * 营业期限（截止）
     */
    @TableField("business_date")
    private LocalDate businessDate;

    /**
     * 营业执照（图片地址）
     */
    @TableField("business_licence")
    private String businessLicence;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    @TableField("status_value")
    private String statusValue;

    @TableField("create_time")
    private LocalDateTime createTime;


}
