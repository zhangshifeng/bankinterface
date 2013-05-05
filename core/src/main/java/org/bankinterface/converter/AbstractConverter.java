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
package org.bankinterface.converter;

import org.bankinterface.exception.ConversionException;

public abstract class AbstractConverter implements Converter {

    protected abstract Object internalConvert(String key, Object value, String template) throws Exception;

    @Override
    public Object convert(String key, Object value, String template) throws ConversionException {
        try {
            return internalConvert(key, value, template);
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Occurred in the covert methood ").append(getName());
            sb.append(" parameter [").append(key).append(",");
            sb.append(template).append(",").append(value).append("]");
            throw new ConversionException(sb.toString(), e);
        }
    }

}
