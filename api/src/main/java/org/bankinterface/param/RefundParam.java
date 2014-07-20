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

import java.math.BigDecimal;
import java.util.Date;

import org.bankinterface.enums.ServiceType;

/**
 * 退款请求对象,封装退款请求参数
 * 
 */
public class RefundParam extends Parameter {
    private String     bankSerialNo;
    private String     refundNo;
    private Date       refundDate;
    private String     currencyType;
    private BigDecimal refundAmount;

    public RefundParam() {
        setServiceType(ServiceType.REFUND);
    }

    /**
     * 支付机构交易流水号
     * 
     * @return
     */
    public String getBankSerialNo() {
        return bankSerialNo;
    }

    /**
     * 支付机构交易流水号
     * 
     * @param bankSerialNo
     */
    public void setBankSerialNo(String bankSerialNo) {
        this.bankSerialNo = bankSerialNo;
    }

    /**
     * 退款单号
     * 
     * @return
     */
    public String getRefundNo() {
        return refundNo;
    }

    /**
     * 退款单号
     * 
     * @param refundNo
     */
    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
    }

    /**
     * 退款日期
     * 
     * @return
     */
    public Date getRefundDate() {
        return refundDate;
    }

    /**
     * 退款日期
     * 
     * @param refundDate
     */
    public void setRefundDate(Date refundDate) {
        this.refundDate = refundDate;
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

}
