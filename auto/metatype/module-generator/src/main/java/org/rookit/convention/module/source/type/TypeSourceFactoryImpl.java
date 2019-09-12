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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.rookit.auto.javax.naming.Identifier;
import org.rookit.auto.javapoet.type.JavaPoetTypeSource;
import org.rookit.auto.javapoet.type.JavaPoetTypeSourceFactory;
import org.rookit.convention.auto.config.MetatypeModuleConfig;
import org.rookit.utils.guice.Proxied;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;
import java.util.Collection;
import java.util.concurrent.Executor;

import static java.lang.String.format;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

final class TypeSourceFactoryImpl implements JavaPoetTypeSourceFactory {

    // TODO copied from ModuleTypeSourceFactoryImpl
    private static final String WARN_DELEGATE = "No specification for module %s on generator '%s'. " +
            "Falling back to default implementation";

    private final JavaPoetTypeSourceFactory delegate;
    private final Executor executor;
    private final Collection<MethodSpec> methods;
    private final Messager messager;
    private final MetatypeModuleConfig config;

    @Inject
    private TypeSourceFactoryImpl(@Proxied final JavaPoetTypeSourceFactory delegate,
                                  final Executor executor,
                                  final MetatypeModuleConfig config,
                                  final Messager messager) {
        this.delegate = delegate;
        this.executor = executor;

        this.methods = ImmutableSet.of(
                MethodSpec.constructorBuilder()
                        .addModifiers(PRIVATE)
                        .build(),
                MethodSpec.methodBuilder(config.singletonMethodName())
                        .addModifiers(PUBLIC, STATIC)
                        .returns(Module.class)
                        // TODO make available through configuration
                        .addStatement("return MODULE")
                        .build()
        );
        this.messager = messager;
        this.config = config;
    }

    @Override
    public JavaPoetTypeSource createClass(final Identifier identifier) {
        return new TypeSourceImpl(this.executor, identifier, this.methods, ImmutableList.of());
    }

    @Override
    public JavaPoetTypeSource createInterface(final Identifier identifier) {
        this.messager.printMessage(Diagnostic.Kind.NOTE, format(WARN_DELEGATE, "interfaces", this.config.name()));
        return this.delegate.createInterface(identifier);
    }

    @Override
    public JavaPoetTypeSource createAnnotation(final Identifier identifier) {
        this.messager.printMessage(Diagnostic.Kind.NOTE, format(WARN_DELEGATE, "annotations", this.config.name()));
        return this.delegate.createAnnotation(identifier);
    }

    @Override
    public JavaPoetTypeSource fromTypeSpec(final Identifier identifier, final TypeSpec source) {
        return this.delegate.fromTypeSpec(identifier, source);
    }

    @Override
    public String toString() {
        return "TypeSourceFactoryImpl{" +
                "delegate=" + this.delegate +
                ", executor=" + this.executor +
                ", methods=" + this.methods +
                ", messager=" + this.messager +
                ", config=" + this.config +
                "}";
    }
}
