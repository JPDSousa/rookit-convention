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
package org.rookit.convention.guice.source.type;

import com.google.inject.Inject;
import org.rookit.auto.identifier.Identifier;
import org.rookit.auto.identifier.PropertyIdentifierFactory;
import org.rookit.auto.javapoet.type.TypeSourceAdapter;
import org.rookit.auto.javax.property.ExtendedProperty;
import org.rookit.auto.javax.element.ExtendedTypeElement;
import org.rookit.auto.source.PropertyTypeSourceFactory;
import org.rookit.auto.source.TypeSource;

final class BindAnnotationPropertyFactory extends AbstractBindAnnotationFactory implements PropertyTypeSourceFactory {

    private final PropertyIdentifierFactory idFactory;

    @Inject
    private BindAnnotationPropertyFactory(final TypeSourceAdapter adapter, final PropertyIdentifierFactory idFactory) {
        super(adapter);
        this.idFactory = idFactory;
    }

    @Override
    public TypeSource create(final ExtendedTypeElement parent, final ExtendedProperty property) {
        final Identifier identifier = this.idFactory.create(parent, property);
        return create(identifier, identifier.name());
    }

    @Override
    public TypeSource create(final ExtendedProperty property) {
        final Identifier identifier = this.idFactory.create(property);
        return create(identifier, identifier.name());
    }

    @Override
    public String toString() {
        return "BindAnnotationPropertyFactory{" +
                "idFactory=" + this.idFactory +
                "} " + super.toString();
    }
}
