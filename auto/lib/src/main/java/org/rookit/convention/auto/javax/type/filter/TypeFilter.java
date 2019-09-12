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
package org.rookit.convention.auto.javax.type.filter;

import com.google.inject.Inject;
import org.rookit.auto.javax.type.ExtendedTypeMirror;
import org.rookit.convention.auto.javax.visitor.TypeBasedMethodVisitor;
import org.rookit.convention.auto.property.Property;

import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;

@Deprecated // TODO move this to the common approach of filtering the properties inside the ExtendedTypeElement
public final class TypeFilter<P> implements Predicate<Property> {

    public static <P> Predicate<Property> create(
            final Set<TypeBasedMethodVisitor<P>> factories) {
        return new TypeFilter<>(factories);
    }

    private final Collection<TypeBasedMethodVisitor<P>> typeFactories;

    @Inject
    private TypeFilter(final Set<TypeBasedMethodVisitor<P>> typeFactories) {
        this.typeFactories = typeFactories;
    }

    @Override
    public boolean test(final Property property) {
        final ExtendedTypeMirror propertyType = property.type();

        return this.typeFactories.stream()
                .anyMatch(factory -> factory.type().isSameTypeErasure(propertyType));
    }

    @Override
    public String toString() {
        return "TypeFilter{" +
                "typeFactories=" + this.typeFactories +
                "}";
    }
}
