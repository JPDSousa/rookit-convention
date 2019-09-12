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

import com.google.inject.Inject;
import com.squareup.javapoet.MethodSpec;
import org.rookit.auto.javax.guice.QualifiedName;
import org.rookit.auto.javax.visitor.ExtendedElementVisitor;
import org.rookit.auto.source.spec.ExtendedElementAggregator;
import org.rookit.convention.auto.module.ModuleExtendedPropertyAggregatorFactory;
import org.rookit.convention.auto.module.ModuleExtendedPropertyMethodAggregatorFactory;
import org.rookit.utils.primitive.VoidUtils;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Name;
import java.util.Collection;

final class ModuleExtendedPropertyAggregatorFactoryImpl implements ModuleExtendedPropertyAggregatorFactory<MethodSpec> {

    private final ModuleExtendedPropertyMethodAggregatorFactory aggregatorFactory;
    private final VoidUtils voidUtils;
    private final Messager messager;
    private final ExtendedElementVisitor<Name, Void> qualifiedNameVisitor;

    @Inject
    private ModuleExtendedPropertyAggregatorFactoryImpl(
            final ModuleExtendedPropertyMethodAggregatorFactory aggregatorFactory,
            final VoidUtils voidUtils,
            final Messager messager,
            @QualifiedName final ExtendedElementVisitor<Name, Void> qualifiedNameVisitor) {
        this.aggregatorFactory = aggregatorFactory;
        this.voidUtils = voidUtils;
        this.messager = messager;
        this.qualifiedNameVisitor = qualifiedNameVisitor;
    }

    @Override
    public ExtendedElementAggregator<Collection<MethodSpec>> create() {
        return new ExtendedTypeElementPropertyAggregator(
                this.aggregatorFactory,
                this.voidUtils,
                this.messager,
                this.qualifiedNameVisitor
        );
    }

    @Override
    public String toString() {
        return "ModuleExtendedPropertyAggregatorFactoryImpl{" +
                "aggregatorFactory=" + this.aggregatorFactory +
                ", voidUtils=" + this.voidUtils +
                ", messager=" + this.messager +
                ", qualifiedNameVisitor=" + this.qualifiedNameVisitor +
                "}";
    }
}
