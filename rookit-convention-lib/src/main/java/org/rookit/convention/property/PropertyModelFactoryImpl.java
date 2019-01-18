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

import org.rookit.convention.Metatype;
import org.rookit.utils.optional.Optional;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

final class PropertyModelFactoryImpl implements PropertyModelFactory {

    @Override
    public <E, T> MutablePropertyModel<E, T> createMutableProperty(final String name,
                                                            final BiConsumer<E, T> setter,
                                                            final Function<E, T> getter,
                                                            final Metatype<T> metatype) {
        return new MutablePropertyModelImpl<>(name, setter, getter, metatype);
    }

    @Override
    public <E, T> ImmutablePropertyModel<E, T> createImmutableProperty(final String name,
                                                                       final Function<E, T> getter,
                                                                       final Metatype<T> metatype) {
        return new ImmutablePropertyModelImpl<>(name, metatype, getter);
    }

    @Override
    public <E, T> MutableOptionalPropertyModel<E, T> createMutableOptionalProperty(
            final String name,
            final BiConsumer<E, T> setter,
            final Function<E, Optional<T>> getter,
            final Metatype<T> metatype) {
        return new MutableOptionalPropertyModelImpl<>(name, metatype, getter, setter);
    }

    @Override
    public <E, T> ImmutableOptionalPropertyModel<E, T> createImmutableOptionalProperty(
            final String name,
            final Function<E, Optional<T>> getter,
            final Metatype<T> metatype) {
        return new ImmutableOptionalPropertyModelImpl<>(name, metatype, getter);
    }

    @Override
    public <E, T> MutableCollectionPropertyModel<E, T> createMutableCollectionProperty(
            final String name,
            final BiConsumer<E, Collection<T>> setter,
            final BiConsumer<E, T> add,
            final BiConsumer<E, Collection<T>> addAll,
            final BiConsumer<E, T> remove,
            final BiConsumer<E, Collection<T>> removeAll,
            final Function<E, Collection<T>> getter,
            final Metatype<T> metatype) {
        return new MutableCollectionPropertyModelImpl<>(name, metatype, getter, setter, add, addAll, remove, removeAll);
    }

    @Override
    public <E, T> ImmutableCollectionPropertyModel<E, T> createImmutableCollectionProperty(
            final String name,
            final Function<E, Collection<T>> getter,
            final Metatype<T> metatype) {
        return new ImmutableCollectionPropertyModelImpl<>(name, metatype, getter);
    }

    @Override
    public <E, K, V> ImmutableMapPropertyModel<E, K, V> createImmutableMapProperty(final String name,
                                                                                   final Function<E, Map<K, V>> getter,
                                                                                   final Metatype<V> metatype) {
        return new ImmutableMapPropertyModelImpl<>(name, metatype, getter);
    }

    @Override
    public <E, K, V> MutableMapPropertyModel<E, K, V> createMutableMapProperty(
            final String name,
            final Function<E, Map<K, V>> getter,
            final BiConsumer<E, Map<K, V>> setter,
            final BiConsumer<E, Map.Entry<K, V>> putter,
            final BiConsumer<E, Map<K, V>> allPutter,
            final BiFunction<E, K, V> remover,
            final Metatype<V> metatype) {
        return new MutableMapPropertyModelImpl<>(name, metatype, getter, setter, putter, allPutter, remover);
    }


}
