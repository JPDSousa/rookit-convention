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
package org.rookit.convention.auto.javax;

import one.util.streamex.StreamEx;
import org.rookit.auto.javax.visitor.ExtendedElementVisitor;
import org.rookit.convention.auto.javax.visitor.ConventionTypeElementVisitor;
import org.rookit.convention.auto.property.Property;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.utils.optional.Optional;

import java.util.Collection;

public interface ConventionTypeElement extends ExtendedTypeElement {

    Collection<Property> properties();

    boolean isTopLevel();

    boolean isEntity();

    boolean isPartialEntity();

    boolean isEntityExtension();

    boolean isPropertyContainer();

    boolean isConvention();

    Optional<ConventionTypeElement> upstreamEntity();

    StreamEx<ConventionTypeElement> conventionInterfaces();

    @Override
    default <R, P> R accept(final ExtendedElementVisitor<R, P> visitor, final P parameter) {
        if (visitor instanceof ConventionTypeElementVisitor) {
            return ((ConventionTypeElementVisitor<R, P>) visitor).visitConventionType(this, parameter);
        }
        return visitor.visitType(this, parameter);
    }
}
