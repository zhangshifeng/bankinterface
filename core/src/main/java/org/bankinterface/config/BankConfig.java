/*
 * Copyright 2013 bankinterface.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bankinterface.config;

import java.util.Map;

import org.bankinterface.enums.ServiceType;

/**
 * 支付机构配置接口 根据请求类型,获取相应支付机构配置信息.
 * 包括报文定义,验证规则,转换规则,签名规则等
 * 
 */
public interface BankConfig {

    // 配置常量
    // 验证方法名
    /**
     * 非空验证,判断一个对象不为空
     */
    public static final String VALIDATOR_NOT_NULL             = "NOT_NULL";

    /**
     * 等于验证,判断一个对象等于另一个对象
     */
    public static final String VALIDATOR_EQUALS               = "EQUALS";

    // 转换方法名
    /**
     * 替换方法,用入参中的值替换默认的值
     */
    public static final String CONVERTER_REPLACE              = "REPLACE";

    /**
     * 金额格式化,以分为单位,精确到分,之后四舍五入
     */
    public static final String CONVERTER_AMOUNT_TO_FEN        = "AMOUNT_TO_FEN";

    /**
     * 金额格式化,以元为单位,精确到分,之后四舍五入
     */
    public static final String CONVERTER_AMOUNT_TO_YUAN       = "AMOUNT_TO_YUAN";

    /**
     * 时间格式化,依据给定的模板进行转换
     */
    public static final String CONVERTER_DATE_TO_STRING       = "DATE_TO_STRING";

    /**
     * 字符串到时间类型转换,依据给定的模板进行转换
     */
    public static final String CONVERTER_STRING_TO_DATE       = "STRING_TO_DATE";

    /**
     * 字符串到金额类型转换,以分为单位,精确到分,之后四舍五入
     */
    public static final String CONVERTER_FEN_TO_AMOUNT        = "FEN_TO_AMOUNT";

    /**
     * 字符串到金额类型转换,以元为单位,精确到分,之后四舍五入
     */
    public static final String CONVERTER_YUAN_TO_AMOUNT       = "YUAN_TO_AMOUNT";

    /**
     * 订单状态转换,转换支付订单状态
     */
    public static final String CONVERTER_PAYMENT_ORDER_STATUS = "PAYMENT_ORDER_STATUS";

    /**
     * 订单状态转换,转换查询订单状态
     */
    public static final String CONVERTER_QUERY_ORDER_STATUS   = "QUERY_ORDER_STATUS";

    /**
     * 订单状态转换,转换退款订单状态
     */
    public static final String CONVERTER_REFUND_ORDER_STATUS  = "REFUND_ORDER_STATUS";

    /**
     * 交易类型转换,转换查询时的交易类型
     */
    public static final String CONVERTER_TRANSACTION_TYPE     = "TRANSACTION_TYPE";

    // 组装报文风格
    /**
     * 键值对报文风格类型
     */
    public static final String JOIN_STYLE_KEY_VALUE           = "KEY_VALUE";

    /**
     * 值连接报文风格类型
     */
    public static final String JOIN_STYLE_VALUE               = "VALUE";

    /**
     * 键值对报文风格类型,以&作为连接符
     */
    public static final String JOIN_STYLE_KEY_VALUE_AND       = "KEY_VALUE_AND";

    /**
     * 键值对报文风格类型,以|作为连接符
     */
    public static final String JOIN_STYLE_KEY_VALUE_OR        = "KEY_VALUE_OR";

    /**
     * 值连接报文风格类型,以|作为连接符
     */
    public static final String JOIN_STYLE_VALUE_OR            = "VALUE_OR";

    /**
     * 值连接报文风格类型,以空字符作为连接符
     */
    public static final String JOIN_STYLE_VALUE_NULL          = "VALUE_NULL";

    /**
     * 获得请求参数的默认值
     * 
     * @param type
     * @return
     */
    public Map<String, String> getParameterValue(String type);

    /**
     * 获得报文/结果参数名和默认值
     * 
     * @param type
     * @return
     */
    public Map<String, String> getDefaultValue(String type);

    /**
     * 获得映射关系
     * 
     * @param type
     * @return
     */
    public Map<String, String> getMapping(String type);

    /**
     * 获得验证方式
     * 
     * @param type
     * @return
     */
    public Map<String, String> getValidator(String type);

    /**
     * 获得转换方式
     * 
     * @param type
     * @return
     */
    public Map<String, String> getConverter(String type);

    /**
     * 获得转换模板
     * 
     * @param type
     * @return
     */
    public Map<String, String> getTemplate(String type);

    /**
     * 获得签名或验签服务
     * @param type
     * @return
     */
    public String getSignerOrVerifier(String type);

    /**
     * 获得被签名的参数名称
     * 
     * @param type
     * @return
     */
    public String[] getSignedField(String type);

    /**
     * 获得组装报文风格
     * 
     * @param type
     * @return
     */
    public String getJoinStyle(String type);

    /**
     * 获得存放签名的参数名称
     * 
     * @param type
     * @return
     */
    public String getSignatureKey(String type);

    /**
     * 根据类型获得配置项标识
     * 
     * @param serviceType
     * @param isIn true获得结果配置项标识,false获得请求配置项标识
     * @return
     */
    public String getConfigType(ServiceType serviceType, boolean isIn);

}
