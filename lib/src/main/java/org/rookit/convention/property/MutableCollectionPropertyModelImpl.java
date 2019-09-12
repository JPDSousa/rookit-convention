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

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Function;

final class MutableCollectionPropertyModelImpl<E, T> extends ImmutableCollectionPropertyModelImpl<E, T>
        implements MutableCollectionPropertyModel<E, T> {

    private final BiConsumer<E, Collection<T>> setter;
    private final BiConsumer<E, T> adder;
    private final BiConsumer<E, Collection<T>> addAller;
    private final BiConsumer<E, T> remover;
    private final BiConsumer<E, Collection<T>> removeAller;

    MutableCollectionPropertyModelImpl(final String name,
                                       final MetaType<T> metaType,
                                       final Function<E, Collection<T>> getter,
                                       final BiConsumer<E, Collection<T>> setter,
                                       final BiConsumer<E, T> adder,
                                       final BiConsumer<E, Collection<T>> addAller,
                                       final BiConsumer<E, T> remover,
                                       final BiConsumer<E, Collection<T>> removeAller) {
        super(name, metaType, getter);
        this.setter = setter;
        this.adder = adder;
        this.addAller = addAller;
        this.remover = remover;
        this.removeAller = removeAller;
    }

    @Override
    public void set(final E entity, final Collection<T> items) {
        this.setter.accept(entity, items);
    }

    @Override
    public void add(final E entity, final T item) {
        this.adder.accept(entity, item);
    }

    @Override
    public void addAll(final E entity, final Collection<T> item) {
        this.addAller.accept(entity, item);
    }

    @Override
    public void remove(final E entity, final T item) {
        this.remover.accept(entity, item);
    }

    @Override
    public void removeAll(final E entity, final Collection<T> item) {
        this.removeAller.accept(entity, item);
    }

    @Override
    public String toString() {
        return "MutableCollectionPropertyModelImpl{" +
                "setter=" + this.setter +
                ", adder=" + this.adder +
                ", addAller=" + this.addAller +
                ", remover=" + this.remover +
                ", removeAller=" + this.removeAller +
                "} " + super.toString();
    }
}
