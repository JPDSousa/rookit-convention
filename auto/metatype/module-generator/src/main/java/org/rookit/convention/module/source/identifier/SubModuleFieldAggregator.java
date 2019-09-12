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

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.util.Modules;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.ArrayUtils;
import org.rookit.auto.javax.naming.Identifier;
import org.rookit.auto.source.identifier.IdentifierFieldAggregator;
import org.rookit.auto.source.identifier.IdentifierFieldAggregatorFactory;
import org.rookit.auto.javapoet.field.FieldSpecFactory;

import java.util.Arrays;
import java.util.Collection;

import static java.lang.String.format;

final class SubModuleFieldAggregator implements IdentifierFieldAggregator<FieldSpec> {

    private final IdentifierFieldAggregatorFactory<FieldSpec> factory;
    private final FieldSpecFactory fieldSpecFactory;
    private final Collection<Identifier> subModules;
    private final String separator;

    SubModuleFieldAggregator(final IdentifierFieldAggregatorFactory<FieldSpec> factory,
                             final FieldSpecFactory fieldSpecFactory,
                             final Iterable<Identifier> subModules,
                             final String separator) {
        this.factory = factory;
        this.fieldSpecFactory = fieldSpecFactory;
        // this must be a list, because we are treating the FIRST item differently
        // TODO should we change the design here to consider the first field separately?
        this.subModules = Lists.newArrayList(subModules);
        this.separator = separator;
    }

    @Override
    public boolean accept(final Identifier item) {
        // even if the module already exists, return true because it's ok
        if (!this.subModules.contains(item)) {
            this.subModules.add(item);
        }
        return true;
    }

    @Override
    public IdentifierFieldAggregator<FieldSpec> reduce(final IdentifierFieldAggregator<FieldSpec> aggregator) {
        return new ReducedSubModuleFieldAggregator(this.factory, this.fieldSpecFactory, this, aggregator);
    }

    @Override
    public FieldSpec result() {
        return this.fieldSpecFactory.create()
                .toBuilder()
                .initializer(initializer())
                .build();
    }

    private CodeBlock initializer() {
        if (this.subModules.isEmpty()) {
            return CodeBlock.of("$T.combine()", Modules.class);
        }
        if (this.subModules.size() == 1) {
            final Identifier onlyElement = Iterables.getOnlyElement(this.subModules);
            final ClassName className = ClassName.get(onlyElement.packageElement().fullName().asString(),
                    onlyElement.name());
            return CodeBlock.of("$T.combine(new $T())", Modules.class, className);
        }

        final String initializer = format("$T.combine(new $T(),%s%s)", this.separator, combineBody());
        final Object[] args = StreamEx.of(this.subModules)
                .map(identifier -> ClassName.get(identifier.packageElement().fullName().asString(), identifier.name()))
                .toArray(ClassName[]::new);
        final Object[] modules = {ClassName.get(Modules.class)};

        return CodeBlock.of(initializer, ArrayUtils.addAll(modules, args));
    }

    private String combineBody() {
        final String[] combines = new String[this.subModules.size() - 1];
        Arrays.fill(combines, "$T.getModule()");
        return String.join("," + this.separator, combines);
    }

    @Override
    public String toString() {
        return "SubModuleFieldAggregator{" +
                "factory=" + this.factory +
                ", fieldSpecFactory=" + this.fieldSpecFactory +
                ", subModules=" + this.subModules +
                ", separator='" + this.separator + '\'' +
                "}";
    }
}
