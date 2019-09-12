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
import one.util.streamex.StreamEx;
import org.rookit.auto.javax.visitor.StreamExtendedElementVisitor;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.convention.auto.config.NamingConfig;

import static javax.lang.model.element.Modifier.PUBLIC;

final class PropertyNameGetterJavaPoetMethodVisitor<P> implements StreamExtendedElementVisitor<MethodSpec, P> {

    private final MethodSpec methodSpec;

    @Inject
    private PropertyNameGetterJavaPoetMethodVisitor(final NamingConfig namingConfig) {
        // TODO this can be an injected field
        // TODO the propertyName method className should not be hardcoded here
        this.methodSpec = MethodSpec.methodBuilder("propertyName")
                .addModifiers(PUBLIC)
                .addAnnotation(Override.class)
                .returns(String.class)
                .addStatement("return this.$L", namingConfig.propertyName())
                .build();
    }

    @Override
    public StreamEx<MethodSpec> visitType(final ExtendedTypeElement extendedType, final P parameter) {
        return StreamEx.of(this.methodSpec);
    }

    @Override
    public String toString() {
        return "PropertyNameGetterJavaPoetMethodVisitor{" +
                "methodSpec=" + this.methodSpec +
                "}";
    }
}
