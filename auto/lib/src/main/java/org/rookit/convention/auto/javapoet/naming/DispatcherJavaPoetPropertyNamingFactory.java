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
package org.rookit.convention.auto.javapoet.naming;

import com.squareup.javapoet.TypeName;
import org.rookit.convention.auto.javax.ConventionTypeElement;
import org.rookit.convention.auto.property.Property;
import org.rookit.convention.auto.property.PropertyFactory;

final class DispatcherJavaPoetPropertyNamingFactory implements JavaPoetPropertyNamingFactory {

    private final PropertyFactory propertyFactory;
    private final JavaPoetContainerPropertyNamingFactory containerFactory;
    private final JavaPoetPropertyNamingFactory nonContainerFactory;

    DispatcherJavaPoetPropertyNamingFactory(
            final PropertyFactory propertyFactory,
            final JavaPoetContainerPropertyNamingFactory containerFactory,
            final JavaPoetPropertyNamingFactory nonContainerFactory) {
        this.propertyFactory = propertyFactory;
        this.containerFactory = containerFactory;
        this.nonContainerFactory = nonContainerFactory;
    }

    @Override
    public TypeName typeNameFor(final ConventionTypeElement owner, final Property property) {
        final JavaPoetContainerPropertyNamingFactory containerFactory = this.containerFactory;
        final JavaPoetPropertyNamingFactory nonContainerFactory = this.nonContainerFactory;
        return this.propertyFactory.toContainer(property)
                .map(containerProperty -> containerFactory.typeNameFor(owner, containerProperty))
                .orElseGet(() -> nonContainerFactory.typeNameFor(owner, property));
    }

    @Override
    public String toString() {
        return "DispatcherJavaPoetPropertyNamingFactory{" +
                "propertyFactory=" + this.propertyFactory +
                ", containerFactory=" + this.containerFactory +
                ", nonContainerFactory=" + this.nonContainerFactory +
                "}";
    }

}
