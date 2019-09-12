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
package org.rookit.convention.module.source.type;

import com.google.inject.Inject;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import org.rookit.auto.javax.naming.Identifier;
import org.rookit.auto.source.identifier.IdentifierFieldAggregator;
import org.rookit.auto.source.identifier.IdentifierFieldAggregatorFactory;
import org.rookit.auto.source.spec.ExtendedElementAggregator;
import org.rookit.auto.source.spec.ExtendedElementSpecAggregatorFactory;
import org.rookit.auto.source.type.MutableTypeSource;
import org.rookit.auto.source.type.MutableTypeSourceFactory;
import org.rookit.convention.auto.config.MetatypeModuleConfig;
import org.rookit.convention.auto.module.ModuleExtendedPropertyAggregatorFactory;
import org.rookit.convention.auto.module.ModuleTypeSource;
import org.rookit.convention.auto.module.ModuleTypeSourceFactory;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;
import java.util.Collection;

import static java.lang.String.format;

final class ModuleTypeSourceFactoryImpl implements ModuleTypeSourceFactory<MethodSpec, FieldSpec> {

    private static final String WARN_DELEGATE = "No specification for module %s on generator '%s'. " +
            "Falling back to default implementation";

    private final MutableTypeSourceFactory<MethodSpec, FieldSpec> delegate;
    private final MetatypeModuleConfig config;
    private final Messager messager;
    private final ExtendedElementSpecAggregatorFactory<MethodSpec> methodAggregatorFactory;
    private final IdentifierFieldAggregatorFactory<FieldSpec> moduleAggregatorFactory;
    private final ModuleExtendedPropertyAggregatorFactory<MethodSpec> propertyAggregatorFactory;

    @Inject
    private ModuleTypeSourceFactoryImpl(final MetatypeModuleConfig config,
                                        final MutableTypeSourceFactory<MethodSpec, FieldSpec> delegate,
                                        final Messager messager,
                                        final ExtendedElementSpecAggregatorFactory<MethodSpec> methodFactory,
                                        final IdentifierFieldAggregatorFactory<FieldSpec> moduleFactory,
                                        final ModuleExtendedPropertyAggregatorFactory<MethodSpec> propAggFactory) {
        this.config = config;
        this.delegate = delegate;
        this.messager = messager;
        this.methodAggregatorFactory = methodFactory;
        this.moduleAggregatorFactory = moduleFactory;
        this.propertyAggregatorFactory = propAggFactory;
    }

    @Override
    public ModuleTypeSource<MethodSpec, FieldSpec> createClass(final Identifier identifier) {
        final MutableTypeSource<MethodSpec, FieldSpec> delegate = this.delegate.createClass(identifier);
        final ExtendedElementAggregator<MethodSpec> methodAggregator = this.methodAggregatorFactory.create();
        final IdentifierFieldAggregator<FieldSpec> moduleAggregator = this.moduleAggregatorFactory.create(identifier);
        final ExtendedElementAggregator<Collection<MethodSpec>> propertyAggregator
                = this.propertyAggregatorFactory.create();
        return new ModuleTypeSourceImpl<>(delegate, methodAggregator, moduleAggregator, propertyAggregator);
    }

    @Override
    public MutableTypeSource<MethodSpec, FieldSpec> createInterface(final Identifier identifier) {
        this.messager.printMessage(Diagnostic.Kind.NOTE, format(WARN_DELEGATE, "interfaces", this.config.name()));
        return this.delegate.createInterface(identifier);
    }

    @Override
    public MutableTypeSource<MethodSpec, FieldSpec> createAnnotation(final Identifier identifier) {
        this.messager.printMessage(Diagnostic.Kind.NOTE, format(WARN_DELEGATE, "annotations", this.config.name()));
        return this.delegate.createAnnotation(identifier);
    }

    @Override
    public String toString() {
        return "ModuleTypeSourceFactoryImpl{" +
                "delegate=" + this.delegate +
                ", config=" + this.config +
                ", messager=" + this.messager +
                ", methodAggregatorFactory=" + this.methodAggregatorFactory +
                ", moduleAggregatorFactory=" + this.moduleAggregatorFactory +
                ", propertyAggregatorFactory=" + this.propertyAggregatorFactory +
                "}";
    }
}
