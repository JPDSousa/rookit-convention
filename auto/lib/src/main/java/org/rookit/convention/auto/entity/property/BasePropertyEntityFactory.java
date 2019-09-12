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
import com.google.inject.Provider;
import org.rookit.auto.source.CodeSource;
import org.rookit.auto.source.CodeSourceContainerFactory;
import org.rookit.auto.source.CodeSourceFactory;
import org.rookit.convention.auto.entity.PropertyEntityFactory;
import org.rookit.convention.auto.entity.PropertyPartialEntityFactory;
import org.rookit.convention.auto.entity.PropertyTypeSourceFactory;
import org.rookit.convention.auto.javax.ConventionTypeElement;
import org.rookit.convention.auto.javax.naming.PropertyIdentifierFactory;
import org.rookit.convention.auto.property.Property;
import org.rookit.convention.auto.property.PropertyFactory;

import java.util.Collection;

public final class BasePropertyEntityFactory implements PropertyEntityFactory {

    public static PropertyEntityFactory create(final PropertyIdentifierFactory identifierFactory,
                                               final PropertyTypeSourceFactory typeSourceFactory,
                                               final Provider<CodeSourceFactory> entityFactory,
                                               final PropertyPartialEntityFactory partialEntityFactory,
                                               final CodeSourceContainerFactory containerFactory,
                                               final PropertyFactory propertyFactory) {
        return new BasePropertyEntityFactory(identifierFactory, typeSourceFactory, entityFactory,
                partialEntityFactory, containerFactory, propertyFactory);
    }

    private final PropertyIdentifierFactory identifierFactory;
    private final PropertyTypeSourceFactory typeSourceFactory;
    private final Provider<CodeSourceFactory> entityFactoryProvider;
    private final PropertyPartialEntityFactory partialEntityFactory;
    private final CodeSourceContainerFactory containerFactory;
    private final PropertyFactory propertyFactory;

    @Inject
    private BasePropertyEntityFactory(final PropertyIdentifierFactory identifierFactory,
                                      final PropertyTypeSourceFactory typeSourceFactory,
                                      final Provider<CodeSourceFactory> entityFactory,
                                      final PropertyPartialEntityFactory partialEntityFactory,
                                      final CodeSourceContainerFactory containerFactory,
                                      final PropertyFactory propertyFactory) {
        this.identifierFactory = identifierFactory;
        this.typeSourceFactory = typeSourceFactory;
        this.entityFactoryProvider = entityFactory;
        this.partialEntityFactory = partialEntityFactory;
        this.containerFactory = containerFactory;
        this.propertyFactory = propertyFactory;
    }

    @Override
    public CodeSource create(final Property property) {
        final CodeSourceFactory codeSourceFactory = this.entityFactoryProvider.get();

        final Collection<CodeSource> children = this.propertyFactory.extend(property).typeAsElement()
                .filter(ConventionTypeElement::isPropertyContainer)
                .map(codeSourceFactory::create)
                .toImmutableSet();

        return new PropertyFlatEntity(
                this.identifierFactory.create(property),
                this.containerFactory.create(children),
                this.typeSourceFactory.create(property),
                this.partialEntityFactory.create(property)
        );
    }

    @Override
    public String toString() {
        return "BasePropertyEntityFactory{" +
                "identifierFactory=" + this.identifierFactory +
                ", typeSourceFactory=" + this.typeSourceFactory +
                ", entityFactoryProvider=" + this.entityFactoryProvider +
                ", partialEntityFactory=" + this.partialEntityFactory +
                ", containerFactory=" + this.containerFactory +
                ", propertyFactory=" + this.propertyFactory +
                "}";
    }
}
