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
package org.rookit.convention.property.source.javapoet;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeVariableName;
import one.util.streamex.StreamEx;
import org.rookit.auto.source.spec.SpecFactory;
import org.rookit.auto.source.spec.parameter.Parameter;
import org.rookit.auto.spec.AutoSpecFactories;
import org.rookit.convention.auto.config.NamingConfig;
import org.rookit.convention.auto.javax.visitor.ConventionTypeElementVisitor;
import org.rookit.convention.guice.MetaType;
import org.rookit.convention.property.guice.PropertyModel;

import java.util.Set;

@SuppressWarnings("MethodMayBeStatic")
public final class JavaPoetModule extends AbstractModule {

    private static final Module MODULE = new JavaPoetModule();

    public static Module getModule() {
        return MODULE;
    }

    private JavaPoetModule() {}

    @SuppressWarnings({"AnonymousInnerClassMayBeStatic", "AnonymousInnerClass", "EmptyClass"})
    @Override
    protected void configure() {
        bind(new TypeLiteral<ConventionTypeElementVisitor<StreamEx<Parameter<ParameterSpec>>, Void>>() {})
                .annotatedWith(PropertyModel.class)
                .to(MetaTypePropertyJavaPoetParameterVisitor.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    @PropertyModel(includeAnnotations = true)
    ConventionTypeElementVisitor<StreamEx<Parameter<ParameterSpec>>, Void> parameterFactoryWithAnnotations(
            @MetaType(includeAnnotations = true) final ConventionTypeElementVisitor<
                    StreamEx<ParameterSpec>, Void> baseFactory,
            @MetaType final TypeVariableName variableName,
            final NamingConfig namingConfig) {
        return new MetaTypePropertyJavaPoetParameterVisitor(baseFactory, variableName, namingConfig);
    }

    @Provides
    @Singleton
    @PropertyModel
    SpecFactory<MethodSpec> metaTypePropertyMethodFactory(
            final AutoSpecFactories autoFactories,
            @PropertyModel final Set<SpecFactory<MethodSpec>> methodFactories) {
        return autoFactories.createMultiFactory(methodFactories);
    }
}
