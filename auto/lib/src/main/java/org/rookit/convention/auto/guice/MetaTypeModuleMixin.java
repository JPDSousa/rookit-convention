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
package org.rookit.convention.auto.guice;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.multibindings.Multibinder;
import org.rookit.convention.guice.MetaTypeModelSerializer;
import org.rookit.convention.guice.MetaTypeModelType;
import org.rookit.convention.guice.MetaTypeProperties;
import org.rookit.convention.guice.MetaTypeProperty;

public interface MetaTypeModuleMixin<T> {

    Key<T> key();

    default Multibinder<T> bindMetaTypeProperties(final Binder binder) {
        final Key<T> key = key();
        final Multibinder<T> mBinder = Multibinder.newSetBinder(binder, key);

        bindMetaTypeType(key, mBinder.addBinding());
        bindMetaTypeSerializer(key, mBinder.addBinding());
        bindMetaTypeProperty(key, mBinder.addBinding());
        bindMetaTypeProperties(key, mBinder.addBinding());

        return mBinder;
    }

    default void bindMetaTypeType(final Key<T> key, final LinkedBindingBuilder<T> bindingBuilder) {
        final Key<T> targetKey = Key.get(key.getTypeLiteral(), MetaTypeModelType.class);
        bindingBuilder.to(targetKey).in(Singleton.class);
    }

    default void bindMetaTypeSerializer(final Key<T> key, final LinkedBindingBuilder<T> bindingBuilder) {
        final Key<T> targetKey = Key.get(key.getTypeLiteral(), MetaTypeModelSerializer.class);
        bindingBuilder.to(targetKey).in(Singleton.class);
    }

    default void bindMetaTypeProperty(final Key<T> key, final LinkedBindingBuilder<T> bindingBuilder) {
        final Key<T> targetKey = Key.get(key.getTypeLiteral(), MetaTypeProperty.class);
        bindingBuilder.to(targetKey).in(Singleton.class);
    }

    default void bindMetaTypeProperties(final Key<T> key, final LinkedBindingBuilder<T> bindingBuilder) {
        final Key<T> targetKey = Key.get(key.getTypeLiteral(), MetaTypeProperties.class);
        bindingBuilder.to(targetKey).in(Singleton.class);
    }

}
