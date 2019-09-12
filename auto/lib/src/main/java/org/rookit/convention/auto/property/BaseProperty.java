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
package org.rookit.convention.auto.property;

import org.rookit.auto.javax.type.ExtendedTypeMirror;

// TODO use immutables
final class BaseProperty extends AbstractProperty {

    private final String name;
    private final ExtendedTypeMirror typeMirror;
    private final boolean isContainer;
    private final boolean isFinal;

    BaseProperty(final String name,
                 final ExtendedTypeMirror typeMirror,
                 final boolean isContainer,
                 final boolean isFinal) {
        this.name = name;
        this.typeMirror = typeMirror;
        this.isContainer = isContainer;
        this.isFinal = isFinal;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public ExtendedTypeMirror type() {
        return this.typeMirror;
    }

    @Override
    public boolean isContainer() {
        return this.isContainer;
    }

    @Override
    public boolean isFinal() {
        return this.isFinal;
    }

}
