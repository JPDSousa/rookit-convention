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
package org.rookit.convention.auto.entity.property;

import com.google.inject.Inject;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.auto.source.CodeSource;
import org.rookit.auto.source.CodeSourceContainer;
import org.rookit.auto.source.CodeSourceContainerFactory;
import org.rookit.auto.source.CodeSourceFactory;
import org.rookit.convention.auto.entity.parent.ParentExtractor;
import org.rookit.convention.auto.javax.ConventionTypeElement;
import org.rookit.convention.auto.javax.ConventionTypeElementFactory;
import org.rookit.utils.optional.OptionalFactory;

import java.util.Collection;

public final class NoGenericPartialEntityFactory implements CodeSourceFactory {

    public static CodeSourceFactory create(final ParentExtractor parentExtractor,
                                           final OptionalFactory optionalFactory,
                                           final CodeSourceContainerFactory containerFactory,
                                           final ConventionTypeElementFactory elementFactory) {
        return new NoGenericPartialEntityFactory(parentExtractor, optionalFactory, containerFactory, elementFactory);
    }

    private final ParentExtractor parentExtractor;
    private final OptionalFactory optionalFactory;
    private final CodeSourceContainerFactory containerFactory;
    private final ConventionTypeElementFactory elementFactory;

    @Inject
    private NoGenericPartialEntityFactory(final ParentExtractor parentExtractor,
                                          final OptionalFactory optionalFactory,
                                          final CodeSourceContainerFactory containerFactory,
                                          final ConventionTypeElementFactory elementFactory) {
        this.parentExtractor = parentExtractor;
        this.optionalFactory = optionalFactory;
        this.containerFactory = containerFactory;
        this.elementFactory = elementFactory;
    }

    @Override
    public CodeSource create(final ExtendedTypeElement element) {
        final ConventionTypeElement conventionElement = this.elementFactory.extendType(element);
        final Collection<CodeSource> partialEntities = this.parentExtractor.extractAsIterable(conventionElement);
        final CodeSourceContainer<CodeSource> container = this.containerFactory.create(partialEntities);

        return new NoGenericPartialEntity(this.optionalFactory, container);
    }

    @Override
    public String toString() {
        return "NoGenericPartialEntityFactory{" +
                "parentExtractor=" + this.parentExtractor +
                ", optionalFactory=" + this.optionalFactory +
                ", containerFactory=" + this.containerFactory +
                ", elementFactory=" + this.elementFactory +
                "}";
    }
}
