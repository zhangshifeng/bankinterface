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
package org.bankinterface.service;

import org.bankinterface.param.AuthCodeParam;
import org.bankinterface.param.Notification;
import org.bankinterface.param.SyncPayParam;
import org.bankinterface.result.AuthCodeResult;
import org.bankinterface.result.SyncPayResult;

/**
 * 同步方式支付接口,提供获取验证码,同步支付和结果验证转换功能
 * 
 */
public interface SyncPay {

    /**
     * 获取支付机构手机验证码.
     * 
     * @param authCodeParam
     * @return
     */
    AuthCodeResult getAuthCode(AuthCodeParam authCodeParam);

    /**
     * 获取支付机构手机验证码.
     * 
     * @param authCodeParam
     * @return
     */
    String getAuthCode(String authCodeParam);

    /**
     * 调用支付机构快捷支付接口,并返回支付结果.
     * 
     * @param syncPayParam
     * @return
     */
    SyncPayResult pay(SyncPayParam syncPayParam);

    /**
     * 调用支付机构快捷支付接口,并返回支付结果.
     * 
     * @param syncPayParam
     * @return
     */
    String pay(String syncPayParam);

    /**
     * 验证支付机构构通合法性,并进行结果转换.
     * 
     * @param notification
     * @return
     */
    SyncPayResult resolveSyncPayResult(Notification notification);

    /**
     * 验证支付机构构通合法性,并进行结果转换.
     * 
     * @param notification
     * @return
     */
    String resolveSyncPayResult(String notification);
}
