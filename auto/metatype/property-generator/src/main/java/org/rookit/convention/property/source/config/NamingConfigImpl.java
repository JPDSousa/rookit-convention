/*******************************************************************************
 * Copyright (C) 2018 Joao Sousa
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package org.rookit.convention.property.source.config;

import org.rookit.utils.object.DynamicObject;
import org.rookit.convention.auto.config.NamingConfig;

final class NamingConfigImpl implements NamingConfig {

    private final DynamicObject configuration;

    NamingConfigImpl(final DynamicObject configuration) {
        this.configuration = configuration;
    }

    @Override
    public String serializer() {
        return this.configuration.getString("serializer");
    }

    @Override
    public String type() {
        return this.configuration.getString("type");
    }

    @Override
    public String propertyName() {
        return this.configuration.getString("propertyName");
    }

    @Override
    public String getter() {
        return this.configuration.getString("getter");
    }

    @Override
    public String toString() {
        return "NamingConfigImpl{" +
                "configuration=" + this.configuration +
                "}";
    }
}
