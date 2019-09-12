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
package org.rookit.convention.meta.source.metatype;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeVariableName;
import org.rookit.auto.javapoet.naming.JavaPoetNamingFactory;
import org.rookit.auto.javapoet.naming.JavaPoetParameterResolver;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.convention.guice.MetaType;
import org.rookit.convention.auto.metatype.guice.MetaTypeAPI;

final class MetatypeParameterResolver implements JavaPoetParameterResolver {

    private final TypeVariableName variableName;
    private final JavaPoetNamingFactory namingFactory;

    @Inject
    private MetatypeParameterResolver(@MetaType final TypeVariableName variableName,
                                      @MetaTypeAPI final JavaPoetNamingFactory namingFactory) {
        this.variableName = variableName;
        this.namingFactory = namingFactory;
    }

    @Override
    public TypeName resolveParameters(final ExtendedTypeElement element, final TypeVariableName... typeVariables) {
        final ClassName className = this.namingFactory.classNameFor(element);
        if (typeVariables.length == 0) {
            return className;
        }
        return ParameterizedTypeName.get(className, typeVariables);
    }

    @Override
    public Iterable<TypeVariableName> createParameters(final ExtendedTypeElement element) {
        return ImmutableList.of(
                TypeVariableName.get(this.variableName.name, ClassName.get(element))
        );
    }

    @Override
    public String toString() {
        return "MetatypeParameterResolver{" +
                "variableName=" + this.variableName +
                ", namingFactory=" + this.namingFactory +
                "}";
    }
}
