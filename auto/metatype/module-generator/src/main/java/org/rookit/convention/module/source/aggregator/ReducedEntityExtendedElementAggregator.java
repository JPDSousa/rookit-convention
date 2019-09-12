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
package org.rookit.convention.module.source.aggregator;

import org.rookit.auto.source.CodeSource;
import org.rookit.auto.source.spec.ExtendedElementAggregator;

import javax.annotation.processing.Messager;

final class ReducedEntityExtendedElementAggregator extends AbstractReducedExtendedElementAggregator<CodeSource> {

    private final Messager messager;

    ReducedEntityExtendedElementAggregator(final ExtendedElementAggregator<CodeSource> left,
                                           final ExtendedElementAggregator<CodeSource> right,
                                           final Messager messager) {
        super(left, right);
        this.messager = messager;
    }

    @Override
    public ExtendedElementAggregator<CodeSource> reduce(final ExtendedElementAggregator<CodeSource> aggregator) {
        return new ReducedEntityExtendedElementAggregator(this, aggregator, this.messager);
    }

    @Override
    public CodeSource result() {
        final CodeSource left = left().result();
        final CodeSource right = right().result();

        // TODO avoid direct initializations
        return new MultiEntity(left, right);
    }

    @Override
    public String toString() {
        return "ReducedEntityExtendedElementAggregator{" +
                "messager=" + this.messager +
                "} " + super.toString();
    }
}
