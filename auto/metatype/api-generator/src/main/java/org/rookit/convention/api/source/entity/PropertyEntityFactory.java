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
package org.rookit.convention.api.source.entity;

import com.google.inject.Inject;
import one.util.streamex.StreamEx;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.auto.source.CodeSource;
import org.rookit.auto.source.CodeSourceContainerFactory;
import org.rookit.auto.source.CodeSourceFactory;
import org.rookit.convention.api.guice.Inner;
import org.rookit.convention.auto.javax.ConventionTypeElement;
import org.rookit.convention.auto.javax.ConventionTypeElementFactory;
import org.rookit.convention.auto.property.ContainerProperty;
import org.rookit.convention.auto.property.PropertyFactory;
import org.rookit.utils.optional.Optional;

import java.util.Collection;
import java.util.stream.Collectors;

final class PropertyEntityFactory implements CodeSourceFactory {
    
    private final CodeSourceFactory codeSourceFactory;
    private final CodeSourceContainerFactory containerFactory;
    private final PropertyFactory propertyFactory;
    private final ConventionTypeElementFactory elementFactory;

    @Inject
    private PropertyEntityFactory(@Inner final CodeSourceFactory codeSourceFactory,
                                  final CodeSourceContainerFactory containerFactory,
                                  final PropertyFactory propertyFactory,
                                  final ConventionTypeElementFactory elementFactory) {
        this.codeSourceFactory = codeSourceFactory;
        this.containerFactory = containerFactory;
        this.propertyFactory = propertyFactory;
        this.elementFactory = elementFactory;
    }

    @Override
    public CodeSource create(final ExtendedTypeElement element) {
        final Collection<CodeSource> propertyEntities = StreamEx.of(element)
                .map(this.elementFactory::extendType)
                .map(ConventionTypeElement::properties)
                .flatMap(Collection::stream)
                .map(this.propertyFactory::extend)
                .map(this.propertyFactory::toContainer)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(ContainerProperty::typeAsElement)
                .map(this::create)
                .collect(Collectors.toList());

        return new EntityWithProperties(this.codeSourceFactory.create(element),
                this.containerFactory.create(propertyEntities));
    }

    @Override
    public String toString() {
        return "PropertyEntityFactory{" +
                "entityFactory=" + this.codeSourceFactory +
                ", containerFactory=" + this.containerFactory +
                ", propertyFactory=" + this.propertyFactory +
                ", elementFactory=" + this.elementFactory +
                "}";
    }
}
