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
package org.rookit.convention.property.source;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Modules;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeVariableName;
import one.util.streamex.StreamEx;
import org.rookit.auto.TypeProcessor;
import org.rookit.auto.javapoet.identifier.JavaPoetIdentifierFactories;
import org.rookit.auto.javapoet.naming.JavaPoetNamingFactories;
import org.rookit.auto.javax.naming.IdentifierFactory;
import org.rookit.auto.javax.naming.NamingFactory;
import org.rookit.auto.javax.visitor.ExtendedElementVisitor;
import org.rookit.auto.source.CodeSourceFactory;
import org.rookit.auto.source.spec.SpecFactory;
import org.rookit.auto.source.type.SingleTypeSourceFactory;
import org.rookit.convention.auto.ConventionLibModule;
import org.rookit.convention.auto.config.PropertyConfig;
import org.rookit.convention.auto.entity.PropertyEntityFactory;
import org.rookit.convention.auto.entity.property.PropertyFlatEntityFactory;
import org.rookit.convention.auto.property.ExtendedPropertyEvaluator;
import org.rookit.convention.auto.property.ExtendedPropertyExtractor;
import org.rookit.convention.auto.property.ExtendedPropertyExtractorFactory;
import org.rookit.convention.auto.property.Property;
import org.rookit.convention.guice.MetaType;
import org.rookit.convention.property.source.config.ConfigurationModule;
import org.rookit.convention.property.source.javapoet.JavaPoetModule;
import org.rookit.failsafe.FailsafeModule;
import org.rookit.io.path.PathModule;
import org.rookit.utils.guice.Self;
import org.rookit.utils.guice.UtilsModule;
import org.rookit.utils.string.template.Template1;

@SuppressWarnings("MethodMayBeStatic")
public final class SourceModule extends AbstractModule {

    private static final Module BASE_MODULE = Modules.override(
            org.rookit.convention.guice.source.SourceModule.getModule()
    ).with(
            org.rookit.convention.api.source.SourceModule.getModule()
    );

    private static final Module MODULE = Modules.override(
            BASE_MODULE
    ).with(
            new SourceModule(),
            ConfigurationModule.getModule(),
            ConventionLibModule.getModule(),
            FailsafeModule.getModule(),
            JavaPoetModule.getModule(),
            PathModule.getModule(),
            UtilsModule.getModule()
    );

    public static Module getModule() {
        return MODULE;
    }

    private SourceModule() {}

    @SuppressWarnings({"AnonymousInnerClassMayBeStatic", "AnonymousInnerClass", "EmptyClass"})
    @Override
    protected void configure() {
        bind(TypeProcessor.class).to(PropertyTypeProcessor.class).in(Singleton.class);
        bind(PropertyEntityFactory.class).to(MetaTypePropertyEntityFactory.class).in(Singleton.class);
        bind(ExtendedPropertyEvaluator.class).toInstance(Property::isContainer);
        bind(CodeSourceFactory.class).to(PropertyFlatEntityFactory.class).in(Singleton.class);
        bind(SingleTypeSourceFactory.class).to(PropertySingleTypeSourceFactory.class).in(Singleton.class);
        bind(new TypeLiteral<ExtendedElementVisitor<StreamEx<FieldSpec>, Void>>() {})
                .to(MetaTypePropertyFieldVisitor.class).in(Singleton.class);
        bind(new TypeLiteral<SpecFactory<MethodSpec>>() {})
                .to(Key.get(new TypeLiteral<SpecFactory<MethodSpec>>() {}, MetaType.class)).in(Singleton.class);
    }

    @Provides
    @Singleton
    IdentifierFactory identifierFactory(final JavaPoetIdentifierFactories factories,
                                        final NamingFactory namingFactory) {
        return factories.create(namingFactory);
    }

    @Provides
    @Singleton
    ExtendedPropertyExtractor propertyExtractor(final ExtendedPropertyExtractorFactory factory) {
        final ExtendedPropertyExtractor baseExtractor = factory.create(executableElement -> true);
        return factory.createRecursive(baseExtractor);
    }

    @Provides
    @Singleton
    NamingFactory namingFactory(final JavaPoetNamingFactories factories,
                                final PropertyConfig config,
                                @Self final Template1 noopTemplate) {
        return factories.create(config.basePackage(), config.entityTemplate(), noopTemplate);
    }

    @Provides
    @Singleton
    TypeVariableName typeVariableName(final PropertyConfig config) {
        return config.parameterName();
    }
}
