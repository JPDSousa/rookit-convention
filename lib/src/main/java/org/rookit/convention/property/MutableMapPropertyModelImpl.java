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

import org.apache.commons.lang3.tuple.Pair;
import org.rookit.convention.MetaType;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

final class MutableMapPropertyModelImpl<E, K, V> extends ImmutableMapPropertyModelImpl<E, K, V>
        implements MutableMapPropertyModel<E, K, V> {

    private final BiConsumer<E, Map<K, V>> setter;
    private final BiConsumer<E, Map.Entry<K, V>> putter;
    private final BiConsumer<E, Map<K, V>> allPutter;
    private final BiFunction<E, K, V> remover;

    MutableMapPropertyModelImpl(final String name,
                                final MetaType<V> metaType,
                                final Function<E, Map<K, V>> getter,
                                final BiConsumer<E, Map<K, V>> setter,
                                final BiConsumer<E, Map.Entry<K, V>> putter,
                                final BiConsumer<E, Map<K, V>> allPutter,
                                final BiFunction<E, K, V> remover) {
        super(name, metaType, getter);
        this.setter = setter;
        this.putter = putter;
        this.allPutter = allPutter;
        this.remover = remover;
    }

    @Override
    public void set(final E entity, final Map<K, V> map) {
        this.setter.accept(entity, map);
    }

    @Override
    public void put(final E entity, final K key, final V value) {
        this.putter.accept(entity, Pair.of(key, value));
    }

    @Override
    public void putAll(final E entity, final Map<K, V> map) {
        this.allPutter.accept(entity, map);
    }

    @Override
    public V remove(final E entity, final K key) {
        return this.remover.apply(entity, key);
    }

    @Override
    public String toString() {
        return "MutableMapPropertyModelImpl{" +
                "setter=" + this.setter +
                ", putter=" + this.putter +
                ", allPutter=" + this.allPutter +
                ", remover=" + this.remover +
                "} " + super.toString();
    }
}
