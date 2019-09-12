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

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.squareup.javapoet.FieldSpec;
import org.rookit.auto.javax.naming.Identifier;
import org.rookit.auto.source.identifier.IdentifierFieldAggregator;
import org.rookit.auto.source.identifier.IdentifierFieldAggregatorFactory;
import org.rookit.auto.javapoet.field.FieldSpecFactory;
import org.rookit.utils.guice.Separator;

final class SubModuleFieldAggregatorFactory implements IdentifierFieldAggregatorFactory<FieldSpec> {

    private final FieldSpecFactory fieldSpecFactory;
    private final String separator;

    @Inject
    private SubModuleFieldAggregatorFactory(final FieldSpecFactory fieldSpecFactory,
                                            @Separator final String separator) {
        this.fieldSpecFactory = fieldSpecFactory;
        this.separator = separator;
    }

    @Override
    public IdentifierFieldAggregator<FieldSpec> create() {
        return new SubModuleFieldAggregator(this, this.fieldSpecFactory, ImmutableSet.of(), this.separator);
    }

    @Override
    public IdentifierFieldAggregator<FieldSpec> create(final Identifier identifier) {
        return new SubModuleFieldAggregator(this, this.fieldSpecFactory, ImmutableSet.of(identifier), this.separator);
    }

    @Override
    public String toString() {
        return "SubModuleFieldAggregatorFactory{" +
                "fieldSpecFactory=" + this.fieldSpecFactory +
                ", separator='" + this.separator + '\'' +
                "}";
    }
}
