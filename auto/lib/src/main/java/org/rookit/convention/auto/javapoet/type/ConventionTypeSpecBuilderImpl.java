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
package org.rookit.convention.auto.javapoet.type;

import com.google.inject.Provider;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.TypeSpec;
import one.util.streamex.StreamEx;
import org.rookit.auto.javapoet.doc.JavaPoetJavadocTemplate1;
import org.rookit.auto.javapoet.type.TypeSpecBuilder;
import org.rookit.auto.javax.ExtendedElement;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.auto.javax.visitor.ExtendedElementVisitor;
import org.rookit.auto.javax.visitor.StreamExBuilder;
import org.rookit.convention.auto.javax.visitor.ConventionTypeElementVisitor;
import org.rookit.convention.auto.javax.visitor.ConventionTypeElementVisitors;
import org.rookit.convention.auto.javax.visitor.StreamExConventionBuilder;
import org.rookit.utils.adapt.Adapter;

import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.function.BinaryOperator;
import java.util.function.Function;

final class ConventionTypeSpecBuilderImpl<V extends ConventionTypeElementVisitor<StreamEx<TypeSpec.Builder>, P>, P>
        implements ConventionTypeSpecBuilder<V, P> {

    private final TypeSpecBuilder<V, P> builder;
    private final ConventionTypeElementVisitors visitors;

    ConventionTypeSpecBuilderImpl(
            final TypeSpecBuilder<V, P> builder,
            final ConventionTypeElementVisitors visitors) {
        this.builder = builder;
        this.visitors = visitors;
    }

    private ConventionTypeSpecBuilder<V, P> newStage(
            final TypeSpecBuilder<V, P> builder) {
        return new ConventionTypeSpecBuilderImpl<>(builder, this.visitors);
    }

    @Override
    public ConventionTypeSpecBuilder<V, P> withAnnotations(
            final Iterable<AnnotationSpec> annotations) {
        return newStage(this.builder.withAnnotations(annotations));
    }

    @Override
    public ConventionTypeSpecBuilder<V, P> copyBodyFrom(
            final TypeMirror typeMirror) {
        return newStage(this.builder.copyBodyFrom(typeMirror));
    }

    @Override
    public ConventionTypeSpecBuilder<V, P> copyBodyFrom(
            final Function<ExtendedElement, TypeMirror> extractionFunction) {
        return newStage(this.builder.copyBodyFrom(extractionFunction));
    }

    @Override
    public ConventionTypeSpecBuilder<V, P> withClassJavadoc(
            final JavaPoetJavadocTemplate1 template) {
        return newStage(this.builder.withClassJavadoc(template));
    }

    @Override
    public <V1 extends ExtendedElementVisitor<StreamEx<TypeSpec>, P>> StreamExBuilder<V1,
                TypeSpec, P> buildTypeSpec(
                    final Function<ExtendedElementVisitor<StreamEx<TypeSpec>, P>, V1> downcastAdapter) {
        return this.builder.buildTypeSpec(downcastAdapter);
    }

    @Override
    public <V1 extends ConventionTypeElementVisitor<StreamEx<TypeSpec>, P>>
    StreamExConventionBuilder<V1, TypeSpec, P> buildConventionTypeSpec(
            final Function<ConventionTypeElementVisitor<StreamEx<TypeSpec>, P>, V1> downcastAdapter) {
        return this.visitors.streamExConventionBuilder(downcastAdapter.apply(new TypeSpecBuildVisitor<>(build())),
                                                       downcastAdapter);
    }

    @Override
    public ConventionTypeSpecBuilder<V, P> withTypeAdapter(
            final Adapter<ExtendedTypeElement> adapter) {
        return newStage(this.builder.withTypeAdapter(adapter));
    }

    @Override
    public ConventionTypeSpecBuilder<V, P> withRecursiveVisiting(
            final BinaryOperator<StreamEx<TypeSpec.Builder>> resultReducer) {
        return newStage(this.builder.withRecursiveVisiting(resultReducer));
    }

    @Override
    public V build() {
        return this.builder.build();
    }

    @Override
    public ConventionTypeSpecBuilder<V, P> add(final V visitor) {
        return newStage(this.builder.add(visitor));
    }

    @Override
    public ConventionTypeSpecBuilder<V, P> add(final Provider<V> visitor) {
        return newStage(this.builder.add(visitor));
    }

    @Override
    public ConventionTypeSpecBuilder<V, P> addAll(
            final Collection<? extends V> visitors) {
        return newStage(this.builder.addAll(visitors));
    }

    @Override
    public ConventionTypeSpecBuilder<V, P> withDirtyFallback(
            final ExtendedElementVisitor<StreamEx<TypeSpec.Builder>, P> visitor) {
        return newStage(this.builder.withDirtyFallback(visitor));
    }

    @Override
    public ConventionTypeSpecBuilder<V, P> filterIfAnnotationAbsent(
            final Class<? extends Annotation> annotationClass) {
        return newStage(this.builder.filterIfAnnotationAbsent(annotationClass));
    }

    @Override
    public ConventionTypeSpecBuilder<V, P> filterIfAllAnnotationsAbsent(
            final Iterable<? extends Class<? extends Annotation>> annotationClasses) {
        return newStage(this.builder.filterIfAllAnnotationsAbsent(annotationClasses));
    }

    @Override
    public String toString() {
        return "ConventionTypeSpecBuilderImpl{" +
                "builder=" + this.builder +
                ", visitors=" + this.visitors +
                "}";
    }

}
