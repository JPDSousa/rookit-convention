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
package org.rookit.convention.guice.source.type;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.rookit.auto.javapoet.type.BaseTypeSourceAdapter;
import org.rookit.auto.javapoet.type.TypeSourceAdapter;
import org.rookit.auto.javax.ExtendedExecutableElementFactory;
import org.rookit.auto.javax.ExtendedTypeMirrorFactory;
import org.rookit.auto.javax.JavaxRepetitionFactory;
import org.rookit.auto.javax.element.BaseExtendedTypeElementFactory;
import org.rookit.auto.javax.element.ElementUtils;
import org.rookit.auto.javax.element.ExtendedTypeElementFactory;
import org.rookit.auto.javax.property.BaseExtendedPropertyFactory;
import org.rookit.auto.javax.property.BasePropertyExtractor;
import org.rookit.auto.javax.property.ExtendedPropertyFactory;
import org.rookit.auto.javax.property.PropertyExtractor;
import org.rookit.auto.javax.property.UnwrapperPropertyFactory;
import org.rookit.auto.naming.PackageReferenceFactory;
import org.rookit.auto.source.PropertyTypeSourceFactory;
import org.rookit.auto.source.SingleTypeSourceFactory;
import org.rookit.utils.optional.OptionalFactory;
import org.rookit.utils.primitive.VoidUtils;

import javax.annotation.processing.Messager;

@SuppressWarnings("MethodMayBeStatic")
public final class TypeModule extends AbstractModule {

    private static final Module MODULE = new TypeModule();

    public static Module getModule() {
        return MODULE;
    }

    private TypeModule() {}

    @Override
    protected void configure() {
        bind(SingleTypeSourceFactory.class).to(BindAnnotationEntityFactory.class).in(Singleton.class);
        bind(PropertyTypeSourceFactory.class).to(BindAnnotationPropertyFactory.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    TypeSourceAdapter typeSourceAdapter(final VoidUtils utils) {
        return BaseTypeSourceAdapter.create(utils);
    }

    @Provides
    @Singleton
    PropertyExtractor propertyExtractor(final ExtendedPropertyFactory propertyFactory,
                                        final ExtendedExecutableElementFactory executableFactory) {
        return BasePropertyExtractor.create(propertyFactory, executableFactory);
    }

    @Provides
    @Singleton
    ExtendedPropertyFactory extendedPropertyFactory(final JavaxRepetitionFactory repetitionFactory,
                                                    final ElementUtils utils,
                                                    final Provider<ExtendedTypeElementFactory> elementFactory) {
        final ExtendedPropertyFactory baseFactory = BaseExtendedPropertyFactory.create(repetitionFactory,
                utils,
                elementFactory);

        return UnwrapperPropertyFactory.create(baseFactory, repetitionFactory);
    }

    @Provides
    @Singleton
    ExtendedTypeElementFactory elementFactory(final PackageReferenceFactory packageFactory,
                                              final OptionalFactory optionalFactory,
                                              final PropertyExtractor extractor,
                                              final ElementUtils utils,
                                              final Messager messager,
                                              final ExtendedTypeMirrorFactory mirrorFactory) {
        return BaseExtendedTypeElementFactory.create(packageFactory, optionalFactory, extractor,
                utils, messager, mirrorFactory);
    }

}
