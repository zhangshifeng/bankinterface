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

import org.bankinterface.builder.ResultBuilder;
import org.bankinterface.param.Notification;
import org.bankinterface.param.Parameter;

/**
 * 适配处理器
 * 
 */
public interface BankAdaptorHandler {

    /**
     * 处理异步支付请求参数组装
     * 
     * @param resultBuilder
     * @param parameter
     */
    public void handlePrePay(ResultBuilder resultBuilder, Parameter parameter);

    /**
     * 处理完整的请求
     * 
     * @param resultBuilder
     * @param parameter
     */
    public void handleAllInOne(ResultBuilder resultBuilder, Parameter parameter);

    /**
     * 
     * 处理通知
     * 
     * @param resultBuilder
     * @param notification
     */
    public void handleNotification(ResultBuilder resultBuilder, Notification notification);

    /**
     * 批量处理完整的请求
     * 
     * @param resultBuilder
     * @param parameter
     */
    public void handleBatchAllInOne(ResultBuilder resultBuilder, Parameter parameter);

}
