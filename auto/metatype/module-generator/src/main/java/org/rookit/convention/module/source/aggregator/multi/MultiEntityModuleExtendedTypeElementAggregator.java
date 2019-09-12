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
package org.rookit.convention.module.source.aggregator.multi;

import com.google.common.collect.ImmutableSet;
import org.rookit.auto.javax.ExtendedElement;
import org.rookit.auto.javax.pack.ExtendedPackageElement;
import org.rookit.auto.javax.pack.PackageReferenceWalker;
import org.rookit.auto.source.CodeSource;
import org.rookit.auto.source.CodeSourceContainerFactory;
import org.rookit.auto.source.spec.ExtendedElementAggregator;
import org.rookit.convention.auto.module.ModuleEntityTypeElementAggregatorFactory;

import javax.annotation.processing.Filer;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

final class MultiEntityModuleExtendedTypeElementAggregator
        implements ExtendedElementAggregator<Collection<CodeSource>> {

    private final ExtendedPackageElement packageId;
    private final ExtendedElementAggregator<CodeSource> moduleAggregator;
    private final ExtendedElementAggregator<Collection<CodeSource>> delegate;

    MultiEntityModuleExtendedTypeElementAggregator(final PackageReferenceWalker step,
                                                   final CodeSourceContainerFactory containerFactory,
                                                   final ModuleEntityTypeElementAggregatorFactory aggregatorFactory) {
        this.packageId = step.materialize();
        this.moduleAggregator = aggregatorFactory.create(this.packageId);
        // TODO avoid explicit initializations
        //only subpackages are to be processed by this aggregator
        this.delegate = new DispatchEntityExtendedTypeElementAggregator(step, containerFactory, aggregatorFactory);
    }

    @Override
    public CompletableFuture<Void> writeTo(final Filer filer) {
        return CompletableFuture.allOf(this.delegate.writeTo(filer), this.moduleAggregator.writeTo(filer));
    }

    @Override
    public boolean accept(final ExtendedElement item) {
        final boolean acceptedByModule = this.moduleAggregator.accept(item);
        final boolean acceptedByDelegate = this.delegate.accept(item);

        return acceptedByDelegate || acceptedByModule;
    }

    @Override
    public ExtendedElementAggregator<Collection<CodeSource>> reduce(
            final ExtendedElementAggregator<Collection<CodeSource>> aggregator) {
        // TODO not really confident about this
        return this.delegate.reduce(aggregator);
    }

    @Override
    public Collection<CodeSource> result() {
        return ImmutableSet.<CodeSource>builder()
                .addAll(this.delegate.result())
                .add(this.moduleAggregator.result())
                .build();
    }

    @Override
    public String toString() {
        return "MultiEntityModuleExtendedTypeElementAggregator{" +
                "packageId=" + this.packageId +
                ", moduleAggregator=" + this.moduleAggregator +
                ", delegate=" + this.delegate +
                "}";
    }
}
