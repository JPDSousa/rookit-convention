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

import java.util.function.BiConsumer;
import java.util.function.Function;

final class MutablePropertyModelImpl<E, T> extends ImmutablePropertyModelImpl<E, T>
        implements MutablePropertyModel<E, T> {

    private final BiConsumer<E, T> setter;

    MutablePropertyModelImpl(final String name,
                             final BiConsumer<E, T> setter,
                             final Function<E, T> getter,
                             final MetaType<T> metaType) {
        super(name, metaType, getter);
        this.setter = setter;
    }

    @Override
    public void set(final E model, final T value) {
        this.setter.accept(model, value);
    }

    @Override
    public String toString() {
        return "MutablePropertyModelImpl{" +
                "setter=" + this.setter +
                "} " + super.toString();
    }

}
