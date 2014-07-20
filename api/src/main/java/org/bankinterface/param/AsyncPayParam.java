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
 * 异步支付请求对象,封装异步支付请求参数
 * 
 */
public class AsyncPayParam extends Parameter {
    private String     orderNo;
    private Date       orderDate;
    private String     currencyType;
    private BigDecimal orderAmount;
    private String     commodityName;

    public AsyncPayParam() {
        setServiceType(ServiceType.ASYNC_PAY);
    }

    /**
     * 订单号
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * 订单号
     * @param orderNo
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * 下单日期
     */
    public Date getOrderDate() {
        return orderDate;
    }

    /**
     * 下单日期
     * @param orderDate
     */
    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    /**
     * 货币类型
     */
    public String getCurrencyType() {
        return currencyType;
    }

    /**
     * 货币类型
     * @param currencyType
     */
    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    /**
     * 订单金额
     */
    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    /**
     * 订单金额
     * @param orderAmount
     */
    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    /**
     * 商品名称或描述
     */
    public String getCommodityName() {
        return commodityName;
    }

    /**
     * 商品名称或描述
     * @param commodityName
     */
    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }
}
