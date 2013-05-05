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
package org.bankinterface.signer;

import java.util.Map;

import org.bankinterface.exception.SignVerifyException;
import org.bankinterface.param.Parameter;

/**
 * 签名服务提供者
 * 
 */
public interface Signer {

    /**
     * 获取服务名
     * 
     * @return
     */
    String getName();

    /**
     * 签名
     * 
     * @param contextMap
     * @param signedField
     * @param joinStyle
     * @param parameter
     * @return
     * @throws SignVerifyException
     */
    public String sign(Map<String, String> contextMap, String[] signedField, String joinStyle, Parameter parameter)
            throws SignVerifyException;
}
