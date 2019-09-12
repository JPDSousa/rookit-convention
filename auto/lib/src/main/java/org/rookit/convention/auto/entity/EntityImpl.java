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
package org.rookit.convention.auto.entity;

import org.rookit.auto.javax.naming.Identifier;
import org.rookit.auto.source.CodeSource;
import org.rookit.auto.source.type.TypeSource;

import javax.annotation.processing.Filer;
import java.util.concurrent.CompletableFuture;

final class EntityImpl extends AbstractEntity {

    private final Identifier identifier;
    private final TypeSource source;

    EntityImpl(final CodeSource genericReference,
               final Identifier identifier,
               final TypeSource source) {
        super(genericReference);
        this.identifier = identifier;
        this.source = source;
    }

    @Override
    protected CompletableFuture<Void> writeEntityTo(final Filer filer) {
        return this.source.writeTo(filer);
    }

    @Override
    public String toString() {
        return "EntityImpl{" +
                "identifier=" + this.identifier +
                ", source=" + this.source +
                "} " + super.toString();
    }
}
