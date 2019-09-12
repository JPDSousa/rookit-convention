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
package org.rookit.convention.auto.entity.nopartial;

import com.google.inject.Inject;
import org.rookit.auto.javax.naming.Identifier;
import org.rookit.auto.javax.naming.IdentifierFactory;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.auto.source.CodeSource;
import org.rookit.auto.source.CodeSourceFactory;
import org.rookit.auto.source.type.SingleTypeSourceFactory;
import org.rookit.auto.source.type.TypeSource;
import org.rookit.utils.optional.OptionalFactory;

public final class NoPartialEntityFactory implements CodeSourceFactory {

    public static CodeSourceFactory create(final IdentifierFactory identifierFactory,
                                           final SingleTypeSourceFactory typeSourceFactory,
                                           final OptionalFactory optionalFactory) {
        return new NoPartialEntityFactory(identifierFactory, typeSourceFactory, optionalFactory);
    }

    private final IdentifierFactory identifierFactory;
    private final SingleTypeSourceFactory typeSourceFactory;
    private final OptionalFactory optionalFactory;

    @Inject
    private NoPartialEntityFactory(final IdentifierFactory identifierFactory,
                                   final SingleTypeSourceFactory typeSourceFactory,
                                   final OptionalFactory optionalFactory) {
        this.identifierFactory = identifierFactory;
        this.typeSourceFactory = typeSourceFactory;
        this.optionalFactory = optionalFactory;
    }

    @Override
    public CodeSource create(final ExtendedTypeElement element) {
        final Identifier identifier = this.identifierFactory.create(element);
        final TypeSource typeSource = this.typeSourceFactory.create(identifier, element);

        return new NoPartialEntity(identifier, this.optionalFactory, typeSource);
    }

    @Override
    public String toString() {
        return "NoPartialEntityFactory{" +
                "identifierFactory=" + this.identifierFactory +
                ", typeSourceFactory=" + this.typeSourceFactory +
                ", optionalFactory=" + this.optionalFactory +
                "} " + super.toString();
    }
}
