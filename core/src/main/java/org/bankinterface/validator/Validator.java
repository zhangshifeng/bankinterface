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
package org.bankinterface.validator;

import org.bankinterface.exception.ValidationException;

/**
 * 验证规则
 * 
 */
public interface Validator {

    /**
     * 获取规则名
     * @return
     */
    String getName();

    /**
     * 数据验证
     * @param key 标识
     * @param value 数据
     * @param reference 参考数据
     * @throws ValidationException
     */
    void validate(String key, Object value, Object... reference) throws ValidationException;
}
