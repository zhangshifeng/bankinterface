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

import java.util.Date;

import org.bankinterface.enums.ServiceType;
import org.bankinterface.enums.TransactionType;

/**
 * 查询请求对象,封装查询请求参数
 * 
 */
public class QueryParam extends Parameter {
    private TransactionType transactionType;
    private String          orderNO;
    private Date            orderDate;
    private Date            startDate;
    private Date            endDate;

    private QueryParam() {
    }

    /**
     * 交易类型
     * 
     * @return
     */
    public TransactionType getTransactionType() {
        return transactionType;
    }

    /**
     * 交易类型
     * 
     * @param transactionType
     */
    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
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
     * 下单日期
     * 
     * @return
     */
    public Date getOrderDate() {
        return orderDate;
    }

    /**
     * 下单日期
     * 
     * @param orderDate
     */
    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    /**
     * 开始时间
     * 
     * @return
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * 开始时间
     * 
     * @param startDate
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * 结束时间
     * 
     * @return
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * 结束时间
     * 
     * @param endDate
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * 支付查询
     * 
     * @return
     */
    public static QueryParam payQueryParam() {
        QueryParam queryParam = new QueryParam();
        queryParam.setServiceType(ServiceType.QUERY);
        queryParam.setTransactionType(TransactionType.PAYMENT);
        return queryParam;
    }

    /**
     * 退款查询
     * 
     * @return
     */
    public static QueryParam refundQueryParam() {
        QueryParam queryParam = new QueryParam();
        queryParam.setServiceType(ServiceType.QUERY);
        queryParam.setTransactionType(TransactionType.REFUND);
        return queryParam;
    }

    /**
     * 批量支付查询
     * 
     * @return
     */
    public static QueryParam batchPayQueryParam() {
        QueryParam queryParam = new QueryParam();
        queryParam.setServiceType(ServiceType.BATCH_QUERY);
        queryParam.setTransactionType(TransactionType.PAYMENT);
        return queryParam;
    }

    /**
     * 批量退款查询
     * 
     * @return
     */
    public static QueryParam batchRefundQueryParam() {
        QueryParam queryParam = new QueryParam();
        queryParam.setServiceType(ServiceType.BATCH_QUERY);
        queryParam.setTransactionType(TransactionType.REFUND);
        return queryParam;
    }
}
