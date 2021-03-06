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

import com.squareup.javapoet.MethodSpec;
import org.rookit.auto.javapoet.naming.JavaPoetParameterResolver;
import org.rookit.auto.javapoet.type.JavaPoetTypeSourceFactory;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.auto.source.spec.SpecFactory;
import org.rookit.convention.auto.javax.ConventionTypeElementFactory;

import java.util.Collection;
import java.util.stream.Collectors;

final class PropertyBasedTypeSourceFactory extends AbstractInterfaceTypeSourceFactory {

    private final SpecFactory<MethodSpec> specFactory;

    PropertyBasedTypeSourceFactory(final JavaPoetParameterResolver parameterResolver,
                                   final JavaPoetTypeSourceFactory adapter,
                                   final SpecFactory<MethodSpec> specFactory,
                                   final ConventionTypeElementFactory elementFactory) {
        super(parameterResolver, adapter, elementFactory);
        this.specFactory = specFactory;
    }

    @Override
    protected Collection<MethodSpec> methodsFor(final ExtendedTypeElement element) {
        return this.specFactory.create(element)
                .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return "PropertyBasedTypeSourceFactory{" +
                "specFactory=" + this.specFactory +
                "} " + super.toString();
    }
}
