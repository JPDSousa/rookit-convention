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
import one.util.streamex.StreamEx;
import org.apache.commons.text.WordUtils;
import org.rookit.convention.auto.javax.ConventionTypeElement;
import org.rookit.convention.auto.javax.ConventionTypeElementFactory;
import org.rookit.convention.auto.property.ContainerProperty;
import org.rookit.convention.auto.property.Property;
import org.rookit.convention.auto.property.PropertyFactory;
import org.rookit.utils.adapt.Adapter;
import org.rookit.utils.optional.Optional;

import java.util.Collection;

final class PropertyFlatAdapter implements Adapter<ConventionTypeElement> {

    private final PropertyFactory propertyFactory;
    private final ConventionTypeElementFactory factory;

    @Inject
    PropertyFlatAdapter(final PropertyFactory propertyFactory, final ConventionTypeElementFactory factory) {
        this.propertyFactory = propertyFactory;
        this.factory = factory;
    }

    @Override
    public ConventionTypeElement adapt(final ConventionTypeElement source) {
        final Collection<Property> flatProperties = extractContainerProperties(source)
                .flatMap(this::flatProperties)
                .toImmutableList();

        return this.factory.changeElementProperties(source, flatProperties);
    }

    private StreamEx<Property> flatProperties(final ContainerProperty property) {
        return extractContainerProperties(property.typeAsElement())
                .map(nestedProperty -> concat(property, nestedProperty))
                .flatMap(this::flatProperties);
    }

    private StreamEx<ContainerProperty> extractContainerProperties(final ConventionTypeElement element) {
        return StreamEx.of(element.properties())
                .filter(Property::isContainer)
                .map(this.propertyFactory::toContainer)
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    private ContainerProperty concat(final Property property, final ContainerProperty nestedProperty) {
        final String name = property.name() + WordUtils.capitalize(nestedProperty.name());
        return this.propertyFactory.changeName(nestedProperty, name);
    }

    @Override
    public String toString() {
        return "PropertyFlatAdapter{" +
                "propertyFactory=" + this.propertyFactory +
                ", factory=" + this.factory +
                "}";
    }
}
