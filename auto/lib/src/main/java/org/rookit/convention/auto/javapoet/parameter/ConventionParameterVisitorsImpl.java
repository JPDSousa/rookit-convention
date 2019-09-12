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

import com.google.inject.Inject;
import com.google.inject.Provider;
import one.util.streamex.StreamEx;
import org.rookit.auto.javax.visitor.ExtendedElementVisitor;
import org.rookit.auto.javax.visitor.GenericBuilder;
import org.rookit.auto.javax.visitor.StreamExBuilder;
import org.rookit.auto.source.spec.parameter.GenericParameterBuilder;
import org.rookit.auto.source.spec.parameter.ParameterVisitors;
import org.rookit.convention.auto.javax.ConventionTypeElement;
import org.rookit.convention.auto.javax.visitor.ConventionTypeElementVisitor;
import org.rookit.convention.auto.javax.visitor.ConventionTypeElementVisitors;
import org.rookit.convention.auto.javax.visitor.GenericStreamExConventionBuilder;
import org.rookit.convention.auto.javax.visitor.StreamExConventionBuilder;
import org.rookit.convention.auto.property.Property;

import javax.lang.model.element.Name;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;

final class ConventionParameterVisitorsImpl implements ConventionParameterVisitors {

    private final ParameterVisitors delegate;
    private final ConventionTypeElementVisitors conventionDelegate;

    @Inject
    private ConventionParameterVisitorsImpl(final ParameterVisitors visitors,
                                            final ConventionTypeElementVisitors conventionDelegate) {
        this.delegate = visitors;
        this.conventionDelegate = conventionDelegate;
    }

    @Override
    public <B extends GenericParameterBuilder<B, V, R, P>,
            V extends ExtendedElementVisitor<StreamEx<R>, P>, R, P> B parameterBuilder(
            final V visitor,
            final Function<ExtendedElementVisitor<StreamEx<R>, P>, V> downcastAdapter) {
        return this.delegate.parameterBuilder(visitor, downcastAdapter);
    }

    @Override
    public <P> ExtendedElementVisitor<Name, P> qualifiedNameVisitor() {
        return this.delegate.qualifiedNameVisitor();
    }

    @Override
    public <B extends GenericBuilder<B, V, R, P>,
            V extends ExtendedElementVisitor<R, P>, R, P> B builder(
            final V visitor,
            final Function<ExtendedElementVisitor<R, P>, V> downcastAdapter) {
        return this.delegate.builder(visitor, downcastAdapter);
    }

    @Override
    public <B extends GenericBuilder<B, V, R, P>,
            V extends ExtendedElementVisitor<R, P>, R, P> B builder(
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
        return this.conventionDelegate.downcastToConvention(visitor);
    }

    @Override
    public <V extends ConventionTypeElementVisitor<R, P>, R, P> V downcastToV(
            final ExtendedElementVisitor<R, P> visitor,
            final Function<ConventionTypeElementVisitor<R, P>, V> downcast) {
        return this.conventionDelegate.downcastToV(visitor, downcast);
    }

    @Override
    public <P> ConventionTypeElementVisitor<Boolean, P> isPresent(final Class<? extends Annotation> annotationClass) {
        return this.conventionDelegate.isPresent(annotationClass);
    }

    @Override
    public <R, P> ConventionTypeElementVisitor<StreamEx<R>, P> emptyStreamVisitor() {
        return this.conventionDelegate.emptyStreamVisitor();
    }

    @Override
    public <V extends ConventionTypeElementVisitor<StreamEx<R>, P>,
            R, P> StreamExConventionBuilder<V, R, P> createPropertyLevelVisitor(
            final BiFunction<ConventionTypeElement, Property, StreamEx<R>> transformation,
            final Function<ConventionTypeElementVisitor<StreamEx<R>, P>, V> downcastAdapter) {
        return this.conventionDelegate.createPropertyLevelVisitor(transformation, downcastAdapter);
    }

    @Override
    public <B extends GenericStreamExConventionBuilder<B, V, R, P>,
            V extends ConventionTypeElementVisitor<StreamEx<R>, P>, R, P> B streamExConventionBuilder(
            final V visitor,
            final Function<ConventionTypeElementVisitor<StreamEx<R>, P>, V> downcastAdapter) {
        return this.conventionDelegate.streamExConventionBuilder(visitor, downcastAdapter);
    }

    @Override
    public <B extends GenericStreamExConventionBuilder<B, V, R, P>,
            V extends ConventionTypeElementVisitor<StreamEx<R>, P>, R, P> B streamExConventionBuilder(
            final Provider<V> visitor,
            final Function<ConventionTypeElementVisitor<StreamEx<R>, P>, V> downcastAdapter) {
        return this.conventionDelegate.streamExConventionBuilder(visitor, downcastAdapter);
    }

    @Override
    public <B extends GenericStreamExConventionBuilder<B, V, R, P>,
            V extends ConventionTypeElementVisitor<StreamEx<R>, P>, R, P> B streamExConventionBuilder(
            final Iterable<? extends V> visitors,
            final Function<ConventionTypeElementVisitor<StreamEx<R>, P>, V> downcastAdapter) {
        return this.conventionDelegate.streamExConventionBuilder(visitors, downcastAdapter);
    }

    @Override
    public <R, P> ConventionTypeElementVisitor<StreamEx<R>, P> streamExConvention(
            final ConventionTypeElementVisitor<R, P> visitor) {
        return this.conventionDelegate.streamExConvention(visitor);
    }

    @Override
    public String toString() {
        return "ConventionParameterVisitorsImpl{" +
                "delegate=" + this.delegate +
                ", conventionDelegate=" + this.conventionDelegate +
                "}";
    }
}
