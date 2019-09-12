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
import one.util.streamex.StreamEx;
import org.rookit.auto.javax.visitor.StreamExtendedElementVisitor;
import org.rookit.convention.auto.metatype.guice.MetaTypeAPI;
import org.rookit.convention.auto.javapoet.naming.JavaPoetPropertyNamingFactory;
import org.rookit.convention.auto.javax.ConventionTypeElement;
import org.rookit.convention.auto.javax.visitor.ConventionTypeElementVisitor;

import javax.lang.model.element.Modifier;

final class EntityPropertiesMethodVisitor implements ConventionTypeElementVisitor<StreamEx<MethodSpec>, Void>,
        StreamExtendedElementVisitor<MethodSpec, Void> {

    private final JavaPoetPropertyNamingFactory propertyNamingFactory;

    @Inject
    private EntityPropertiesMethodVisitor(@MetaTypeAPI final JavaPoetPropertyNamingFactory namingFactory) {
        this.propertyNamingFactory = namingFactory;
    }

    @Override
    public StreamEx<MethodSpec> visitConventionType(final ConventionTypeElement element, final Void parameter) {
        return StreamEx.of(element.properties())
                .map(property -> MethodSpec.methodBuilder(property.name())
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .returns(this.propertyNamingFactory.typeNameFor(element, property))
                        .build());
    }

    @Override
    public String toString() {
        return "EntityPropertiesMethodVisitor{" +
                "propertyNamingFactory=" + this.propertyNamingFactory +
                "}";
    }
}
