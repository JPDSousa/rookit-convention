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
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import org.rookit.auto.javapoet.naming.JavaPoetNamingFactory;
import org.rookit.auto.javapoet.type.JavaPoetTypeSourceFactory;
import org.rookit.auto.javax.naming.Identifier;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.auto.source.spec.SpecFactory;
import org.rookit.auto.source.type.SingleTypeSourceFactory;
import org.rookit.auto.source.type.TypeSource;
import org.rookit.convention.auto.metatype.guice.MetaTypeAPI;
import org.rookit.convention.metatype.AbstractMetaType;
import org.rookit.convention.property.guice.PropertyModel;

import javax.lang.model.element.Modifier;

final class PropertySingleTypeSourceFactory implements SingleTypeSourceFactory {

    private final JavaPoetTypeSourceFactory adapter;
    private final SpecFactory<FieldSpec> fieldFactory;
    private final SpecFactory<MethodSpec> methodFactory;
    private final TypeVariableName typeVariableName;
    private final JavaPoetNamingFactory apiNamingFactory;

    @Inject
    private PropertySingleTypeSourceFactory(final JavaPoetTypeSourceFactory adapter,
                                            @PropertyModel final SpecFactory<MethodSpec> methodFactory,
                                            final SpecFactory<FieldSpec> fieldFactory,
                                            final TypeVariableName typeVariableName,
                                            @MetaTypeAPI final JavaPoetNamingFactory apiNamingFactory) {
        this.adapter = adapter;
        this.methodFactory = methodFactory;
        this.fieldFactory = fieldFactory;
        this.typeVariableName = typeVariableName;
        this.apiNamingFactory = apiNamingFactory;
    }

    @Override
    public TypeSource create(final Identifier identifier, final ExtendedTypeElement element) {
        final ClassName className = ClassName.get(identifier.packageElement().fullName().asString(), identifier.name());

        final TypeSpec.Builder spec = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addTypeVariable(this.typeVariableName)
                .superclass(ParameterizedTypeName.get(
                        ClassName.get(AbstractMetaType.class),
                        ClassName.get(element)
                ))
                .addSuperinterface(ParameterizedTypeName.get(
                        this.apiNamingFactory.classNameFor(element),
                        this.typeVariableName
                ))
                .addMethods(this.methodFactory.create(element))
                .addFields(this.fieldFactory.create(element));

        return this.adapter.fromTypeSpec(identifier, spec.build());
    }

    @Override
    public String toString() {
        return "PropertySingleTypeSourceFactory{" +
                "adapter=" + this.adapter +
                ", fieldFactory=" + this.fieldFactory +
                ", methodFactory=" + this.methodFactory +
                ", typeVariableName=" + this.typeVariableName +
                ", apiNamingFactory=" + this.apiNamingFactory +
                "}";
    }
}
