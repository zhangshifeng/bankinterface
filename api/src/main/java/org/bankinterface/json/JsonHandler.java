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

public interface JsonHandler {
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 指定的对象转换为JSON字符串
     * 
     * @param src 指定的对象
     * @return JSON字符串
     */
    public String toJson(Object src);

    /**
     * JSON字符串转换为指定的类型对象
     * 
     * @param json JSON字符串
     * @param classOfT 指定的类型
     * @return
     */
    public <T> T fromJson(String json, Class<T> classOfT);

}