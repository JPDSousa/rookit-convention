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

import com.google.inject.Inject;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeVariableName;
import org.rookit.auto.javapoet.type.TypeVariableNameResolver;
import org.rookit.auto.javax.element.ExtendedTypeElement;
import org.rookit.convention.guice.Metatype;

final class MetatypeTypeVariableNameResolver implements TypeVariableNameResolver {

    private final TypeVariableName typeVariableName;

    @Inject
    private MetatypeTypeVariableNameResolver(@Metatype final TypeVariableName typeVariableName) {
        this.typeVariableName = typeVariableName;
    }

    @Override
    public TypeVariableName resolve(final ExtendedTypeElement element) {
        return TypeVariableName.get(this.typeVariableName.name, ClassName.get(element));
    }

    @Override
    public String toString() {
        return "MetatypeTypeVariableNameResolver{" +
                "typeVariableName=" + this.typeVariableName +
                "}";
    }
}
