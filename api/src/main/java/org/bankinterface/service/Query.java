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
package org.bankinterface.service;

import org.bankinterface.param.QueryParam;
import org.bankinterface.result.QueryResult;

/**
 * 查询接口,提供查询支付机构订单功能
 */
public interface Query {

    /**
     * 调用支付机构查询接口,返回订单信息
     * 
     * @param queryParam
     * @return
     */
    QueryResult query(QueryParam queryParam);

    /**
     * 调用支付机构查询接口,返回订单信息
     * 
     * @param queryParam
     * @return
     */
    String query(String queryParam);
}
