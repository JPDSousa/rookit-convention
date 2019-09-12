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

import org.rookit.auto.javax.naming.Identifier;
import org.rookit.auto.source.CodeSource;
import org.rookit.auto.source.CodeSourceContainer;
import org.rookit.auto.source.type.TypeSource;

import javax.annotation.processing.Filer;
import java.util.concurrent.CompletableFuture;

final class PropertyFlatEntity implements CodeSource {

    private final Identifier identifier;
    private final TypeSource source;
    private final CodeSourceContainer<CodeSource> childEntities;
    private final CodeSource codeSource;

    PropertyFlatEntity(final Identifier identifier,
                       final CodeSourceContainer<CodeSource> childEntities,
                       final TypeSource source,
                       final CodeSource codeSource) {
        this.identifier = identifier;
        this.source = source;
        this.childEntities = childEntities;
        this.codeSource = codeSource;
    }

    @Override
    public CompletableFuture<Void> writeTo(final Filer filer) {
        return CompletableFuture.allOf(
                this.codeSource.writeTo(filer),
                this.source.writeTo(filer),
                this.childEntities.writeTo(filer)
        );
    }

    @Override
    public String toString() {
        return "PropertyFlatEntity{" +
                "identifier=" + this.identifier +
                ", source=" + this.source +
                ", childEntities=" + this.childEntities +
                ", partialEntity=" + this.codeSource +
                "}";
    }
}
