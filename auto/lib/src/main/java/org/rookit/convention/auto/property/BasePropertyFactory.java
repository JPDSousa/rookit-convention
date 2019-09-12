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
package org.rookit.convention.auto.property;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.rookit.auto.javax.ElementUtils;
import org.rookit.auto.javax.type.ExtendedTypeMirror;
import org.rookit.convention.annotation.PropertyContainer;
import org.rookit.convention.auto.javax.ConventionTypeElement;
import org.rookit.convention.auto.javax.ConventionTypeElementFactory;
import org.rookit.utils.optional.Optional;

import java.util.Objects;

public final class BasePropertyFactory implements PropertyFactory {

    public static PropertyFactory create(final ElementUtils utils,
                                         final Provider<ConventionTypeElementFactory> elementFactory) {
        return new BasePropertyFactory(utils, elementFactory);
    }

    private final ElementUtils utils;
    // TODO do not use providers
    private final Provider<ConventionTypeElementFactory> elementFactory;

    @Inject
    private BasePropertyFactory(final ElementUtils utils,
                                final Provider<ConventionTypeElementFactory> elementFactory) {
        this.utils = utils;
        this.elementFactory = elementFactory;
    }

    @Override
    public Property changeType(final Property source, final ExtendedTypeMirror newReturnType) {
        return new ReturnProperty(source, newReturnType);
    }

    @Override
    public Property changeName(final Property source, final String newName) {
        return new NameProperty(newName, source);
    }

    @Override
    public ContainerProperty changeName(final ContainerProperty source, final String newName) {
        return new NameContainerProperty(newName, source);
    }

    @Override
    public Optional<ContainerProperty> toContainer(final ExtendedProperty property) {
        return property.typeAsElement()
                .filter(ConventionTypeElement::isPropertyContainer)
                .map(type -> new BaseContainerProperty(property, type));
    }

    @Override
    public ExtendedProperty extend(final Property property) {
        if (property instanceof ExtendedProperty) {
            return (ExtendedProperty) property;
        }

        return new BaseExtendedProperty(property, this.utils, this.elementFactory.get());
    }

    @Override
    public Property createMutable(final CharSequence name, final ExtendedTypeMirror type) {
        return create(name, type, false);
    }

    @Override
    public Property createImmutable(final CharSequence name, final ExtendedTypeMirror type) {
        return create(name, type, true);
    }

    private Property create(final CharSequence name,
                            final ExtendedTypeMirror typeMirror,
                            final boolean isFinal) {
        final boolean isContainer = typeMirror.toElement()
                .filter(type -> Objects.nonNull(type.getAnnotation(PropertyContainer.class)))
                .isPresent();
        return new BaseProperty(name.toString(), typeMirror, isContainer, isFinal);
    }

    @Override
    public String toString() {
        return "BasePropertyFactory{" +
                ", utils=" + this.utils +
                ", elementFactory=" + this.elementFactory +
                "}";
    }
}
