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
package org.rookit.convention.property.source.javapoet;

import com.google.inject.Inject;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import one.util.streamex.StreamEx;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.auto.javax.visitor.ExtendedElementVisitor;
import org.rookit.auto.javax.visitor.StreamExtendedElementVisitor;
import org.rookit.auto.source.spec.parameter.Parameter;
import org.rookit.convention.property.guice.PropertyModel;

import static javax.lang.model.element.Modifier.PUBLIC;

final class PropertiesJavaPoetMethodVisitor<P> implements StreamExtendedElementVisitor<MethodSpec, P> {

    private final ExtendedElementVisitor<StreamEx<Parameter<ParameterSpec>>, P> parameterFactory;

    @Inject
    private PropertiesJavaPoetMethodVisitor(
            @PropertyModel final ExtendedElementVisitor<StreamEx<Parameter<ParameterSpec>>, P> parameterFactory) {
        this.parameterFactory = parameterFactory;
    }

    @Override
    public StreamEx<MethodSpec> visitType(final ExtendedTypeElement extendedType, final P parameter) {
        return this.parameterFactory.visitType(extendedType, parameter)
                // TODO this filter is hammerish
                .filter(Parameter::isSuper)
                .map(Parameter::spec)
                .map(this::createGetter);
    }

    private MethodSpec createGetter(final ParameterSpec parameter) {
        return MethodSpec.methodBuilder(parameter.name)
                .addModifiers(PUBLIC)
                .addAnnotation(Override.class)
                .returns(parameter.type)
                .addStatement("return this.$L", parameter.name)
                .build();
    }

    @Override
    public String toString() {
        return "PropertiesJavaPoetMethodVisitor{" +
                "parameterFactory=" + this.parameterFactory +
                "}";
    }
}
