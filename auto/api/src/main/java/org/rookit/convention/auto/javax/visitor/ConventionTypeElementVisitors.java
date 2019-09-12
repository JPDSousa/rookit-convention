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
import org.rookit.auto.javax.visitor.ExtendedElementVisitor;
import org.rookit.auto.javax.visitor.ExtendedElementVisitors;
import org.rookit.convention.auto.javax.ConventionTypeElement;
import org.rookit.convention.auto.property.Property;

import java.lang.annotation.Annotation;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface ConventionTypeElementVisitors extends ExtendedElementVisitors {

    <R, P> ConventionTypeElementVisitor<R, P> downcastToConvention(ExtendedElementVisitor<R, P> visitor);

    <V extends ConventionTypeElementVisitor<R, P>, R, P> V downcastToV(
            ExtendedElementVisitor<R, P> visitor,
            Function<ConventionTypeElementVisitor<R, P>, V> downcast);

    @Override
    <P> ConventionTypeElementVisitor<Boolean, P> isPresent(Class<? extends Annotation> annotationClass);

    @Override
    <R, P> ConventionTypeElementVisitor<StreamEx<R>, P> emptyStreamVisitor();

    <V extends ConventionTypeElementVisitor<StreamEx<R>, P>, R, P> StreamExConventionBuilder<V, R, P>
    createPropertyLevelVisitor(BiFunction<ConventionTypeElement, Property, StreamEx<R>> transformation,
                               Function<ConventionTypeElementVisitor<StreamEx<R>, P>, V> downcastAdapter);

    default <R, P> StreamExConventionBuilder<ConventionTypeElementVisitor<StreamEx<R>, P>, R, P>
    createPropertyLevelVisitor(final BiFunction<ConventionTypeElement, Property, StreamEx<R>> transformation) {
        return createPropertyLevelVisitor(transformation, element -> element);
    }

    <B extends GenericStreamExConventionBuilder<B, V, R, P>,
            V extends ConventionTypeElementVisitor<StreamEx<R>, P>, R, P> B streamExConventionBuilder(
            V visitor,
            Function<ConventionTypeElementVisitor<StreamEx<R>, P>, V> downcastAdapter);

    default <B extends GenericStreamExConventionBuilder<B, ConventionTypeElementVisitor<StreamEx<R>, P>, R, P>,
            R, P> B streamExConventionBuilder(final ConventionTypeElementVisitor<StreamEx<R>, P> visitor) {
        return streamExConventionBuilder(visitor, element -> element);
    }

    <B extends GenericStreamExConventionBuilder<B, V, R, P>,
            V extends ConventionTypeElementVisitor<StreamEx<R>, P>, R, P> B streamExConventionBuilder(
            Provider<V> visitor,
            Function<ConventionTypeElementVisitor<StreamEx<R>, P>, V> downcastAdapter);

    default <B extends GenericStreamExConventionBuilder<B, ConventionTypeElementVisitor<StreamEx<R>, P>, R, P>,
            R, P> B streamExConventionBuilder(final Provider<ConventionTypeElementVisitor<StreamEx<R>, P>> visitor) {
        return streamExConventionBuilder(visitor, element -> element);
    }

    <B extends GenericStreamExConventionBuilder<B, V, R, P>,
            V extends ConventionTypeElementVisitor<StreamEx<R>, P>, R, P> B streamExConventionBuilder(
            Iterable<? extends V> visitors,
            Function<ConventionTypeElementVisitor<StreamEx<R>, P>, V> downcastAdapter);

    default <B extends GenericStreamExConventionBuilder<B, ConventionTypeElementVisitor<StreamEx<R>, P>, R, P>,
            R, P> B streamExConventionBuilder(
                    final Iterable<? extends ConventionTypeElementVisitor<StreamEx<R>, P>> visitors) {
        return streamExConventionBuilder(visitors, element -> element);
    }

    <R, P> ConventionTypeElementVisitor<StreamEx<R>, P> streamExConvention(ConventionTypeElementVisitor<R, P> visitor);

}
