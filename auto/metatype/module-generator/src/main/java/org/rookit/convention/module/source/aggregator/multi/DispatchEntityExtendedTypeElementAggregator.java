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

import com.google.common.collect.Maps;
import one.util.streamex.StreamEx;
import org.rookit.auto.javax.ExtendedElement;
import org.rookit.auto.javax.pack.ExtendedPackageElement;
import org.rookit.auto.javax.pack.PackageReferenceWalker;
import org.rookit.auto.javax.pack.RootPackageReferenceWalker;
import org.rookit.auto.source.CodeSource;
import org.rookit.auto.source.CodeSourceContainerFactory;
import org.rookit.auto.source.spec.ExtendedElementAggregator;
import org.rookit.convention.auto.module.ModuleEntityTypeElementAggregatorFactory;

import javax.annotation.processing.Filer;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

final class DispatchEntityExtendedTypeElementAggregator implements ExtendedElementAggregator<Collection<CodeSource>> {

    private final RootPackageReferenceWalker walker;
    private final Map<ExtendedPackageElement, ExtendedElementAggregator<Collection<CodeSource>>> subModules;
    private final CodeSourceContainerFactory containerFactory;
    private final ModuleEntityTypeElementAggregatorFactory aggregatorFactory;

    DispatchEntityExtendedTypeElementAggregator(final RootPackageReferenceWalker walker,
                                                final CodeSourceContainerFactory containerFactory,
                                                final ModuleEntityTypeElementAggregatorFactory aggregatorFactory) {
        this.walker = walker;
        this.containerFactory = containerFactory;
        this.aggregatorFactory = aggregatorFactory;
        this.subModules = Maps.newHashMap();
    }

    @Override
    public CompletableFuture<Void> writeTo(final Filer filer) {
        return this.containerFactory
                .create(this.subModules.values())
                .writeTo(filer);
    }

    @Override
    public boolean accept(final ExtendedElement item) {
        return this.walker.nextStepFrom(item.packageInfo())
                .map(this::aggregatorFor)
                .mapToBoolean(aggregator -> aggregator.accept(item))
                // the next step is impossible
                .orElse(false);
    }

    private ExtendedElementAggregator<Collection<CodeSource>> aggregatorFor(final PackageReferenceWalker step) {
        return this.subModules.computeIfAbsent(step.materialize(), key -> createAggregator(step));
    }

    private ExtendedElementAggregator<Collection<CodeSource>> createAggregator(final PackageReferenceWalker step) {
        // TODO avoid direct initialization
        return new MultiEntityModuleExtendedTypeElementAggregator(step, this.containerFactory,
                this.aggregatorFactory);
    }

    @Override
    public ExtendedElementAggregator<Collection<CodeSource>> reduce(
            final ExtendedElementAggregator<Collection<CodeSource>> aggregator) {
        // TODO avoid direct initialization
        return new ReducedMultiEntityExtendedElementAggregator(this, aggregator);
    }

    @Override
    public Collection<CodeSource> result() {
        return StreamEx.of(this.containerFactory.create(this.subModules.values()).stream())
                .map(ExtendedElementAggregator<Collection<CodeSource>>::result)
                .flatMap(Collection::stream)
                .toImmutableSet();
    }

    @Override
    public String toString() {
        return "DispatchEntityExtendedTypeElementAggregator{" +
                "walker=" + this.walker +
                ", subModules=" + this.subModules +
                ", containerFactory=" + this.containerFactory +
                ", aggregatorFactory=" + this.aggregatorFactory +
                "}";
    }
}
