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
package org.rookit.convention.module.source.aggregator.property;

import com.google.common.collect.ImmutableList;
import com.squareup.javapoet.MethodSpec;
import org.rookit.convention.auto.property.ExtendedProperty;
import org.rookit.convention.auto.property.ExtendedPropertyAggregator;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;
import java.util.Collection;

final class ReducedExtendedPropertyMultiMethodAggregator implements ExtendedPropertyAggregator<Collection<MethodSpec>> {

    private final Messager messager;
    private final ExtendedPropertyAggregator<Collection<MethodSpec>> left;
    private final ExtendedPropertyAggregator<Collection<MethodSpec>> right;

    ReducedExtendedPropertyMultiMethodAggregator(final Messager messager,
                                                 final ExtendedPropertyAggregator<Collection<MethodSpec>> left,
                                                 final ExtendedPropertyAggregator<Collection<MethodSpec>> right) {
        this.messager = messager;
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean accept(final ExtendedProperty item) {
        this.messager.printMessage(Diagnostic.Kind.WARNING, "This aggregator is already in reduction stage. " +
                "It can no longer accept more items.");
        return false;
    }

    @Override
    public ExtendedPropertyAggregator<Collection<MethodSpec>> reduce(
            final ExtendedPropertyAggregator<Collection<MethodSpec>> aggregator) {
        return new ReducedExtendedPropertyMultiMethodAggregator(this.messager, this, aggregator);
    }

    @Override
    public Collection<MethodSpec> result() {
        return ImmutableList.<MethodSpec>builder()
                .addAll(this.left.result())
                .addAll(this.right.result())
                .build();
    }

    @Override
    public String toString() {
        return "ReducedExtendedPropertyMultiMethodAggregator{" +
                "messager=" + this.messager +
                ", left=" + this.left +
                ", right=" + this.right +
                "}";
    }
}
