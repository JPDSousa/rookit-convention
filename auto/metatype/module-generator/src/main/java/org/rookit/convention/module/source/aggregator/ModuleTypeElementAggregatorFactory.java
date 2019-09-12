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

import com.google.inject.Inject;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import org.rookit.auto.javax.naming.Identifier;
import org.rookit.auto.javax.naming.IdentifierFactory;
import org.rookit.auto.javax.pack.ExtendedPackageElement;
import org.rookit.auto.javax.pack.PackageReferenceWalker;
import org.rookit.auto.javax.pack.PackageReferenceWalkerFactory;
import org.rookit.auto.source.CodeSource;
import org.rookit.auto.source.spec.ExtendedElementAggregator;
import org.rookit.convention.auto.module.ModuleEntityTypeElementAggregatorFactory;
import org.rookit.convention.auto.module.ModuleTypeSource;
import org.rookit.convention.auto.module.ModuleTypeSourceFactory;
import org.rookit.utils.optional.OptionalFactory;

import javax.annotation.processing.Messager;

final class ModuleTypeElementAggregatorFactory implements ModuleEntityTypeElementAggregatorFactory {

    private final ModuleTypeSourceFactory<MethodSpec, FieldSpec> sourceFactory;
    private final OptionalFactory optionalFactory;
    private final IdentifierFactory identifierFactory;
    private final Messager messager;
    private final PackageReferenceWalkerFactory walkerFactory;

    @Inject
    private ModuleTypeElementAggregatorFactory(final ModuleTypeSourceFactory<MethodSpec, FieldSpec> sourceFactory,
                                               final OptionalFactory optionalFactory,
                                               final IdentifierFactory identifierFactory,
                                               final Messager messager,
                                               final PackageReferenceWalkerFactory walkerFactory) {
        this.sourceFactory = sourceFactory;
        this.optionalFactory = optionalFactory;
        this.identifierFactory = identifierFactory;
        this.messager = messager;
        this.walkerFactory = walkerFactory;
    }

    @Override
    public ExtendedElementAggregator<CodeSource> create(final ExtendedPackageElement packageElement) {
        final Identifier identifier = this.identifierFactory.create(packageElement);
        final ModuleTypeSource<MethodSpec, FieldSpec> typeSource = this.sourceFactory.createClass(identifier);
        final PackageReferenceWalker step = this.walkerFactory.create(packageElement);
        return new EntityExtendedTypeElementAggregator(step, packageElement, this.identifierFactory,
                typeSource, this.optionalFactory, identifier, this.messager);
    }

    @Override
    public String toString() {
        return "ModuleTypeElementAggregatorFactory{" +
                "sourceFactory=" + this.sourceFactory +
                ", optionalFactory=" + this.optionalFactory +
                ", identifierFactory=" + this.identifierFactory +
                ", messager=" + this.messager +
                ", walkerFactory=" + this.walkerFactory +
                "}";
    }
}
