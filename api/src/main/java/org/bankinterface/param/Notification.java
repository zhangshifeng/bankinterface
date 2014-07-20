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
 * 通知对象,封装支付机构通知
 * 
 */
public class Notification extends Parameter {
    private String charset;
    private String content;

    private Notification() {
    }

    public Notification(Parameter parameter) {
        if (parameter != null) {
            this.setServiceId(parameter.getServiceId());
            this.setServiceType(parameter.getServiceType());
            this.setServiceVersion(parameter.getServiceVersion());
            this.setConfig(parameter.getConfig());
        }
    }

    /**
     * 字符集
     * 
     * @return
     */
    public String getCharset() {
        return charset;
    }

    /**
     * 字符集
     * 
     * @param charset
     */
    public void setCharset(String charset) {
        this.charset = charset;
    }

    /**
     * 通知內容
     * 
     * @return
     */
    public String getContent() {
        return content;
    }

    /**
     * 通知內容
     * 
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 异步支付通知
     * 
     * @return
     */
    public static Notification asyncPayNotification() {
        Notification notification = new Notification();
        notification.setServiceType(ServiceType.ASYNC_PAY_NOTIFICATION);
        return notification;
    }

    /**
     * 同步支付通知
     * 
     * @return
     */
    public static Notification syncPayNotification() {
        Notification notification = new Notification();
        notification.setServiceType(ServiceType.SYNC_PAY_NOTIFICATION);
        return notification;
    }

    /**
     * 退款通知
     * 
     * @return
     */
    public static Notification refundNotification() {
        Notification notification = new Notification();
        notification.setServiceType(ServiceType.REFUND_NOTIFICATION);
        return notification;
    }
}
