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
package org.bankinterface.bank;

import java.util.HashMap;
import java.util.Map;

import org.bankinterface.adaptor.BankAdaptor;
import org.bankinterface.adaptor.DefaultBankAdaptorHandler;
import org.bankinterface.config.BankConfig;
import org.bankinterface.config.JsonBankConfig;
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
import org.bankinterface.exception.SignVerifyException;
import org.bankinterface.exception.ValidationException;
import org.bankinterface.signer.Signer;
import org.bankinterface.validator.EqualsValidator;
import org.bankinterface.validator.NotNullValidator;
import org.bankinterface.validator.Validator;
import org.bankinterface.verifier.Verifier;

/**
 * 支付机构抽象类
 * 
 */
public abstract class BaseBank extends BankAdaptor {
    // 配置文件名
    private final String configName = this.getClass().getSimpleName();
    // 配置缓存
    private final Map<String, BankConfig> configMap = new HashMap<String, BankConfig>();
    // 转换规则
    private final Map<String, Converter> converterMap = new HashMap<String, Converter>();
    // 验证规则
    private final Map<String, Validator> validatorMap = new HashMap<String, Validator>();
    // 签名服务
    private final Map<String, Signer> signerMap = new HashMap<String, Signer>();
    // 验签服务
    private final Map<String, Verifier> verifierMap = new HashMap<String, Verifier>();

    /**
     * 初始化
     * 
     * @return
     * @throws ConfigException
     */
    public void init() throws ConfigException {
        getBankConfig(configName, false);
        if (getHandler() == null) {
            setHandler(new DefaultBankAdaptorHandler(this));
        }
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
     * 获得支付机构指定版本配置
     * 
     * @param version
     * @return
     * @throws ConfigException
     */
    public BankConfig getBankConfig(String version) throws ConfigException {
        StringBuilder sb = new StringBuilder(configName);
        if (version != null) {
            sb.append(version);
        }
        return getBankConfig(sb.toString(), true);
    }

    /**
     * 获得支付机构配置
     * 
     * @param configName
     * @param useCache
     * @return
     * @throws ConfigException
     */
    public BankConfig getBankConfig(String configName, boolean useCache) throws ConfigException {
        BankConfig config = null;
        // 缓存中存在从缓存中获取
        if (useCache && configMap.containsKey(configName)) {
            config = configMap.get(configName);
        }
        if (config == null) {
            config = new JsonBankConfig(configName);
            configMap.put(configName, config);
        }
        return config;
    }

    public void registerConverter(Converter convert) {
        if (convert != null) {
            converterMap.put(convert.getName(), convert);
        }
    }

    public void registerValidator(Validator validate) {
        if (validate != null) {
            validatorMap.put(validate.getName(), validate);
        }
    }

    public void registerSigner(Signer signer) {
        if (signer != null) {
            signerMap.put(signer.getName(), signer);
        }
    }

    public void registerVerifier(Verifier verifier) {
        if (verifier != null) {
            verifierMap.put(verifier.getName(), verifier);
        }
    }

    /**
     * 获取验证规则
     * 
     * @param validatorName
     */
    public Validator getValidator(String validatorName) throws ValidationException {
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
    public Converter getConverter(String converterName) throws ConversionException {
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
    public Signer getSigner(String signerName) throws SignVerifyException {
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
    public Verifier getVerifier(String verifierName) throws SignVerifyException {
        Verifier verifier = verifierMap.get(verifierName);
        if (verifier == null) {
            throw SignVerifyException.UNSUPPORTED;
        }
        return verifier;
    }

}
