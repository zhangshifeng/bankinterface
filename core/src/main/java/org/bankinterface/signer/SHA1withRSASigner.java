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

import org.bankinterface.builder.MessageBuilder;
import org.bankinterface.exception.SignVerifyException;
import org.bankinterface.param.Parameter;
import org.bankinterface.util.Utils;

public class SHA1withRSASigner implements Signer {
    private String name;

    public SHA1withRSASigner(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name must not be null.");
        }
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String sign(Map<String, String> contextMap, String[] signedField, String joinStyle, Parameter parameter)
            throws SignVerifyException {
        try {
            StringBuilder sb = assembeMessage(contextMap, signedField, joinStyle);
            Map<String, String> config = parameter.getConfig();
            return Utils.signSHA1withRSA(sb.toString(), config.get("charset"), config.get("certFilePath"),
                    config.get("privateKeyAlias"), config.get("encode"));
        } catch (Exception e) {
            throw new SignVerifyException("Sign Exception!", e);
        }
    }

    /**
     * 组装报文
     * 
     * @param context
     * @param signedField
     * @param joinStyle
     * @return
     */
    protected StringBuilder assembeMessage(Map<String, String> context, String[] signedField, String joinStyle) {
        return MessageBuilder.build(context, signedField, joinStyle);
    }
}
