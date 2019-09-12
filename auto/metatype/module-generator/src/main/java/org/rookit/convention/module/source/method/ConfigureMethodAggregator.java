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

import com.google.common.collect.Sets;
import com.google.inject.Singleton;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import one.util.streamex.StreamEx;
import org.rookit.auto.javapoet.method.MethodSpecFactory;
import org.rookit.auto.javax.ExtendedElement;
import org.rookit.auto.javax.naming.NamingFactory;
import org.rookit.auto.source.spec.ExtendedElementAggregator;
import org.rookit.auto.source.spec.ExtendedElementSpecAggregatorFactory;
import org.rookit.convention.auto.javax.ConventionTypeElement;
import org.rookit.utils.primitive.VoidUtils;

import javax.annotation.processing.Filer;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

final class ConfigureMethodAggregator implements ExtendedElementAggregator<MethodSpec> {

    private final Set<ExtendedElement> configureBindings;
    private final NamingFactory apiNamingFactory;
    private final NamingFactory implNamingFactory;
    private final MethodSpecFactory methodSpecFactory;
    private final ExtendedElementSpecAggregatorFactory<MethodSpec> factory;
    private final VoidUtils voidUtils;

    ConfigureMethodAggregator(final NamingFactory apiNamingFactory,
                              final NamingFactory implNamingFactory,
                              final MethodSpecFactory methodSpecFactory,
                              final ExtendedElementSpecAggregatorFactory<MethodSpec> factory,
                              final VoidUtils voidUtils) {
        this.apiNamingFactory = apiNamingFactory;
        this.implNamingFactory = implNamingFactory;
        this.methodSpecFactory = methodSpecFactory;
        this.factory = factory;
        this.voidUtils = voidUtils;
        this.configureBindings = Sets.newHashSet();
    }

    @Override
    public boolean accept(final ExtendedElement element) {
        this.configureBindings.add(element);
        return true;
    }

    @Override
    public ExtendedElementAggregator<MethodSpec> reduce(
            final ExtendedElementAggregator<MethodSpec> aggregator) {
        return new ReducedConfigureMethodAggregator(this.factory, result(), aggregator.result(), voidUtils);
    }

    @Override
    public MethodSpec result() {
        final MethodSpec.Builder configureBuilder = this.methodSpecFactory.create().toBuilder();
        StreamEx.of(this.configureBindings)
                .select(ConventionTypeElement.class)
                .filter(ConventionTypeElement::isConvention)
                .map(this::createBinding)
                .forEach(configureBuilder::addStatement);

        return configureBuilder.build();
    }

    private CodeBlock createBinding(final ExtendedElement element) {
        final String apiName = this.apiNamingFactory.type(element);
        final String implName = this.implNamingFactory.type(element);
        final String bindPreStatement = "bind($L.class).to($L.class).in($T.class)";

        return CodeBlock.of(bindPreStatement, apiName, implName, Singleton.class);
    }

    @Override
    public CompletableFuture<Void> writeTo(final Filer filer) {
        return this.voidUtils.completeVoid();
    }

    @Override
    public String toString() {
        return "ConfigureMethodAggregator{" +
                "configureBindings=" + this.configureBindings +
                ", apiNamingFactory=" + this.apiNamingFactory +
                ", implNamingFactory=" + this.implNamingFactory +
                ", methodSpecFactory=" + this.methodSpecFactory +
                ", factory=" + this.factory +
                ", voidUtils=" + this.voidUtils +
                "}";
    }
}
