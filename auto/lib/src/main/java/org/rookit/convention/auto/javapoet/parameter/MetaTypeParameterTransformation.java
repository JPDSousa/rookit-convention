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

import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import one.util.streamex.StreamEx;
import org.rookit.convention.auto.javapoet.naming.JavaPoetPropertyNamingFactory;
import org.rookit.convention.auto.javax.ConventionTypeElement;
import org.rookit.convention.auto.property.Property;

import java.util.function.BiFunction;

import static javax.lang.model.element.Modifier.FINAL;

final class MetaTypeParameterTransformation
        implements BiFunction<ConventionTypeElement, Property, StreamEx<ParameterSpec>> {

    private final JavaPoetPropertyNamingFactory propertyNamingFactory;
    private final BiFunction<Property, ParameterSpec, ParameterSpec> resultAccumulator;

    MetaTypeParameterTransformation(final JavaPoetPropertyNamingFactory propNamingFactory,
                                    final BiFunction<Property, ParameterSpec, ParameterSpec> resultAccumulator) {
        this.propertyNamingFactory = propNamingFactory;
        this.resultAccumulator = resultAccumulator;
    }

    @Override
    public StreamEx<ParameterSpec> apply(final ConventionTypeElement element, final Property property) {
        final TypeName paramType = this.propertyNamingFactory.typeNameFor(element, property);
        final ParameterSpec.Builder builder = ParameterSpec.builder(paramType, property.name(), FINAL);
        return StreamEx.of(this.resultAccumulator.apply(property, builder.build()));
    }

    @Override
    public String toString() {
        return "MetaTypeParameterTransformation{" +
                "propertyNamingFactory=" + this.propertyNamingFactory +
                ", resultAccumulator=" + this.resultAccumulator +
                "}";
    }
}
