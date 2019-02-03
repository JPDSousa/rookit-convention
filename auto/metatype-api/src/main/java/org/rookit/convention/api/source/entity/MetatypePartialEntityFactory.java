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
import org.rookit.auto.entity.Entity;
import org.rookit.auto.entity.EntityFactory;
import org.rookit.auto.entity.PartialEntity;
import org.rookit.auto.entity.PartialEntityFactory;
import org.rookit.auto.entity.cache.AbstractCachePartialEntityFactory;
import org.rookit.auto.javax.element.ExtendedTypeElement;
import org.rookit.auto.javax.property.ExtendedProperty;
import org.rookit.convention.api.guice.Container;
import org.rookit.convention.api.guice.Inner;
import org.rookit.utils.optional.Optional;

import java.util.Collection;
import java.util.stream.Collectors;

final class MetatypePartialEntityFactory extends AbstractCachePartialEntityFactory {

    private final EntityFactory entityFactory;
    private final PartialEntityFactory innerFactory;

    @Inject
    private MetatypePartialEntityFactory(@Container final EntityFactory entityFactory,
                                         @Inner final PartialEntityFactory innerFactory) {
        this.entityFactory = entityFactory;
        this.innerFactory = innerFactory;
    }

    @Override
    public PartialEntity createNew(final ExtendedTypeElement element) {
        final Collection<Entity> propertyEntities = element.properties().stream()
                .filter(ExtendedProperty::isContainer)
                .map(ExtendedProperty::typeAsElement)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(this.entityFactory::create)
                .collect(Collectors.toList());

        return new PartialEntityWithProperties(this.innerFactory.create(element), propertyEntities);
    }

}
