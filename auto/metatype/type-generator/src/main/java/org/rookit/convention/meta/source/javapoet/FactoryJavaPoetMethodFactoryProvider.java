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
package org.rookit.convention.meta.source.javapoet;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeVariableName;
import one.util.streamex.StreamEx;
import org.rookit.auto.javapoet.naming.JavaPoetNamingFactory;
import org.rookit.auto.javapoet.naming.JavaPoetParameterResolver;
import org.rookit.auto.source.spec.parameter.Parameter;
import org.rookit.convention.auto.javapoet.method.ConventionTypeElementMethodSpecVisitors;
import org.rookit.convention.auto.javax.visitor.ConventionTypeElementVisitor;
import org.rookit.convention.guice.MetaType;

final class FactoryJavaPoetMethodFactoryProvider
        implements Provider<ConventionTypeElementVisitor<StreamEx<MethodSpec>, Void>> {

    private final ConventionTypeElementMethodSpecVisitors visitors;
    private final ConventionTypeElementVisitor<StreamEx<Parameter<ParameterSpec>>, Void> parameterFactory;
    private final JavaPoetParameterResolver parameterResolver;
    private final JavaPoetNamingFactory namingFactory;
    private final TypeVariableName variableName;

    @Inject
    private FactoryJavaPoetMethodFactoryProvider(
            final ConventionTypeElementMethodSpecVisitors visitors,
            @MetaType final ConventionTypeElementVisitor<StreamEx<Parameter<ParameterSpec>>, Void> parameterFactory,
            @MetaType final JavaPoetParameterResolver parameterResolver,
            @MetaType final JavaPoetNamingFactory namingFactory,
            @MetaType final TypeVariableName variableName) {
        this.visitors = visitors;
        this.parameterFactory = parameterFactory;
        this.parameterResolver = parameterResolver;
        this.namingFactory = namingFactory;
        this.variableName = variableName;
    }

    @Override
    public ConventionTypeElementVisitor<StreamEx<MethodSpec>, Void> get() {
        return this.visitors.factoryVisitorBuilder(
                this.parameterFactory,
                this.namingFactory,
                this.parameterResolver,
                this.variableName
        ).build();
    }

    @Override
    public String toString() {
        return "FactoryJavaPoetMethodFactoryProvider{" +
                "visitors=" + this.visitors +
                ", parameterFactory=" + this.parameterFactory +
                ", parameterResolver=" + this.parameterResolver +
                ", namingFactory=" + this.namingFactory +
                ", variableName=" + this.variableName +
                "}";
    }
}
