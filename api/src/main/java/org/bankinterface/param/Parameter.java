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
package org.bankinterface.param;

import java.util.HashMap;
import java.util.Map;

import org.bankinterface.enums.ServiceType;

/**
 * 请求参数对象,封装基本请求参数
 * 
 */
public class Parameter {
    private String              serviceID;
    private String              serviceVersion;
    private ServiceType         serviceType;
    private Map<String, String> config = new HashMap<String, String>();
    private Map<String, Object> custom;

    /**
     * 服务标识
     * 
     * @return
     */
    public String getServiceID() {
        return serviceID;
    }

    /**
     * 服务标识
     * 
     * @param serviceID
     */
    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    /**
     * 服务版本号
     * 
     * @return
     */
    public String getServiceVersion() {
        return serviceVersion;
    }

    /**
     * 服务版本号
     * 
     * @param serviceVersion
     */
    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    /**
     * 服务类型
     * 
     * @return
     */
    public ServiceType getServiceType() {
        return serviceType;
    }

    /**
     * 服务类型
     * 
     * @param serviceType
     */
    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * 配置信息
     * 
     * <pre>
     * key:                 value
     * merchantNO           商户号
     * merchantName:        商户名
     * userNO:              用户号
     * requestURL           请求地址
     * charset              字符集
     * notifictionURL       服务通知地址
     * callbackURL          页面回调地址
     * securityKey          密钥串
     * clientCertFilePath:  客户端证书证书文件路径
     * clientCertPassword:  客户端证书证书密码
     * clientCertAlias:     客户端证书别名(建立HTTPS连接使用)
     * publicCertFilePath:  公钥证书证书文件路径
     * publicCertPassword:  公钥证书证书密码
     * publicCertAlias:     公钥证书别名(验签使用)
     * privateCertFilePath: 私钥证书证书文件路径
     * privateCertPassword: 私钥证书证书密码
     * privateKeyAlias:     私钥证书别名(签名使用)
     * bankNO:              银行编号
     * payerIP:             付款方IP
     * ...:                 ...
     * </pre>
     * 
     * @param config
     */
    public void setConfig(Map<String, String> config) {
        this.config = config;
    }

    /**
     * 配置信息
     * 
     * <pre>
     * key:                 value
     * merchantNO           商户号
     * merchantName:        商户名
     * userNO:              用户号
     * requestURL           请求地址
     * charset              字符集
     * notifictionURL       服务通知地址
     * callbackURL          页面回调地址
     * securityKey          密钥串
     * clientCertFilePath:  客户端证书证书文件路径
     * clientCertPassword:  客户端证书证书密码
     * clientCertAlias:     客户端证书别名(建立HTTPS连接使用)
     * publicCertFilePath:  公钥证书证书文件路径
     * publicCertPassword:  公钥证书证书密码
     * publicCertAlias:     公钥证书别名(验签使用)
     * privateCertFilePath: 私钥证书证书文件路径
     * privateCertPassword: 私钥证书证书密码
     * privateKeyAlias:     私钥证书别名(签名使用)
     * bankNO:              银行编号
     * payerIP:             付款方IP
     * ...:                 ...
     * </pre>
     * 
     * @return
     */
    public Map<String, String> getConfig() {
        return config;
    }

    /**
     * 自定义参数
     * 
     * @return
     */
    public Map<String, Object> getCustom() {
        return custom;
    }

    /**
     * 自定义参数
     * 
     * @param custom
     */
    public void setCustom(Map<String, Object> custom) {
        this.custom = custom;
    }

    /**
     * 自定义参数
     * 
     * @param name
     * @param value
     * @return
     */
    public void set(String name, Object value) {
        if (custom == null) {
            custom = new HashMap<String, Object>();
        }
        custom.put(name, value);
    }

    /**
     * 自定义参数
     * 
     * @param name
     * @return
     */
    public Object get(String name) {
        if (custom == null) {
            return null;
        } else {
            return custom.get(name);
        }
    }

}
