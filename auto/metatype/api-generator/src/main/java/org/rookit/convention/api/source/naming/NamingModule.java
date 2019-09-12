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
package org.rookit.convention.api.source.naming;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.squareup.javapoet.TypeVariableName;
import org.rookit.auto.javapoet.naming.JavaPoetNamingFactory;
import org.rookit.auto.javapoet.naming.JavaPoetParameterResolver;
import org.rookit.auto.javapoet.naming.LeafSingleParameterResolver;
import org.rookit.convention.api.guice.Container;
import org.rookit.convention.auto.config.MetatypeApiConfig;
import org.rookit.convention.auto.metatype.guice.MetaTypeAPI;
import org.rookit.convention.auto.metatype.guice.PartialMetaTypeAPI;
import org.rookit.convention.guice.MetaType;
import org.rookit.utils.guice.Self;

import static org.rookit.auto.guice.RookitAutoModuleTools.bindNaming;

@SuppressWarnings("MethodMayBeStatic")
public final class NamingModule extends AbstractModule {

    private static final Module MODULE = new NamingModule();

    public static Module getModule() {
        return MODULE;
    }

    private NamingModule() {}

    @Override
    protected void configure() {
        bindNaming(binder(), MetaTypeAPI.class).toProvider(MetatypeAPIJavaPoetNamingFactoryProvider.class)
                .in(Singleton.class);
        bindNaming(binder(), PartialMetaTypeAPI.class).toProvider(PartialMetatypeAPIJavaPoetNamingFactoryProvider.class)
                .in(Singleton.class);

        bind(JavaPoetParameterResolver.class).annotatedWith(PartialMetaTypeAPI.class)
                .to(PartialMetaTypeParameterResolver.class).in(Singleton.class);

        bind(JavaPoetParameterResolver.class).annotatedWith(Container.class)
                .to(PropertyParameterResolver.class).in(Singleton.class);

        bind(JavaPoetNamingFactory.class).to(Key.get(JavaPoetNamingFactory.class, MetaType.class));
    }

    @Provides
    @Singleton
    @MetaTypeAPI
    TypeVariableName parameterName(final MetatypeApiConfig config) {
        return config.parameterName();
    }

    @Singleton
    @Provides
    @MetaTypeAPI
    JavaPoetParameterResolver metaTypeResolver(@Self final JavaPoetNamingFactory entity,
                                               @PartialMetaTypeAPI final JavaPoetNamingFactory partialEntity) {
        return LeafSingleParameterResolver.create(entity, partialEntity);
    }
}
