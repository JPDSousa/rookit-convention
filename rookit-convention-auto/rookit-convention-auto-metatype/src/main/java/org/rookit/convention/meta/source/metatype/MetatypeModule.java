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
package org.rookit.convention.meta.source.metatype;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.squareup.javapoet.TypeVariableName;
import org.rookit.auto.entity.BasePartialEntityFactory;
import org.rookit.auto.entity.EntityFactory;
import org.rookit.auto.entity.PartialEntityFactory;
import org.rookit.auto.entity.nopartial.NoPartialEntityFactory;
import org.rookit.auto.entity.parent.ParentExtractor;
import org.rookit.auto.identifier.BaseEntityIdentifierFactory;
import org.rookit.auto.identifier.EntityIdentifierFactory;
import org.rookit.auto.javapoet.FieldFactory;
import org.rookit.auto.javapoet.method.EntityMethodFactory;
import org.rookit.auto.javapoet.method.MethodFactory;
import org.rookit.auto.javapoet.naming.JavaPoetNamingFactory;
import org.rookit.auto.javapoet.naming.JavaPoetParameterResolver;
import org.rookit.auto.javapoet.naming.JavaPoetPropertyNamingFactory;
import org.rookit.auto.javapoet.type.TypeVariableNameResolver;
import org.rookit.auto.javax.element.ElementUtils;
import org.rookit.auto.javax.property.PropertyTypeResolver;
import org.rookit.auto.naming.BaseJavaPoetNamingFactory;
import org.rookit.auto.naming.NamingFactory;
import org.rookit.auto.source.SingleTypeSourceFactory;
import org.rookit.convention.meta.guice.Metatype;
import org.rookit.convention.meta.guice.MetatypeProperty;
import org.rookit.convention.meta.guice.PartialMetatype;
import org.rookit.convention.utils.ConventionPropertyResolver;
import org.rookit.convention.utils.config.ConventionMetatypeConfig;
import org.rookit.convention.utils.guice.MetatypeAPI;
import org.rookit.convention.utils.javapoet.MetatypeJavaPoetPropertyNamingFactory;
import org.rookit.utils.optional.OptionalFactory;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@SuppressWarnings("MethodMayBeStatic")
public final class MetatypeModule extends AbstractModule {

    private static final Module MODULE = new MetatypeModule();

    public static Module getModule() {
        return MODULE;
    }

    private MetatypeModule() {}

    @Override
    protected void configure() {
        bind(SingleTypeSourceFactory.class).annotatedWith(PartialMetatype.class)
                .to(MetatypePartialTypeSourceFactory.class).in(Singleton.class);
        bind(SingleTypeSourceFactory.class).annotatedWith(Metatype.class)
                .to(MetatypeSingleTypeSourceFactory.class).in(Singleton.class);
        bind(FieldFactory.class).annotatedWith(Metatype.class)
                .to(MetatypeFieldFactory.class).in(Singleton.class);
        bind(MethodFactory.class).annotatedWith(Metatype.class)
                .to(MetatypeMethodFactory.class).in(Singleton.class);
        bind(NamingFactory.class).annotatedWith(Metatype.class)
                .to(Key.get(JavaPoetNamingFactory.class, Metatype.class)).in(Singleton.class);
        bind(EntityMethodFactory.class).annotatedWith(Metatype.class)
                .to(MetatypeEntityMethodFactory.class).in(Singleton.class);
        bind(TypeVariableName.class).annotatedWith(Metatype.class).toInstance(TypeVariableName.get("T"));
        bind(JavaPoetParameterResolver.class).annotatedWith(Metatype.class).to(MetatypeParameterResolver.class)
                .in(Singleton.class);
        bind(PropertyTypeResolver.class).annotatedWith(Metatype.class)
                .to(ConventionPropertyResolver.class).in(Singleton.class);
        bind(TypeVariableNameResolver.class).annotatedWith(Metatype.class)
                .to(MetatypeTypeVariableNameResolver.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    @Metatype
    JavaPoetNamingFactory namingFactory(final ConventionMetatypeConfig config) {
        return BaseJavaPoetNamingFactory.create(config.basePackage(), config.entitySuffix(), EMPTY, EMPTY);
    }

    @Provides
    @Singleton
    EntityFactory entityFactory(@Metatype final EntityIdentifierFactory identifierFactory,
                                @Metatype final SingleTypeSourceFactory typeSpecFactory,
                                final OptionalFactory optionalFactory) {
        return NoPartialEntityFactory.create(identifierFactory, typeSpecFactory, optionalFactory);
    }

    @Provides
    @Singleton
    @Metatype
    PartialEntityFactory partialEntityFactory(@Metatype final EntityIdentifierFactory identifierFactory,
                                              @PartialMetatype final SingleTypeSourceFactory typeSourceFactory,
                                              final OptionalFactory optionalFactory,
                                              final ParentExtractor extractor) {
        return BasePartialEntityFactory.create(identifierFactory, typeSourceFactory, optionalFactory, extractor);
    }

    @Provides
    @Singleton
    @MetatypeProperty
    EntityFactory metatypePropertyEntityFactory(final OptionalFactory optionalFactory,
                                                @Metatype final EntityIdentifierFactory identifierFactory,
                                                @Metatype final SingleTypeSourceFactory typeSpecFactory) {
        return NoPartialEntityFactory.create(identifierFactory, typeSpecFactory, optionalFactory);
    }

    @Provides
    @Singleton
    @Metatype
    EntityIdentifierFactory identifierFactory(@Metatype final NamingFactory namingFactory) {
        return BaseEntityIdentifierFactory.create(namingFactory);
    }

    @Provides
    @Singleton
    @Metatype
    JavaPoetPropertyNamingFactory javaPoetPropertyNamingFactory(
            @MetatypeAPI final JavaPoetNamingFactory namingFactory,
            @Metatype final PropertyTypeResolver resolver,
            final ElementUtils utils,
            @Metatype final TypeVariableName typeVariableName) {
        return MetatypeJavaPoetPropertyNamingFactory.create(
                resolver,
                utils,
                typeVariableName,
                namingFactory,
                false);
    }

}
