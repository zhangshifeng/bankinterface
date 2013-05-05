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
package org.bankinterface.exception;

import java.security.GeneralSecurityException;

/**
 * 签名验签异常
 * 
 */
public class SignVerifyException extends GeneralSecurityException {
    public static final SignVerifyException UNSUPPORTED      = new SignVerifyException("unsupported");
    private static final long               serialVersionUID = -3901605554778641652L;

    public SignVerifyException(String message) {
        super(message);
    }

    public SignVerifyException(String message, Exception e) {
        super(message, e);
    }
}
