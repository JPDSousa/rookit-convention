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

import one.util.streamex.StreamEx;
import org.rookit.auto.javax.ExtendedElement;
import org.rookit.auto.javax.executable.ExtendedExecutableElement;
import org.rookit.auto.javax.pack.ExtendedPackageElement;
import org.rookit.auto.javax.parameter.ExtendedTypeParameterElement;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.auto.javax.variable.ExtendedVariableElement;
import org.rookit.convention.auto.javax.ConventionTypeElement;

final class FilterStreamVisitor<R, P> implements ConventionTypeElementVisitor<StreamEx<R>, P> {

    private final ConventionTypeElementVisitor<Boolean, P> filterVisitor;
    private final ConventionTypeElementVisitor<StreamEx<R>, P> delegate;

    FilterStreamVisitor(final ConventionTypeElementVisitor<Boolean, P> filterVisitor,
                        final ConventionTypeElementVisitor<StreamEx<R>, P> delegate) {
        this.filterVisitor = filterVisitor;
        this.delegate = delegate;
    }

    @Override
    public StreamEx<R> visitConventionType(final ConventionTypeElement element, final P parameter) {
        if (this.filterVisitor.visitConventionType(element, parameter)) {
            return this.delegate.visitConventionType(element, parameter);
        }

        return StreamEx.empty();
    }

    @Override
    public StreamEx<R> visitPackage(final ExtendedPackageElement packageElement, final P parameter) {
        if (this.filterVisitor.visitPackage(packageElement, parameter)) {
            return this.delegate.visitPackage(packageElement, parameter);
        }

        return StreamEx.empty();
    }

    @Override
    public StreamEx<R> visitType(final ExtendedTypeElement extendedType, final P parameter) {
        if (this.filterVisitor.visitType(extendedType, parameter)) {
            return this.delegate.visitType(extendedType, parameter);
        }

        return StreamEx.empty();
    }

    @Override
    public StreamEx<R> visitExecutable(final ExtendedExecutableElement extendedExecutable, final P parameter) {
        if (this.filterVisitor.visitExecutable(extendedExecutable, parameter)) {
            return this.delegate.visitExecutable(extendedExecutable, parameter);
        }

        return StreamEx.empty();
    }

    @Override
    public StreamEx<R> visitTypeParameter(final ExtendedTypeParameterElement extendedParameter, final P parameter) {
        if (this.filterVisitor.visitTypeParameter(extendedParameter, parameter)) {
            return this.delegate.visitTypeParameter(extendedParameter, parameter);
        }

        return StreamEx.empty();
    }

    @Override
    public StreamEx<R> visitVariable(final ExtendedVariableElement extendedElement, final P parameter) {
        if (this.filterVisitor.visitVariable(extendedElement, parameter)) {
            return this.delegate.visitVariable(extendedElement, parameter);
        }

        return StreamEx.empty();
    }

    @Override
    public StreamEx<R> visitUnknown(final ExtendedElement extendedElement, final P parameter) {
        if (this.filterVisitor.visitUnknown(extendedElement, parameter)) {
            return this.delegate.visitUnknown(extendedElement, parameter);
        }

        return StreamEx.empty();
    }

    @Override
    public String toString() {
        return "FilterStreamVisitor{" +
                "filterVisitor=" + this.filterVisitor +
                ", delegate=" + this.delegate +
                "}";
    }
}
