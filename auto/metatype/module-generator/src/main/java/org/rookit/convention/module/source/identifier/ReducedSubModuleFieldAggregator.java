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
package org.rookit.convention.module.source.identifier;

import com.squareup.javapoet.FieldSpec;
import org.rookit.auto.javapoet.field.FieldSpecFactory;
import org.rookit.auto.javax.naming.Identifier;
import org.rookit.auto.source.identifier.IdentifierFieldAggregator;
import org.rookit.auto.source.identifier.IdentifierFieldAggregatorFactory;

final class ReducedSubModuleFieldAggregator implements IdentifierFieldAggregator<FieldSpec> {

    private final IdentifierFieldAggregatorFactory<FieldSpec> factory;
    private final FieldSpecFactory fieldSpecFactory;
    private final IdentifierFieldAggregator<FieldSpec> delegate;
    private final IdentifierFieldAggregator<FieldSpec> left;
    private final IdentifierFieldAggregator<FieldSpec> right;

    ReducedSubModuleFieldAggregator(final IdentifierFieldAggregatorFactory<FieldSpec> factory,
                                    final FieldSpecFactory fieldSpecFactory,
                                    final IdentifierFieldAggregator<FieldSpec> left,
                                    final IdentifierFieldAggregator<FieldSpec> right) {
        this.factory = factory;
        this.fieldSpecFactory = fieldSpecFactory;
        this.left = left;
        this.right = right;
        this.delegate = factory.create();
    }

    @Override
    public boolean accept(final Identifier item) {
        return this.delegate.accept(item);
    }

    @Override
    public IdentifierFieldAggregator<FieldSpec> reduce(final IdentifierFieldAggregator<FieldSpec> aggregator) {
        return new ReducedSubModuleFieldAggregator(this.factory, this.fieldSpecFactory, this, aggregator);
    }

    @Override
    public FieldSpec result() {
        final FieldSpec left = this.left.result();
        final FieldSpec right = this.right.result();
        final FieldSpec delegate = this.delegate.result();

        return this.fieldSpecFactory.create()
                .toBuilder()
                .initializer("$T.combine(\n$L, \n$L, \n$L)",
                        delegate.initializer, left.initializer, right.initializer)
                .build();
    }

    @Override
    public String toString() {
        return "ReducedSubModuleFieldAggregator{" +
                "factory=" + this.factory +
                ", fieldSpecFactory=" + this.fieldSpecFactory +
                ", delegate=" + this.delegate +
                ", left=" + this.left +
                ", right=" + this.right +
                "}";
    }
}
