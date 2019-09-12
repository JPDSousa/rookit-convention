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
package org.rookit.convention.auto.javax.visitor;

import com.google.common.collect.Lists;
import one.util.streamex.StreamEx;
import org.rookit.auto.javax.visitor.StreamExtendedElementVisitor;
import org.rookit.convention.auto.javax.ConventionTypeElement;
import org.rookit.convention.auto.javax.ConventionTypeElementFactory;
import org.rookit.convention.auto.property.Property;

import java.util.Collection;
import java.util.function.Predicate;

final class RoutingVisitor<T, P> implements ConventionTypeElementVisitor<StreamEx<T>, P>,
        StreamExtendedElementVisitor<T, P> {

    private final ConventionTypeElementVisitor<StreamEx<T>, P> primaryFactory;
    private final ConventionTypeElementVisitor<StreamEx<T>, P> secondaryFactory;
    private final Predicate<Property> primaryFilter;
    private final ConventionTypeElementFactory elementFactory;

    RoutingVisitor(final ConventionTypeElementVisitor<StreamEx<T>, P> primaryFactory,
                   final ConventionTypeElementVisitor<StreamEx<T>, P> secondaryFactory,
                   final Predicate<Property> primaryFilter,
                   final ConventionTypeElementFactory elementFactory) {
        this.primaryFactory = primaryFactory;
        this.secondaryFactory = secondaryFactory;
        this.primaryFilter = primaryFilter;
        this.elementFactory = elementFactory;
    }

    @Override
    public StreamEx<T> visitConventionType(final ConventionTypeElement element, final P parameter) {
        final Collection<Property> properties = element.properties();
        final int propertiesSize = properties.size();
        final Collection<Property> primaryProperties = Lists.newArrayListWithCapacity(propertiesSize /2);
        final Collection<Property> secondaryProperties = Lists.newArrayListWithCapacity(propertiesSize /2);

        for (final Property property : properties) {
            if (this.primaryFilter.test(property)) {
                primaryProperties.add(property);
            }
            else {
                secondaryProperties.add(property);
            }
        }

        final ConventionTypeElement primaryElement = this.elementFactory
                .changeElementProperties(element, primaryProperties);
        final ConventionTypeElement secondaryElement = this.elementFactory
                .changeElementProperties(element, secondaryProperties);
        return this.primaryFactory.visitConventionType(primaryElement, parameter)
                .append(this.secondaryFactory.visitConventionType(secondaryElement, parameter));
    }

    @Override
    public String toString() {
        return "RoutingVisitor{" +
                "primaryFactory=" + this.primaryFactory +
                ", secondaryFactory=" + this.secondaryFactory +
                ", primaryFilter=" + this.primaryFilter +
                ", elementFactory=" + this.elementFactory +
                "}";
    }
}
