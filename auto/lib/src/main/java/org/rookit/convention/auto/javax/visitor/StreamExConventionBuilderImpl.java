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
package org.rookit.convention.auto.javax.visitor;

import com.google.inject.Provider;
import one.util.streamex.StreamEx;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.auto.javax.visitor.ExtendedElementVisitor;
import org.rookit.auto.javax.visitor.StreamExBuilder;
import org.rookit.convention.auto.javax.ConventionTypeElement;
import org.rookit.convention.auto.javax.ConventionTypeElementFactory;
import org.rookit.convention.auto.property.Property;
import org.rookit.utils.adapt.Adapter;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

final class StreamExConventionBuilderImpl<V extends ConventionTypeElementVisitor<StreamEx<R>, P>, R, P>
        implements StreamExConventionBuilder<V, R, P> {

    private final StreamExBuilder<V, R, P> delegate;
    private final ConventionTypeElementVisitors visitors;
    private final ConventionTypeElementFactory factory;
    private final Function<ConventionTypeElementVisitor<StreamEx<R>, P>, V> downcastAdapter;

    StreamExConventionBuilderImpl(
            final StreamExBuilder<V, R, P> delegate,
            final ConventionTypeElementVisitors visitors,
            final ConventionTypeElementFactory factory,
            final Function<ConventionTypeElementVisitor<StreamEx<R>, P>, V> downcastAdapter) {
        this.delegate = delegate;
        this.visitors = visitors;
        this.factory = factory;
        this.downcastAdapter = downcastAdapter;
    }

    private StreamExConventionBuilder<V, R, P> newStage(
            final StreamExBuilder<V, R, P> builder) {
        return new StreamExConventionBuilderImpl<>(builder, this.visitors, this.factory, this.downcastAdapter);
    }

    @Override
    public StreamExConventionBuilder<V, R, P> withTypeAdapter(
            final Adapter<ExtendedTypeElement> adapter) {
        return newStage(this.delegate.withTypeAdapter(adapter));
    }

    @Override
    public StreamExConventionBuilder<V, R, P> withConventionTypeAdapter(
            final Adapter<ConventionTypeElement> adapter) {
        return this.visitors.streamExConventionBuilder(
                this.downcastAdapter.apply(new TypeAdapterVisitor<>(build(), adapter)), this.downcastAdapter);
    }

    @Override
    public StreamExConventionBuilder<V, R, P> withRecursiveVisiting(
            final BinaryOperator<StreamEx<R>> resultReducer) {
        return newStage(this.delegate.withRecursiveVisiting(resultReducer));
    }

    @Override
    public V build() {
        return this.delegate.build();
    }

    @Override
    public StreamExConventionBuilder<V, R, P> add(final V visitor) {
        return newStage(this.delegate.add(visitor));
    }

    @Override
    public StreamExConventionBuilder<V, R, P> add(final Provider<V> visitor) {
        return newStage(this.delegate.add(visitor));
    }

    @Override
    public StreamExConventionBuilder<V, R, P> addAll(
            final Collection<? extends V> visitors) {
        return newStage(this.delegate.addAll(visitors));
    }

    @Override
    public StreamExConventionBuilder<V, R, P> withDirtyFallback(
            final ExtendedElementVisitor<StreamEx<R>, P> visitor) {
        return newStage(this.delegate.withDirtyFallback(visitor));
    }

    @Override
    public StreamExConventionBuilder<V, R, P> routeThroughFilter(
            final ConventionTypeElementVisitor<StreamEx<R>, P> fallbackVisitor,
            final Predicate<Property> filter) {
        final ConventionTypeElementVisitor<StreamEx<R>, P> primary = build();
        return this.visitors.streamExConventionBuilder(this.downcastAdapter.apply(new RoutingVisitor<>(
                primary,
                fallbackVisitor,
                filter,
                this.factory
        )), downcastAdapter);
    }

    @Override
    public StreamExConventionBuilder<V, R, P> filterIfAnnotationAbsent(
            final Class<? extends Annotation> annotationClass) {
        return this.visitors.streamExConventionBuilder(this.downcastAdapter.apply(new FilterStreamVisitor<>(
                this.visitors.isPresent(annotationClass),
                build()
        )), downcastAdapter);
    }

    @Override
    public StreamExConventionBuilder<V, R, P> filterIfAllAnnotationsAbsent(
            final Iterable<? extends Class<? extends Annotation>> annotationClasses) {
        return newStage(this.delegate.filterIfAllAnnotationsAbsent(annotationClasses));
    }

    @Override
    public String toString() {
        return "StreamExConventionBuilderImpl{" +
                "delegate=" + this.delegate +
                ", visitors=" + this.visitors +
                ", factory=" + this.factory +
                ", downcastAdapter=" + this.downcastAdapter +
                "}";
    }

}
