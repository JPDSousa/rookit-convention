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

import java.util.Collection;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface PropertyModelFactory {

    <E, T> MutablePropertyModel<E, T> createMutableProperty(String name,
                                                            BiConsumer<E, T> setter,
                                                            Function<E, T> getter,
                                                            MetaType<T> metaType);

    <E, T> ImmutablePropertyModel<E, T> createImmutableProperty(String name,
                                                                Function<E, T> getter,
                                                                MetaType<T> metaType);

    <E, T> MutableOptionalPropertyModel<E, T> createMutableOptionalProperty(String name,
                                                                            BiConsumer<E, T> setter,
                                                                            Function<E, Optional<T>> getter,
                                                                            MetaType<T> metaType);

    <E, T> ImmutableOptionalPropertyModel<E, T> createImmutableOptionalProperty(String name,
                                                                                Function<E, Optional<T>> getter,
                                                                                MetaType<T> metaType);

    <E, T> MutableCollectionPropertyModel<E, T> createMutableCollectionProperty(String name,
                                                                                BiConsumer<E, Collection<T>> setter,
                                                                                BiConsumer<E, T> add,
                                                                                BiConsumer<E, Collection<T>> addAll,
                                                                                BiConsumer<E, T> remove,
                                                                                BiConsumer<E, Collection<T>> removeAll,
                                                                                Function<E, Collection<T>> getter,
                                                                                MetaType<T> metaType);

    <E, T> ImmutableCollectionPropertyModel<E, T> createImmutableCollectionProperty(String name,
                                                                                    Function<E, Collection<T>> getter,
                                                                                    MetaType<T> metaType);

    <E, K, V> ImmutableMapPropertyModel<E, K, V> createImmutableMapProperty(String name,
                                                                            Function<E, Map<K, V>> getter,
                                                                            MetaType<V> metaType);

    <E, K, V> MutableMapPropertyModel<E, K, V> createMutableMapProperty(String name,
                                                                        Function<E, Map<K, V>> getter,
                                                                        BiConsumer<E, Map<K, V>> setter,
                                                                        BiConsumer<E, Map.Entry<K, V>> putter,
                                                                        BiConsumer<E, Map<K, V>> allPutter,
                                                                        BiFunction<E, K, V> remover,
                                                                        MetaType<V> metaType);

}
