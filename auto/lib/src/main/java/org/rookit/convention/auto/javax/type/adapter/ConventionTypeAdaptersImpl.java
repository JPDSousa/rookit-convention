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
package org.rookit.convention.auto.javax.type.adapter;

import com.google.inject.Inject;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.auto.javax.type.ExtendedTypeMirror;
import org.rookit.convention.auto.property.Property;
import org.rookit.convention.auto.property.PropertyFactory;
import org.rookit.convention.auto.javax.ConventionTypeElement;
import org.rookit.convention.auto.javax.ConventionTypeElementFactory;
import org.rookit.utils.adapt.Adapter;

import java.util.function.Predicate;

final class ConventionTypeAdaptersImpl implements ConventionTypeAdapters {

    private final ConventionTypeElementFactory elementFactory;

    @Inject
    private ConventionTypeAdaptersImpl(final ConventionTypeElementFactory elementFactory) {
        this.elementFactory = elementFactory;
    }

    @Override
    public Adapter<ExtendedTypeElement> toExtendedElementAdapter(
            final Adapter<ConventionTypeElement> conventionAdapter) {
        return new Convention2AutoAdapter(conventionAdapter, this.elementFactory);
    }

    @Override
    public Adapter<ConventionTypeElement> createPropertyFilterAdapter(
            final Predicate<Property> propertyFilter,
            final ConventionTypeElementFactory elementFactory) {
        return new PropertyFilterAdapter(propertyFilter, elementFactory);
    }

    @Override
    public Adapter<ConventionTypeElement> createPropertyFlatAdapter(
            final PropertyFactory propertyFactory,
            final ConventionTypeElementFactory elementFactory) {
        return new PropertyFlatAdapter(propertyFactory, elementFactory);
    }

    @Override
    public Adapter<ConventionTypeElement> createPropertyTypeAdapter(
            final PropertyFactory propertyFactory,
            final Adapter<ExtendedTypeMirror> typeMirrorAdapter,
            final Adapter<ConventionTypeElement> upstreamAdapter,
            final ConventionTypeElementFactory elementFactory) {
        return new PropertyTypeAdapter(propertyFactory, typeMirrorAdapter, upstreamAdapter, elementFactory);
    }

    @Override
    public String toString() {
        return "ConventionTypeAdaptersImpl{" +
                "elementFactory=" + this.elementFactory +
                "}";
    }
}
