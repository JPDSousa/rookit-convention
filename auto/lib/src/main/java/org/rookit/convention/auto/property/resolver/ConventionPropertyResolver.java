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
package org.rookit.convention.auto.property.resolver;

import com.google.inject.Inject;
import org.rookit.convention.auto.property.PropertyTypeResolver;
import org.rookit.convention.auto.property.Property;
import org.rookit.convention.property.ImmutableCollectionPropertyModel;
import org.rookit.convention.property.ImmutableMapPropertyModel;
import org.rookit.convention.property.ImmutableOptionalPropertyModel;
import org.rookit.convention.property.ImmutablePropertyModel;
import org.rookit.convention.property.MutableCollectionPropertyModel;
import org.rookit.convention.property.MutableMapPropertyModel;
import org.rookit.convention.property.MutableOptionalPropertyModel;
import org.rookit.convention.property.MutablePropertyModel;
import org.rookit.utils.optional.Optional;
import org.rookit.utils.optional.OptionalFactory;
import org.rookit.utils.repetition.Repetition;

final class ConventionPropertyResolver implements PropertyTypeResolver {

    private final OptionalFactory optionalFactory;

    @Inject
    private ConventionPropertyResolver(final OptionalFactory optionalFactory) {
        this.optionalFactory = optionalFactory;
    }

    @Override
    public Class<?> resolve(final Property property) {
        return isCollection(property)
                .orElseMaybe(() -> isMap(property))
                .orElseMaybe(() -> isOptional(property))
                .orElseMaybe(() -> isImmutable(property))
                .orElse(MutablePropertyModel.class);
    }

    private Optional<Class<?>> isImmutable(final Property property) {
        return property.isFinal() ? this.optionalFactory.of(ImmutablePropertyModel.class)
                : this.optionalFactory.empty();
    }

    private Optional<Class<?>> isMap(final Property property) {
        final Repetition repetition = property.type().repetition();

        if (repetition.isKeyed() && repetition.isMulti()) {
            if (property.isFinal()) {
                return this.optionalFactory.of(ImmutableMapPropertyModel.class);
            }
            return this.optionalFactory.of(MutableMapPropertyModel.class);
        }
        return this.optionalFactory.empty();
    }

    private Optional<Class<?>> isOptional(final Property property) {
        final Repetition repetition = property.type().repetition();

        if (repetition.isOptional() && !repetition.isMulti() && !repetition.isKeyed()) {
            if (property.isFinal()) {
                return this.optionalFactory.of(ImmutableOptionalPropertyModel.class);
            }
            return this.optionalFactory.of(MutableOptionalPropertyModel.class);
        }
        return this.optionalFactory.empty();
    }

    private Optional<Class<?>> isCollection(final Property property) {
        final Repetition repetition = property.type().repetition();
        if (repetition.isMulti() && !repetition.isKeyed()) {
            if (property.isFinal()) {
                return this.optionalFactory.of(ImmutableCollectionPropertyModel.class);
            }
            return this.optionalFactory.of(MutableCollectionPropertyModel.class);
        }
        return this.optionalFactory.empty();
    }

    @Override
    public String toString() {
        return "ConventionPropertyResolver{" +
                "optionalFactory=" + this.optionalFactory +
                "}";
    }
}
