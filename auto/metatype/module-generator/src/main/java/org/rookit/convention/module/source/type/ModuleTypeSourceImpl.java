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
package org.rookit.convention.module.source.type;

import org.rookit.auto.javax.ExtendedElement;
import org.rookit.auto.javax.naming.Identifier;
import org.rookit.auto.source.identifier.IdentifierFieldAggregator;
import org.rookit.auto.source.spec.ExtendedElementAggregator;
import org.rookit.auto.source.type.MutableTypeSource;
import org.rookit.convention.auto.module.ModuleTypeSource;

import javax.annotation.processing.Filer;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

final class ModuleTypeSourceImpl<M, F> implements ModuleTypeSource<M, F> {

    private boolean written;
    private final MutableTypeSource<M, F> delegate;
    private final ExtendedElementAggregator<M> configureAggregator;
    private final IdentifierFieldAggregator<F> subModules;
    private final ExtendedElementAggregator<Collection<M>> propertyAggregator;

    ModuleTypeSourceImpl(final MutableTypeSource<M, F> delegate,
                         final ExtendedElementAggregator<M> configureAggregator,
                         final IdentifierFieldAggregator<F> subModules,
                         final ExtendedElementAggregator<Collection<M>> propertyAggregator) {
        this.configureAggregator = configureAggregator;
        this.propertyAggregator = propertyAggregator;
        this.written = false;
        this.delegate = delegate;
        this.subModules = subModules;
    }

    @Override
    public void addModule(final Identifier identifier) {
        attemptToWrite();
        this.subModules.accept(identifier);
    }

    @Override
    public void addBinding(final ExtendedElement element) {
        attemptToWrite();
        if (this.configureAggregator.accept(element)) {
            this.propertyAggregator.accept(element);
        }
    }

    @Override
    public Identifier identifier() {
        return this.delegate.identifier();
    }

    @Override
    public void addMethod(final M method) {
        attemptToWrite();
        this.delegate.addMethod(method);
    }

    @Override
    public void addField(final F field) {
        attemptToWrite();
        this.delegate.addField(field);
    }

    private void attemptToWrite() {
        if (this.written) {
            throw new UnsupportedOperationException("This type source has already been written to a filer, and as such"
                    + " can no longer accept operations that modify its state.");
        }
    }

    @Override
    public CompletableFuture<Void> writeTo(final Filer filer) {
        if (!this.written) {
            this.delegate.addMethod(this.configureAggregator.result());
            this.delegate.addField(this.subModules.result());
            this.propertyAggregator.result().forEach(this.delegate::addMethod);

            this.written = true;
        }
        return this.delegate.writeTo(filer);
    }

    @Override
    public String toString() {
        return "ModuleTypeSourceImpl{" +
                "written=" + this.written +
                ", delegate=" + this.delegate +
                ", configureAggregator=" + this.configureAggregator +
                ", subModules=" + this.subModules +
                ", propertyAggregator=" + this.propertyAggregator +
                "}";
    }
}
