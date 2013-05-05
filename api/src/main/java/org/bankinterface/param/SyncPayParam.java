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

import org.bankinterface.enums.CardType;
import org.bankinterface.enums.IDType;
import org.bankinterface.enums.ServiceType;

/**
 * 同步支付请求对象,封装同步支付请求参数
 * 
 */
public class SyncPayParam extends Parameter {
    private String     orderNO;
    private String     currencyType;
    private BigDecimal orderAmount;
    private String     commodityName;
    private String     authCode;
    private CardType   cardType;
    private String     cardNO;
    private String     validDate;
    private String     cvv2;
    private IDType     idType;
    private String     idNO;
    private String     cardHolderName;

    public SyncPayParam() {
        setServiceType(ServiceType.SYNC_PAY);
    }

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
     * 订单金额
     * 
     * @return
     */
    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    /**
     * 订单金额
     * 
     * @param orderAmount
     */
    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    /**
     * 商品名称或描述
     * 
     * @return
     */
    public String getCommodityName() {
        return commodityName;
    }

    /**
     * 商品名称或描述
     * 
     * @param commodityName
     */
    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    /**
     * 验证码
     * 
     * @return
     */
    public String getAuthCode() {
        return authCode;
    }

    /**
     * 验证码
     * 
     * @param authCode
     */
    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    /**
     * 银行卡类型
     * 
     * @return
     */
    public CardType getCardType() {
        return cardType;
    }

    /**
     * 卡类型
     * 
     * @param cardType
     */
    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    /**
     * 卡号
     * 
     * @return
     */
    public String getCardNO() {
        return cardNO;
    }

    /**
     * 卡号
     * 
     * @param cardNO
     */
    public void setCardNO(String cardNO) {
        this.cardNO = cardNO;
    }

    /**
     * 卡有效期
     * 
     * @return
     */
    public String getValidDate() {
        return validDate;
    }

    /**
     * 卡有效期
     * 
     * @param validDate
     */
    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    /**
     * 卡cvv2码
     * 
     * @return
     */
    public String getCvv2() {
        return cvv2;
    }

    /**
     * 卡cvv2码
     * 
     * @param cvv2
     */
    public void setCvv2(String cvv2) {
        this.cvv2 = cvv2;
    }

    /**
     * 证件类型
     * 
     * @return
     */
    public IDType getIdType() {
        return idType;
    }

    /**
     * 证件类型
     * 
     * @param idType
     */
    public void setIdType(IDType idType) {
        this.idType = idType;
    }

    /**
     * 证件号
     * 
     * @return
     */
    public String getIdNO() {
        return idNO;
    }

    /**
     * 证件号
     * 
     * @param idNO
     */
    public void setIdNO(String idNO) {
        this.idNO = idNO;
    }

    /**
     * 持卡人姓名
     * 
     * @return
     */
    public String getCardHolderName() {
        return cardHolderName;
    }

    /**
     * 持卡人姓名
     * 
     * @param cardHolderName
     */
    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

}
