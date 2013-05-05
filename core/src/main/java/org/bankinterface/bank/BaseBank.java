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
import org.bankinterface.exception.ConfigException;

/**
 * 支付机构抽象类
 * 
 */
public abstract class BaseBank extends BankAdaptor {
    // 配置缓存
    private final Map<String, BankConfig> configMap  = new HashMap<String, BankConfig>();
    // 配置文件名
    private final String                  configName = this.getClass().getSimpleName();

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
}
