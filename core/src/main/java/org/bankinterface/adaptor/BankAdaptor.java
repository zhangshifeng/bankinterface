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
import org.bankinterface.enums.ServiceType;
import org.bankinterface.json.JsonHelper;
import org.bankinterface.param.AuthCodeParam;
import org.bankinterface.param.Notification;
import org.bankinterface.param.AsyncPayParam;
import org.bankinterface.param.QueryParam;
import org.bankinterface.param.RefundParam;
import org.bankinterface.param.SyncPayParam;
import org.bankinterface.result.AsyncPayResult;
import org.bankinterface.result.AuthCodeResult;
import org.bankinterface.result.PrePayResult;
import org.bankinterface.result.QueryResult;
import org.bankinterface.result.RefundResult;
import org.bankinterface.result.SyncPayResult;
import org.bankinterface.service.AsyncPay;
import org.bankinterface.service.Query;
import org.bankinterface.service.Refund;
import org.bankinterface.service.SyncPay;

/**
 * 银行接口适配类
 * 
 */
public class BankAdaptor implements AsyncPay, SyncPay, Query, Refund {
    private BankAdaptorHandler handler;

    public BankAdaptorHandler getHandler() {
        return handler;
    }

    public void setHandler(BankAdaptorHandler handler) {
        this.handler = handler;
    }

    public PrePayResult prePay(AsyncPayParam asyncPayParam) {
        ResultBuilder resultBuilder = new ResultBuilder(PrePayResult.class);
        handler.handlePrePay(resultBuilder, asyncPayParam);
        return resultBuilder.create();
    }

    public String prePay(String json) {
        AsyncPayParam parameter = JsonHelper.fromJson(json, AsyncPayParam.class);
        ResultBuilder resultBuilder = new ResultBuilder(PrePayResult.class);
        handler.handlePrePay(resultBuilder, parameter);
        return resultBuilder.toJson();
    }

    public AsyncPayResult resolveAsyncPayResult(Notification notification) {
        ResultBuilder resultBuilder = new ResultBuilder(AsyncPayResult.class);
        handler.handleNotification(resultBuilder, notification);
        return resultBuilder.create();
    }

    public String resolveAsyncPayResult(String json) {
        Notification notification = JsonHelper.fromJson(json, Notification.class);
        ResultBuilder resultBuilder = new ResultBuilder(AsyncPayResult.class);
        handler.handleNotification(resultBuilder, notification);
        return resultBuilder.toJson();
    }

    @Override
    public AuthCodeResult getAuthCode(AuthCodeParam authCodeParam) {
        ResultBuilder resultBuilder = new ResultBuilder(AuthCodeResult.class);
        handler.handleAllInOne(resultBuilder, authCodeParam);
        return resultBuilder.create();
    }

    @Override
    public String getAuthCode(String json) {
        AuthCodeParam authCodeParam = JsonHelper.fromJson(json, AuthCodeParam.class);
        ResultBuilder resultBuilder = new ResultBuilder(AuthCodeResult.class);
        handler.handleAllInOne(resultBuilder, authCodeParam);
        return resultBuilder.toJson();
    }

    @Override
    public SyncPayResult pay(SyncPayParam syncPayParam) {
        ResultBuilder resultBuilder = new ResultBuilder(SyncPayResult.class);
        handler.handleAllInOne(resultBuilder, syncPayParam);
        return resultBuilder.create();
    }

    @Override
    public String pay(String json) {
        SyncPayParam syncPayParam = JsonHelper.fromJson(json, SyncPayParam.class);
        ResultBuilder resultBuilder = new ResultBuilder(SyncPayResult.class);
        handler.handleAllInOne(resultBuilder, syncPayParam);
        return resultBuilder.toJson();
    }

    @Override
    public SyncPayResult resolveSyncPayResult(Notification notification) {
        ResultBuilder resultBuilder = new ResultBuilder(SyncPayResult.class);
        handler.handleNotification(resultBuilder, notification);
        return resultBuilder.create();
    }

    @Override
    public String resolveSyncPayResult(String json) {
        Notification notification = JsonHelper.fromJson(json, Notification.class);
        ResultBuilder resultBuilder = new ResultBuilder(SyncPayResult.class);
        handler.handleNotification(resultBuilder, notification);
        return resultBuilder.toJson();
    }

    @Override
    public QueryResult query(QueryParam queryParam) {
        ResultBuilder resultBuilder = new ResultBuilder(QueryResult.class);
        if (ServiceType.BATCH_QUERY.equals(queryParam.getServiceType())) {
            handler.handleBatchAllInOne(resultBuilder, queryParam);
        } else {
            handler.handleAllInOne(resultBuilder, queryParam);
        }
        return resultBuilder.create();
    }

    @Override
    public String query(String json) {
        QueryParam queryParam = JsonHelper.fromJson(json, QueryParam.class);
        ResultBuilder resultBuilder = new ResultBuilder(QueryResult.class);
        if (ServiceType.BATCH_QUERY.equals(queryParam.getServiceType())) {
            handler.handleBatchAllInOne(resultBuilder, queryParam);
        } else {
            handler.handleAllInOne(resultBuilder, queryParam);
        }
        return resultBuilder.toJson();
    }

    @Override
    public RefundResult refund(RefundParam refundParam) {
        ResultBuilder resultBuilder = new ResultBuilder(RefundResult.class);
        if (ServiceType.BATCH_REFUND.equals(refundParam.getServiceType())) {
            handler.handleBatchAllInOne(resultBuilder, refundParam);
        } else {
            handler.handleAllInOne(resultBuilder, refundParam);
        }
        return resultBuilder.create();
    }

    @Override
    public String refund(String json) {
        RefundParam refundParam = JsonHelper.fromJson(json, RefundParam.class);
        ResultBuilder resultBuilder = new ResultBuilder(RefundResult.class);
        if (ServiceType.BATCH_REFUND.equals(refundParam.getServiceType())) {
            handler.handleBatchAllInOne(resultBuilder, refundParam);
        } else {
            handler.handleAllInOne(resultBuilder, refundParam);
        }
        return resultBuilder.toJson();
    }

    @Override
    public RefundResult resolveRefundResult(Notification notification) {
        ResultBuilder resultBuilder = new ResultBuilder(RefundResult.class);
        handler.handleNotification(resultBuilder, notification);
        return resultBuilder.create();
    }

    @Override
    public String resolveRefundResult(String json) {
        Notification notification = JsonHelper.fromJson(json, Notification.class);
        ResultBuilder resultBuilder = new ResultBuilder(RefundResult.class);
        handler.handleNotification(resultBuilder, notification);
        return resultBuilder.toJson();
    }

}
