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

import org.bankinterface.config.BankConfig;
import org.bankinterface.exception.ValidationException;
import org.bankinterface.util.Utils;

/**
 * 非空验证
 * 
 */
public class NotNullValidator implements Validator {

    public String getName() {
        return BankConfig.VALIDATOR_NOT_NULL;
    }

    public void validate(String key, Object value, Object... reference) throws ValidationException {
        if (Utils.isEmpty(value)) {
            throw new ValidationException(key + " is null");
        }
    }

}
