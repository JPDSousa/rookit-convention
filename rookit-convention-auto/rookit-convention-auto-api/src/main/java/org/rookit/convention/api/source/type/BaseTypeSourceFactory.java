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
import org.rookit.auto.identifier.Identifier;
import org.rookit.auto.javapoet.method.EntityMethodFactory;
import org.rookit.auto.javapoet.method.MethodFactory;
import org.rookit.auto.javapoet.naming.JavaPoetParameterResolver;
import org.rookit.auto.javapoet.type.TypeSourceAdapter;
import org.rookit.auto.javax.property.ExtendedProperty;
import org.rookit.auto.javax.property.PropertyExtractor;
import org.rookit.auto.javax.element.ExtendedTypeElement;
import org.rookit.auto.source.SingleTypeSourceFactory;
import org.rookit.auto.source.TypeSource;
import org.rookit.convention.Metatype;
import org.rookit.convention.property.ImmutablePropertyModel;

import javax.lang.model.element.Modifier;
import java.util.Collection;
import java.util.stream.Collectors;

// TODO break me down into pieces
class BaseTypeSourceFactory implements SingleTypeSourceFactory {

    private final ClassName typeModel;
    private final ClassName propertyModel;
    private final PropertyExtractor extractor;
    private final MethodFactory methodFactory;
    private final EntityMethodFactory entityMethodFactory;
    private final TypeSourceAdapter adapter;
    private final JavaPoetParameterResolver parameterResolver;
    private final TypeVariableName typeVariableName;

    BaseTypeSourceFactory(final PropertyExtractor extractor,
                          final MethodFactory methodFactory,
                          final EntityMethodFactory entityMethodFactory,
                          final TypeSourceAdapter adapter,
                          final JavaPoetParameterResolver parameterResolver,
                          final TypeVariableName typeVariableName) {
        this.extractor = extractor;
        this.methodFactory = methodFactory;
        this.entityMethodFactory = entityMethodFactory;
        this.adapter = adapter;
        this.parameterResolver = parameterResolver;
        this.typeVariableName = typeVariableName;
        this.typeModel = ClassName.get(Metatype.class);
        this.propertyModel = ClassName.get(ImmutablePropertyModel.class);
    }

    @Override
    public TypeSource create(final Identifier identifier,
                             final ExtendedTypeElement element) {
        final ClassName className = ClassName.get(identifier.packageName().fullName(), identifier.name());
        final Collection<ExtendedProperty> properties = this.extractor.fromType(element)
                .collect(Collectors.toList());

        final TypeSpec.Builder builder = TypeSpec.interfaceBuilder(className)
                .addTypeVariables(this.parameterResolver.createParameters(element))
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterfaces(superInterfaces(element, identifier))
                .addMethods(methods(element, properties));

        return this.adapter.fromTypeSpec(identifier, builder.build());
    }

    private Iterable<TypeName> superInterfaces(final ExtendedTypeElement element,
                                               final Identifier identifier) {
        final Collection<TypeName> superTypes = element.conventionInterfaces()
                .map(conventionInterface -> this.parameterResolver.resolveParameters(conventionInterface))
                .collect(Collectors.toList());

        final ParameterizedTypeName typeModel = getModelTypeName(element, identifier);

        return ImmutableList.<TypeName>builder()
                .add(typeModel)
                .addAll(superTypes)
                .build();
    }

    private ParameterizedTypeName getModelTypeName(final ExtendedTypeElement element,
                                                   final Identifier identifier) {
        if (element.isEntity() || element.isPartialEntity()) {
            return ParameterizedTypeName.get(this.typeModel, this.typeVariableName);
        }

        final ClassName elementName = ClassName.bestGuess(identifier.qualifiedOriginal());
        return ParameterizedTypeName.get(this.propertyModel, this.typeVariableName, elementName);
    }

    private Iterable<MethodSpec> methods(final ExtendedTypeElement element,
                                         final Collection<ExtendedProperty> properties) {
        return ImmutableList.<MethodSpec>builder()
                .addAll(this.entityMethodFactory.create(element))
                .addAll(this.methodFactory.filterCompatible(element, properties))
                .build();
    }

    @Override
    public String toString() {
        return "BaseTypeSourceFactory{" +
                "typeModel=" + this.typeModel +
                ", propertyModel=" + this.propertyModel +
                ", extractor=" + this.extractor +
                ", methodFactory=" + this.methodFactory +
                ", entityMethodFactory=" + this.entityMethodFactory +
                ", adapter=" + this.adapter +
                ", parameterResolver=" + this.parameterResolver +
                ", typeVariableName=" + this.typeVariableName +
                "}";
    }
}
