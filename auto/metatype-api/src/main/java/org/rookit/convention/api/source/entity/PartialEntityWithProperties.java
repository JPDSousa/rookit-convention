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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.rookit.auto.entity.Entity;
import org.rookit.auto.entity.PartialEntity;
import org.rookit.auto.identifier.Identifier;
import org.rookit.utils.optional.Optional;

import javax.annotation.processing.Filer;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

class PartialEntityWithProperties implements PartialEntity {

    private final PartialEntity delegate;
    private final Collection<Entity> properties;

    PartialEntityWithProperties(final PartialEntity delegate, final Collection<Entity> properties) {
        this.delegate = delegate;
        this.properties = ImmutableList.copyOf(properties);
    }

    @Override
    public Optional<Identifier> genericIdentifier() {
        return this.delegate.genericIdentifier();
    }

    @Override
    public Collection<PartialEntity> parents() {
        return this.delegate.parents();
    }

    @Override
    public CompletableFuture<Void> writeTo(final Filer filer) throws IOException {
        final Collection<CompletableFuture<Void>> ops = Lists.newArrayListWithCapacity(this.properties.size());
        ops.add(this.delegate.writeTo(filer));

        for (final Entity property : this.properties) {
            ops.add(property.writeTo(filer));
        }

        //noinspection ZeroLengthArrayAllocation
        return CompletableFuture.allOf(ops.toArray(new CompletableFuture[0]));
    }

    @Override
    public String toString() {
        return "PartialEntityWithProperties{" +
                "delegate=" + this.delegate +
                ", properties=" + this.properties +
                "}";
    }
}
