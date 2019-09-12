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
package org.rookit.convention.auto.javapoet.naming;

import com.google.inject.Inject;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeVariableName;
import org.rookit.auto.javapoet.naming.JavaPoetNamingFactory;
import org.rookit.convention.auto.javax.ConventionTypeElement;
import org.rookit.convention.auto.property.PropertyFactory;
import org.rookit.convention.auto.property.PropertyTypeResolver;

import java.util.function.Function;

final class JavaPoetPropertyNamingFactoriesImpl implements JavaPoetPropertyNamingFactories {

    private final PropertyFactory propertyFactory;
    private final PropertyTypeResolver resolver;

    @Inject
    private JavaPoetPropertyNamingFactoriesImpl(
            final PropertyFactory propertyFactory,
            final PropertyTypeResolver resolver) {
        this.propertyFactory = propertyFactory;
        this.resolver = resolver;
    }

    @Override
    public JavaPoetPropertyNamingFactory createDispatcherFactory(final JavaPoetNamingFactory namingFactory) {
        return createDispatcherFactory(element -> TypeName.get(element.asType()),
                                       namingFactory);
    }

    @Override
    public JavaPoetPropertyNamingFactory createDispatcherFactory(
            final Function<ConventionTypeElement, TypeName> paramFunction,
            final JavaPoetNamingFactory namingFactory) {
        return createDispatcherFactory(paramFunction, this.resolver, namingFactory);
    }

    @Override
    public JavaPoetPropertyNamingFactory createDispatcherFactory(
            final Function<ConventionTypeElement, TypeName> paramFunction,
            final PropertyTypeResolver resolver,
            final JavaPoetNamingFactory namingFactory) {
        return new DispatcherJavaPoetPropertyNamingFactory(
                this.propertyFactory,
                createContainerFactory(paramFunction, namingFactory),
                createNonContainerFactory(paramFunction, resolver)
        );
    }

    @Override
    public JavaPoetContainerPropertyNamingFactory createContainerFactory(
            final Function<ConventionTypeElement, TypeName> paramFunction,
            final JavaPoetNamingFactory namingFactory) {
        return new BaseJavaPoetContainerPropertyNamingFactory(namingFactory, paramFunction);
    }

    @Override
    public JavaPoetPropertyNamingFactory createNonContainerFactory(
            final Function<ConventionTypeElement, TypeName> paramFunction) {
        return createNonContainerFactory(paramFunction, this.resolver);
    }

    @Override
    public JavaPoetPropertyNamingFactory createNonContainerFactory(
            final Function<ConventionTypeElement, TypeName> paramFunction,
            final PropertyTypeResolver resolver) {
        return new NonContainerJavaPoetPropertyNamingFactory(resolver, paramFunction);
    }

    @Override
    public Function<ConventionTypeElement, TypeName> parameterWithVariableEntity(final TypeVariableName variableName) {
        return new PropertiesParamFunction(true, variableName);
    }

    @Override
    public Function<ConventionTypeElement, TypeName> parameterWithoutVariableEntity(
            final TypeVariableName variableName) {
        return new PropertiesParamFunction(false, variableName);
    }

    @Override
    public String toString() {
        return "JavaPoetPropertyNamingFactoriesImpl{" +
                ", propertyFactory=" + this.propertyFactory +
                ", resolver=" + this.resolver +
                "}";
    }

}
