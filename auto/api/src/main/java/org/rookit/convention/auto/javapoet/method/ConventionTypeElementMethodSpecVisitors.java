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

import com.google.common.collect.ImmutableList;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeVariableName;
import one.util.streamex.StreamEx;
import org.rookit.auto.javapoet.method.MethodSpecFactory;
import org.rookit.auto.javapoet.naming.JavaPoetNamingFactory;
import org.rookit.auto.javapoet.naming.JavaPoetParameterResolver;
import org.rookit.auto.javax.executable.ExtendedExecutableElement;
import org.rookit.auto.javax.visitor.ExtendedElementVisitor;
import org.rookit.auto.source.spec.parameter.Parameter;
import org.rookit.convention.auto.javax.visitor.ConventionTypeElementVisitor;
import org.rookit.convention.auto.javax.visitor.ConventionTypeElementVisitors;
import org.rookit.convention.auto.property.Property;
import org.rookit.utils.string.template.Template1;

import java.util.Collection;
import java.util.function.Function;

public interface ConventionTypeElementMethodSpecVisitors extends ConventionTypeElementVisitors {

    <V extends ConventionTypeElementVisitor<StreamEx<MethodSpec>, P>, P> StreamExMethodSpecBuilder<V, P>
    constructorVisitorBuilder(
            ExtendedElementVisitor<StreamEx<Parameter<ParameterSpec>>, P> aParameterVisitor,
            String separator,
            Function<ConventionTypeElementVisitor<StreamEx<MethodSpec>, P>, V> downcastAdapter);

    default <P> StreamExMethodSpecBuilder<ConventionTypeElementVisitor<StreamEx<MethodSpec>, P>, P>
    constructorVisitorBuilder(
            final ExtendedElementVisitor<StreamEx<Parameter<ParameterSpec>>, P> aParameterVisitor,
            final String separator) {
        return constructorVisitorBuilder(aParameterVisitor, separator, element -> element);
    }

    <V extends ConventionTypeElementVisitor<StreamEx<MethodSpec>, P>, P> StreamExMethodSpecBuilder<V, P>
    factoryVisitorBuilder(
            ExtendedElementVisitor<StreamEx<Parameter<ParameterSpec>>, P> parameterFactory,
            JavaPoetNamingFactory namingFactory,
            JavaPoetParameterResolver parameterResolver,
            TypeVariableName variableName,
            Function<ConventionTypeElementVisitor<StreamEx<MethodSpec>, P>, V> downcastAdapter);

    default <P> StreamExMethodSpecBuilder<ConventionTypeElementVisitor<StreamEx<MethodSpec>, P>, P>
    factoryVisitorBuilder(
            final ExtendedElementVisitor<StreamEx<Parameter<ParameterSpec>>, P> parameterFactory,
            final JavaPoetNamingFactory namingFactory,
            final JavaPoetParameterResolver parameterResolver,
            final TypeVariableName variableName) {
        return factoryVisitorBuilder(parameterFactory, namingFactory, parameterResolver, variableName,
                                     element -> element);
    }

    <V extends ConventionTypeElementVisitor<StreamEx<MethodSpec>, P>, P> StreamExMethodSpecBuilder<V, P>
    templateMethodSpecVisitorBuilder(
            MethodSpecFactory methodSpecFactory,
            Template1 template,
            Function<Property, Collection<ParameterSpec>> parameterResolver,
            Function<ConventionTypeElementVisitor<StreamEx<MethodSpec>, P>, V> downcastAdapter);

    default <P> StreamExMethodSpecBuilder<ConventionTypeElementVisitor<StreamEx<MethodSpec>, P>, P>
    templateMethodSpecVisitorBuilder(
            final MethodSpecFactory methodSpecFactory,
            final Template1 template,
            final Function<Property, Collection<ParameterSpec>> parameterResolver) {
        return templateMethodSpecVisitorBuilder(methodSpecFactory, template, parameterResolver, element -> element);
    }

    default <V extends ConventionTypeElementVisitor<StreamEx<MethodSpec>, P>, P> StreamExMethodSpecBuilder<V, P>
    templateMethodSpecVisitorBuilder(
            final Function<ConventionTypeElementVisitor<StreamEx<MethodSpec>, P>, V> downcastAdapter,
            final MethodSpecFactory methodSpecFactory,
            final Template1 template) {
        return templateMethodSpecVisitorBuilder(methodSpecFactory, template, property -> ImmutableList.of(),
                                                downcastAdapter);
    }

    default <P> StreamExMethodSpecBuilder<ConventionTypeElementVisitor<StreamEx<MethodSpec>, P>, P>
    templateMethodSpecVisitorBuilder(
            final MethodSpecFactory methodSpecFactory,
            final Template1 template) {
        return templateMethodSpecVisitorBuilder(methodSpecFactory, template, property -> ImmutableList.of());
    }

    <V extends ConventionTypeElementVisitor<StreamEx<MethodSpec>, P>, P>
    StreamExMethodSpecBuilder<V, P> streamExMethodBuilder(
            V visitor,
            Function<ConventionTypeElementVisitor<StreamEx<MethodSpec>, P>, V> downcastAdapter);

    default <P>
    StreamExMethodSpecBuilder<ConventionTypeElementVisitor<StreamEx<MethodSpec>, P>, P> streamExMethodBuilder(
            final ConventionTypeElementVisitor<StreamEx<MethodSpec>, P> visitor) {
        return streamExMethodBuilder(visitor, element -> element);
    }

    <V extends ConventionTypeElementVisitor<StreamEx<MethodSpec>, P>, P>
    StreamExMethodSpecBuilder<V, P> getterMethodBuilder(
            ExtendedExecutableElement executable,
            Function<ConventionTypeElementVisitor<StreamEx<MethodSpec>, P>, V> downcastAdapter);

    default <P> StreamExMethodSpecBuilder<ConventionTypeElementVisitor<StreamEx<MethodSpec>, P>, P>
    getterMethodBuilder(final ExtendedExecutableElement executable) {
        return getterMethodBuilder(executable, element -> element);
    }

}
