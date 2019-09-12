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
package org.rookit.convention.module.source.method;

import com.squareup.javapoet.MethodSpec;
import org.rookit.auto.javax.ExtendedElement;
import org.rookit.auto.source.spec.ExtendedElementAggregator;
import org.rookit.auto.source.spec.ExtendedElementSpecAggregatorFactory;
import org.rookit.utils.primitive.VoidUtils;

import javax.annotation.processing.Filer;
import java.util.concurrent.CompletableFuture;

final class ReducedConfigureMethodAggregator implements ExtendedElementAggregator<MethodSpec> {

    private final ExtendedElementSpecAggregatorFactory<MethodSpec> factory;
    private final ExtendedElementAggregator<MethodSpec> delegate;
    private final MethodSpec left;
    private final MethodSpec right;
    private final VoidUtils voidUtils;

    ReducedConfigureMethodAggregator(final ExtendedElementSpecAggregatorFactory<MethodSpec> factory,
                                     final MethodSpec left,
                                     final MethodSpec right,
                                     final VoidUtils voidUtils) {
        this.factory = factory;
        this.delegate = factory.create();
        this.left = left;
        this.right = right;
        this.voidUtils = voidUtils;
    }

    @Override
    public boolean accept(final ExtendedElement item) {
        return this.delegate.accept(item);
    }

    @Override
    public ExtendedElementAggregator<MethodSpec> reduce(
            final ExtendedElementAggregator<MethodSpec> aggregator) {
        return new ReducedConfigureMethodAggregator(this.factory, result(), aggregator.result(), this.voidUtils);
    }

    @Override
    public MethodSpec result() {
        // TODO we can do better...
        // TODO for real: this implementation >assumes< that the method signature is the same in each
        // TODO reducted aggregators
        return this.delegate.result().toBuilder()
                .addCode(this.left.code)
                .addCode(this.right.code)
                .build();
    }

    @Override
    public CompletableFuture<Void> writeTo(final Filer filer) {
        return this.voidUtils.completeVoid();
    }

    @Override
    public String toString() {
        return "ReducedConfigureMethodAggregator{" +
                "factory=" + this.factory +
                ", delegate=" + this.delegate +
                ", left=" + this.left +
                ", right=" + this.right +
                ", voidUtils=" + this.voidUtils +
                "}";
    }
}
