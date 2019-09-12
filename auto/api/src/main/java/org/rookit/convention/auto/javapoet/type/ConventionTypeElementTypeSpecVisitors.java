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

import com.squareup.javapoet.TypeSpec;
import one.util.streamex.StreamEx;
import org.rookit.auto.javapoet.identifier.JavaPoetIdentifierFactory;
import org.rookit.auto.javapoet.type.ExtendedElementTypeSpecVisitors;
import org.rookit.convention.auto.javax.visitor.ConventionTypeElementVisitor;
import org.rookit.convention.auto.javax.visitor.ConventionTypeElementVisitors;

import java.util.function.Function;

public interface ConventionTypeElementTypeSpecVisitors extends ExtendedElementTypeSpecVisitors,
        ConventionTypeElementVisitors {

    <V extends ConventionTypeElementVisitor<StreamEx<TypeSpec.Builder>, P>, P> ConventionTypeSpecBuilder<V, P>
    conventionTypeSpecBuilder(
            V visitor,
            Function<ConventionTypeElementVisitor<StreamEx<TypeSpec.Builder>, P>, V> downcastAdapter);

    <V extends ConventionTypeElementVisitor<StreamEx<TypeSpec.Builder>, P>, P> ConventionAnnotationBuilder<V, P>
    conventionAnnotationBuilder(
            JavaPoetIdentifierFactory identifierFactory,
            Function<ConventionTypeElementVisitor<StreamEx<TypeSpec.Builder>, P>, V> downcastAdapter);

    default <P> ConventionAnnotationBuilder<ConventionTypeElementVisitor<StreamEx<TypeSpec.Builder>, P>, P>
    conventionAnnotationBuilder(final JavaPoetIdentifierFactory identifierFactory) {
        return conventionAnnotationBuilder(identifierFactory, element -> element);
    }

    <V extends ConventionTypeElementVisitor<StreamEx<TypeSpec.Builder>, P>, P> ConventionAnnotationBuilder<V, P>
    conventionAnnotationBuilder(
            JavaPoetIdentifierFactory identifierFactory,
            Class<P> parameterClass,
            Function<ConventionTypeElementVisitor<StreamEx<TypeSpec.Builder>, P>, V> downcastAdapter);

    default <P> ConventionAnnotationBuilder<ConventionTypeElementVisitor<StreamEx<TypeSpec.Builder>, P>, P>
    conventionAnnotationBuilder(
            final JavaPoetIdentifierFactory identifierFactory,
            final Class<P> parameterClass) {
        return conventionAnnotationBuilder(identifierFactory, parameterClass, element -> element);
    }

}
