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

import com.google.inject.Inject;
import org.rookit.auto.javax.ExtendedElementFactory;
import org.rookit.auto.javax.type.ExtendedTypeMirrorFactory;
import org.rookit.convention.auto.javax.ConventionElementUtils;

import javax.lang.model.element.ExecutableElement;
import java.util.function.Predicate;

final class ExtendedPropertyExtractorFactoryImpl implements ExtendedPropertyExtractorFactory {

    private final ExtendedElementFactory elementFactory;
    private final PropertyFactory propertyFactory;
    private final ConventionElementUtils utils;
    private final ExtendedTypeMirrorFactory mirrorFactory;

    @Inject
    private ExtendedPropertyExtractorFactoryImpl(final ExtendedElementFactory elementFactory,
                                                 final PropertyFactory propertyFactory,
                                                 final ConventionElementUtils utils,
                                                 final ExtendedTypeMirrorFactory mirrorFactory) {
        this.elementFactory = elementFactory;
        this.propertyFactory = propertyFactory;
        this.utils = utils;
        this.mirrorFactory = mirrorFactory;
    }

    @Override
    public ExtendedPropertyExtractor create(final Predicate<ExecutableElement> executableFilter) {
        return create(this.propertyFactory, executableFilter);
    }

    @Override
    public ExtendedPropertyExtractor create(final PropertyFactory propertyFactory,
                                            final Predicate<ExecutableElement> executableFilter) {
        return new MethodPropertyExtractor(this.elementFactory, propertyFactory, executableFilter);
    }

    @Override
    public ExtendedPropertyExtractor createRecursive(final ExtendedPropertyExtractor delegate) {
        return new RecursiveExtendedPropertyExtractor(delegate, this.utils, this.mirrorFactory);
    }

    @Override
    public String toString() {
        return "ExtendedPropertyExtractorFactoryImpl{" +
                "elementFactory=" + this.elementFactory +
                ", propertyFactory=" + this.propertyFactory +
                ", utils=" + this.utils +
                ", mirrorFactory=" + this.mirrorFactory +
                "}";
    }
}
