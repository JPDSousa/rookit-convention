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
package org.rookit.convention.api.source.entity;

import org.rookit.auto.source.CodeSource;
import org.rookit.auto.source.CodeSourceContainer;

import javax.annotation.processing.Filer;
import java.util.concurrent.CompletableFuture;

class PartialEntityWithProperties implements CodeSource {

    private final CodeSource delegate;
    private final CodeSourceContainer<CodeSource> properties;

    PartialEntityWithProperties(final CodeSource delegate, final CodeSourceContainer<CodeSource> properties) {
        this.delegate = delegate;
        this.properties = properties;
    }

    @Override
    public CompletableFuture<Void> writeTo(final Filer filer) {
        return CompletableFuture.allOf(this.delegate.writeTo(filer), this.properties.writeTo(filer));
    }

    @Override
    public String toString() {
        return "PartialEntityWithProperties{" +
                "delegate=" + this.delegate +
                ", properties=" + this.properties +
                "}";
    }
}
