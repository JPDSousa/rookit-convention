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
package org.rookit.convention.module.source.aggregator.property;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import one.util.streamex.StreamEx;
import org.rookit.auto.javapoet.naming.JavaPoetNamingFactory;
import org.rookit.auto.javax.naming.Identifier;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.auto.source.spec.parameter.Parameter;
import org.rookit.convention.auto.javax.naming.PropertyIdentifierFactory;
import org.rookit.convention.auto.javax.visitor.ConventionTypeElementVisitor;
import org.rookit.convention.auto.property.ContainerProperty;
import org.rookit.convention.auto.property.ExtendedProperty;
import org.rookit.convention.auto.property.ExtendedPropertyAggregator;
import org.rookit.convention.auto.property.Property;
import org.rookit.convention.auto.property.PropertyFactory;
import org.rookit.utils.optional.Optional;
import org.rookit.utils.primitive.VoidUtils;

import javax.annotation.processing.Messager;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.apache.commons.text.WordUtils.uncapitalize;

final class ExtendedPropertyMultiMethodAggregator implements ExtendedPropertyAggregator<Collection<MethodSpec>> {

    private final ConventionTypeElementVisitor<StreamEx<Parameter<ParameterSpec>>, Void> parameterVisitor;
    private final ExtendedTypeElement element;
    private final Collection<Property> properties;
    private final PropertyFactory propertyFactory;
    private final PropertyIdentifierFactory identifierFactory;
    private final Messager messager;
    private final JavaPoetNamingFactory apiNamingFactory;
    private final JavaPoetNamingFactory implNamingFactory;
    private final VoidUtils voidUtils;

    ExtendedPropertyMultiMethodAggregator(final ConventionTypeElementVisitor<
            StreamEx<Parameter<ParameterSpec>>, Void> parameterVisitor,
                                          final ExtendedTypeElement element,
                                          final Iterable<Property> properties,
                                          final PropertyFactory propertyFactory,
                                          final PropertyIdentifierFactory identifierFactory,
                                          final Messager messager,
                                          final JavaPoetNamingFactory apiNamingFactory,
                                          final JavaPoetNamingFactory implNamingFactory,
                                          final VoidUtils voidUtils) {
        this.parameterVisitor = parameterVisitor;
        this.element = element;
        this.properties = Lists.newArrayList(properties);
        this.propertyFactory = propertyFactory;
        this.identifierFactory = identifierFactory;
        this.messager = messager;
        this.apiNamingFactory = apiNamingFactory;
        this.implNamingFactory = implNamingFactory;
        this.voidUtils = voidUtils;
    }

    @Override
    public boolean accept(final ExtendedProperty item) {
        this.properties.add(item);
        return true;
    }

    @Override
    public ExtendedPropertyAggregator<Collection<MethodSpec>> reduce(
            final ExtendedPropertyAggregator<Collection<MethodSpec>> aggregator) {
        return new ReducedExtendedPropertyMultiMethodAggregator(this.messager, this, aggregator);
    }

    @Override
    public Collection<MethodSpec> result() {
        return StreamEx.of(this.properties)
                .map(this.propertyFactory::toContainer)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(this::createPropertyBinding)
                .toImmutableList();
    }

    private MethodSpec createPropertyBinding(final ContainerProperty property) {
        final String methodName = uncapitalize(this.element.getSimpleName().toString()) + property.name();
        final Identifier identifier = this.identifierFactory.create(property);
        final ExtendedTypeElement propertyType = property.typeAsElement();
        final TypeName returnType = ParameterizedTypeName.get(
                this.apiNamingFactory.classNameFor(propertyType),
                ClassName.get(this.element)
        );
        final Collection<ParameterSpec> parameters = parameters(propertyType);
        final String paramString = StreamEx.of(parameters)
                .map(parameterSpec -> parameterSpec.name)
                .collect(Collectors.joining(", "));

        return MethodSpec.methodBuilder(methodName)
                .addAnnotations(annotations(identifier))
                .returns(returnType)
                .addParameters(parameters)
                // TODO there should be some config for 'createMutable'
                .addStatement("return $T.createMutable($L)", this.implNamingFactory.classNameFor(propertyType),
                        paramString)
                .build();
    }

    private Collection<AnnotationSpec> annotations(final Identifier identifier) {
        return ImmutableSet.of(
                AnnotationSpec.builder(Provides.class).build(),
                AnnotationSpec.builder(Singleton.class).build(),
                AnnotationSpec.builder(ClassName
                        .get(identifier.packageElement().fullName().asString(), identifier.name())).build()
        );
    }

    private Collection<ParameterSpec> parameters(final ExtendedTypeElement propertyType) {
        return propertyType.accept(this.parameterVisitor, this.voidUtils.returnVoid())
                .map(Parameter::spec)
                .toImmutableList();
    }

    @Override
    public String toString() {
        return "ExtendedPropertyMultiMethodAggregator{" +
                "parameterVisitor=" + this.parameterVisitor +
                ", element=" + this.element +
                ", properties=" + this.properties +
                ", propertyFactory=" + this.propertyFactory +
                ", identifierFactory=" + this.identifierFactory +
                ", messager=" + this.messager +
                ", apiNamingFactory=" + this.apiNamingFactory +
                ", implNamingFactory=" + this.implNamingFactory +
                ", voidUtils=" + this.voidUtils +
                "}";
    }
}
