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
package org.bankinterface.verifier;

import java.util.HashMap;
import java.util.Map;

import org.bankinterface.builder.MessageBuilder;
import org.bankinterface.exception.SignVerifyException;
import org.bankinterface.param.Notification;
import org.bankinterface.util.Utils;

public class SHA1withRSAVerfier implements Verifier {
    private String name;

    public SHA1withRSAVerfier(String name) {
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
    public Map<String, String> verify(String[] signedField, String joinStyle, String signatureKey,
            Notification notification) throws SignVerifyException {
        boolean success = false;
        Map<String, String> context = null;
        try {
            context = notificationToMap(notification.getContent());
            StringBuilder sb = assembeMessage(context, signedField, joinStyle);
            Map<String, String> config = notification.getConfig();
            success = Utils.verifySHA1withRSA(sb.toString(), context.get(signatureKey), config.get("charset"),
                    config.get("certFilePath"), config.get("publicKeyAlias"));
        } catch (Exception e) {
            throw new SignVerifyException("Verify Exception!", e);
        }
        if (success) {
            return context;
        } else {
            throw new SignVerifyException("Verify Exception!");
        }
    }

    /**
     * 将通知转换为Map
     * 
     * @param notification
     * @return
     */
    protected Map<String, String> notificationToMap(String notification) {
        Map<String, String> result = new HashMap<String, String>();
        String[] maps;
        if (notification.contains("&")) {
            maps = notification.split("&");
        } else {
            maps = notification.split("\n");
        }
        for (String map : maps) {
            int i = map.indexOf("=");
            result.put(map.substring(0, i), map.substring(i + 1));
        }
        return result;
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
