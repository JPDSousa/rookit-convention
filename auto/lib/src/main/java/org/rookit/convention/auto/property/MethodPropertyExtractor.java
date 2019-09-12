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

import one.util.streamex.StreamEx;
import org.rookit.auto.javax.ExtendedElementFactory;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.function.Predicate;

final class MethodPropertyExtractor implements ExtendedPropertyExtractor {

    private final ExtendedElementFactory elementFactory;
    private final PropertyFactory propertyFactory;
    private final Predicate<ExecutableElement> methodFilter;

    MethodPropertyExtractor(final ExtendedElementFactory elementFactory,
                            final PropertyFactory propertyFactory,
                            final Predicate<ExecutableElement> methodFilter) {
        this.elementFactory = elementFactory;
        this.propertyFactory = propertyFactory;
        this.methodFilter = methodFilter;
    }

    @Override
    public StreamEx<Property> fromType(final TypeElement element) {
        return StreamEx.of(element.getEnclosedElements())
                .filter(el -> el.getKind() == ElementKind.METHOD)
                .select(ExecutableElement.class)
                .filter(this.methodFilter)
                .select(ExecutableElement.class)
                .map(this.elementFactory::extendExecutable)
                .map(this.propertyFactory::create);
    }

    @Override
    public String toString() {
        return "MethodPropertyExtractor{" +
                "elementFactory=" + this.elementFactory +
                ", propertyFactory=" + this.propertyFactory +
                ", methodFilter=" + this.methodFilter +
                "}";
    }
}
