package com.web.common.utils;

import com.web.core.util.LocalAssert;

/**
 * 单据类型相关适配工具
 * @author
 * @version 1.0
 */
public class BillAdapter {

    /**
     * 订单号 规则适配
     * @param type 单据类型
     * @return String
     * @author
     *      */
    public static String getOrderNoRule(String type) {
        LocalAssert.notBlank(type, "单据类型，不能为空");
        return "";
             /*   LocalStringUtils.caseWhen(type,
             //订单类型
             CustomConst.OrderType.ORDER, CustomConst.EncodeRuleChildType.PO_ORDER_BILL_CODE,
             CustomConst.OrderType.HIGH_ORDER, CustomConst.EncodeRuleChildType.GO_ORDER_BILL_CODE,
             CustomConst.OrderType.SETTLE_ORDER, CustomConst.EncodeRuleChildType.JO_ORDER_BILL_CODE,
             CustomConst.OrderType.OPER_ORDER, CustomConst.EncodeRuleChildType.SO_ORDER_BILL_CODE,
             CustomConst.OrderType.BACK_ORDER, CustomConst.EncodeRuleChildType.TO_ORDER_BILL_CODE
        );*/
    }

    /**
     * 送货单号 规则适配
     * @param type 单据类型
     * @return String
     * @author
     *      */
    public static String getDeliveryNoRule(String type) {
        LocalAssert.notBlank(type, "单据类型，不能为空");
        return "";
             /*   LocalStringUtils.caseWhen(type,
             //送货单类型
             CustomConst.DeliveryType.DELIVERY, CustomConst.EncodeRuleChildType.PD_SEND_BILL_CODE,
             CustomConst.DeliveryType.HIGH_DELIVERY, CustomConst.EncodeRuleChildType.GD_SEND_BILL_CODE,
             CustomConst.DeliveryType.SETTLE_DELIVERY, CustomConst.EncodeRuleChildType.JD_SEND_BILL_CODE,
             CustomConst.DeliveryType.OPER_DELIVERY, CustomConst.EncodeRuleChildType.SD_SEND_BILL_CODE,
             CustomConst.DeliveryType.GIVE_DELIVERY, CustomConst.EncodeRuleChildType.ZD_SEND_BILL_CODE,
             CustomConst.DeliveryType.BACK_DELIVERY, CustomConst.EncodeRuleChildType.TD_SEND_BILL_CODE
        );*/
    }

    /**
     * 计划单号 规则适配
     * @param type 单据类型
     * @return String
     * @author
     *      */
    public static String getPlanNoRule(String type) {
        LocalAssert.notBlank(type, "单据类型，不能为空");
        return "";
          /*      LocalStringUtils.caseWhen(type,
             //计划单类型
             CustomConst.PlanType.PLAN, CustomConst.EncodeRuleChildType.PP_PLAN_BILL_CODE,
             CustomConst.PlanType.HIGH_PLAN, CustomConst.EncodeRuleChildType.GP_PLAN_BILL_CODE,
             CustomConst.PlanType.SETTLE_PLAN, CustomConst.EncodeRuleChildType.JP_PLAN_BILL_CODE,
             CustomConst.PlanType.OPER_PLAN, CustomConst.EncodeRuleChildType.SP_PLAN_BILL_CODE
        );*/
    }

    /**
     * 汇总单号 规则适配
     * @param type 单据类型
     * @return String
     * @author
     *      */
    public static String getGatherNoRule(String type) {
        LocalAssert.notBlank(type, "单据类型，不能为空");
        return "";
             /*   LocalStringUtils.caseWhen(type,
             //汇总单类型
             CustomConst.GatherType.GATHER, CustomConst.EncodeRuleChildType.PG_GATHER_BILL_CODE,
             CustomConst.GatherType.HIGH_GATHER, CustomConst.EncodeRuleChildType.GG_GATHER_BILL_CODE,
             CustomConst.GatherType.SETTLE_GATHER, CustomConst.EncodeRuleChildType.JG_GATHER_BILL_CODE
        );*/
    }

    /**
     * 申请单号 规则适配
     * @param type 单据类型
     * @return String
     * @author
     *      */
   /* public static String getApplyNoRule(String type) {
        LocalAssert.notBlank(type, "单据类型，不能为空");
        return LocalStringUtils.caseWhen(type,
             //申请单类型
            *//* CustomConst.ApplyType.APPLY, CustomConst.EncodeRuleChildType.PA_APPLY_BILL_CODE,
             CustomConst.ApplyType.HIGH_APPLY, CustomConst.EncodeRuleChildType.GA_APPLY_BILL_CODE,
             CustomConst.ApplyType.OPER_APPLY, CustomConst.EncodeRuleChildType.SA_APPLY_BILL_CODE*//*
        );
    }*/

}
