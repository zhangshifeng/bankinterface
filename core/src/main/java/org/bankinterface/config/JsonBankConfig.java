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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bankinterface.enums.ServiceType;
import org.bankinterface.exception.ConfigException;
import org.bankinterface.util.KeyStoreUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JSON格式报文配置
 * 
 */
public class JsonBankConfig implements BankConfig {
    // 配置常量
    /**
     * 证书信息配置区域
     */
    public static final String CONFIG_KEY_STORE_INFO        = "KeyStoreInfo";

    /**
     * 证书存放路径
     */
    public static final String CONFIG_KEY_STORE_URL         = "url";

    /**
     * 证书类型
     */
    public static final String CONFIG_KEY_STORE_TYPE        = "type";

    /**
     * 证书密码
     */
    public static final String CONFIG_KEY_STORE_PASSWORD    = "password";

    /**
     * 是否是HTTPS客户端证书
     */
    public static final String CONFIG_KEY_STORE_CERT        = "iscertstore";

    /**
     * 是否是授信证书
     */
    public static final String CONFIG_KEY_STORE_TRUST       = "istruststore";

    /**
     * 是否是签名验签证书
     */
    public static final String CONFIG_KEY_STORE_SIGN_VERIFY = "issignverifystore";

    /**
     * 入参默认值区域
     */
    public static final String CONFIG_PARAMETER             = "parameter";

    /**
     * 报文默认值区域
     */
    public static final String CONFIG_DEFAULT               = "default";

    /**
     * 报文入参映射区域
     */
    public static final String CONFIG_MAPPING               = "mapping";

    /**
     * 验证方法定义区域
     * 
     * @see BankConfig#VALIDATOR_NOT_NULL
     * @see BankConfig#VALIDATOR_EQUALS
     */
    public static final String CONFIG_VALIDATOR             = "validator";

    /**
     * 转换方法定义区域
     * 
     * @see BankConfig#CONVERTER_AMOUNT_TO_FEN
     * @see BankConfig#CONVERTER_AMOUNT_TO_YUAN
     * @see BankConfig#CONVERTER_DATE_TO_STRING
     * @see BankConfig#CONVERTER_FEN_TO_AMOUNT
     * @see BankConfig#CONVERTER_PAYMENT_ORDER_STATUS
     * @see BankConfig#CONVERTER_QUERY_ORDER_STATUS
     * @see BankConfig#CONVERTER_REFUND_ORDER_STATUS
     * @see BankConfig#CONVERTER_REPLACE
     * @see BankConfig#CONVERTER_STRING_TO_DATE
     * @see BankConfig#CONVERTER_TRANSACTION_TYPE
     * @see BankConfig#CONVERTER_YUAN_TO_AMOUNT
     */
    public static final String CONFIG_CONVERTER             = "converter";

    /**
     * 转换方法模板定义区域
     */
    public static final String CONFIG_TEMPLATE              = "template";

    /**
     * 签名或验签服务名称
     */
    public static final String CONFIG_SIGNER_OR_VERIFIER    = "signerorverifier";

    /**
     * 称被签名的参数名称
     */
    public static final String CONFIG_SIGNED_FIELD          = "signedfield";

    /**
     * 报文组装风格
     * 
     * @see BankConfig#JOIN_STYLE_KEY_VALUE_AND
     * @see BankConfig#JOIN_STYLE_KEY_VALUE_OR
     * @see BankConfig#JOIN_STYLE_VALUE_OR
     * @see BankConfig#JOIN_STYLE_VALUE_NULL
     */
    public static final String CONFIG_JOIN_STYLE            = "joinstyle";

    /**
     * 存放签名的参数名
     */
    public static final String CONFIG_SIGNATURE_KEY         = "signaturekey";

    private static final Logger log = LoggerFactory.getLogger(JsonBankConfig.class);
    private static final Map<ServiceType, String> IN_TYPE = new HashMap<ServiceType, String>();
    private static final Map<ServiceType, String> OUT_TYPE = new HashMap<ServiceType, String>();

    static {
        // 请求配置项标识映射
        OUT_TYPE.put(ServiceType.ASYNC_PAY, "AsyncPayParam");
        OUT_TYPE.put(ServiceType.AUTH_CODE, "AuthCodeParam");
        OUT_TYPE.put(ServiceType.BATCH_QUERY, "QueryParam");
        OUT_TYPE.put(ServiceType.BATCH_REFUND, "RefundParam");
        OUT_TYPE.put(ServiceType.QUERY, "QueryParam");
        OUT_TYPE.put(ServiceType.REFUND, "RefundParam");
        OUT_TYPE.put(ServiceType.SYNC_PAY, "SyncPayParam");

        // 结果配置项标识映射
        IN_TYPE.put(ServiceType.AUTH_CODE, "AuthCodeResult");
        IN_TYPE.put(ServiceType.BATCH_QUERY, "QueryResult");
        IN_TYPE.put(ServiceType.BATCH_REFUND, "RefundResult");
        IN_TYPE.put(ServiceType.QUERY, "QueryResult");
        IN_TYPE.put(ServiceType.REFUND, "RefundResult");
        IN_TYPE.put(ServiceType.SYNC_PAY, "SyncPayResult");

        // 通知配置项标识映射
        IN_TYPE.put(ServiceType.ASYNC_PAY_NOTIFICATION, "AsyncPayResult");
        IN_TYPE.put(ServiceType.SYNC_PAY_NOTIFICATION, "SyncPayResult");
        IN_TYPE.put(ServiceType.REFUND_NOTIFICATION, "RefundResult");
    }

    // 配置缓存
    private final Map<String, Map<String, String>> configValueMap = new HashMap<String, Map<String, String>>();
    private final Map<String, String[]> configValueArray = new HashMap<String, String[]>();
    private final Map<String, String> configValueString = new HashMap<String, String>();
    private final String configName;
    private JSONObject config;

    public JsonBankConfig(String configName) throws ConfigException {
        this.configName = configName;
        init();
    }

    public Map<String, String> getParameterValue(String type) {
        return configValueToMap(type, CONFIG_PARAMETER, true);
    }

    public Map<String, String> getDefaultValue(String type) {
        return configValueToMap(type, CONFIG_DEFAULT, true);
    }

    public Map<String, String> getMapping(String type) {
        return configValueToMap(type, CONFIG_MAPPING, true);
    }

    public Map<String, String> getValidator(String type) {
        return configValueToMap(type, CONFIG_VALIDATOR, true);
    }

    public Map<String, String> getConverter(String type) {
        return configValueToMap(type, CONFIG_CONVERTER, true);
    }

    public Map<String, String> getTemplate(String type) {
        return configValueToMap(type, CONFIG_TEMPLATE, true);
    }

    public String getSignerOrVerifier(String type) {
        return configValueToString(type, CONFIG_SIGNER_OR_VERIFIER, true);
    }

    public String[] getSignedField(String type) {
        return configValueToArray(type, CONFIG_SIGNED_FIELD, true);
    }

    public String getJoinStyle(String type) {
        return configValueToString(type, CONFIG_JOIN_STYLE, true);
    }

    public String getSignatureKey(String type) {
        return configValueToString(type, CONFIG_SIGNATURE_KEY, true);
    }

    public String getConfigType(ServiceType serviceType, boolean isIn) {
        if (isIn) {
            return IN_TYPE.get(serviceType);
        } else {
            return OUT_TYPE.get(serviceType);
        }
    }

    /**
     * 初始化配置
     * 
     * @throws ConfigException
     */
    public void init() throws ConfigException {
        log.info("Begin Init BankConfig:" + configName);
        try {
            // 获取配置内容
            String json = getContent(configName);
            config = new JSONObject(json);
            Iterator<?> iterator = config.keys();
            if (iterator != null) {
                while (iterator.hasNext()) {
                    String type = (String) iterator.next();
                    if (CONFIG_KEY_STORE_INFO.equals(type)) {
                        JSONArray keyStoreInfos = config.optJSONArray(type);
                        registerKeyStoreInfo(keyStoreInfos);
                    } else {
                        configValueToMap(type, CONFIG_PARAMETER, false);
                        configValueToMap(type, CONFIG_DEFAULT, false);
                        configValueToMap(type, CONFIG_MAPPING, false);
                        configValueToMap(type, CONFIG_VALIDATOR, false);
                        configValueToMap(type, CONFIG_CONVERTER, false);
                        configValueToMap(type, CONFIG_TEMPLATE, false);
                        configValueToString(type, CONFIG_SIGNER_OR_VERIFIER, false);
                        configValueToArray(type, CONFIG_SIGNED_FIELD, false);
                        configValueToString(type, CONFIG_JOIN_STYLE, false);
                        configValueToString(type, CONFIG_SIGNATURE_KEY, false);
                    }
                }
            }
        } catch (Exception e) {
            String message = "Read " + configName + " BankConfig Error!";
            log.error(message, e);
            throw new ConfigException(message, e);
        }
        log.info("End Init BankConfig:" + configName);
    }

    /**
     * 读取证书配置信息,注册证书
     * 
     * @param keyStoreInfos
     */
    private void registerKeyStoreInfo(JSONArray keyStoreInfos) {
        if (keyStoreInfos == null) {
            return;
        }

        JSONObject keyStoreInfo;
        String url, type, password;
        for (int i = 0, l = keyStoreInfos.length(); i < l; i++) {
            keyStoreInfo = keyStoreInfos.optJSONObject(i);
            if (keyStoreInfo != null) {
                url = keyStoreInfo.optString(CONFIG_KEY_STORE_URL);
                type = keyStoreInfo.optString(CONFIG_KEY_STORE_TYPE);
                password = keyStoreInfo.optString(CONFIG_KEY_STORE_PASSWORD);
                if (keyStoreInfo.optBoolean(CONFIG_KEY_STORE_CERT)) {
                    KeyStoreUtil.registerCertStore(url, password, type);
                }
                if (keyStoreInfo.optBoolean(CONFIG_KEY_STORE_TRUST)) {
                    KeyStoreUtil.registerTrustStore(url, password, type);
                }
                if (keyStoreInfo.optBoolean(CONFIG_KEY_STORE_SIGN_VERIFY)) {
                    KeyStoreUtil.registerSignVerifyStore(url, password, type);
                }
            }
        }
    }

    /**
     * 获取配置内容
     * 
     * @param configName
     * @return
     * @throws IOException
     */
    private String getContent(String configName) throws IOException {
        InputStreamReader in = null;
        String content = null;
        try {
            in = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(configName), "UTF-8");
            StringWriter out = new StringWriter();
            int n = 0;
            char[] buffer = new char[4096];
            while (-1 != (n = in.read(buffer))) {
                out.write(buffer, 0, n);
            }
            content = out.toString();
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return content;
    }

    /**
     * 将指定配置参数转换为Map
     * 
     * @param type
     * @param key
     * @return
     */
    private Map<String, String> configValueToMap(String type, String key, boolean useCache) {
        Map<String, String> result = null;

        // 缓存中存在从缓存中获取
        String cacheKey = type + "." + key;
        if (useCache && configValueMap.containsKey(cacheKey)) {
            result = configValueMap.get(cacheKey);
        }

        // 解析配置获取指定的值
        if (result == null) {
            result = new HashMap<String, String>();
            // Ignore NullPointerException
            JSONObject map = config.optJSONObject(type).optJSONObject(key);
            if (map != null) {
                Iterator<?> iterator = map.keys();
                String k, v;
                while (iterator.hasNext()) {
                    k = (String) iterator.next();
                    v = map.optString(k);
                    if ("null".equalsIgnoreCase(v)) {
                        v = "";
                    }
                    result.put(k, v);
                }
            }
            configValueMap.put(cacheKey, result);
        }
        return result;
    }

    /**
     * 将指定配置参数转换为数组
     * 
     * @param type
     * @param key
     * @return
     */
    private String[] configValueToArray(String type, String key, boolean useCache) {
        String[] result = null;

        // 缓存中存在从缓存中获取
        String cacheKey = type + "." + key;
        if (useCache && configValueArray.containsKey(cacheKey)) {
            result = configValueArray.get(cacheKey);
        }

        // 解析配置获取指定的值
        if (result == null) {
            // Ignore NullPointerException
            JSONArray values = config.optJSONObject(type).optJSONArray(key);
            int length = values.length();
            result = new String[length];
            for (int i = 0; i < length; i++) {
                result[i] = values.optString(i);
            }
            configValueArray.put(cacheKey, result);
        }

        return result;
    }

    /**
     * 将指定配置参数转换为字符串
     * 
     * @param type
     * @param key
     * @return
     */
    private String configValueToString(String type, String key, boolean useCache) {
        String result = null;

        // 缓存中存在从缓存中获取
        String cacheKey = type + "." + key;
        if (useCache && configValueString.containsKey(cacheKey)) {
            result = configValueString.get(cacheKey);
        }

        // 解析配置获取指定的值
        if (result == null) {
            // Ignore NullPointerException
            result = config.optJSONObject(type).optString(key);
            if ("null".equalsIgnoreCase(result)) {
                result = "";
            }
            configValueString.put(cacheKey, result);
        }
        return result;
    }

}
