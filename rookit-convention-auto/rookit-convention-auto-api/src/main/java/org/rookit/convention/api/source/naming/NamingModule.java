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

import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.squareup.javapoet.TypeVariableName;
import org.rookit.auto.guice.Self;
import org.rookit.auto.identifier.BaseEntityIdentifierFactory;
import org.rookit.auto.identifier.EntityIdentifierFactory;
import org.rookit.auto.javapoet.naming.JavaPoetNamingFactory;
import org.rookit.auto.javapoet.naming.JavaPoetParameterResolver;
import org.rookit.auto.javapoet.naming.LeafSingleParameterResolver;
import org.rookit.auto.javapoet.naming.SelfJavaPoetNamingFactory;
import org.rookit.auto.naming.AbstractNamingModule;
import org.rookit.auto.naming.BaseJavaPoetNamingFactory;
import org.rookit.auto.naming.NamingFactory;
import org.rookit.auto.naming.PackageReferenceFactory;
import org.rookit.convention.api.guice.Container;
import org.rookit.convention.utils.config.ConventionApiConfig;
import org.rookit.convention.utils.guice.MetatypeAPI;
import org.rookit.convention.utils.guice.PartialMetatypeAPI;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@SuppressWarnings("MethodMayBeStatic")
public final class NamingModule extends AbstractNamingModule {

    private static final Module MODULE = new NamingModule();

    public static Module getModule() {
        return MODULE;
    }

    private NamingModule() {}

    @Override
    protected void configure() {
        bindNaming(MetatypeAPI.class);
        bindNaming(PartialMetatypeAPI.class);

        bind(JavaPoetParameterResolver.class).annotatedWith(PartialMetatypeAPI.class)
                .to(PartialMetaTypeParameterResolver.class).in(Singleton.class);

        bind(JavaPoetParameterResolver.class).annotatedWith(Container.class)
                .to(PropertyParameterResolver.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    @MetatypeAPI
    JavaPoetNamingFactory namingFactory(final ConventionApiConfig config) {
        return BaseJavaPoetNamingFactory.create(config.basePackage(), config.entitySuffix(), EMPTY, EMPTY);
    }

    @Provides
    @Singleton
    @PartialMetatypeAPI
    JavaPoetNamingFactory partialNamingFactory(final ConventionApiConfig config) {
        return BaseJavaPoetNamingFactory.create(config.basePackage(), config.entitySuffix(),
                config.partialEntityPrefix(), EMPTY);
    }

    @Provides
    @Singleton
    @MetatypeAPI
    TypeVariableName parameterName(final ConventionApiConfig config) {
        return config.parameterName();
    }

    @Singleton
    @Provides
    @MetatypeAPI
    JavaPoetParameterResolver metaTypeResolver(@Self final JavaPoetNamingFactory entity,
                                               @PartialMetatypeAPI final JavaPoetNamingFactory partialEntity) {
        return LeafSingleParameterResolver.create(entity, partialEntity);
    }

    @Singleton
    @Provides
    @Self
    JavaPoetNamingFactory selfNamingFactory(final PackageReferenceFactory factory) {
        return SelfJavaPoetNamingFactory.create(factory);
    }

    @Singleton
    @Provides
    @MetatypeAPI
    EntityIdentifierFactory metaTypeIdentifierFactory(@MetatypeAPI final NamingFactory namingFactory) {
        return BaseEntityIdentifierFactory.create(namingFactory);
    }

    @Singleton
    @Provides
    @PartialMetatypeAPI
    EntityIdentifierFactory partialMetaTypeIdentifierFactory(@PartialMetatypeAPI final NamingFactory namingFactory) {
        return BaseEntityIdentifierFactory.create(namingFactory);
    }
}
