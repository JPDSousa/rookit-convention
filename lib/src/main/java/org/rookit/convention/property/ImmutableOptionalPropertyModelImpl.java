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
package org.rookit.convention.property;

import org.rookit.convention.MetaType;
import org.rookit.utils.optional.Optional;

import java.util.function.Function;

class ImmutableOptionalPropertyModelImpl<E, T> extends BasePropertyModel<T>
        implements ImmutableOptionalPropertyModel<E, T> {

    private final Function<E, Optional<T>> getter;

    ImmutableOptionalPropertyModelImpl(final String name,
                                       final MetaType<T> metaType,
                                       final Function<E, Optional<T>> getter) {
        super(name, metaType);
        this.getter = getter;
    }

    @Override
    public Optional<T> get(final E entity) {
        return this.getter.apply(entity);
    }

    @Override
    public String toString() {
        return "ImmutableOptionalPropertyModelImpl{" +
                "getter=" + this.getter +
                "} " + super.toString();
    }
}
