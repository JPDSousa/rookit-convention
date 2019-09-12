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
package org.rookit.convention.auto.property.filter;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import org.rookit.auto.guice.Annotation;
import org.rookit.convention.auto.property.Property;
import org.rookit.auto.javax.type.ExtendedTypeMirror;
import org.rookit.utils.guice.Collection;
import org.rookit.utils.guice.Immutable;
import org.rookit.utils.guice.Mutable;
import org.rookit.utils.guice.Optional;

import java.util.function.Predicate;

public final class FilterModule extends AbstractModule {

    private static final Module MODULE = new FilterModule();

    public static Module getModule() {
        return MODULE;
    }

    private FilterModule() {}

    @SuppressWarnings({"AnonymousInnerClassMayBeStatic", "AnonymousInnerClass", "EmptyClass"})
    @Override
    protected void configure() {
        bind(new TypeLiteral<Predicate<Property>>() {}).annotatedWith(Annotation.class)
                .to(EntityFilter.class).in(Singleton.class);
        bind(new TypeLiteral<Predicate<Property>>() {}).annotatedWith(Immutable.class)
                .toInstance(Property::isFinal);
        bind(new TypeLiteral<Predicate<Property>>() {}).annotatedWith(Mutable.class)
                .toInstance(property -> !property.isFinal());
    }

    @Provides
    @Singleton
    @Collection
    Predicate<Property> collectionPropertyFilter(@Collection final Predicate<ExtendedTypeMirror> collectionTypeFilter) {
        return new TypeFilteredFilter(collectionTypeFilter);
    }

    @Provides
    @Singleton
    @Optional
    Predicate<Property> optionalPropertyFilter(@Optional final Predicate<ExtendedTypeMirror> optionalTypeFilter) {
        return new TypeFilteredFilter(optionalTypeFilter);
    }
}
