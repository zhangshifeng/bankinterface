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
package org.bankinterface.result;

/**
 * 结果对象,封装服务调用结果
 * 
 */
public class Result {
    /** 成功状态 */
    public static final String SUCCESS            = "1";
    /** 第三方支付接口系统异常 */
    public static final String SYSTEM_ERROR       = "2";
    /** 输入参数无效异常 */
    public static final String VALIDATE_ERROR     = "3";
    /** 参数处理异常 */
    public static final String CONVERT_ERROR      = "4";
    /** 签名验签异常 */
    public static final String SIGN_VERFY_ERROR   = "5";
    /** 网络通信异常 */
    public static final String COMMUNICTION_ERROR = "6";
    private String             status;
    private String             message;

    /**
     * 获取状态
     * 
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态
     * 
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取消息
     * 
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置消息
     * 
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 是否成功
     * 
     * @return
     */
    public boolean isSuccess() {
        return SUCCESS.equals(status);
    }
}
