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
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeVariableName;
import org.rookit.auto.javapoet.naming.JavaPoetParameterResolver;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.convention.auto.metatype.guice.MetaTypeAPI;

final class PropertyParameterResolver implements JavaPoetParameterResolver {

    private final TypeVariableName variableName;

    @Inject
    private PropertyParameterResolver(@MetaTypeAPI final TypeVariableName variableName) {
        this.variableName = variableName;
    }

    @Override
    public TypeName resolveParameters(final ExtendedTypeElement element, final TypeVariableName... typeVariables) {
        return TypeName.get(element.asType());
    }

    @Override
    public Iterable<TypeVariableName> createParameters(final ExtendedTypeElement element) {
        return ImmutableList.of(this.variableName);
    }

    @Override
    public String toString() {
        return "PropertyParameterResolver{" +
                "variableName=" + this.variableName +
                "}";
    }
}
