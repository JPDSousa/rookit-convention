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

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.squareup.javapoet.MethodSpec;
import one.util.streamex.StreamEx;
import org.rookit.auto.javax.executable.ExtendedExecutableElement;
import org.rookit.convention.auto.javapoet.method.ConventionTypeElementMethodSpecVisitors;
import org.rookit.convention.auto.javax.visitor.ConventionTypeElementVisitor;
import org.rookit.convention.guice.MetaTypeModelType;

final class ModelTypeMethodFactoryProvider
        implements Provider<ConventionTypeElementVisitor<StreamEx<MethodSpec>, Void>> {

    private final ConventionTypeElementMethodSpecVisitors visitors;
    private final ExtendedExecutableElement method;

    @Inject
    private ModelTypeMethodFactoryProvider(@MetaTypeModelType final ExtendedExecutableElement method,
                                           final ConventionTypeElementMethodSpecVisitors visitors) {
        this.method = method;
        this.visitors = visitors;
    }

    @Override
    public ConventionTypeElementVisitor<StreamEx<MethodSpec>, Void> get() {
        return this.visitors.<Void>getterMethodBuilder(this.method)
                .build();
    }

    @Override
    public String toString() {
        return "ModelTypeMethodFactoryProvider{" +
                "method=" + this.method +
                ", visitors=" + this.visitors +
                "}";
    }
}
