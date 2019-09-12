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
package org.rookit.convention.auto.javapoet.parameter;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.squareup.javapoet.ParameterSpec;
import one.util.streamex.StreamEx;
import org.rookit.auto.javax.visitor.ExtendedElementVisitor;
import org.rookit.auto.source.spec.parameter.Parameter;
import org.rookit.auto.source.spec.parameter.ParameterVisitors;
import org.rookit.convention.auto.javapoet.naming.JavaPoetPropertyNamingFactory;
import org.rookit.convention.auto.javax.ConventionTypeElement;
import org.rookit.convention.auto.javax.naming.PropertyIdentifierFactory;
import org.rookit.convention.auto.javax.visitor.ConventionTypeElementVisitor;
import org.rookit.convention.auto.javax.visitor.ConventionTypeElementVisitors;
import org.rookit.convention.auto.property.Property;
import org.rookit.convention.guice.MetaType;
import org.rookit.convention.property.guice.PropertyModel;
import org.rookit.guice.auto.annotation.Guice;

import java.util.Set;
import java.util.function.BiFunction;

public final class ParameterModule extends AbstractModule {

    private static final Module MODULE = new ParameterModule();

    public static Module getModule() {
        return MODULE;
    }

    private ParameterModule() {}

    @Override
    protected void configure() {
        bind(ConventionParameterVisitors.class).to(ConventionParameterVisitorsImpl.class).in(Singleton.class);
        bindBaseParameterVisitor();
    }

    @SuppressWarnings({"AnonymousInnerClassMayBeStatic", "AnonymousInnerClass", "EmptyClass"})
    private void bindBaseParameterVisitor() {
        final Multibinder<ConventionTypeElementVisitor<StreamEx<Parameter<ParameterSpec>>, Void>> mBinder = Multibinder
                .newSetBinder(binder(), new TypeLiteral<ConventionTypeElementVisitor<
                        StreamEx<Parameter<ParameterSpec>>, Void>>() {}, PropertyModel.class);
        mBinder.addBinding().toProvider(EmptyParameterProvider.class);
    }

    @Provides
    @Singleton
    @MetaType(includeAnnotations = true)
    BiFunction<Property, ParameterSpec, ParameterSpec> conversionFunction(
            @Guice final PropertyIdentifierFactory identifierFactory) {
        return new MetaTypeParameterWithAnnotationsResultAccumulator(identifierFactory);
    }


    @Provides
    @Singleton
    @MetaType
    ExtendedElementVisitor<StreamEx<ParameterSpec>, Void> createMetaTypeFactory(
            final ConventionTypeElementVisitors visitors,
            @MetaType final BiFunction<ConventionTypeElement, Property, StreamEx<ParameterSpec>> transformation) {
        return visitors.<ParameterSpec, Void>createPropertyLevelVisitor(transformation)
                .build();
    }

    @Provides
    @Singleton
    @MetaType(includeAnnotations = true)
    ExtendedElementVisitor<StreamEx<Parameter<ParameterSpec>>, Void> createAnnotatedExtendedParameterFactory(
            final ParameterVisitors visitors,
            @MetaType(includeAnnotations = true) final ExtendedElementVisitor<StreamEx<ParameterSpec>, Void> visitor) {
        return visitors.parameterBuilder(visitor)
                .asParameter()
                .build();
    }


    @Provides
    @Singleton
    @MetaType(includeAnnotations = true)
    ExtendedElementVisitor<StreamEx<ParameterSpec>, Void> createAnnotatedMetaTypeFactory(
            final ConventionTypeElementVisitors visitors,
            @MetaType(includeAnnotations = true) final BiFunction<ConventionTypeElement, Property,
                    StreamEx<ParameterSpec>> transformation) {
        return visitors.<ParameterSpec, Void>createPropertyLevelVisitor(transformation)
                .build();
    }

    @Provides
    @Singleton
    @MetaType
    BiFunction<ConventionTypeElement, Property, StreamEx<ParameterSpec>> baseTransformation(
            @MetaType final JavaPoetPropertyNamingFactory factory) {
        return new MetaTypeParameterTransformation(
                factory,
                (property, parameterSpec) -> parameterSpec
        );
    }

    @Provides
    @Singleton
    @MetaType(includeAnnotations = true)
    BiFunction<ConventionTypeElement, Property, StreamEx<ParameterSpec>> annotationTransformation(
            @MetaType final JavaPoetPropertyNamingFactory factory,
            @MetaType(includeAnnotations = true) final BiFunction<Property,
                    ParameterSpec, ParameterSpec> resultAccumulator) {
        return new MetaTypeParameterTransformation(
                factory,
                resultAccumulator
        );
    }

    @Provides
    @Singleton
    @PropertyModel
    ConventionTypeElementVisitor<StreamEx<Parameter<ParameterSpec>>, Void> propertyParameterFactory(
            final ConventionParameterVisitors elementVisitors,
            @PropertyModel final Set<ConventionTypeElementVisitor<
                                StreamEx<Parameter<ParameterSpec>>, Void>> visitors) {
        return elementVisitors.streamExConventionBuilder(visitors).build();
    }
}
