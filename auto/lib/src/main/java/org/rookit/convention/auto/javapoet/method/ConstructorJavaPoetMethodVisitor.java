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

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import one.util.streamex.MoreCollectors;
import one.util.streamex.StreamEx;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.auto.javax.visitor.ExtendedElementVisitor;
import org.rookit.auto.javax.visitor.StreamExtendedElementVisitor;
import org.rookit.auto.source.spec.parameter.Parameter;
import org.rookit.convention.auto.javax.ConventionTypeElement;
import org.rookit.convention.auto.javax.visitor.ConventionTypeElementVisitor;

import javax.lang.model.element.Modifier;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

final class ConstructorJavaPoetMethodVisitor<P> implements StreamExtendedElementVisitor<MethodSpec, P>,
        ConventionTypeElementVisitor<StreamEx<MethodSpec>, P> {

    private final ExtendedElementVisitor<StreamEx<Parameter<ParameterSpec>>, P> annotationParameterVisitor;
    private final String separator;
    private final Collector<CharSequence, ?, CodeBlock> collector;

    ConstructorJavaPoetMethodVisitor(
            final ExtendedElementVisitor<StreamEx<Parameter<ParameterSpec>>, P> aParameterVisitor,
            final String separator) {
        this.annotationParameterVisitor = aParameterVisitor;
        this.separator = separator;
        this.collector = MoreCollectors.collectingAndThen(Collectors.joining(", "),
                args -> CodeBlock.of("super($T.of($L))", ImmutableSet.class, args));
    }

    private CodeBlock createSuperStatement(final Collection<Parameter<ParameterSpec>> parameters) {
        return StreamEx.of(parameters)
                .filter(Parameter::isSuper)
                .map(Parameter::spec)
                .map(extendedParameter -> extendedParameter.name)
                .collect(this.collector);
    }

    @Override
    public StreamEx<MethodSpec> visitType(final ExtendedTypeElement extendedType, final P parameter) {
        final List<Parameter<ParameterSpec>> parameters = this.annotationParameterVisitor
                .visitType(extendedType, parameter)
                .toImmutableList();

        final Collection<CodeBlock> blocks = StreamEx.of(parameters)
                .map(Parameter::spec)
                .map(parameterSpec -> parameterSpec.name)
                .map(name -> CodeBlock.of("this.$L = $L", name, name))
                .collect(Collectors.toSet());

        return StreamEx.of(
                MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PRIVATE)
                        .addAnnotation(Inject.class)
                        .addParameters(Collections2.transform(parameters, Parameter::spec))
                        .addStatement(createSuperStatement(parameters))
                        .addStatement(CodeBlock.join(blocks, ";" + this.separator))
                        .build()
        );
    }

    @Override
    public String toString() {
        return "ConstructorJavaPoetMethodVisitor{" +
                "annotationParameterVisitor=" + this.annotationParameterVisitor +
                ", separator='" + this.separator + '\'' +
                ", collector=" + this.collector +
                "}";
    }

    @Override
    public StreamEx<MethodSpec> visitConventionType(final ConventionTypeElement element, final P parameter) {
        return visitType(element, parameter);
    }
}
