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
import org.rookit.auto.javax.naming.IdentifierFactory;
import org.rookit.auto.source.CodeSourceContainerFactory;
import org.rookit.auto.source.CodeSourceFactory;
import org.rookit.auto.source.type.SingleTypeSourceFactory;
import org.rookit.convention.api.guice.Container;
import org.rookit.convention.api.guice.Inner;
import org.rookit.convention.auto.entity.BaseEntityFactory;
import org.rookit.convention.auto.entity.BasePartialEntityFactory;
import org.rookit.convention.auto.entity.parent.NoOpParentExtractor;
import org.rookit.convention.auto.entity.parent.ParentExtractor;
import org.rookit.convention.auto.metatype.guice.MetaTypeAPI;
import org.rookit.convention.auto.metatype.guice.PartialMetaTypeAPI;
import org.rookit.convention.auto.javax.ConventionTypeElementFactory;
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
        bind(CodeSourceFactory.class).to(Key.get(CodeSourceFactory.class, MetaTypeAPI.class)).in(Singleton.class);

        bind(CodeSourceFactory.class).annotatedWith(MetaTypeAPI.class)
                .to(MetaTypePartialEntityFactory.class).in(Singleton.class);

        bind(CodeSourceFactory.class).annotatedWith(Container.class)
                .to(PropertyEntityFactory.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    @Inner
    CodeSourceFactory innerPartialEntityFactory(@PartialMetaTypeAPI final IdentifierFactory identifierFactory,
                                                @PartialMetaTypeAPI final SingleTypeSourceFactory typeSpecFactory,
                                                final OptionalFactory optionalFactory,
                                                final ParentExtractor extractor,
                                                final CodeSourceContainerFactory containerFactory,
                                                final ConventionTypeElementFactory elementFactory) {
        return BasePartialEntityFactory.create(identifierFactory, typeSpecFactory, optionalFactory,
                extractor, containerFactory, elementFactory);
    }

    @Provides
    @Singleton
    CodeSourceFactory entityFactory(final CodeSourceFactory codeSourceFactory,
                                    @MetaTypeAPI final IdentifierFactory identifierFactory,
                                    @MetaTypeAPI final SingleTypeSourceFactory typeSpecFactory) {
        return BaseEntityFactory.create(codeSourceFactory, identifierFactory, typeSpecFactory);
    }

    @Provides
    @Singleton
    @Inner
    CodeSourceFactory innerEntityFactory(@Container final CodeSourceFactory codeSourceFactory,
                                         @MetaTypeAPI final IdentifierFactory identifierFactory,
                                         @Container final SingleTypeSourceFactory typeSpecFactory) {
        return BaseEntityFactory.create(codeSourceFactory, identifierFactory, typeSpecFactory);
    }

    @Provides
    @Singleton
    @Container
    CodeSourceFactory containerPartialEntityFactory(@MetaTypeAPI final IdentifierFactory identifierFactory,
                                                    @Container final SingleTypeSourceFactory typeSpecFactory,
                                                    final OptionalFactory optionalFactory,
                                                    final CodeSourceContainerFactory containerFactory,
                                                    final ConventionTypeElementFactory elementFactory) {
        return BasePartialEntityFactory.create(identifierFactory, typeSpecFactory, optionalFactory,
                // FIXME and inject me
                NoOpParentExtractor.create(), containerFactory, elementFactory);
    }
}
