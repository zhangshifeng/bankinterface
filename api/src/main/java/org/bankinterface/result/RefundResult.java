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

import java.math.BigDecimal;
import java.util.Date;

import org.bankinterface.enums.OrderStatus;

/**
 * 退款结果对象,封装支付机构退款结果
 * 
 */
public class RefundResult extends Result {
    private String      orderNo;
    private String      bankSerialNo;
    private String      currencyType;
    private BigDecimal  refundAmount;
    private Date        refundSuccessDate;
    private OrderStatus orderStatus;
    private String      merchantNo;
    private String      callbackMessage;

    /**
     * 退款单号
     * 
     * @return
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * 退款单号
     * 
     * @param orderNo
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * 银行交易流水号
     * 
     * @return
     */
    public String getBankSerialNo() {
        return bankSerialNo;
    }

    /**
     * 银行交易流水号
     * 
     * @param bankSerialNo
     */
    public void setBankSerialNo(String bankSerialNo) {
        this.bankSerialNo = bankSerialNo;
    }

    /**
     * 货币类型
     * 
     * @return
     */
    public String getCurrencyType() {
        return currencyType;
    }

    /**
     * 货币类型
     * 
     * @param currencyType
     */
    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    /**
     * 退款金额
     * 
     * @return
     */
    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    /**
     * 退款金额
     * 
     * @param refundAmount
     */
    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    /**
     * 退款成功时间
     * 
     * @return
     */
    public Date getRefundSuccessDate() {
        return refundSuccessDate;
    }

    /**
     * 退款成功时间
     * 
     * @param refundSuccessDate
     */
    public void setRefundSuccessDate(Date refundSuccessDate) {
        this.refundSuccessDate = refundSuccessDate;
    }

    /**
     * 订单状态
     * 
     * @return
     */
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    /**
     * 订单状态
     * 
     * @param orderStatus
     */
    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    /**
     * 商户号
     * 
     * @return
     */
    public String getMerchantNo() {
        return merchantNo;
    }

    /**
     * 商户号
     * 
     * @param merchantNo
     */
    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    /**
     * 回调信息
     * 
     * @return
     */
    public String getCallbackMessage() {
        return callbackMessage;
    }

    /**
     * 回调信息
     * 
     * @param callbackMessage
     */
    public void setCallbackMessage(String callbackMessage) {
        this.callbackMessage = callbackMessage;
    }

}
