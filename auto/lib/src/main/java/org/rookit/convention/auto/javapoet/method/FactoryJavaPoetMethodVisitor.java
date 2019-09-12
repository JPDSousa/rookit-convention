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
package org.rookit.convention.auto.javapoet.method;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeVariableName;
import one.util.streamex.StreamEx;
import org.rookit.auto.javapoet.naming.JavaPoetNamingFactory;
import org.rookit.auto.javapoet.naming.JavaPoetParameterResolver;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.auto.javax.visitor.ExtendedElementVisitor;
import org.rookit.auto.javax.visitor.StreamExtendedElementVisitor;
import org.rookit.auto.source.spec.parameter.Parameter;
import org.rookit.convention.auto.javax.ConventionTypeElement;
import org.rookit.convention.auto.javax.visitor.ConventionTypeElementVisitor;

import java.util.Collection;
import java.util.stream.Collectors;

import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

// TODO this might be more generified
final class FactoryJavaPoetMethodVisitor<P> implements StreamExtendedElementVisitor<MethodSpec, P>,
        ConventionTypeElementVisitor<StreamEx<MethodSpec>, P> {

    private final ExtendedElementVisitor<StreamEx<Parameter<ParameterSpec>>, P> parameterFactory;
    private final JavaPoetParameterResolver parameterResolver;
    private final JavaPoetNamingFactory namingFactory;
    private final TypeVariableName variableName;

    FactoryJavaPoetMethodVisitor(
            final ExtendedElementVisitor<StreamEx<Parameter<ParameterSpec>>, P> parameterFactory,
            final JavaPoetNamingFactory namingFactory,
            final JavaPoetParameterResolver parameterResolver,
            final TypeVariableName variableName) {
        this.namingFactory = namingFactory;
        this.parameterFactory = parameterFactory;
        this.variableName = variableName;
        this.parameterResolver = parameterResolver;
    }

    @Override
    public StreamEx<MethodSpec> visitType(final ExtendedTypeElement extendedType, final P parameter) {
        final Collection<ParameterSpec> parameters = this.parameterFactory.visitType(extendedType, parameter)
                .map(Parameter::spec)
                .toImmutableList();
        final String paramString = StreamEx.of(parameters)
                .map(parameterSpec -> parameterSpec.name)
                .collect(Collectors.joining(", "));

        return StreamEx.of(
                MethodSpec.methodBuilder("createMutable")
                        .addModifiers(PUBLIC, STATIC)
                        .addTypeVariable(this.variableName)
                        .returns(this.parameterResolver.resolveParameters(extendedType, this.variableName))
                        .addParameters(parameters)
                        .addStatement("return new $T($L)", this.namingFactory.classNameFor(extendedType), paramString)
                        .build()
        );
    }

    @Override
    public String toString() {
        return "FactoryJavaPoetMethodVisitor{" +
                "parameterFactory=" + this.parameterFactory +
                ", parameterResolver=" + this.parameterResolver +
                ", namingFactory=" + this.namingFactory +
                ", variableName=" + this.variableName +
                "}";
    }

    @Override
    public StreamEx<MethodSpec> visitConventionType(final ConventionTypeElement element, final P parameter) {
        return visitType(element, parameter);
    }
}
