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

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import one.util.streamex.StreamEx;
import org.rookit.auto.javax.visitor.StreamExtendedElementVisitor;
import org.rookit.auto.javax.executable.ExtendedExecutableElement;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.auto.javax.type.ExtendedTypeMirror;
import org.rookit.convention.auto.javax.ConventionTypeElement;
import org.rookit.convention.auto.javax.visitor.ConventionTypeElementVisitor;

import javax.lang.model.element.TypeElement;

final class Parameter1GetterMethodVisitor<P> implements ConventionTypeElementVisitor<StreamEx<MethodSpec>, P>,
        StreamExtendedElementVisitor<MethodSpec, P> {

    private final ExtendedExecutableElement method;

    Parameter1GetterMethodVisitor(final ExtendedExecutableElement method) {
        this.method = method;
    }

    @Override
    public StreamEx<MethodSpec> visitType(final ExtendedTypeElement extendedType, final P parameter) {
        final ExtendedTypeMirror returnType = this.method.getReturnType();
        final TypeName returnTypeName = returnType.toElement()
                .select(TypeElement.class)
                .map(ClassName::get)
                .map(className -> ParameterizedTypeName.get(className, ClassName.get(extendedType)))
                .map(TypeName.class::cast)
                .orElseGet(() -> TypeName.get(returnType));

        return StreamEx.of(
                MethodSpec.overriding(this.method)
                        .returns(returnTypeName)
                        .addStatement("return this.$L", this.method.getSimpleName().toString())
                        .build()
        );
    }

    @Override
    public StreamEx<MethodSpec> visitConventionType(
            final ConventionTypeElement element, final P parameter) {
        return visitType(element, parameter);
    }

    @Override
    public String toString() {
        return "Parameter1GetterMethodVisitor{" +
                "method=" + this.method +
                "}";
    }

}
