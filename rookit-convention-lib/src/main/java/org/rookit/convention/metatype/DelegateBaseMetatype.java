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
import org.rookit.serialization.Serializer;

import java.util.Collection;
import java.util.Optional;

public class DelegateBaseMetatype<T> implements Metatype<T> {

    private final Metatype<T> delegate;

    protected DelegateBaseMetatype(final Metatype<T> delegate,
                                   final Collection<PropertyModel<?>> properties) {
        this.delegate = delegate;
    }

    @Override
    public Collection<PropertyModel<?>> properties() {
        return this.delegate.properties();
    }

    @Override
    public Optional<PropertyModel<?>> property(final String name) {
        return this.delegate.property(name);
    }

    @Override
    public Class<T> modelType() {
        return this.delegate.modelType();
    }

    @Override
    public Serializer<T> modelSerializer() {
        return this.delegate.modelSerializer();
    }

    @Override
    public String toString() {
        return "DelegateBaseMetatype{" +
                "delegate=" + this.delegate +
                "}";
    }
}
