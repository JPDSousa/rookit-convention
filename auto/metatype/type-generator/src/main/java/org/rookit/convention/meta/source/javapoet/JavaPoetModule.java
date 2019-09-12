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
package org.rookit.convention.meta.source.javapoet;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.squareup.javapoet.MethodSpec;
import one.util.streamex.StreamEx;
import org.rookit.auto.spec.AutoSpecFactories;
import org.rookit.auto.source.spec.SpecFactory;
import org.rookit.convention.auto.javax.visitor.ConventionTypeElementVisitor;
import org.rookit.convention.guice.MetaType;

import java.util.Set;

public final class JavaPoetModule extends AbstractModule {

    private static final Module MODULE = new JavaPoetModule();

    public static Module getModule() {
        return MODULE;
    }

    private JavaPoetModule() {}

    @Override
    protected void configure() {
        final Multibinder<ConventionTypeElementVisitor<StreamEx<MethodSpec>, Void>> mBinder = Multibinder
                .newSetBinder(binder(), new TypeLiteral<ConventionTypeElementVisitor<StreamEx<MethodSpec>, Void>>() {},
                        MetaType.class);
        mBinder.addBinding().toProvider(FactoryJavaPoetMethodFactoryProvider.class).in(Singleton.class);
        mBinder.addBinding().toProvider(ConstructorJavaPoetMethodFactoryProvider.class).in(Singleton.class);
        mBinder.addBinding().toProvider(ModelTypeMethodFactoryProvider.class).in(Singleton.class);
    }

    @SuppressWarnings("TypeMayBeWeakened") // due to guice
    @Provides
    @Singleton
    @MetaType
    SpecFactory<MethodSpec> metaTypeMethodFactory(final AutoSpecFactories autoFactories,
                                                  @MetaType final Set<SpecFactory<MethodSpec>> factories) {
        return autoFactories.createMultiFactory(factories);
    }
}
