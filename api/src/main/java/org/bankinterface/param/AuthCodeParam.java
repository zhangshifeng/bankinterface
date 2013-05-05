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

import org.bankinterface.enums.ServiceType;

/**
 * 获取验证码请求参数对象
 * 
 */
public class AuthCodeParam extends Parameter {
    private String phoneNO;

    public AuthCodeParam() {
        setServiceType(ServiceType.AUTH_CODE);
    }

    /**
     * 手机号码
     * 
     * @return
     */
    public String getPhoneNO() {
        return phoneNO;
    }

    /**
     * 手机号码
     * 
     * @param phoneNO
     */
    public void setPhoneNO(String phoneNO) {
        this.phoneNO = phoneNO;
    }

}
