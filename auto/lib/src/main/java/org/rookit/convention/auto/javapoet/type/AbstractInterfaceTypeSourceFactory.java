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
package org.rookit.convention.auto.javapoet.type;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import one.util.streamex.StreamEx;
import org.rookit.auto.javapoet.naming.JavaPoetParameterResolver;
import org.rookit.auto.javapoet.type.JavaPoetTypeSourceFactory;
import org.rookit.auto.javax.naming.Identifier;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.auto.source.type.SingleTypeSourceFactory;
import org.rookit.auto.source.type.TypeSource;
import org.rookit.convention.auto.javax.ConventionTypeElement;
import org.rookit.convention.auto.javax.ConventionTypeElementFactory;

import javax.lang.model.element.Modifier;
import java.util.Collection;
import java.util.stream.Collectors;

public abstract class AbstractInterfaceTypeSourceFactory implements SingleTypeSourceFactory {

    private final JavaPoetParameterResolver parameterResolver;
    private final JavaPoetTypeSourceFactory adapter;
    private final ConventionTypeElementFactory elementFactory;

    AbstractInterfaceTypeSourceFactory(final JavaPoetParameterResolver parameterResolver,
                                       final JavaPoetTypeSourceFactory adapter,
                                       final ConventionTypeElementFactory elementFactory) {
        this.parameterResolver = parameterResolver;
        this.adapter = adapter;
        this.elementFactory = elementFactory;
    }

    @Override
    public TypeSource create(final Identifier identifier,
                             final ExtendedTypeElement element) {
        final ClassName className = ClassName.get(identifier.packageElement().fullName().asString(), identifier.name());
        final ConventionTypeElement conventionElement = this.elementFactory.extendType(element);

        final TypeSpec.Builder spec = TypeSpec.interfaceBuilder(className)
                .addTypeVariables(this.parameterResolver.createParameters(element))
                .addMethods(methodsFor(element))
                .addSuperinterfaces(parentNamesOf(conventionElement))
                .addModifiers(Modifier.PUBLIC);
        return this.adapter.fromTypeSpec(identifier, spec.build());
    }

    protected abstract Collection<MethodSpec> methodsFor(ExtendedTypeElement element);

    private StreamEx<TypeName> superTypesFor(final ExtendedTypeElement parent) {
        return StreamEx.of(this.parameterResolver.resolveParameters(parent));
    }

    private Collection<TypeName> parentNamesOf(final ConventionTypeElement baseElement) {
        return baseElement.conventionInterfaces()
                .flatMap(this::superTypesFor)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "AbstractInterfaceTypeSourceFactory{" +
                "parameterResolver=" + this.parameterResolver +
                ", adapter=" + this.adapter +
                ", elementFactory=" + this.elementFactory +
                "}";
    }
}
