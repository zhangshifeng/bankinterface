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
 * 查询结果对象,封装支付机构查询结果
 * 
 */
public class QueryResult extends Result {
    private String      orderNO;
    private String      bankSerialNO;
    private String      currencyType;
    private BigDecimal  payAmount;
    private BigDecimal  refundAmount;
    private Date        bankSuccessDate;
    private OrderStatus orderStatus;
    private String      merchantNO;

    /**
     * 订单号
     * 
     * @return
     */
    public String getOrderNO() {
        return orderNO;
    }

    /**
     * 订单号
     * 
     * @param orderNO
     */
    public void setOrderNO(String orderNO) {
        this.orderNO = orderNO;
    }

    /**
     * 支付机构交易流水号
     * 
     * @return
     */
    public String getBankSerialNO() {
        return bankSerialNO;
    }

    /**
     * 支付机构交易流水号
     * 
     * @param bankSerialNO
     */
    public void setBankSerialNO(String bankSerialNO) {
        this.bankSerialNO = bankSerialNO;
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
     * 支付金额
     * 
     * @return
     */
    public BigDecimal getPayAmount() {
        return payAmount;
    }

    /**
     * 支付金额
     * 
     * @param payAmount
     */
    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
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
     * 成功时间
     * 
     * @return
     */
    public Date getBankSuccessDate() {
        return bankSuccessDate;
    }

    /**
     * 成功时间
     * 
     * @param bankSuccessDate
     */
    public void setBankSuccessDate(Date bankSuccessDate) {
        this.bankSuccessDate = bankSuccessDate;
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
    public String getMerchantNO() {
        return merchantNO;
    }

    /**
     * 商户号
     * 
     * @param merchantNO
     */
    public void setMerchantNO(String merchantNO) {
        this.merchantNO = merchantNO;
    }

}
