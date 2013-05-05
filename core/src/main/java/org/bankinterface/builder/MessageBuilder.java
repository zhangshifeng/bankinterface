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
package org.bankinterface.builder;

import java.util.Map;

import org.bankinterface.config.BankConfig;

public class MessageBuilder {

    /**
     * 获得签名字符串连接类型
     * 
     * @param joinStyle
     * @return
     */
    public static String getJoinType(String joinStyle) {
        if (BankConfig.JOIN_STYLE_KEY_VALUE_AND.equals(joinStyle)) {
            return BankConfig.JOIN_STYLE_KEY_VALUE;
        } else if (BankConfig.JOIN_STYLE_KEY_VALUE_OR.equals(joinStyle)) {
            return BankConfig.JOIN_STYLE_KEY_VALUE;
        } else if (BankConfig.JOIN_STYLE_VALUE_OR.equals(joinStyle)) {
            return BankConfig.JOIN_STYLE_VALUE;
        } else if (BankConfig.JOIN_STYLE_VALUE_NULL.equals(joinStyle)) {
            return BankConfig.JOIN_STYLE_VALUE;
        } else {
            throw new IllegalArgumentException(joinStyle);
        }
    }

    /**
     * 获得签名字符串连接符
     * 
     * @param joinStyle
     * @return
     */
    public static String getJoinConnector(String joinStyle) {
        if (BankConfig.JOIN_STYLE_KEY_VALUE_AND.equals(joinStyle)) {
            return "&";
        } else if (BankConfig.JOIN_STYLE_KEY_VALUE_OR.equals(joinStyle)) {
            return "|";
        } else if (BankConfig.JOIN_STYLE_VALUE_OR.equals(joinStyle)) {
            return "|";
        } else if (BankConfig.JOIN_STYLE_VALUE_NULL.equals(joinStyle)) {
            return "";
        } else {
            throw new IllegalArgumentException(joinStyle);
        }
    }

    /**
     * 组装签名报文
     * 
     * @param context
     * @param signedField
     * @param joinStyle
     * @return
     */
    public static StringBuilder build(Map<String, String> context, String[] signedField, String joinStyle) {
        StringBuilder sb = new StringBuilder();
        String joinType = getJoinType(joinStyle);
        String joinConnector = getJoinConnector(joinStyle);
        boolean isFirst = true;
        String value;
        for (String key : signedField) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(joinConnector);
            }
            value = context.get(key);
            if (value == null){
                value = "";
            }
            if (BankConfig.JOIN_STYLE_VALUE.equals(joinType)) { // 值连接方式
                sb.append(value);
            } else if (BankConfig.JOIN_STYLE_KEY_VALUE.equals(joinType)) { // 键值对方式
                sb.append(key).append("=").append(value);
            }
        }
        return sb;
    }

}
