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

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import org.rookit.auto.javax.ExtendedElement;
import org.rookit.auto.javax.naming.Identifier;
import org.rookit.auto.javax.naming.IdentifierFactory;
import org.rookit.auto.javax.pack.ExtendedPackageElement;
import org.rookit.auto.javax.pack.PackageReferenceWalker;
import org.rookit.auto.source.CodeSource;
import org.rookit.auto.source.spec.ExtendedElementAggregator;
import org.rookit.convention.auto.module.ModuleTypeSource;
import org.rookit.utils.optional.OptionalFactory;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

final class EntityExtendedTypeElementAggregator implements ExtendedElementAggregator<CodeSource> {

    private final PackageReferenceWalker step;
    private final ExtendedPackageElement packageId;
    private final IdentifierFactory identifierFactory;
    private final ModuleTypeSource<MethodSpec, FieldSpec> source;
    private final OptionalFactory optionalFactory;
    private final Identifier identifier;
    private final Messager messager;

    // TODO too much dependencies :(
    EntityExtendedTypeElementAggregator(final PackageReferenceWalker step,
                                        final ExtendedPackageElement packageId,
                                        final IdentifierFactory identifierFactory,
                                        final ModuleTypeSource<MethodSpec, FieldSpec> source,
                                        final OptionalFactory optionalFactory,
                                        final Identifier identifier,
                                        final Messager messager) {
        this.step = step;
        this.packageId = packageId;
        this.identifierFactory = identifierFactory;
        this.source = source;
        this.identifier = identifier;
        this.optionalFactory = optionalFactory;
        this.messager = messager;
    }

    @Override
    public CompletableFuture<Void> writeTo(final Filer filer) {
        return this.source.writeTo(filer);
    }

    @Override
    public boolean accept(final ExtendedElement item) {
        final ExtendedPackageElement packageElement = item.packageInfo();

        if (Objects.equals(this.packageId, packageElement)) {
            this.source.addBinding(item);
            return true;
        }
        return this.step.nextStepFrom(packageElement)
                .map(PackageReferenceWalker::materialize)
                .map(this.identifierFactory::create)
                .mapToBoolean(moduleIdentifier -> {
                    this.source.addModule(moduleIdentifier);
                    return true;
                }).orElse(false);
    }

    @Override
    public ExtendedElementAggregator<CodeSource> reduce(final ExtendedElementAggregator<CodeSource> aggregator) {
        // TODO avoid direct initializations
        return new ReducedEntityExtendedElementAggregator(this, aggregator, this.messager);
    }

    @Override
    public CodeSource result() {
        return this;
    }

    @Override
    public String toString() {
        return "EntityExtendedTypeElementAggregator{" +
                "step=" + this.step +
                ", packageId=" + this.packageId +
                ", identifierFactory=" + this.identifierFactory +
                ", source=" + this.source +
                ", optionalFactory=" + this.optionalFactory +
                ", identifier=" + this.identifier +
                ", messager=" + this.messager +
                "}";
    }
}
