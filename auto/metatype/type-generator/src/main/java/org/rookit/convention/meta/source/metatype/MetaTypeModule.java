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
import com.google.inject.TypeLiteral;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeVariableName;
import one.util.streamex.StreamEx;
import org.rookit.auto.javapoet.identifier.JavaPoetIdentifierFactories;
import org.rookit.auto.javapoet.method.EntityMethodFactory;
import org.rookit.auto.javapoet.naming.JavaPoetNamingFactories;
import org.rookit.auto.javapoet.naming.JavaPoetNamingFactory;
import org.rookit.auto.javapoet.naming.JavaPoetParameterResolver;
import org.rookit.auto.javapoet.type.TypeVariableNameResolver;
import org.rookit.auto.javax.naming.IdentifierFactory;
import org.rookit.auto.javax.naming.NamingFactory;
import org.rookit.auto.source.CodeSourceContainerFactory;
import org.rookit.auto.source.CodeSourceFactory;
import org.rookit.auto.source.type.SingleTypeSourceFactory;
import org.rookit.convention.auto.config.ConventionMetatypeConfig;
import org.rookit.convention.auto.entity.BasePartialEntityFactory;
import org.rookit.convention.auto.entity.nopartial.NoPartialEntityFactory;
import org.rookit.convention.auto.entity.parent.ParentExtractor;
import org.rookit.convention.auto.metatype.guice.MetaTypeAPI;
import org.rookit.convention.auto.javapoet.naming.JavaPoetPropertyNamingFactories;
import org.rookit.convention.auto.javapoet.naming.JavaPoetPropertyNamingFactory;
import org.rookit.convention.auto.javax.ConventionTypeElementFactory;
import org.rookit.convention.auto.javax.visitor.ConventionTypeElementVisitor;
import org.rookit.convention.auto.property.PropertyTypeResolver;
import org.rookit.convention.guice.MetaType;
import org.rookit.convention.meta.guice.PartialMetatype;
import org.rookit.convention.property.guice.PropertyModel;
import org.rookit.utils.guice.Self;
import org.rookit.utils.optional.OptionalFactory;
import org.rookit.utils.string.template.Template1;

@SuppressWarnings("MethodMayBeStatic")
public final class MetaTypeModule extends AbstractModule {

    private static final Module MODULE = new MetaTypeModule();

    public static Module getModule() {
        return MODULE;
    }

    private MetaTypeModule() {}

    @SuppressWarnings({"AnonymousInnerClassMayBeStatic", "AnonymousInnerClass", "EmptyClass"})
    @Override
    protected void configure() {
        bind(SingleTypeSourceFactory.class).annotatedWith(PartialMetatype.class)
                .to(MetaTypePartialTypeSourceFactory.class).in(Singleton.class);
        bind(SingleTypeSourceFactory.class).annotatedWith(MetaType.class)
                .to(MetaTypeSingleTypeSourceFactory.class).in(Singleton.class);
        bind(new TypeLiteral<ConventionTypeElementVisitor<StreamEx<FieldSpec>, Void>>() {})
                .annotatedWith(MetaType.class).to(MetaTypeFieldVisitor.class).in(Singleton.class);
        bind(new TypeLiteral<ConventionTypeElementVisitor<StreamEx<MethodSpec>, Void>>() {})
                .annotatedWith(MetaType.class).to(MetaTypeMethodVisitor.class).in(Singleton.class);
        bind(NamingFactory.class).annotatedWith(MetaType.class)
                .to(Key.get(JavaPoetNamingFactory.class, MetaType.class)).in(Singleton.class);
        bind(EntityMethodFactory.class).annotatedWith(MetaType.class)
                .to(MetaTypeEntityMethodFactory.class).in(Singleton.class);
        bind(TypeVariableName.class).annotatedWith(MetaType.class).toInstance(TypeVariableName.get("T"));
        bind(JavaPoetParameterResolver.class).annotatedWith(MetaType.class).to(MetatypeParameterResolver.class)
                .in(Singleton.class);
        bind(PropertyTypeResolver.class).annotatedWith(MetaType.class)
                .to(PropertyTypeResolver.class).in(Singleton.class);
        bind(TypeVariableNameResolver.class).annotatedWith(MetaType.class)
                .to(MetatypeTypeVariableNameResolver.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    @MetaType
    JavaPoetNamingFactory namingFactory(final JavaPoetNamingFactories factories,
                                        final ConventionMetatypeConfig config,
                                        @Self final Template1 noopTemplate) {
        return factories.create(config.basePackage(), config.entityTemplate(), noopTemplate);
    }

    @Provides
    @Singleton
    CodeSourceFactory entityFactory(@MetaType final IdentifierFactory identifierFactory,
                                    @MetaType final SingleTypeSourceFactory typeSpecFactory,
                                    final OptionalFactory optionalFactory) {
        return NoPartialEntityFactory.create(identifierFactory, typeSpecFactory, optionalFactory);
    }

    @Provides
    @Singleton
    @MetaType
    CodeSourceFactory partialEntityFactory(@MetaType final IdentifierFactory identifierFactory,
                                           @PartialMetatype final SingleTypeSourceFactory typeSourceFactory,
                                           final OptionalFactory optionalFactory,
                                           final ParentExtractor extractor,
                                           final CodeSourceContainerFactory containerFactory,
                                           final ConventionTypeElementFactory elementFactory) {
        return BasePartialEntityFactory.create(identifierFactory, typeSourceFactory, optionalFactory,
                extractor, containerFactory, elementFactory);
    }

    @Provides
    @Singleton
    @PropertyModel
    CodeSourceFactory metatypePropertyEntityFactory(final OptionalFactory optionalFactory,
                                                    @MetaType final IdentifierFactory identifierFactory,
                                                    @MetaType final SingleTypeSourceFactory typeSpecFactory) {
        return NoPartialEntityFactory.create(identifierFactory, typeSpecFactory, optionalFactory);
    }

    @Provides
    @Singleton
    @MetaType
    IdentifierFactory identifierFactory(final JavaPoetIdentifierFactories factories,
                                        @MetaType final NamingFactory namingFactory) {
        return factories.create(namingFactory);
    }

    @Provides
    @Singleton
    @MetaType
    JavaPoetPropertyNamingFactory javaPoetPropertyNamingFactory(
            final JavaPoetPropertyNamingFactories factories,
            @MetaTypeAPI final JavaPoetNamingFactory namingFactory,
            @MetaType final TypeVariableName variableName,
            @MetaType final PropertyTypeResolver resolver) {
        return factories.createDispatcherFactory(factories.parameterWithoutVariableEntity(variableName),
                                                 resolver,
                                                 namingFactory);
    }

}
