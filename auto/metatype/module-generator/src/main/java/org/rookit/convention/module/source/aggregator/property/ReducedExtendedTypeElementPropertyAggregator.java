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

import com.google.common.collect.ImmutableSet;
import com.squareup.javapoet.MethodSpec;
import org.rookit.auto.javax.ExtendedElement;
import org.rookit.auto.source.spec.ExtendedElementAggregator;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

final class ReducedExtendedTypeElementPropertyAggregator
        implements ExtendedElementAggregator<Collection<MethodSpec>> {

    private final ExtendedElementAggregator<Collection<MethodSpec>> left;
    private final ExtendedElementAggregator<Collection<MethodSpec>> right;
    private final Messager messager;

    ReducedExtendedTypeElementPropertyAggregator(final ExtendedElementAggregator<Collection<MethodSpec>> left,
                                                 final ExtendedElementAggregator<Collection<MethodSpec>> right,
                                                 final Messager messager) {
        this.left = left;
        this.right = right;
        this.messager = messager;
    }

    @Override
    public CompletableFuture<Void> writeTo(final Filer filer) {
        return CompletableFuture.allOf(this.left.writeTo(filer), this.right.writeTo(filer));
    }

    @Override
    public boolean accept(final ExtendedElement item) {
        this.messager.printMessage(Diagnostic.Kind.WARNING, "This aggregator is already in reduction stage. " +
                "It can no longer accept more items.");
        return false;
    }

    @Override
    public ExtendedElementAggregator<Collection<MethodSpec>> reduce(
            final ExtendedElementAggregator<Collection<MethodSpec>> aggregator) {
        return new ReducedExtendedTypeElementPropertyAggregator(this, aggregator, this.messager);
    }

    @Override
    public Collection<MethodSpec> result() {
        return ImmutableSet.<MethodSpec>builder()
                .addAll(this.left.result())
                .addAll(this.right.result())
                .build();
    }

    @Override
    public String toString() {
        return "ReducedExtendedTypeElementPropertyAggregator{" +
                "left=" + this.left +
                ", right=" + this.right +
                ", messager=" + this.messager +
                "}";
    }
}
