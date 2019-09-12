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
package org.rookit.convention.auto.javapoet;

import com.google.inject.Inject;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import one.util.streamex.StreamEx;
import org.rookit.auto.javapoet.method.MethodSpecFactory;
import org.rookit.convention.auto.javax.ConventionTypeElement;
import org.rookit.convention.auto.property.Property;

import javax.lang.model.element.Element;
import java.util.function.BiFunction;

final class TypeTransformation implements BiFunction<ConventionTypeElement, Property, StreamEx<MethodSpec>> {

    private static final String ERR_MSG = "This transformation function requires all properties to declare a declared" +
            " type";

    private final MethodSpecFactory methodSpecFactory;

    @Inject
    TypeTransformation(final MethodSpecFactory methodSpecFactory) {
        this.methodSpecFactory = methodSpecFactory;
    }

    @Override
    public StreamEx<MethodSpec> apply(final ConventionTypeElement owner, final Property property) {
        final Element propertyElement = property.type()
                .toElement()
                .orElseThrow(() -> new IllegalArgumentException(ERR_MSG));
        final String elementName = propertyElement.getSimpleName().toString();
        final TypeName typeName = TypeName.get(propertyElement.asType());
        final ParameterSpec parameter = ParameterSpec.builder(typeName, elementName)
                .build();

        return StreamEx.of(this.methodSpecFactory.create(elementName, parameter));
    }

    @Override
    public String toString() {
        return "TypeTransformation{" +
                "methodSpecFactory=" + this.methodSpecFactory +
                "}";
    }
}
