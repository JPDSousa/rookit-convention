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
import org.rookit.auto.javax.pack.ExtendedPackageElement;
import org.rookit.auto.source.CodeSource;
import org.rookit.auto.source.CodeSourceContainerFactory;
import org.rookit.auto.source.spec.ExtendedElementAggregator;
import org.rookit.convention.auto.module.ModuleEntityTypeElementAggregatorFactory;

import javax.annotation.processing.Filer;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

abstract class AbstractMapEntityExtendedTypeElementAggregator
        implements ExtendedElementAggregator<Collection<CodeSource>> {

    private final Map<ExtendedPackageElement, ExtendedElementAggregator<Collection<CodeSource>>> subModules;
    private final CodeSourceContainerFactory containerFactory;
    private final ModuleEntityTypeElementAggregatorFactory aggregatorFactory;

    AbstractMapEntityExtendedTypeElementAggregator(final CodeSourceContainerFactory containerFactory,
                                                   final ModuleEntityTypeElementAggregatorFactory aggregatorFactory) {
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
}
