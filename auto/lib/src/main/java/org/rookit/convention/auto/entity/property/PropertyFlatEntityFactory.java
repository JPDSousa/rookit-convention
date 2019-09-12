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
import one.util.streamex.StreamEx;
import org.rookit.auto.guice.NoGeneric;
import org.rookit.auto.javax.naming.Identifier;
import org.rookit.auto.javax.naming.IdentifierFactory;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.auto.source.CodeSource;
import org.rookit.auto.source.CodeSourceContainerFactory;
import org.rookit.auto.source.CodeSourceFactory;
import org.rookit.auto.source.type.SingleTypeSourceFactory;
import org.rookit.auto.source.type.TypeSource;
import org.rookit.convention.auto.entity.PropertyEntityFactory;
import org.rookit.convention.auto.javax.ConventionTypeElement;
import org.rookit.convention.auto.javax.ConventionTypeElementFactory;
import org.rookit.convention.auto.property.ExtendedPropertyEvaluator;
import org.rookit.convention.auto.property.PropertyFactory;

import java.util.Collection;

public final class PropertyFlatEntityFactory implements CodeSourceFactory {

    public static CodeSourceFactory create(final IdentifierFactory identifierFactory,
                                           final CodeSourceFactory codeSourceFactory,
                                           final SingleTypeSourceFactory typeSourceFactory,
                                           final PropertyEntityFactory propEntityFactory,
                                           final ExtendedPropertyEvaluator evaluator,
                                           final CodeSourceContainerFactory containerFactory,
                                           final PropertyFactory propertyFactory,
                                           final ConventionTypeElementFactory elementFactory) {
        return new PropertyFlatEntityFactory(
                identifierFactory,
                codeSourceFactory,
                typeSourceFactory,
                propEntityFactory,
                evaluator, containerFactory, propertyFactory, elementFactory);
    }

    private final IdentifierFactory identifierFactory;
    private final CodeSourceFactory codeSourceFactory;
    private final SingleTypeSourceFactory singleTypeSourceFactory;
    private final PropertyEntityFactory propertyEntityFactory;
    private final ExtendedPropertyEvaluator filter;
    private final CodeSourceContainerFactory containerFactory;
    private final PropertyFactory propertyFactory;
    private final ConventionTypeElementFactory elementFactory;

    @Inject
    private PropertyFlatEntityFactory(final IdentifierFactory identifierFactory,
                                      @NoGeneric final CodeSourceFactory codeSourceFactory,
                                      final SingleTypeSourceFactory typeSourceFactory,
                                      final PropertyEntityFactory propEntityFactory,
                                      final ExtendedPropertyEvaluator filter,
                                      final CodeSourceContainerFactory containerFactory,
                                      final PropertyFactory propertyFactory,
                                      final ConventionTypeElementFactory elementFactory) {
        this.identifierFactory = identifierFactory;
        this.codeSourceFactory = codeSourceFactory;
        this.singleTypeSourceFactory = typeSourceFactory;
        this.propertyEntityFactory = propEntityFactory;
        this.filter = filter;
        this.containerFactory = containerFactory;
        this.propertyFactory = propertyFactory;
        this.elementFactory = elementFactory;
    }

    @Override
    public CodeSource create(final ExtendedTypeElement element) {
        final Identifier identifier = this.identifierFactory.create(element);
        final TypeSource source = this.singleTypeSourceFactory.create(identifier, element);

        final Collection<CodeSource> entities = StreamEx.of(element)
                .map(this.elementFactory::extendType)
                .map(ConventionTypeElement::properties)
                .flatMap(Collection::stream)
                .filter(this.filter)
                .map(this.propertyEntityFactory::create)
                .toImmutableSet();

        return new PropertyFlatEntity(identifier, this.containerFactory.create(entities), source,
                this.codeSourceFactory.create(element));
    }

    @Override
    public String toString() {
        return "PropertyFlatEntityFactory{" +
                "identifierFactory=" + this.identifierFactory +
                ", partialEntityFactory=" + this.codeSourceFactory +
                ", singleTypeSourceFactory=" + this.singleTypeSourceFactory +
                ", propertyEntityFactory=" + this.propertyEntityFactory +
                ", filter=" + this.filter +
                ", containerFactory=" + this.containerFactory +
                ", propertyFactory=" + this.propertyFactory +
                ", elementFactory=" + this.elementFactory +
                "}";
    }
}
