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
package org.rookit.convention.auto.entity.parent;

import com.google.inject.Inject;
import one.util.streamex.StreamEx;
import org.rookit.auto.source.CodeSource;
import org.rookit.auto.source.CodeSourceFactory;
import org.rookit.convention.auto.javax.ConventionTypeElement;
import org.rookit.convention.auto.property.Property;
import org.rookit.convention.auto.property.PropertyFactory;

public final class MultiFactoryParentExtractor implements ParentExtractor {

    public static ParentExtractor create(final CodeSourceFactory elementFactory,
                                         final CodeSourceFactory childFactory,
                                         final PropertyFactory propertyFactory) {
        return new MultiFactoryParentExtractor(elementFactory, childFactory, propertyFactory);
    }

    private final CodeSourceFactory elementFactory;
    private final CodeSourceFactory childFactory;
    private final PropertyFactory propertyFactory;

    @Inject
    private MultiFactoryParentExtractor(final CodeSourceFactory elementFactory,
                                        final CodeSourceFactory childFactory,
                                        final PropertyFactory propertyFactory) {
        this.elementFactory = elementFactory;
        this.childFactory = childFactory;
        this.propertyFactory = propertyFactory;
    }

    @Override
    public StreamEx<CodeSource> extractFrom(final ConventionTypeElement element) {
        return element.conventionInterfaces()
                .map(this.elementFactory::create)
                .append(this.childFactory.create(element));
    }

    @Override
    public StreamEx<CodeSource> extractFrom(final Property property) {
        return this.propertyFactory.extend(property).typeAsElement()
                .map(this::extractFrom)
                .orElse(StreamEx.empty());
    }

    @Override
    public String toString() {
        return "MultiFactoryParentExtractor{" +
                "elementFactory=" + this.elementFactory +
                ", childFactory=" + this.childFactory +
                ", propertyFactory=" + this.propertyFactory +
                "}";
    }
}
