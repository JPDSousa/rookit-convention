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
package org.rookit.convention.property.source.javapoet;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeVariableName;
import one.util.streamex.StreamEx;
import org.rookit.auto.javax.visitor.StreamExtendedElementVisitor;
import org.rookit.auto.source.spec.parameter.ImmutableParameter;
import org.rookit.auto.source.spec.parameter.Parameter;
import org.rookit.convention.auto.config.NamingConfig;
import org.rookit.convention.guice.MetaType;
import org.rookit.convention.auto.javax.ConventionTypeElement;
import org.rookit.convention.auto.javax.visitor.ConventionTypeElementVisitor;
import org.rookit.serialization.Serializer;

import java.util.Collection;
import java.util.function.Function;

import static javax.lang.model.element.Modifier.FINAL;

final class MetaTypePropertyJavaPoetParameterVisitor
        implements ConventionTypeElementVisitor<StreamEx<Parameter<ParameterSpec>>, Void>,
        StreamExtendedElementVisitor<Parameter<ParameterSpec>, Void> {

    private final ConventionTypeElementVisitor<StreamEx<ParameterSpec>, Void> metaTypeFactory;
    private final TypeVariableName variableName;
    private final NamingConfig namingConfig;
    private final Parameter<ParameterSpec> propertyName;
    private final ClassName serializerName;
    private final ClassName typeName;

    @Inject
    MetaTypePropertyJavaPoetParameterVisitor(
            @MetaType final ConventionTypeElementVisitor<StreamEx<ParameterSpec>, Void> metaTypeFactory,
            @MetaType final TypeVariableName variableName,
            final NamingConfig namingConfig) {
        this.metaTypeFactory = metaTypeFactory;
        this.variableName = variableName;
        this.namingConfig = namingConfig;
        this.serializerName = ClassName.get(Serializer.class);
        this.typeName = ClassName.get(Class.class);
        this.propertyName = ImmutableParameter.<ParameterSpec>builder()
                .isSuper(false)
                .spec(ParameterSpec.builder(String.class, namingConfig.propertyName(), FINAL).build())
                .build();
    }

    private Collection<Parameter<ParameterSpec>> entityParameters(final ConventionTypeElement element) {
        final TypeName param = element.isPartialEntity() ? this.variableName : ClassName.get(element);
        final ParameterizedTypeName type = ParameterizedTypeName.get(this.typeName, param);
        final ParameterizedTypeName serializer = ParameterizedTypeName.get(this.serializerName, param);
        final ParameterizedTypeName function = ParameterizedTypeName.get(
                ClassName.get(Function.class),
                this.variableName,
                ClassName.get(element)
        );
        final Parameter<ParameterSpec> typeParam = createFromNaming(type, NamingConfig::type);
        final Parameter<ParameterSpec> serializerParam = createFromNaming(serializer, NamingConfig::serializer);
        final Parameter<ParameterSpec> functionParam = createFromNaming(function, NamingConfig::getter);

        return ImmutableList.of(
                typeParam,
                serializerParam,
                this.propertyName,
                functionParam
        );
    }

    private Parameter<ParameterSpec> createFromNaming(final TypeName type,
                                                      final Function<NamingConfig, String> name) {
        return ImmutableParameter.<ParameterSpec>builder()
                .isSuper(false)
                .spec(ParameterSpec.builder(type, name.apply(this.namingConfig), FINAL).build())
                .build();
    }

    @SuppressWarnings("unchecked")
    @Override
    public StreamEx<Parameter<ParameterSpec>> visitConventionType(final ConventionTypeElement element, 
                                                                  final Void parameter) {
        return this.metaTypeFactory.visitConventionType(element, parameter)
                .map(parameterSpec -> ImmutableParameter.builder()
                        .isSuper(true)
                        .spec(parameterSpec)
                        .build())
                .select(Parameter.class)
                .map(param -> (Parameter<ParameterSpec>) param)
                .append(entityParameters(element));
    }

    @Override
    public String toString() {
        return "MetaTypePropertyJavaPoetParameterVisitor{" +
                "metaTypeFactory=" + this.metaTypeFactory +
                ", variableName=" + this.variableName +
                ", namingConfig=" + this.namingConfig +
                ", propertyName=" + this.propertyName +
                ", serializerName=" + this.serializerName +
                ", className=" + this.typeName +
                "}";
    }
}
