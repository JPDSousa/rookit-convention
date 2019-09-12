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
import org.rookit.auto.source.CodeSource;
import org.rookit.auto.source.spec.ExtendedElementAggregator;
import org.rookit.convention.module.source.aggregator.AbstractReducedExtendedElementAggregator;

import java.util.Collection;

final class ReducedMultiEntityExtendedElementAggregator
        extends AbstractReducedExtendedElementAggregator<Collection<CodeSource>> {

    ReducedMultiEntityExtendedElementAggregator(final ExtendedElementAggregator<Collection<CodeSource>> left,
                                                final ExtendedElementAggregator<Collection<CodeSource>> right) {
        super(left, right);
    }

    @Override
    public ExtendedElementAggregator<Collection<CodeSource>> reduce(
            final ExtendedElementAggregator<Collection<CodeSource>> aggregator) {
        return new ReducedMultiEntityExtendedElementAggregator(this, aggregator);
    }

    @Override
    public Collection<CodeSource> result() {
        return ImmutableSet.<CodeSource>builder()
                .addAll(left().result())
                .addAll(right().result())
                .build();
    }

}
