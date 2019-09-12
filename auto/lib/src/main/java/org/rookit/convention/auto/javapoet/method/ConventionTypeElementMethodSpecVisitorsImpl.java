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

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeVariableName;
import one.util.streamex.StreamEx;
import org.rookit.auto.javapoet.method.MethodSpecFactory;
import org.rookit.auto.javapoet.naming.JavaPoetNamingFactory;
import org.rookit.auto.javapoet.naming.JavaPoetParameterResolver;
import org.rookit.auto.javax.executable.ExtendedExecutableElement;
import org.rookit.auto.javax.visitor.ExtendedElementVisitor;
import org.rookit.auto.javax.visitor.GenericBuilder;
import org.rookit.auto.javax.visitor.StreamExBuilder;
import org.rookit.auto.source.spec.parameter.Parameter;
import org.rookit.convention.auto.javax.ConventionTypeElement;
import org.rookit.convention.auto.javax.visitor.ConventionTypeElementVisitor;
import org.rookit.convention.auto.javax.visitor.ConventionTypeElementVisitors;
import org.rookit.convention.auto.javax.visitor.GenericStreamExConventionBuilder;
import org.rookit.convention.auto.javax.visitor.StreamExConventionBuilder;
import org.rookit.convention.auto.property.Property;
import org.rookit.utils.string.template.Template1;

import javax.lang.model.element.Name;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;

final class ConventionTypeElementMethodSpecVisitorsImpl implements ConventionTypeElementMethodSpecVisitors {

    private final ConventionTypeElementVisitors delegate;

    @Inject
    private ConventionTypeElementMethodSpecVisitorsImpl(final ConventionTypeElementVisitors delegate) {
        this.delegate = delegate;
    }

    @Override
    public <V extends ConventionTypeElementVisitor<StreamEx<MethodSpec>, P>, P> StreamExMethodSpecBuilder<V, P>
    constructorVisitorBuilder(
            final ExtendedElementVisitor<StreamEx<Parameter<ParameterSpec>>, P> aParameterVisitor,
            final String separator,
            final Function<ConventionTypeElementVisitor<StreamEx<MethodSpec>, P>, V> downcastAdapter) {
        final V visitor = downcastAdapter.apply(new ConstructorJavaPoetMethodVisitor<>(aParameterVisitor, separator));
        return streamExMethodBuilder(visitor, downcastAdapter);
    }

    @Override
    public <V extends ConventionTypeElementVisitor<StreamEx<MethodSpec>, P>, P> StreamExMethodSpecBuilder<V, P>
    factoryVisitorBuilder(
            final ExtendedElementVisitor<StreamEx<Parameter<ParameterSpec>>, P> parameterFactory,
            final JavaPoetNamingFactory namingFactory,
            final JavaPoetParameterResolver parameterResolver,
            final TypeVariableName variableName,
            final Function<ConventionTypeElementVisitor<StreamEx<MethodSpec>, P>, V> downcastAdapter) {
        return streamExMethodBuilder(downcastAdapter.apply(new FactoryJavaPoetMethodVisitor<>(
                parameterFactory,
                namingFactory,
                parameterResolver,
                variableName
        )), downcastAdapter);
    }

    @Override
    public <V extends ConventionTypeElementVisitor<StreamEx<MethodSpec>, P>, P> StreamExMethodSpecBuilder<V, P>
    templateMethodSpecVisitorBuilder(
            final MethodSpecFactory methodSpecFactory,
            final Template1 template,
            final Function<Property, Collection<ParameterSpec>> parameterResolver,
            final Function<ConventionTypeElementVisitor<StreamEx<MethodSpec>, P>, V> downcastAdapter) {
        final V visitor = downcastAdapter.apply(new TemplateMethodVisitor<>(methodSpecFactory,
                                                                            template,
                                                                            parameterResolver));
        return streamExMethodBuilder(visitor, downcastAdapter);
    }

    @Override
    public <V extends ConventionTypeElementVisitor<StreamEx<MethodSpec>, P>, P>
    StreamExMethodSpecBuilder<V, P> streamExMethodBuilder(
            final V visitor,
            final Function<ConventionTypeElementVisitor<StreamEx<MethodSpec>, P>, V> downcastAdapter) {
        return new StreamExMethodSpecBuilderImpl<>(
                this.delegate.builder(visitor,
                                      element -> downcastToV(element, downcastAdapter)));
    }

    @Override
    public <V extends ConventionTypeElementVisitor<StreamEx<MethodSpec>, P>, P> StreamExMethodSpecBuilder<V, P>
    getterMethodBuilder(
            final ExtendedExecutableElement executable,
            final Function<ConventionTypeElementVisitor<StreamEx<MethodSpec>, P>, V> downcastAdapter) {
        final boolean isSingleParameter = executable.getReturnType().typeParameters().size() == 1;
        final ConventionTypeElementVisitor<StreamEx<MethodSpec>, P> visitor = isSingleParameter
                ? new Parameter1GetterMethodVisitor<>(executable)
                : new GetterMethodVisitor<>(executable.getSimpleName(), executable.getReturnType());
        return streamExMethodBuilder(downcastAdapter.apply(visitor), downcastAdapter);
    }

    @Override
    public <P> ExtendedElementVisitor<Name, P> qualifiedNameVisitor() {
        return this.delegate.qualifiedNameVisitor();
    }

    @Override
    public <B extends GenericBuilder<B, V, R, P>, V extends ExtendedElementVisitor<R, P>, R, P> B builder(
            final V visitor,
            final Function<ExtendedElementVisitor<R, P>, V> downcastAdapter) {
        return this.delegate.builder(visitor, downcastAdapter);
    }

    @Override
    public <B extends GenericBuilder<B, V, R, P>, V extends ExtendedElementVisitor<R, P>, R, P> B builder(
            final Provider<V> visitor,
            final Function<ExtendedElementVisitor<R, P>, V> downcastAdapter) {
        return this.delegate.builder(visitor, downcastAdapter);
    }

    @Override
    public <V extends ExtendedElementVisitor<StreamEx<R>, P>, R, P> StreamExBuilder<V, R, P> streamExBuilder(
            final V visitor,
            final Function<ExtendedElementVisitor<StreamEx<R>, P>, V> downcastAdapter) {
        return this.delegate.streamExBuilder(visitor, downcastAdapter);
    }

    @Override
    public <V extends ExtendedElementVisitor<StreamEx<R>, P>, R, P> StreamExBuilder<V, R, P> streamExBuilder(
            final Iterable<? extends V> visitors,
            final Function<ExtendedElementVisitor<StreamEx<R>, P>, V> downcastAdapter) {
        return this.delegate.streamExBuilder(visitors, downcastAdapter);
    }

    @Override
    public <R, P> ExtendedElementVisitor<StreamEx<R>, P> streamEx(
            final Collection<? extends ExtendedElementVisitor<R, P>> visitors) {
        return this.delegate.streamEx(visitors);
    }

    @Override
    public <R, P> ExtendedElementVisitor<StreamEx<R>, P> streamEx(final ExtendedElementVisitor<R, P> visitor) {
        return this.delegate.streamEx(visitor);
    }

    @Override
    public <R, P> ConventionTypeElementVisitor<R, P> downcastToConvention(final ExtendedElementVisitor<R, P> visitor) {
        return this.delegate.downcastToConvention(visitor);
    }

    @Override
    public <V extends ConventionTypeElementVisitor<R, P>, R, P> V downcastToV(
            final ExtendedElementVisitor<R, P> visitor,
            final Function<ConventionTypeElementVisitor<R, P>, V> downcast) {
        return this.delegate.downcastToV(visitor, downcast);
    }

    @Override
    public <P> ConventionTypeElementVisitor<Boolean, P> isPresent(final Class<? extends Annotation> annotationClass) {
        return this.delegate.isPresent(annotationClass);
    }

    @Override
    public <R, P> ConventionTypeElementVisitor<StreamEx<R>, P> emptyStreamVisitor() {
        return this.delegate.emptyStreamVisitor();
    }

    @Override
    public String toString() {
        return "ConventionTypeElementMethodSpecVisitorsImpl{" +
                "delegate=" + this.delegate +
                "}";
    }

    @Override
    public <V extends ConventionTypeElementVisitor<StreamEx<R>, P>, R, P> StreamExConventionBuilder<V, R, P>
    createPropertyLevelVisitor(
            final BiFunction<ConventionTypeElement, Property, StreamEx<R>> transformation,
            final Function<ConventionTypeElementVisitor<StreamEx<R>, P>, V> downcastAdapter) {
        return this.delegate.createPropertyLevelVisitor(transformation, downcastAdapter);
    }

    @Override
    public <B extends GenericStreamExConventionBuilder<B, V, R, P>,
            V extends ConventionTypeElementVisitor<StreamEx<R>, P>, R, P> B streamExConventionBuilder(
            final V visitor,
            final Function<ConventionTypeElementVisitor<StreamEx<R>, P>, V> downcastAdapter) {
        return this.delegate.streamExConventionBuilder(visitor, downcastAdapter);
    }

    @Override
    public <B extends GenericStreamExConventionBuilder<B, V, R, P>,
            V extends ConventionTypeElementVisitor<StreamEx<R>, P>, R, P> B streamExConventionBuilder(
            final Provider<V> visitor,
            final Function<ConventionTypeElementVisitor<StreamEx<R>, P>, V> downcastAdapter) {
        return this.delegate.streamExConventionBuilder(visitor, downcastAdapter);
    }

    @Override
    public <B extends GenericStreamExConventionBuilder<B, V, R, P>,
            V extends ConventionTypeElementVisitor<StreamEx<R>, P>, R, P> B streamExConventionBuilder(
            final Iterable<? extends V> visitors,
            final Function<ConventionTypeElementVisitor<StreamEx<R>, P>, V> downcastAdapter) {
        return this.delegate.streamExConventionBuilder(visitors, downcastAdapter);
    }

    @Override
    public <R, P> ConventionTypeElementVisitor<StreamEx<R>, P> streamExConvention(
            final ConventionTypeElementVisitor<R, P> visitor) {
        return this.delegate.streamExConvention(visitor);
    }

}
