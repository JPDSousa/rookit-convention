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
package org.rookit.convention.api.source.naming;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeVariableName;
import org.rookit.auto.javapoet.naming.JavaPoetNamingFactory;
import org.rookit.auto.javapoet.naming.JavaPoetParameterResolver;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.convention.auto.metatype.guice.PartialMetaTypeAPI;
import org.rookit.convention.auto.metatype.guice.MetaTypeAPI;
import org.rookit.utils.guice.Self;

final class PartialMetaTypeParameterResolver implements JavaPoetParameterResolver {

    private final JavaPoetNamingFactory namingFactory;
    private final TypeVariableName typeVariableName;
    private final JavaPoetNamingFactory selfNamingFactory;

    @Inject
    private PartialMetaTypeParameterResolver(@PartialMetaTypeAPI final JavaPoetNamingFactory namingFactory,
                                             @MetaTypeAPI final TypeVariableName typeVariableName,
                                             @Self final JavaPoetNamingFactory selfNamingFactory) {
        this.namingFactory = namingFactory;
        this.typeVariableName = typeVariableName;
        this.selfNamingFactory = selfNamingFactory;
    }

    @Override
    public TypeName resolveParameters(final ExtendedTypeElement element, final TypeVariableName... typeVariables) {
        return ParameterizedTypeName.get(this.namingFactory.classNameFor(element), this.typeVariableName);
    }

    @Override
    public Iterable<TypeVariableName> createParameters(final ExtendedTypeElement element) {
        final ClassName elementClass = this.selfNamingFactory.classNameFor(element);
        final TypeVariableName type = TypeVariableName.get(this.typeVariableName + " extends " + elementClass);
        return ImmutableList.of(type);
    }

    @Override
    public String toString() {
        return "PartialMetaTypeParameterResolver{" +
                "namingFactory=" + this.namingFactory +
                ", typeVariableName=" + this.typeVariableName +
                ", selfNamingFactory=" + this.selfNamingFactory +
                "}";
    }
}
