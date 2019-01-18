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
import org.rookit.auto.entity.AbstractPartialEntityFactory;
import org.rookit.auto.entity.Entity;
import org.rookit.auto.entity.EntityFactory;
import org.rookit.auto.entity.PartialEntity;
import org.rookit.auto.identifier.EntityIdentifierFactory;
import org.rookit.auto.javax.element.ExtendedTypeElement;
import org.rookit.auto.javax.property.ExtendedProperty;
import org.rookit.auto.naming.NamingFactory;
import org.rookit.auto.source.SingleTypeSourceFactory;
import org.rookit.convention.api.guice.Container;
import org.rookit.convention.utils.guice.PartialMetatypeAPI;
import org.rookit.utils.optional.Optional;
import org.rookit.utils.optional.OptionalFactory;

import java.util.Collection;
import java.util.stream.Collectors;

final class MetatypePartialEntityFactory extends AbstractPartialEntityFactory {

    private final NamingFactory namingFactory;
    private final EntityFactory entityFactory;

    @Inject
    private MetatypePartialEntityFactory(@PartialMetatypeAPI final EntityIdentifierFactory identifierFactory,
                                         @PartialMetatypeAPI final SingleTypeSourceFactory typeSpecFactory,
                                         @PartialMetatypeAPI final NamingFactory namingFactory,
                                         final OptionalFactory optionalFactory,
                                         @Container final EntityFactory entityFactory) {
        super(identifierFactory, typeSpecFactory, optionalFactory);
        this.namingFactory = namingFactory;
        this.entityFactory = entityFactory;
    }

    @Override
    public PartialEntity create(final ExtendedTypeElement element) {
        final Collection<Entity> propertyEntities = element.properties().stream()
                .filter(ExtendedProperty::isContainer)
                .map(ExtendedProperty::typeAsElement)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(this.entityFactory::create)
                .collect(Collectors.toList());

        return new PartialEntityWithProperties(super.create(element), propertyEntities);
    }

    @Override
    protected StreamEx<PartialEntity> entitiesFor(final ExtendedTypeElement parent) {
        return StreamEx.of(create(parent));
    }

    @Override
    public String toString() {
        return "MetatypePartialEntityFactory{" +
                "namingFactory=" + this.namingFactory +
                ", entityFactory=" + this.entityFactory +
                "} " + super.toString();
    }
}
