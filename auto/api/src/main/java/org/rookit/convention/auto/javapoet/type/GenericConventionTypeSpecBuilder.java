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
import org.rookit.auto.javapoet.type.GenericTypeSpecBuilder;
import org.rookit.auto.javax.visitor.ExtendedElementVisitor;
import org.rookit.auto.javax.visitor.StreamExBuilder;
import org.rookit.convention.auto.javax.visitor.ConventionTypeElementVisitor;
import org.rookit.convention.auto.javax.visitor.StreamExConventionBuilder;

import java.util.function.Function;

public interface GenericConventionTypeSpecBuilder<B extends GenericConventionTypeSpecBuilder<B, V, P>,
        V extends ConventionTypeElementVisitor<StreamEx<TypeSpec.Builder>, P>, P>
        extends GenericTypeSpecBuilder<B, V, P> {

    @Override
    @Deprecated
    <V1 extends ExtendedElementVisitor<StreamEx<TypeSpec>, P>> StreamExBuilder<V1, TypeSpec, P> buildTypeSpec(
            Function<ExtendedElementVisitor<StreamEx<TypeSpec>, P>, V1> downcastAdapter);

    <V1 extends ConventionTypeElementVisitor<StreamEx<TypeSpec>, P>> StreamExConventionBuilder<V1, TypeSpec, P>
    buildConventionTypeSpec(Function<ConventionTypeElementVisitor<StreamEx<TypeSpec>, P>, V1> downcastAdapter);

    default StreamExConventionBuilder<ConventionTypeElementVisitor<StreamEx<TypeSpec>, P>, TypeSpec, P>
    buildConventionTypeSpec() {
        return buildConventionTypeSpec(element -> element);
    }

}
