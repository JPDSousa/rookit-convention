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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeVariableName;
import one.util.streamex.StreamEx;
import org.rookit.auto.identifier.Identifier;
import org.rookit.auto.identifier.PropertyIdentifierFactory;
import org.rookit.auto.javapoet.method.EntityMethodFactory;
import org.rookit.auto.javapoet.naming.JavaPoetNamingFactory;
import org.rookit.auto.javapoet.naming.JavaPoetParameterResolver;
import org.rookit.auto.javapoet.naming.JavaPoetPropertyNamingFactory;
import org.rookit.auto.javax.element.ExtendedTypeElement;
import org.rookit.auto.javax.property.ExtendedProperty;
import org.rookit.auto.javax.property.PropertyExtractor;
import org.rookit.convention.meta.guice.Metatype;
import org.rookit.convention.utils.config.NamingConfig;
import org.rookit.convention.utils.guice.Guice;
import org.rookit.serialization.Serializer;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

//TODO this is a copy of MetatypeEntityMethodFactory
final class PropertyMetatypeEntityMethodFactory implements EntityMethodFactory {

    private final JavaPoetNamingFactory namingFactory;
    private final JavaPoetParameterResolver parameterResolver;
    private final TypeVariableName variableName;
    private final PropertyExtractor propertyExtractor;
    private final ClassName metatypeName;
    // TODO the separator needs to be injected
    private final String separator;
    private final JavaPoetPropertyNamingFactory propertyNamingFactory;
    private final PropertyIdentifierFactory guiceIdentifierFactory;
    private final NamingConfig namingConfig;

    @Inject
    private PropertyMetatypeEntityMethodFactory(@Metatype final JavaPoetNamingFactory namingFactory,
                                                @Metatype final JavaPoetParameterResolver parameterResolver,
                                                @Metatype final TypeVariableName variableName,
                                                final PropertyExtractor propertyExtractor,
                                                @Metatype final JavaPoetPropertyNamingFactory propNamingFactory,
                                                @Guice final PropertyIdentifierFactory guiceFactory,
                                                final NamingConfig namingConfig) {
        this.namingFactory = namingFactory;
        this.parameterResolver = parameterResolver;
        this.variableName = variableName;
        this.propertyExtractor = propertyExtractor;
        this.namingConfig = namingConfig;
        this.separator = "\n";
        this.propertyNamingFactory = propNamingFactory;
        this.guiceIdentifierFactory = guiceFactory;
        this.metatypeName = ClassName.get(org.rookit.convention.Metatype.class);
    }

    @Override
    public StreamEx<MethodSpec> create(final ExtendedTypeElement element) {
        return StreamEx.of(
                createFactoryMethod(element),
                createConstructor(element),
                createGetter(element),
                createPropertyNameGetter(),
                createModelType(element),
                createModelSerializer(element)
        );
    }

    private MethodSpec createModelSerializer(final TypeElement element) {
        // TODO this can be an injected field
        // TODO the propertyName method className should not be hardcoded here
        return MethodSpec.methodBuilder("modelSerializer")
                .addModifiers(PUBLIC)
                .addAnnotation(Override.class)
                .returns(ParameterizedTypeName.get(ClassName.get(Serializer.class), ClassName.get(element)))
                .addStatement("return this.$L.modelSerializer()", this.namingConfig.metatype())
                .build();
    }

    private MethodSpec createGetter(final TypeElement element) {
        // TODO this can be configurable
        final String entity = "entity";
        return MethodSpec.methodBuilder("get")
                .addModifiers(PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(this.variableName, entity, FINAL)
                .returns(ClassName.get(element))
                .addStatement("return this.$L.apply($L)", this.namingConfig.getter(), entity)
                .build();
    }

    private MethodSpec createModelType(final ExtendedTypeElement element) {
        // TODO this can be an injected field
        // TODO the propertyName method className should not be hardcoded here
        return MethodSpec.methodBuilder("modelType")
                .addModifiers(PUBLIC)
                .addAnnotation(Override.class)
                .returns(ParameterizedTypeName.get(ClassName.get(Class.class), ClassName.get(element)))
                .addStatement("return this.$L.modelType()", this.namingConfig.metatype())
                .build();
    }

    private MethodSpec createPropertyNameGetter() {
        // TODO this can be an injected field
        // TODO the propertyName method className should not be hardcoded here
        return MethodSpec.methodBuilder("propertyName")
                .addModifiers(PUBLIC)
                .addAnnotation(Override.class)
                .returns(String.class)
                .addStatement("return this.$L", this.namingConfig.propertyName())
                .build();
    }

    private MethodSpec createConstructor(final ExtendedTypeElement element) {
        final List<ParameterSpec> parameters = createParameters(element, true);
        final CodeBlock superStatement;
        if (parameters.size() > 1) {
            final String args = StreamEx.of(parameters)
                    .map(parameterSpec -> parameterSpec.name)
                    .collect(Collectors.joining(", "));
            superStatement = CodeBlock.of("super($T.of($L))", ImmutableSet.class, args);
        }
        else {
            superStatement = CodeBlock.of("super($T.of())", ImmutableSet.class);
        }
        final Collection<CodeBlock> blocks = StreamEx.of(parameters)
                .map(parameterSpec -> parameterSpec.name)
                .map(name -> CodeBlock.of("this.$L = $L", name, name))
                .append(CodeBlock.of("this.$L = $L", this.namingConfig.metatype(), this.namingConfig.metatype()))
                .append(CodeBlock.of("this.$L = $L", this.namingConfig.propertyName(), this.namingConfig.propertyName()))
                .append(CodeBlock.of("this.$L = $L", this.namingConfig.getter(), this.namingConfig.getter()))
                .collect(Collectors.toSet());

        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addAnnotation(Inject.class)
                .addParameters(entityParameters(element))
                .addParameters(parameters)
                .addStatement(superStatement)
                .addStatement(CodeBlock.join(blocks, ";" + this.separator))
                .build();
    }

    private MethodSpec createFactoryMethod(final ExtendedTypeElement element) {
        final Collection<ParameterSpec> parameters = createParameters(element, false);
        final Collection<ParameterSpec> entityParameters = entityParameters(element);
        final String paramString = StreamEx.of(entityParameters)
                .append(parameters)
                .map(parameterSpec -> parameterSpec.name)
                .collect(Collectors.joining(", "));

        return MethodSpec.methodBuilder("create")
                .addModifiers(PUBLIC, STATIC)
                .addTypeVariable(this.variableName)
                .returns(this.parameterResolver.resolveParameters(element))
                .addParameters(entityParameters)
                .addParameters(parameters)
                .addStatement("return new $T($L)", this.namingFactory.classNameFor(element), paramString)
                .build();
    }

    private Collection<ParameterSpec> entityParameters(final ExtendedTypeElement element) {
        final TypeName param = element.isPartialEntity() ? this.variableName : ClassName.get(element);
        final ParameterizedTypeName metatypeType = ParameterizedTypeName.get(this.metatypeName, param);
        final ParameterizedTypeName function = ParameterizedTypeName.get(
                ClassName.get(Function.class),
                this.variableName,
                ClassName.get(element)
        );

        return ImmutableSet.of(
                ParameterSpec.builder(metatypeType, this.namingConfig.metatype(), FINAL).build(),
                ParameterSpec.builder(String.class, this.namingConfig.propertyName(), FINAL).build(),
                ParameterSpec.builder(function, this.namingConfig.getter(), FINAL).build()
        );
    }

    private List<ParameterSpec> createParameters(final ExtendedTypeElement element, final boolean includeAnnotations) {
        final ImmutableList.Builder<ParameterSpec> builder = ImmutableList.builder();

        this.propertyExtractor.fromType(element)
                .map(property -> createParameter(element, property, includeAnnotations))
                .forEach(builder::add);
        return builder.build();
    }

    private ParameterSpec createParameter(final ExtendedTypeElement element,
                                          final ExtendedProperty property,
                                          final boolean includeAnnotations) {
        final TypeName paramType = this.propertyNamingFactory.typeNameFor(element, property);
        final ParameterSpec.Builder builder = ParameterSpec.builder(paramType, property.name(), FINAL);
        if (includeAnnotations) {
            final Identifier identifier = this.guiceIdentifierFactory.create(property);
            final ClassName className = ClassName.get(identifier.packageName().fullName(), identifier.name());
            builder.addAnnotation(className);
        }
        return builder.build();
    }
}
