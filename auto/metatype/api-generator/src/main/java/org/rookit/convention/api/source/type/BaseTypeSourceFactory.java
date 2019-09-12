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
package org.rookit.convention.api.source.type;

import com.google.common.collect.ImmutableList;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import org.rookit.auto.javax.naming.Identifier;
import org.rookit.auto.javapoet.method.EntityMethodFactory;
import org.rookit.auto.javapoet.naming.JavaPoetParameterResolver;
import org.rookit.auto.javapoet.type.JavaPoetTypeSourceFactory;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.auto.source.type.SingleTypeSourceFactory;
import org.rookit.auto.source.type.TypeSource;
import org.rookit.auto.source.spec.SpecFactory;
import org.rookit.convention.MetaType;
import org.rookit.convention.auto.javax.ConventionTypeElement;
import org.rookit.convention.auto.javax.ConventionTypeElementFactory;
import org.rookit.convention.property.ImmutablePropertyModel;

import javax.lang.model.element.Modifier;
import java.util.Collection;
import java.util.stream.Collectors;

// TODO break me down into pieces
class BaseTypeSourceFactory implements SingleTypeSourceFactory {

    private final ClassName typeModel;
    private final ClassName propertyModel;
    private final SpecFactory<MethodSpec> methodFactory;
    private final EntityMethodFactory entityMethodFactory;
    private final JavaPoetTypeSourceFactory adapter;
    private final JavaPoetParameterResolver parameterResolver;
    private final TypeVariableName typeVariableName;
    private final ConventionTypeElementFactory elementFactory;

    BaseTypeSourceFactory(final SpecFactory<MethodSpec> methodFactory,
                          final EntityMethodFactory entityMethodFactory,
                          final JavaPoetTypeSourceFactory adapter,
                          final JavaPoetParameterResolver parameterResolver,
                          final TypeVariableName typeVariableName,
                          final ConventionTypeElementFactory elementFactory) {
        this.methodFactory = methodFactory;
        this.entityMethodFactory = entityMethodFactory;
        this.adapter = adapter;
        this.parameterResolver = parameterResolver;
        this.typeVariableName = typeVariableName;
        this.elementFactory = elementFactory;
        this.typeModel = ClassName.get(MetaType.class);
        this.propertyModel = ClassName.get(ImmutablePropertyModel.class);
    }

    @Override
    public TypeSource create(final Identifier identifier,
                             final ExtendedTypeElement element) {
        final ClassName className = ClassName.get(identifier.packageElement().fullName().asString(), identifier.name());
        final ConventionTypeElement conventionElement = this.elementFactory.extendType(element);

        final TypeSpec.Builder builder = TypeSpec.interfaceBuilder(className)
                .addTypeVariables(this.parameterResolver.createParameters(element))
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterfaces(superInterfaces(conventionElement))
                .addMethods(methods(element));

        return this.adapter.fromTypeSpec(identifier, builder.build());
    }

    private Iterable<TypeName> superInterfaces(final ConventionTypeElement element) {
        final Collection<TypeName> superTypes = element.conventionInterfaces()
                .map(conventionInterface -> this.parameterResolver.resolveParameters(conventionInterface))
                .collect(Collectors.toList());

        final ParameterizedTypeName typeModel = getModelTypeName(element);

        return ImmutableList.<TypeName>builder()
                .add(typeModel)
                .addAll(superTypes)
                .build();
    }

    private ParameterizedTypeName getModelTypeName(final ConventionTypeElement element) {
        if (element.isEntity() || element.isPartialEntity()) {
            return ParameterizedTypeName.get(this.typeModel, this.typeVariableName);
        }

        final ClassName elementName = ClassName.get(element);
        return ParameterizedTypeName.get(this.propertyModel, this.typeVariableName, elementName);
    }

    private Iterable<MethodSpec> methods(final ExtendedTypeElement element) {
        return ImmutableList.<MethodSpec>builder()
                .addAll(this.entityMethodFactory.create(element))
                .addAll(this.methodFactory.create(element))
                .build();
    }

    @Override
    public String toString() {
        return "BaseTypeSourceFactory{" +
                "typeModel=" + this.typeModel +
                ", propertyModel=" + this.propertyModel +
                ", methodFactory=" + this.methodFactory +
                ", entityMethodFactory=" + this.entityMethodFactory +
                ", adapter=" + this.adapter +
                ", parameterResolver=" + this.parameterResolver +
                ", typeVariableName=" + this.typeVariableName +
                ", elementFactory=" + this.elementFactory +
                "}";
    }
}
