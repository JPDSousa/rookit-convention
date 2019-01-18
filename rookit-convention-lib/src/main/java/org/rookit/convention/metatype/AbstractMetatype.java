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
package org.rookit.convention.metatype;

import org.rookit.convention.Metatype;
import org.rookit.convention.property.PropertyModel;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractMetatype<T> implements Metatype<T> {

    private final Map<String, PropertyModel<?>> properties;

    protected AbstractMetatype(final Collection<PropertyModel<?>> properties) {
        this.properties = properties.stream()
                .collect(Collectors.toMap(PropertyModel::propertyName, propertyModel -> propertyModel));
    }


    @Override
    public Optional<PropertyModel<?>> property(final String name) {
        return Optional.ofNullable(this.properties.get(name));
    }

    @Override
    public Collection<PropertyModel<?>> properties() {
        return this.properties.values();
    }

    @Override
    public String toString() {
        return "AbstractMetatype{" +
                "properties=" + this.properties +
                "}";
    }
}
