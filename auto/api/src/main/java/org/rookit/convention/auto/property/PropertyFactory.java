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

import org.rookit.auto.javax.executable.ExtendedExecutableElement;
import org.rookit.auto.javax.type.ExtendedTypeMirror;
import org.rookit.utils.optional.Optional;

public interface PropertyFactory {

    Property changeType(Property source, ExtendedTypeMirror newReturnType);

    Property changeName(Property source, String newName);

    ContainerProperty changeName(ContainerProperty source, String newName);

    Optional<ContainerProperty> toContainer(ExtendedProperty property);

    default Optional<ContainerProperty> toContainer(final Property property) {
        return toContainer(extend(property));
    }

    ExtendedProperty extend(Property property);

    Property createMutable(CharSequence name, ExtendedTypeMirror type);

    Property createImmutable(CharSequence name, ExtendedTypeMirror type);

    default Property create(final ExtendedExecutableElement method) {
        return createMutable(method.getSimpleName(), method.getReturnType());
    }

    default Property createMutable(final ExtendedExecutableElement method) {
        return createMutable(method.getSimpleName(), method.getReturnType());
    }

    default Property createImmutable(final ExtendedExecutableElement method) {
        return createImmutable(method.getSimpleName(), method.getReturnType());
    }

}
