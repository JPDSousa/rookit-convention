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
import org.rookit.auto.javax.type.ExtendedTypeMirror;
import org.rookit.auto.javax.type.ExtendedTypeMirrorFactory;
import org.rookit.convention.auto.javax.ConventionElementUtils;
import org.rookit.utils.optional.Optional;

import javax.lang.model.element.TypeElement;

final class RecursiveExtendedPropertyExtractor implements ExtendedPropertyExtractor {

    private final ExtendedPropertyExtractor delegate;
    private final ConventionElementUtils utils;
    private final ExtendedTypeMirrorFactory mirrorFactory;

    RecursiveExtendedPropertyExtractor(final ExtendedPropertyExtractor delegate,
                                       final ConventionElementUtils utils,
                                       final ExtendedTypeMirrorFactory mirrorFactory) {
        this.delegate = delegate;
        this.utils = utils;
        this.mirrorFactory = mirrorFactory;
    }

    @Override
    public StreamEx<Property> fromType(final TypeElement element) {
        // TODO copied from AbstractTypeElementDecorator#conventionInterfaces
        return StreamEx.of(element.getInterfaces())
                .map(this.mirrorFactory::create)
                .map(ExtendedTypeMirror::toElement)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .select(TypeElement.class)
                .filter(this.utils::isConventionElement)
                .flatMap(this::fromType)
                .append(this.delegate.fromType(element));
    }

    @Override
    public String toString() {
        return "RecursiveExtendedPropertyExtractor{" +
                "delegate=" + this.delegate +
                ", utils=" + this.utils +
                ", mirrorFactory=" + this.mirrorFactory +
                "}";
    }
}
