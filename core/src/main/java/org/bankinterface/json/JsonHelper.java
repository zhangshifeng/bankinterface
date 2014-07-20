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
package org.bankinterface.json;

import java.util.ServiceLoader;

public class JsonHelper {
    private static final ServiceLoader<JsonHandler> jsonHandlerLoader = ServiceLoader.load(JsonHandler.class);
    private static JsonHandler                      jsonHandler       = null;

    static {
        if (jsonHandlerLoader.iterator().hasNext()) {
            jsonHandler = jsonHandlerLoader.iterator().next();
        } else {
            System.err.println("Can't find any JsonHandler.");
        }
    }

    private static JsonHandler getJsonHandler() {
        if (jsonHandler == null) {
            throw new UnsupportedOperationException("Can't find any JsonHandler.");
        }
        return jsonHandler;
    }

    /**
     * 指定的对象转换为JSON字符串
     * 
     * @param src
     * @return
     */
    public static String toJson(Object src) {
        return getJsonHandler().toJson(src);
    }

    /**
     * JSON字符串转换为指定的类型对象
     * 
     * @param json
     * @param classOfT
     * @return
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {
        return getJsonHandler().fromJson(json, classOfT);
    }

}
