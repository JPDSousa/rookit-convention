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
import com.squareup.javapoet.ParameterSpec;
import one.util.streamex.StreamEx;
import org.rookit.convention.auto.javax.ConventionTypeElement;
import org.rookit.convention.auto.javax.naming.PropertyIdentifierFactory;
import org.rookit.auto.javapoet.naming.JavaPoetNamingFactory;
import org.rookit.auto.source.spec.parameter.Parameter;
import org.rookit.convention.auto.javax.visitor.ConventionTypeElementVisitor;
import org.rookit.convention.auto.property.ExtendedPropertyAggregator;
import org.rookit.convention.auto.property.PropertyFactory;
import org.rookit.guice.auto.annotation.Guice;
import org.rookit.convention.guice.MetaType;
import org.rookit.convention.auto.metatype.guice.MetaTypeAPI;
import org.rookit.convention.auto.module.ModuleExtendedPropertyMethodAggregatorFactory;
import org.rookit.utils.primitive.VoidUtils;

import javax.annotation.processing.Messager;
import java.util.Collection;

final class ExtendedPropertyMultiMethodAggregatorFactory implements ModuleExtendedPropertyMethodAggregatorFactory {

    private final ConventionTypeElementVisitor<StreamEx<Parameter<ParameterSpec>>, Void> parameterFactory;
    private final PropertyIdentifierFactory identifierFactory;
    private final Messager messager;
    private final JavaPoetNamingFactory apiNamingFactory;
    private final JavaPoetNamingFactory implNamingFactory;
    private final PropertyFactory propertyFactory;
    private final VoidUtils voidUtils;

    @Inject
    private ExtendedPropertyMultiMethodAggregatorFactory(
            @MetaType(includeAnnotations = true) final ConventionTypeElementVisitor<
                    StreamEx<Parameter<ParameterSpec>>, Void> parameterFactory,
            @Guice final PropertyIdentifierFactory identifierFactory,
            @MetaTypeAPI final JavaPoetNamingFactory apiNamingFactory,
            final Messager messager,
            @MetaType final JavaPoetNamingFactory implNamingFactory,
            final PropertyFactory propertyFactory,
            final VoidUtils voidUtils) {
        this.parameterFactory = parameterFactory;
        this.identifierFactory = identifierFactory;
        this.messager = messager;
        this.apiNamingFactory = apiNamingFactory;
        this.implNamingFactory = implNamingFactory;
        this.propertyFactory = propertyFactory;
        this.voidUtils = voidUtils;
    }

    @Override
    public ExtendedPropertyAggregator<Collection<MethodSpec>> create(final ConventionTypeElement element) {
        return new ExtendedPropertyMultiMethodAggregator(
                this.parameterFactory,
                element,
                element.properties(),
                this.propertyFactory,
                this.identifierFactory,
                this.messager,
                this.apiNamingFactory,
                this.implNamingFactory,
                this.voidUtils);
    }

    @Override
    public String toString() {
        return "ExtendedPropertyMultiMethodAggregatorFactory{" +
                "parameterFactory=" + this.parameterFactory +
                ", identifierFactory=" + this.identifierFactory +
                ", messager=" + this.messager +
                ", apiNamingFactory=" + this.apiNamingFactory +
                ", implNamingFactory=" + this.implNamingFactory +
                ", propertyFactory=" + this.propertyFactory +
                "}";
    }
}
