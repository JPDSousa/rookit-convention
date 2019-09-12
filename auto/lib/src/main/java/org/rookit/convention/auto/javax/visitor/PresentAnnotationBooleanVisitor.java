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

import org.rookit.auto.javax.ExtendedElement;
import org.rookit.auto.javax.executable.ExtendedExecutableElement;
import org.rookit.auto.javax.pack.ExtendedPackageElement;
import org.rookit.auto.javax.parameter.ExtendedTypeParameterElement;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.auto.javax.variable.ExtendedVariableElement;
import org.rookit.convention.auto.javax.ConventionTypeElement;

import javax.lang.model.AnnotatedConstruct;
import java.lang.annotation.Annotation;
import java.util.Objects;

final class PresentAnnotationBooleanVisitor<P> implements ConventionTypeElementVisitor<Boolean, P> {

    private final Class<? extends Annotation> annotationClass;

    PresentAnnotationBooleanVisitor(final Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    private boolean isAnnotationPresent(final AnnotatedConstruct element) {
        return Objects.isNull(element.getAnnotation(this.annotationClass));
    }

    @Override
    public Boolean visitConventionType(final ConventionTypeElement element, final P parameter) {
        return isAnnotationPresent(element);
    }

    @Override
    public Boolean visitPackage(final ExtendedPackageElement packageElement, final P parameter) {
        return isAnnotationPresent(packageElement);
    }

    @Override
    public Boolean visitType(final ExtendedTypeElement extendedType, final P parameter) {
        return isAnnotationPresent(extendedType);
    }

    @Override
    public Boolean visitExecutable(final ExtendedExecutableElement extendedExecutable, final P parameter) {
        return isAnnotationPresent(extendedExecutable);
    }

    @Override
    public Boolean visitTypeParameter(final ExtendedTypeParameterElement extendedParameter, final P parameter) {
        return isAnnotationPresent(extendedParameter);
    }

    @Override
    public Boolean visitVariable(final ExtendedVariableElement extendedElement, final P parameter) {
        return isAnnotationPresent(extendedElement);
    }

    @Override
    public Boolean visitUnknown(final ExtendedElement extendedElement, final P parameter) {
        return isAnnotationPresent(extendedElement);
    }

    @Override
    public String toString() {
        return "PresentAnnotationBooleanVisitor{" +
                "annotationClass=" + this.annotationClass +
                "}";
    }
}
