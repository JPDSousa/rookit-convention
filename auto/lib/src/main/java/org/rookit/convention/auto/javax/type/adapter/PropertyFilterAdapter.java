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

import one.util.streamex.StreamEx;
import org.rookit.convention.auto.property.Property;
import org.rookit.convention.auto.javax.ConventionTypeElement;
import org.rookit.convention.auto.javax.ConventionTypeElementFactory;
import org.rookit.utils.adapt.Adapter;

import java.util.Collection;
import java.util.function.Predicate;

final class PropertyFilterAdapter implements Adapter<ConventionTypeElement> {

    private final Predicate<Property> propertyFilter;
    private final ConventionTypeElementFactory factory;

    PropertyFilterAdapter(final Predicate<Property> propertyFilter, final ConventionTypeElementFactory factory) {
        this.propertyFilter = propertyFilter;
        this.factory = factory;
    }

    @Override
    public ConventionTypeElement adapt(final ConventionTypeElement source) {
        final Collection<Property> newProperties = StreamEx.of(source.properties())
                .filter(this.propertyFilter)
                .toImmutableList();

        return this.factory.changeElementProperties(source, newProperties);
    }

    @Override
    public String toString() {
        return "PropertyFilterAdapter{" +
                "propertyFilter=" + this.propertyFilter +
                ", factory=" + this.factory +
                "}";
    }
}
