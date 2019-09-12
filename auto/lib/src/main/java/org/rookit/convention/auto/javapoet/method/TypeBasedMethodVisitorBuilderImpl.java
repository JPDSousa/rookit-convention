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

import com.google.inject.Provider;
import com.squareup.javapoet.MethodSpec;
import one.util.streamex.StreamEx;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.auto.javax.type.ExtendedTypeMirror;
import org.rookit.auto.javax.visitor.ExtendedElementVisitor;
import org.rookit.convention.auto.javax.ConventionTypeElement;
import org.rookit.convention.auto.javax.visitor.ConventionTypeElementVisitor;
import org.rookit.convention.auto.javax.visitor.StreamExConventionBuilder;
import org.rookit.convention.auto.javax.visitor.TypeBasedMethodVisitor;
import org.rookit.convention.auto.property.Property;
import org.rookit.utils.adapt.Adapter;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;

final class TypeBasedMethodVisitorBuilderImpl<P>
        implements TypeBasedMethodVisitorBuilder<TypeBasedMethodVisitor<P>, P> {

    private final StreamExConventionBuilder<TypeBasedMethodVisitor<P>, MethodSpec, P> delegate;
    private final ExtendedTypeMirror typeMirror;

    TypeBasedMethodVisitorBuilderImpl(
            final StreamExConventionBuilder<TypeBasedMethodVisitor<P>, MethodSpec, P> delegate,
            final ExtendedTypeMirror typeMirror) {
        this.delegate = delegate;
        this.typeMirror = typeMirror;
    }

    private TypeBasedMethodVisitorBuilder<TypeBasedMethodVisitor<P>, P>
    newStage(final StreamExConventionBuilder<TypeBasedMethodVisitor<P>, MethodSpec, P> builder) {
        return new TypeBasedMethodVisitorBuilderImpl<>(builder, this.typeMirror);
    }

    @Override
    public TypeBasedMethodVisitorBuilder<TypeBasedMethodVisitor<P>, P>
    withTypeAdapter(final Adapter<ExtendedTypeElement> adapter) {
        return newStage(this.delegate.withTypeAdapter(adapter));
    }

    @Override
    public TypeBasedMethodVisitorBuilder<TypeBasedMethodVisitor<P>, P>
    withConventionTypeAdapter(final Adapter<ConventionTypeElement> adapter) {
        return newStage(this.delegate.withConventionTypeAdapter(adapter));
    }

    @Override
    public TypeBasedMethodVisitorBuilder<TypeBasedMethodVisitor<P>, P>
    withRecursiveVisiting(final BinaryOperator<StreamEx<MethodSpec>> resultReducer) {
        return newStage(this.delegate.withRecursiveVisiting(resultReducer));
    }

    @Override
    public TypeBasedMethodVisitor<P> build() {
        return new TypeBasedMethodVisitorDecorator<>(this.typeMirror, this.delegate.build());
    }

    @Override
    public String toString() {
        return "TypeBasedMethodVisitorBuilderImpl{" +
                "delegate=" + this.delegate +
                ", typeMirror=" + this.typeMirror +
                "}";
    }

    @Override
    public TypeBasedMethodVisitorBuilder<TypeBasedMethodVisitor<P>, P>
    routeThroughFilter(final ConventionTypeElementVisitor<StreamEx<MethodSpec>, P> fallbackVisitor,
                       final Predicate<Property> filter) {
        return newStage(this.delegate.routeThroughFilter(fallbackVisitor, filter));
    }

    @Override
    public TypeBasedMethodVisitorBuilder<TypeBasedMethodVisitor<P>, P> add(
            final TypeBasedMethodVisitor<P> visitor) {
        return newStage(this.delegate.add(visitor));
    }

    @Override
    public TypeBasedMethodVisitorBuilder<TypeBasedMethodVisitor<P>, P> add(
            final Provider<TypeBasedMethodVisitor<P>> visitor) {
        return newStage(this.delegate.add(visitor));
    }

    @Override
    public TypeBasedMethodVisitorBuilder<TypeBasedMethodVisitor<P>, P> addAll(
            final Collection<? extends TypeBasedMethodVisitor<P>> visitors) {
        return newStage(this.delegate.addAll(visitors));
    }

    @Override
    public TypeBasedMethodVisitorBuilder<TypeBasedMethodVisitor<P>, P>
    withDirtyFallback(final ExtendedElementVisitor<StreamEx<MethodSpec>, P> visitor) {
        return newStage(this.delegate.withDirtyFallback(visitor));
    }

    @Override
    public TypeBasedMethodVisitorBuilder<TypeBasedMethodVisitor<P>, P>
    filterIfAnnotationAbsent(final Class<? extends Annotation> annotationClass) {
        return newStage(this.delegate.filterIfAnnotationAbsent(annotationClass));
    }

    @Override
    public TypeBasedMethodVisitorBuilder<TypeBasedMethodVisitor<P>, P>
    filterIfAllAnnotationsAbsent(final Iterable<? extends Class<? extends Annotation>> annotationClasses) {
        return newStage(this.delegate.filterIfAllAnnotationsAbsent(annotationClasses));
    }
}
