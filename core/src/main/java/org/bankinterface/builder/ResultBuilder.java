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
package org.bankinterface.builder;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.bankinterface.json.JsonHelper;
import org.bankinterface.result.Result;

@SuppressWarnings("unchecked")
public class ResultBuilder {
    private Result result;

    public ResultBuilder(Class<? extends Result> clazz) {
        try {
            result = clazz.newInstance();
            result.setStatus(Result.SUCCESS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResultBuilder populate(Map<String, Object> properties) {
        try {
            BeanUtils.populate(result, properties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public ResultBuilder occurValidationException(String message) {
        result.setStatus(Result.VALIDATE_ERROR);
        result.setMessage(message);
        return this;
    }

    public ResultBuilder occurConversionException(String message) {
        result.setStatus(Result.CONVERT_ERROR);
        result.setMessage(message);
        return this;
    }

    public ResultBuilder occurSignVerifyException(String message) {
        result.setStatus(Result.SIGN_VERFY_ERROR);
        result.setMessage(message);
        return this;
    }

    public ResultBuilder occurCommunicationException(String message) {
        result.setStatus(Result.COMMUNICTION_ERROR);
        result.setMessage(message);
        return this;
    }

    public ResultBuilder occurUnkonwnException() {
        result.setStatus(Result.SYSTEM_ERROR);
        return this;
    }

    public <T extends Result> T create() {
        return (T) result;
    }

    public String toJson() {
        return JsonHelper.toJson(result);
    }

    public static <T extends Result> T fromJson(String json, Class<? extends Result> clazz) {
        return (T) JsonHelper.fromJson(json, clazz);
    }
}
