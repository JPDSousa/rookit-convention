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
package org.rookit.convention.auto.javapoet.method;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import one.util.streamex.StreamEx;
import org.rookit.auto.javapoet.method.MethodSpecFactory;
import org.rookit.auto.javax.visitor.StreamExtendedElementVisitor;
import org.rookit.convention.auto.javax.ConventionTypeElement;
import org.rookit.convention.auto.javax.visitor.ConventionTypeElementVisitor;
import org.rookit.convention.auto.property.Property;
import org.rookit.utils.string.template.Template1;

import java.util.Collection;
import java.util.function.Function;

final class TemplateMethodVisitor<P> implements ConventionTypeElementVisitor<StreamEx<MethodSpec>, P>,
        StreamExtendedElementVisitor<MethodSpec, P> {
    
    private final MethodSpecFactory methodSpecFactory;
    private final Template1 template;
    private final Function<Property, Collection<ParameterSpec>> parameterResolver;

    TemplateMethodVisitor(final MethodSpecFactory methodSpecFactory,
                          final Template1 template,
                          final Function<Property, Collection<ParameterSpec>> parameterResolver) {
        this.methodSpecFactory = methodSpecFactory;
        this.template = template;
        this.parameterResolver = parameterResolver;
    }

    private MethodSpec create(final Property property) {
        final Collection<ParameterSpec> paramsCollection = this.parameterResolver.apply(property);
        final ParameterSpec[] params = paramsCollection.toArray(new ParameterSpec[paramsCollection.size()]);
        return this.methodSpecFactory.create(property.name(), this.template, params);
    }

    @Override
    public StreamEx<MethodSpec> visitConventionType(final ConventionTypeElement element, final P parameter) {
        return StreamEx.of(element.properties())
                .map(this::create);
    }

    @Override
    public String toString() {
        return "TemplateMethodVisitor{" +
                "methodSpecFactory=" + this.methodSpecFactory +
                ", template=" + this.template +
                ", parameterResolver=" + this.parameterResolver +
                "}";
    }
}
