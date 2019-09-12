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
import org.rookit.auto.javax.ExtendedElement;
import org.rookit.auto.javax.executable.ExtendedExecutableElement;
import org.rookit.auto.javax.pack.ExtendedPackageElement;
import org.rookit.auto.javax.parameter.ExtendedTypeParameterElement;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.auto.javax.variable.ExtendedVariableElement;
import org.rookit.convention.auto.javax.ConventionTypeElement;
import org.rookit.convention.auto.javax.visitor.ConventionTypeElementVisitor;

final class TypeSpecBuildVisitor<P> implements ConventionTypeElementVisitor<StreamEx<TypeSpec>, P> {

    private final ConventionTypeElementVisitor<StreamEx<TypeSpec.Builder>, P> builder;

    TypeSpecBuildVisitor(final ConventionTypeElementVisitor<StreamEx<TypeSpec.Builder>, P> builder) {
        this.builder = builder;
    }


    @Override
    public StreamEx<TypeSpec> visitConventionType(
            final ConventionTypeElement element, final P parameter) {
        return this.builder.visitConventionType(element, parameter)
                .map(TypeSpec.Builder::build);
    }

    @Override
    public StreamEx<TypeSpec> visitPackage(final ExtendedPackageElement packageElement, final P parameter) {
        return this.builder.visitPackage(packageElement, parameter)
                .map(TypeSpec.Builder::build);
    }

    @Override
    public StreamEx<TypeSpec> visitType(final ExtendedTypeElement extendedType, final P parameter) {
        return this.builder.visitType(extendedType, parameter)
                .map(TypeSpec.Builder::build);
    }

    @Override
    public StreamEx<TypeSpec> visitExecutable(
            final ExtendedExecutableElement extendedExecutable, final P parameter) {
        return this.builder.visitExecutable(extendedExecutable, parameter)
                .map(TypeSpec.Builder::build);
    }

    @Override
    public StreamEx<TypeSpec> visitTypeParameter(
            final ExtendedTypeParameterElement extendedParameter, final P parameter) {
        return this.builder.visitTypeParameter(extendedParameter, parameter)
                .map(TypeSpec.Builder::build);
    }

    @Override
    public StreamEx<TypeSpec> visitVariable(
            final ExtendedVariableElement extendedElement, final P parameter) {
        return this.builder.visitVariable(extendedElement, parameter)
                .map(TypeSpec.Builder::build);
    }

    @Override
    public StreamEx<TypeSpec> visitUnknown(final ExtendedElement extendedElement, final P parameter) {
        return this.builder.visitUnknown(extendedElement, parameter)
                .map(TypeSpec.Builder::build);
    }

    @Override
    public String toString() {
        return "TypeSpecBuildVisitor{" +
                "builder=" + this.builder +
                "}";
    }

}
