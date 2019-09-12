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
import org.rookit.convention.auto.javax.ConventionTypeElement;

import java.util.function.Function;

final class PropertiesParamFunction implements Function<ConventionTypeElement, TypeName> {

    private final boolean varEntity;
    private final TypeVariableName variableName;

    PropertiesParamFunction(final boolean varEntity, final TypeVariableName variableName) {
        this.varEntity = varEntity;
        this.variableName = variableName;
    }

    @Override
    public TypeName apply(final ConventionTypeElement element) {
        if (!element.isPropertyContainer() && (this.varEntity || !element.isEntity())) {
            return this.variableName;
        }
        return TypeName.get(element.asType());
    }

    @Override
    public String toString() {
        return "PropertiesParamFunction{" +
                "varEntity=" + this.varEntity +
                ", variableName=" + this.variableName +
                "}";
    }

}