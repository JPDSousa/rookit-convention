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

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.rookit.auto.entity.BaseEntityFactory;
import org.rookit.auto.entity.BasePartialEntityFactory;
import org.rookit.auto.entity.EntityFactory;
import org.rookit.auto.entity.PartialEntityFactory;
import org.rookit.auto.entity.parent.NoOpParentExtractor;
import org.rookit.auto.entity.parent.ParentExtractor;
import org.rookit.auto.identifier.EntityIdentifierFactory;
import org.rookit.auto.source.SingleTypeSourceFactory;
import org.rookit.convention.api.guice.Container;
import org.rookit.convention.api.guice.Inner;
import org.rookit.convention.guice.MetatypeAPI;
import org.rookit.convention.guice.PartialMetatypeAPI;
import org.rookit.utils.optional.OptionalFactory;

@SuppressWarnings("MethodMayBeStatic")
public final class EntityModule extends AbstractModule {

    private static final Module MODULE = new EntityModule();

    public static Module getModule() {
        return MODULE;
    }

    private EntityModule() {}

    @Override
    protected void configure() {
        bind(PartialEntityFactory.class).to(Key.get(PartialEntityFactory.class, MetatypeAPI.class)).in(Singleton.class);

        bind(PartialEntityFactory.class).annotatedWith(MetatypeAPI.class)
                .to(MetatypePartialEntityFactory.class).in(Singleton.class);

        bind(EntityFactory.class).annotatedWith(Container.class)
                .to(PropertyEntityFactory.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    @Inner
    PartialEntityFactory innerPartialEntityFactory(@PartialMetatypeAPI final EntityIdentifierFactory identifierFactory,
                                                   @PartialMetatypeAPI final SingleTypeSourceFactory typeSpecFactory,
                                                   final OptionalFactory optionalFactory,
                                                   final ParentExtractor extractor) {
        return BasePartialEntityFactory.create(identifierFactory, typeSpecFactory, optionalFactory, extractor);
    }

    @Provides
    @Singleton
    EntityFactory entityFactory(final PartialEntityFactory partialEntityFactory,
                                @MetatypeAPI final EntityIdentifierFactory identifierFactory,
                                @MetatypeAPI final SingleTypeSourceFactory typeSpecFactory) {
        return BaseEntityFactory.create(partialEntityFactory, identifierFactory, typeSpecFactory);
    }

    @Provides
    @Singleton
    @Inner
    EntityFactory innerEntityFactory(@Container final PartialEntityFactory partialEntityFactory,
                                     @MetatypeAPI final EntityIdentifierFactory identifierFactory,
                                     @Container final SingleTypeSourceFactory typeSpecFactory) {
        return BaseEntityFactory.create(partialEntityFactory, identifierFactory, typeSpecFactory);
    }

    @Provides
    @Singleton
    @Container
    PartialEntityFactory containerPartialEntityFactory(@MetatypeAPI final EntityIdentifierFactory identifierFactory,
                                                       @Container final SingleTypeSourceFactory typeSpecFactory,
                                                       final OptionalFactory optionalFactory) {
        return BasePartialEntityFactory.create(identifierFactory, typeSpecFactory, optionalFactory,
                // FIXME and inject me
                NoOpParentExtractor.create());
    }
}
