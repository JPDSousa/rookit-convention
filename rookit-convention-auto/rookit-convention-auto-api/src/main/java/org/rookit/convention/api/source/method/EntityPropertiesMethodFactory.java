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
package org.rookit.convention.api.source.method;

import com.google.inject.Inject;
import com.squareup.javapoet.MethodSpec;
import org.rookit.auto.javapoet.method.MethodFactory;
import org.rookit.auto.javapoet.naming.JavaPoetPropertyNamingFactory;
import org.rookit.auto.javax.element.ExtendedTypeElement;
import org.rookit.auto.javax.property.ExtendedProperty;
import org.rookit.convention.utils.guice.MetatypeAPI;

import javax.lang.model.element.Modifier;
import java.util.stream.Stream;

final class EntityPropertiesMethodFactory implements MethodFactory {

    private final JavaPoetPropertyNamingFactory propertyNamingFactory;

    @Inject
    private EntityPropertiesMethodFactory(@MetatypeAPI final JavaPoetPropertyNamingFactory namingFactory) {
        this.propertyNamingFactory = namingFactory;
    }

    @Override
    public Stream<MethodSpec> create(final ExtendedTypeElement owner, final ExtendedProperty property) {
        return Stream.of(MethodSpec.methodBuilder(property.name())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(this.propertyNamingFactory.typeNameFor(owner, property))
                .build());
    }

    @Override
    public boolean isCompatible(final ExtendedProperty property) {
        // all properties match
        return true;
    }

    @Override
    public String toString() {
        return "EntityPropertiesMethodFactory{" +
                "propertyNamingFactory=" + this.propertyNamingFactory +
                "}";
    }
}
