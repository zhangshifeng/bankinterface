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
package org.bankinterface.adaptor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.bankinterface.bank.BaseBank;
import org.bankinterface.builder.ResultBuilder;
import org.bankinterface.config.BankConfig;
import org.bankinterface.converter.AmountToFenConverter;
import org.bankinterface.converter.AmountToYuanConverter;
import org.bankinterface.converter.Converter;
import org.bankinterface.converter.DateToStringConverter;
import org.bankinterface.converter.FenToAmountConverter;
import org.bankinterface.converter.ReplaceConverter;
import org.bankinterface.converter.StringToDateConverter;
import org.bankinterface.converter.YuanToAmountConverter;
import org.bankinterface.exception.ConfigException;
import org.bankinterface.exception.ConversionException;
import org.bankinterface.exception.HttpClientException;
import org.bankinterface.exception.SignVerifyException;
import org.bankinterface.exception.ValidationException;
import org.bankinterface.param.Notification;
import org.bankinterface.param.Parameter;
import org.bankinterface.signer.Signer;
import org.bankinterface.util.HttpClient;
import org.bankinterface.util.Utils;
import org.bankinterface.validator.EqualsValidator;
import org.bankinterface.validator.NotNullValidator;
import org.bankinterface.validator.Validator;
import org.bankinterface.verifier.Verifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认适配处理器
 * 
 */
public class DefaultBankAdaptorHandler implements BankAdaptorHandler {
    private static final Logger          logger       = LoggerFactory.getLogger(DefaultBankAdaptorHandler.class);
    // 转换规则
    private final Map<String, Converter> converterMap = new HashMap<String, Converter>();
    // 验证规则
    private final Map<String, Validator> validatorMap = new HashMap<String, Validator>();
    // 签名服务
    private final Map<String, Signer>    signerMap    = new HashMap<String, Signer>();
    // 验签服务
    private final Map<String, Verifier>  verifierMap  = new HashMap<String, Verifier>();
    private final BaseBank               bank;
    private final String                 bankName;

    public DefaultBankAdaptorHandler(BaseBank bank) {
        if (bank == null) {
            logger.error("bank is null");
            throw new RuntimeException("bank is null");
        }
        this.bank = bank;
        bankName = bank.getClass().getName();
        // register default converters
        registerValidator(new NotNullValidator());
        registerValidator(new EqualsValidator());
        // register default validators
        registerConverter(new AmountToFenConverter());
        registerConverter(new AmountToYuanConverter());
        registerConverter(new YuanToAmountConverter());
        registerConverter(new FenToAmountConverter());
        registerConverter(new DateToStringConverter());
        registerConverter(new ReplaceConverter());
        registerConverter(new StringToDateConverter());
    }

    /**
     * 将请求参数按照支付机构相应规则进行转换.
     * 
     * @param parameter
     * @return
     * @throws ConfigException
     * @throws ValidationException
     * @throws ConversionException
     * @throws SignVerifyException
     */
    protected RequestParameters convertParameter(Parameter parameter) throws ConfigException, ValidationException,
            ConversionException, SignVerifyException {
        // 获取支付机构请求报文处理配置
        BankConfig config = bank.getBankConfig(parameter.getServiceVersion());
        String type = config.getConfigType(parameter.getServiceType(), false);
        Map<String, String> defaultMap, mappingMap, validatorMap, converterMap;
        defaultMap = config.getDefaultValue(type);
        mappingMap = config.getMapping(type);
        validatorMap = config.getValidator(type);
        converterMap = config.getConverter(type);

        // 依据规则进行验证
        if (validatorMap != null) {
            String validator, mapping, defaultValue;
            Object vaule;
            for (String key : validatorMap.keySet()) {
                // 获得报文验证方法名
                validator = validatorMap.get(key);
                // 获得报文默认值
                defaultValue = defaultMap.get(key);
                // 获得报文对应入参名称
                mapping = mappingMap.get(key);
                // 获得入参值
                vaule = getParameterValue(parameter, mapping, defaultValue);
                // 用指定的验证方法验证
                getValidator(validator).validate(mapping, vaule, defaultValue);
            }
        }

        // 依据规则进行转换
        Map<String, String> convertedMap = new HashMap<String, String>(defaultMap);
        if (converterMap != null) {
            String converter, mapping, defaultValue, template;
            Object vaule;
            Map<String, String> templateMap = config.getTemplate(type);
            for (String key : converterMap.keySet()) {
                // 获得报文转换方法名
                converter = converterMap.get(key);
                // 获得报文对应入参名称
                mapping = mappingMap.get(key);
                // 获得报文默认值
                defaultValue = defaultMap.get(key);
                // 获得入参值
                vaule = getParameterValue(parameter, mapping, defaultValue);
                // 获得转换模板
                template = templateMap.get(key);
                // 转换值替换默认值
                convertedMap.put(key, (String) getConverter(converter).convert(mapping, vaule, template));
            }
        }

        // 依据规则进行签名
        String signatureKey = config.getSignatureKey(type);
        if (!Utils.isEmpty(signatureKey)) {
            Signer signer = getSigner(config.getSignerOrVerifier(type));
            convertedMap.put(signatureKey,
                    signer.sign(convertedMap, config.getSignedField(type), config.getJoinStyle(type), parameter));
        }

        return new RequestParameters(parameter, convertedMap);
    }

    /**
     * 验证支付机构通知合法性,如果需要,并解密.
     * 
     * @param notification
     * @return 如果需要解密,返回解密后的通知数据,否则返回原始的通知数据
     * @throws ConfigException
     * @throws ConversionException
     * @throws SignVerifyException
     */
    protected Map<String, String> verifyNotification(Notification notification) throws ConfigException,
            ConversionException, SignVerifyException {
        // 获取支付机构请求报文处理配置
        BankConfig config = bank.getBankConfig(notification.getServiceVersion());
        String type = config.getConfigType(notification.getServiceType(), true);
        Verifier verifier = getVerifier(config.getSignerOrVerifier(type));
        // 验签并解密
        return verifier.verify(config.getSignedField(type), config.getJoinStyle(type), config.getSignatureKey(type),
                notification);
    }

    /**
     * 构建结果
     * 
     * @param verifiedMap
     * @param parameter
     * @return
     * @throws ConfigException
     * @throws ConversionException
     * @throws ValidationException
     */
    protected Map<String, Object> buildResult(Map<String, String> verifiedMap, Parameter parameter)
            throws ConfigException, ConversionException, ValidationException {
        // 获取支付机构请求报文处理配置
        BankConfig config = bank.getBankConfig(parameter.getServiceVersion());
        String type = config.getConfigType(parameter.getServiceType(), true);
        Map<String, String> defaultMap, mappingMap, validatorMap, converterMap;
        defaultMap = config.getDefaultValue(type);
        mappingMap = config.getMapping(type);
        validatorMap = config.getValidator(type);
        converterMap = config.getConverter(type);

        // 依据规则进行验证
        if (validatorMap != null) {
            String validator, mapping, defaultValue, value;
            for (String key : validatorMap.keySet()) {
                // 获得报文验证方法名
                validator = validatorMap.get(key);
                // 获得报文对应结果名称
                mapping = mappingMap.get(key);
                // 获得报文默认值
                defaultValue = defaultMap.get(key);
                // 获得原始结果值
                value = verifiedMap.get(mapping);
                // 用指定的验证方法验证
                getValidator(validator).validate(mapping, validator, value, defaultValue);
            }
        }

        // 依据规则进行转换
        Map<String, Object> convertedMap = new HashMap<String, Object>(verifiedMap);
        if (converterMap != null) {
            String converter, mapping, template, value;
            Map<String, String> templateMap = config.getTemplate(type);
            for (String key : converterMap.keySet()) {
                // 获得报文转换方法名
                converter = converterMap.get(key);
                // 获得报文对应结果名称
                mapping = mappingMap.get(key);
                // 获得转换模板
                template = templateMap.get(key);
                // 获得原始结果值
                value = verifiedMap.get(mapping);
                // 用指定的转换方式转换
                convertedMap.put(key, getConverter(converter).convert(mapping, value, template));
            }
        }

        return convertedMap;
    }

    /**
     * 发送请求并获得结果报文
     * 
     * @param requestParameters
     * @return
     * @throws HttpClientException
     */
    protected String sendHttpRequest(RequestParameters requestParameters) throws HttpClientException {
        HttpClient httpClient = new HttpClient();
        httpClient.setUrl(requestParameters.requestUrl);
        httpClient.setStreamCharset(requestParameters.charset);
        httpClient.setParameters(requestParameters.parameters);
        // httpClient.setRawStream(requestParameters.rawStream);
        // 设置是否允许访问非授信URL
        // httpClient.setAllowUntrusted(requestParameters.allowUntrusted);
        // 设置客户端证书别名
        // httpClient.setClientCertificateAlias(requestParameters.clientCertAlias);
        return httpClient.post();
    }

    /**
     * 获得指定请求的指定参数值,propertys用|分割可获得LinkedList结果集
     * 
     * @param parameter
     * @param propertys
     * @param defaultValue
     * @return
     */
    protected Object getParameterValue(Parameter parameter, String propertys, String defaultValue) {
        Object result = null;
        try {
            if (propertys.contains("|")) {
                LinkedList<Object> list = new LinkedList<Object>();
                for (String property : propertys.split("\\|")) {
                    if (Utils.isEmpty(property)){
                        continue;
                    }
                    list.add(PropertyUtils.getProperty(parameter, property));
                }
                result = list;
            } else {
                result = PropertyUtils.getProperty(parameter, propertys);
            }
        } catch (Exception e) {
        }
        return result == null ? defaultValue : result;
    }

    /**
     * 获取验证规则
     * 
     * @param validatorName
     */
    protected Validator getValidator(String validatorName) throws ValidationException {
        Validator validator = validatorMap.get(validatorName);
        if (validator == null) {
            throw ValidationException.UNSUPPORTED;
        }
        return validator;
    }

    /**
     * 获取转换规则
     * 
     * @param converterName
     * @throws ConversionException
     */
    protected Converter getConverter(String converterName) throws ConversionException {
        Converter converter = converterMap.get(converterName);
        if (converter == null) {
            throw ConversionException.UNSUPPORTED;
        }
        return converter;
    }

    /**
     * 获取签名服务
     * 
     * @param signerName
     * @throws SignVerifyException
     */
    protected Signer getSigner(String signerName) throws SignVerifyException {
        Signer signer = signerMap.get(signerName);
        if (signer == null) {
            throw SignVerifyException.UNSUPPORTED;
        }
        return signer;
    }

    /**
     * 获取验签服务
     * 
     * @param verifierName
     * @throws SignVerifyException
     */
    protected Verifier getVerifier(String verifierName) throws SignVerifyException {
        Verifier verifier = verifierMap.get(verifierName);
        if (verifier == null) {
            throw SignVerifyException.UNSUPPORTED;
        }
        return verifier;
    }

    public void handlePrePay(ResultBuilder resultBuilder, Parameter parameter) {
        try {
            RequestParameters requestParameters = convertParameter(parameter);
            resultBuilder.populate(requestParameters);
        } catch (ValidationException e) {
            resultBuilder.occurValidationException(e.getMessage());
        } catch (ConversionException e) {
            resultBuilder.occurConversionException(e.getMessage());
        } catch (SignVerifyException e) {
            resultBuilder.occurSignVerifyException(e.getMessage());
        } catch (Exception e) {
            resultBuilder.occurUnkonwnException();
            logger.error(bankName, e);
        }
    }

    public void handleAllInOne(ResultBuilder resultBuilder, Parameter parameter) {
        try {
            RequestParameters requestParameters = convertParameter(parameter);
            String response = sendHttpRequest(requestParameters);

            Notification notification = new Notification(parameter);
            notification.setContent(response);
            Map<String, String> verifiedMap = verifyNotification(notification);

            Map<String, Object> resultMap = buildResult(verifiedMap, notification);
            resultBuilder.populate(resultMap);
        } catch (ValidationException e) {
            resultBuilder.occurValidationException(e.getMessage());
        } catch (ConversionException e) {
            resultBuilder.occurConversionException(e.getMessage());
        } catch (HttpClientException e) {
            resultBuilder.occurCommunicationException(e.getMessage());
        } catch (SignVerifyException e) {
            resultBuilder.occurSignVerifyException(e.getMessage());
        } catch (Exception e) {
            resultBuilder.occurUnkonwnException();
            logger.error(bankName, e);
        }
    }

    public void handleNotification(ResultBuilder resultBuilder, Notification notification) {
        try {
            Map<String, String> verifiedMap = verifyNotification(notification);
            Map<String, Object> resultMap = buildResult(verifiedMap, notification);
            resultBuilder.populate(resultMap);
        } catch (ValidationException e) {
            resultBuilder.occurValidationException(e.getMessage());
        } catch (ConversionException e) {
            resultBuilder.occurConversionException(e.getMessage());
        } catch (SignVerifyException e) {
            resultBuilder.occurSignVerifyException(e.getMessage());
        } catch (Exception e) {
            resultBuilder.occurUnkonwnException();
            logger.error(bankName, e);
        }
    }

    public void handleBatchAllInOne(ResultBuilder resultBuilder, Parameter parameter) {
        throw new UnsupportedOperationException();
    }

    public BankAdaptorHandler registerConverter(Converter convert) {
        if (convert != null) {
            converterMap.put(convert.getName(), convert);
        }
        return this;
    }

    public BankAdaptorHandler registerValidator(Validator validate) {
        if (validate != null) {
            validatorMap.put(validate.getName(), validate);
        }
        return this;
    }

    public BankAdaptorHandler registerSigner(Signer signer) {
        if (signer != null) {
            signerMap.put(signer.getName(), signer);
        }
        return this;
    }

    public BankAdaptorHandler registerVerifier(Verifier verifier) {
        if (verifier != null) {
            verifierMap.put(verifier.getName(), verifier);
        }
        return this;
    }

    @SuppressWarnings("serial")
    static class RequestParameters extends HashMap<String, Object>{
        String              requestUrl;
        String              charset;
        String              rawStream;
        String              clientCertAlias;
        boolean             allowUntrusted;
        Map<String, String> parameters;

        private RequestParameters(Parameter parameter, Map<String, String> convertedMap) {
            Map<String, String> config = parameter.getConfig();
            requestUrl = config.get("requestUrl");
            charset = config.get("charset");
            //clientCertAlias = config.get("clientCertAlias");
            //allowUntrusted = "Y".equals(config.get("allowUntrusted"));
            parameters = convertedMap;
            put("requestUrl", requestUrl);
            put("charset", charset == null ? "UTF-8" : charset);
            put("parameters", parameters);
        }
    }

}
