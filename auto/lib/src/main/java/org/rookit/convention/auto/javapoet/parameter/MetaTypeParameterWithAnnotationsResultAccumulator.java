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
package org.rookit.convention.auto.javapoet.parameter;

import com.google.inject.Inject;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterSpec;
import org.rookit.auto.javax.naming.Identifier;
import org.rookit.convention.auto.javax.naming.PropertyIdentifierFactory;
import org.rookit.convention.auto.property.Property;
import org.rookit.guice.auto.annotation.Guice;

import java.util.function.BiFunction;

final class MetaTypeParameterWithAnnotationsResultAccumulator
        implements BiFunction<Property, ParameterSpec, ParameterSpec> {

    private final PropertyIdentifierFactory guiceIdentifierFactory;

    @Inject
    MetaTypeParameterWithAnnotationsResultAccumulator(@Guice final PropertyIdentifierFactory identifierFactory) {
        this.guiceIdentifierFactory = identifierFactory;
    }

    @Override
    public ParameterSpec apply(final Property property, final ParameterSpec parameterSpec) {
        final Identifier identifier = this.guiceIdentifierFactory.create(property);
        final ClassName className = ClassName.get(identifier.packageElement().fullName().asString(), identifier.name());
        return parameterSpec.toBuilder()
                .addAnnotation(className)
                .build();
    }

    @Override
    public String toString() {
        return "MetaTypeParameterWithAnnotationsResultAccumulator{" +
                "guiceIdentifierFactory=" + this.guiceIdentifierFactory +
                "}";
    }
}
