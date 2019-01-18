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

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeVariableName;
import org.rookit.convention.utils.guice.MetatypeAPI;
import org.rookit.convention.utils.guice.PartialMetatypeAPI;
import org.rookit.auto.javapoet.method.EntityMethodFactory;
import org.rookit.auto.javapoet.naming.JavaPoetNamingFactory;
import org.rookit.auto.javax.property.ExtendedProperty;
import org.rookit.auto.javax.property.PropertyExtractor;
import org.rookit.auto.javax.element.ElementUtils;
import org.rookit.auto.javax.element.ExtendedTypeElement;
import org.rookit.convention.property.PropertyModel;

import javax.lang.model.element.Modifier;
import java.util.Collection;

final class EntityPropertiesEntityMethodFactory implements EntityMethodFactory {

    private final ElementUtils utils;
    private final PropertyExtractor extractor;
    private final JavaPoetNamingFactory namingFactory;
    private final TypeVariableName variableName;
    private final TypeName returnType;

    @Inject
    private EntityPropertiesEntityMethodFactory(final ElementUtils utils,
                                                final PropertyExtractor extractor,
                                                @PartialMetatypeAPI final JavaPoetNamingFactory namingFactory,
                                                @MetatypeAPI final TypeVariableName variableName) {
        this.variableName = variableName;
        this.utils = utils;
        this.extractor = extractor;
        this.namingFactory = namingFactory;
        final ParameterizedTypeName property = ParameterizedTypeName.get(ClassName.get(PropertyModel.class),
                TypeVariableName.get("?"));
        this.returnType = ParameterizedTypeName.get(ClassName.get(Collection.class), property);
    }

    @Override
    public Iterable<MethodSpec> create(final ExtendedTypeElement element) {
        final MethodSpec.Builder builder = MethodSpec.methodBuilder("properties")
                .returns(this.returnType)
                .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC);
        final String varName = "allProperties";
        builder.addStatement("final $T<$T<?>> $L = $T.builder()", ImmutableList.Builder.class,
                PropertyModel.class, varName, ImmutableList.class);
        if (!element.isTopLevel()) {
            element.conventionInterfaces()
                    .forEach(superType -> builder.addStatement("$L.addAll($T.super.properties())",

                            varName, this.namingFactory.classNameFor(superType)));
        }
        this.extractor.fromType(element)
                .map(ExtendedProperty::name)
                .map(name -> CodeBlock.of("$L.add($L())", varName, name))
                .forEach(builder::addStatement);
        builder.addStatement("return $L.build()", varName);
        return ImmutableList.of(builder.build());
    }

    @Override
    public String toString() {
        return "EntityPropertiesEntityMethodFactory{" +
                "utils=" + this.utils +
                ", extractor=" + this.extractor +
                ", namingFactory=" + this.namingFactory +
                ", variableName=" + this.variableName +
                ", returnType=" + this.returnType +
                "}";
    }
}
