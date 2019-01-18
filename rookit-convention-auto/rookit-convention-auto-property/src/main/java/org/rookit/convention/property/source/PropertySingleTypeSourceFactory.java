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
package org.rookit.convention.property.source;

import com.google.inject.Inject;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import org.rookit.auto.identifier.Identifier;
import org.rookit.auto.javapoet.FieldFactory;
import org.rookit.auto.javapoet.method.EntityMethodFactory;
import org.rookit.auto.javapoet.method.MethodFactory;
import org.rookit.auto.javapoet.naming.JavaPoetNamingFactory;
import org.rookit.auto.javapoet.type.TypeSourceAdapter;
import org.rookit.auto.javax.element.ExtendedTypeElement;
import org.rookit.auto.javax.property.ExtendedProperty;
import org.rookit.auto.source.SingleTypeSourceFactory;
import org.rookit.auto.source.TypeSource;
import org.rookit.convention.metatype.AbstractMetatype;
import org.rookit.convention.utils.guice.MetatypeAPI;

import javax.lang.model.element.Modifier;
import java.util.Collection;

final class PropertySingleTypeSourceFactory implements SingleTypeSourceFactory {

    private final TypeSourceAdapter adapter;
    private final FieldFactory fieldFactory;
    private final EntityMethodFactory entityMethodFactory;
    private final MethodFactory methodFactory;
    private final TypeVariableName typeVariableName;
    private final JavaPoetNamingFactory apiNamingFactory;

    @Inject
    private PropertySingleTypeSourceFactory(final TypeSourceAdapter adapter,
                                            final MethodFactory methodFactory,
                                            final EntityMethodFactory entityMethodFactory,
                                            final FieldFactory fieldFactory,
                                            final TypeVariableName typeVariableName,
                                            @MetatypeAPI final JavaPoetNamingFactory apiNamingFactory) {
        this.adapter = adapter;
        this.methodFactory = methodFactory;
        this.entityMethodFactory = entityMethodFactory;
        this.fieldFactory = fieldFactory;
        this.typeVariableName = typeVariableName;
        this.apiNamingFactory = apiNamingFactory;
    }

    @Override
    public TypeSource create(final Identifier identifier, final ExtendedTypeElement element) {
        final ClassName className = ClassName.get(identifier.packageName().fullName(), identifier.name());
        final Collection<ExtendedProperty> properties = element.properties();

        final TypeSpec spec = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addTypeVariable(this.typeVariableName)
                .superclass(ParameterizedTypeName.get(
                        ClassName.get(AbstractMetatype.class),
                        ClassName.get(element)
                ))
                .addSuperinterface(ParameterizedTypeName.get(
                        this.apiNamingFactory.classNameFor(element),
                        this.typeVariableName
                ))
                .addMethods(this.entityMethodFactory.create(element))
                .addFields(this.fieldFactory.filterCompatible(element, properties))
                .addMethods(this.methodFactory.filterCompatible(element, properties))
                .build();

        return this.adapter.fromTypeSpec(identifier, spec);
    }

    @Override
    public String toString() {
        return "PropertySingleTypeSourceFactory{" +
                "adapter=" + this.adapter +
                ", fieldFactory=" + this.fieldFactory +
                ", entityMethodFactory=" + this.entityMethodFactory +
                ", methodFactory=" + this.methodFactory +
                ", typeVariableName=" + this.typeVariableName +
                ", apiNamingFactory=" + this.apiNamingFactory +
                "}";
    }
}
