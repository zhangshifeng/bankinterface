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

import org.bankinterface.param.Notification;
import org.bankinterface.param.AsyncPayParam;
import org.bankinterface.result.AsyncPayResult;
import org.bankinterface.result.PrePayResult;

/**
 * 异步方式支付接口,提供异步支付参数组装和结果验证转换功能
 * 
 */
public interface AsyncPay {

    /**
     * 将请求参数按照支付机构相应规则进行转换,并返回处理后的结果
     * 
     * @param asyncPayParam
     * @return
     */
    PrePayResult prePay(AsyncPayParam asyncPayParam);

    /**
     * 将请求参数按照支付机构相应规则进行转换,并返回处理后的结果
     * 
     * @param asyncPayParam
     * @return
     */
    String prePay(String asyncPayParam);

    /**
     * 验证支付机构通知合法性,并进行结果转换
     * 
     * @param notification
     * @return
     */
    AsyncPayResult resolveAsyncPayResult(Notification notification);

    /**
     * 验证支付机构构通合法性,并进行结果转换
     * 
     * @param notification
     * @return
     */
    String resolveAsyncPayResult(String notification);
}
