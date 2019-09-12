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

import com.squareup.javapoet.MethodSpec;
import one.util.streamex.StreamEx;
import org.rookit.auto.javax.ExtendedElement;
import org.rookit.auto.javax.executable.ExtendedExecutableElement;
import org.rookit.auto.javax.pack.ExtendedPackageElement;
import org.rookit.auto.javax.parameter.ExtendedTypeParameterElement;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.auto.javax.type.ExtendedTypeMirror;
import org.rookit.auto.javax.variable.ExtendedVariableElement;
import org.rookit.convention.auto.javax.ConventionTypeElement;
import org.rookit.convention.auto.javax.visitor.ConventionTypeElementVisitor;
import org.rookit.convention.auto.javax.visitor.TypeBasedMethodVisitor;

final class TypeBasedMethodVisitorDecorator<P> implements TypeBasedMethodVisitor<P> {

    private final ExtendedTypeMirror type;
    private final ConventionTypeElementVisitor<StreamEx<MethodSpec>, P> delegate;

    TypeBasedMethodVisitorDecorator(final ExtendedTypeMirror type,
                                    final ConventionTypeElementVisitor<StreamEx<MethodSpec>, P> delegate) {
        this.type = type;
        this.delegate = delegate;
    }

    @Override
    public ExtendedTypeMirror type() {
        return this.type;
    }

    @Override
    public StreamEx<MethodSpec> visitPackage(final ExtendedPackageElement packageElement, final P parameter) {
        return this.delegate.visitPackage(packageElement, parameter);
    }

    @Override
    public StreamEx<MethodSpec> visitType(final ExtendedTypeElement extendedType, final P parameter) {
        return this.delegate.visitType(extendedType, parameter);
    }

    @Override
    public StreamEx<MethodSpec> visitExecutable(final ExtendedExecutableElement extendedExecutable, final P parameter) {
        return this.delegate.visitExecutable(extendedExecutable, parameter);
    }

    @Override
    public StreamEx<MethodSpec> visitTypeParameter(final ExtendedTypeParameterElement extendedParameter, final P parameter) {
        return this.delegate.visitTypeParameter(extendedParameter, parameter);
    }

    @Override
    public StreamEx<MethodSpec> visitVariable(final ExtendedVariableElement extendedElement, final P parameter) {
        return this.delegate.visitVariable(extendedElement, parameter);
    }

    @Override
    public StreamEx<MethodSpec> visitUnknown(final ExtendedElement extendedElement, final P parameter) {
        return this.delegate.visitUnknown(extendedElement, parameter);
    }

    @Override
    public String toString() {
        return "TypeBasedMethodVisitorDecorator{" +
                "type=" + this.type +
                ", delegate=" + this.delegate +
                "}";
    }

    @Override
    public StreamEx<MethodSpec> visitConventionType(final ConventionTypeElement element, final P parameter) {
        return this.delegate.visitConventionType(element, parameter);
    }
}
