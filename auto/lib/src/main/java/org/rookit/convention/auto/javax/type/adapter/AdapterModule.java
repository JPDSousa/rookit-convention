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

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import org.rookit.auto.guice.Flat;
import org.rookit.auto.javax.type.ExtendedTypeMirror;
import org.rookit.convention.auto.javax.ConventionTypeElement;
import org.rookit.convention.auto.javax.ConventionTypeElementFactory;
import org.rookit.convention.auto.property.Property;
import org.rookit.convention.auto.property.PropertyFactory;
import org.rookit.utils.adapt.Adapter;
import org.rookit.utils.guice.Collection;
import org.rookit.utils.guice.Immutable;
import org.rookit.utils.guice.Mutable;
import org.rookit.utils.guice.Optional;

import java.util.function.Predicate;

@SuppressWarnings("MethodMayBeStatic")
final class AdapterModule extends AbstractModule {

    private static final Module MODULE = new AdapterModule();

    public static Module getModule() {
        return MODULE;
    }

    private AdapterModule() {}

    @SuppressWarnings({"AnonymousInnerClassMayBeStatic", "AnonymousInnerClass", "EmptyClass"})
    @Override
    protected void configure() {
        bind(new TypeLiteral<Adapter<ConventionTypeElement>>() {}).annotatedWith(Flat.class)
                .to(PropertyFlatAdapter.class).in(Singleton.class);
        bind(ConventionTypeAdapters.class).to(ConventionTypeAdaptersImpl.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    @Collection
    Adapter<ConventionTypeElement> collectionPropertyFilteredElement(
            final ConventionTypeAdapters typeAdapters,
            @Collection final Predicate<Property> collectionFilter,
            final ConventionTypeElementFactory elementFactory) {
        return typeAdapters.createPropertyFilterAdapter(collectionFilter, elementFactory);
    }

    @Provides
    @Singleton
    @Collection(unwrap = true)
    Adapter<ConventionTypeElement> collectionPropertyFilteredUnwrappedElement(
            final ConventionTypeAdapters typeAdapters,
            final PropertyFactory propertyFactory,
            @Collection(unwrap = true) final Adapter<ExtendedTypeMirror> collectionUnwrapper,
            @Collection final Adapter<ConventionTypeElement> collectionAdapter,
            final ConventionTypeElementFactory elementFactory
    ) {
        return typeAdapters.createPropertyTypeAdapter(
                propertyFactory,
                collectionUnwrapper,
                collectionAdapter,
                elementFactory
        );
    }

    @Provides
    @Singleton
    @Mutable
    Adapter<ConventionTypeElement> mutablePropertyFilteredElement(
            final ConventionTypeAdapters adapters,
            @Mutable final Predicate<Property> filter,
            final ConventionTypeElementFactory elementFactory) {
        return adapters.createPropertyFilterAdapter(filter, elementFactory);
    }

    @Provides
    @Singleton
    @Immutable
    Adapter<ConventionTypeElement> immutablePropertyFilteredElement(
            final ConventionTypeAdapters adapters,
            @Immutable final Predicate<org.rookit.convention.auto.property.Property> filter,
            final ConventionTypeElementFactory elementFactory) {
        return adapters.createPropertyFilterAdapter(filter, elementFactory);
    }

    @Provides
    @Singleton
    @Optional
    Adapter<ConventionTypeElement> optionalUnwrapper(
            final ConventionTypeAdapters adapters,
            final PropertyFactory propertyFactory,
            @Optional final Adapter<ExtendedTypeMirror> mirrorAdapter,
            @Optional final Adapter<ConventionTypeElement> propertyAdapter,
            final ConventionTypeElementFactory elementFactory) {
        return adapters.createPropertyTypeAdapter(propertyFactory, mirrorAdapter, propertyAdapter, elementFactory);
    }
}
