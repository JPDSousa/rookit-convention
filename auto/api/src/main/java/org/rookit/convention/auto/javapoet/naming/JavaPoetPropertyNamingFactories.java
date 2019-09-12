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

import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeVariableName;
import org.rookit.auto.javapoet.naming.JavaPoetNamingFactory;
import org.rookit.convention.auto.javax.ConventionTypeElement;
import org.rookit.convention.auto.property.PropertyTypeResolver;

import java.util.function.Function;

public interface JavaPoetPropertyNamingFactories {

    JavaPoetPropertyNamingFactory createDispatcherFactory(JavaPoetNamingFactory namingFactory);

    JavaPoetPropertyNamingFactory createDispatcherFactory(
            Function<ConventionTypeElement, TypeName> paramFunction,
            JavaPoetNamingFactory namingFactory);

    JavaPoetPropertyNamingFactory createDispatcherFactory(
            Function<ConventionTypeElement, TypeName> paramFunction,
            PropertyTypeResolver resolver, JavaPoetNamingFactory namingFactory);

    JavaPoetContainerPropertyNamingFactory createContainerFactory(
            Function<ConventionTypeElement, TypeName> paramFunction,
            JavaPoetNamingFactory namingFactory);

    JavaPoetPropertyNamingFactory createNonContainerFactory(Function<ConventionTypeElement, TypeName> paramFunction);

    JavaPoetPropertyNamingFactory createNonContainerFactory(Function<ConventionTypeElement, TypeName> paramFunction,
                                                            PropertyTypeResolver resolver);

    // TODO what does this mean?
    Function<ConventionTypeElement, TypeName> parameterWithVariableEntity(TypeVariableName variableName);

    // TODO what does this mean?
    Function<ConventionTypeElement, TypeName> parameterWithoutVariableEntity(TypeVariableName variableName);

}
