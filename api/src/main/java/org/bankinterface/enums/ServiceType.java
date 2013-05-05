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
package org.bankinterface.enums;

/**
 * 服务类型
 * 
 */
public enum ServiceType {

    /** 异步支付 */
    ASYNC_PAY,
    /** 获取验证码 */
    AUTH_CODE,
    /** 同步支付 */
    SYNC_PAY,
    /** 单笔查询 */
    QUERY,
    /** 批量查询 */
    BATCH_QUERY,
    /** 单笔退款 */
    REFUND,
    /** 批量退款 */
    BATCH_REFUND,
    /** 异步支付通知 */
    ASYNC_PAY_NOTIFICATION,
    /** 同步支付通知 */
    SYNC_PAY_NOTIFICATION,
    /** 退款通知 */
    REFUND_NOTIFICATION
}