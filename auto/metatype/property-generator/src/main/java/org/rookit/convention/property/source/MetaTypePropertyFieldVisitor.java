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
package org.rookit.convention.property.source;

import com.google.inject.Inject;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeVariableName;
import one.util.streamex.StreamEx;
import org.rookit.auto.javax.visitor.ExtendedElementVisitor;
import org.rookit.auto.javax.visitor.StreamExtendedElementVisitor;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.convention.auto.config.NamingConfig;
import org.rookit.convention.guice.MetaType;

import java.util.function.Function;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;

final class MetaTypePropertyFieldVisitor implements StreamExtendedElementVisitor<FieldSpec, Void> {

    private final NamingConfig namingConfig;
    private final TypeVariableName variableName;
    private final ExtendedElementVisitor<StreamEx<FieldSpec>, Void> delegate;

    @Inject
    private MetaTypePropertyFieldVisitor(final NamingConfig namingConfig,
                                         @MetaType final TypeVariableName variableName,
                                         @MetaType final ExtendedElementVisitor<StreamEx<FieldSpec>, Void> delegate) {
        this.namingConfig = namingConfig;
        this.variableName = variableName;
        this.delegate = delegate;
    }

    @Override
    public StreamEx<FieldSpec> visitType(final ExtendedTypeElement extendedType, final Void parameter) {
        final ParameterizedTypeName function = ParameterizedTypeName.get(
                ClassName.get(Function.class),
                this.variableName,
                ClassName.get(extendedType)
        );

        // TODO streams are meant to be lazy, and as such we shouldn't createMutable them eagerly
        return StreamEx.of(
                FieldSpec.builder(String.class, this.namingConfig.propertyName(), PRIVATE, FINAL).build(),
                FieldSpec.builder(function, this.namingConfig.getter(), PRIVATE, FINAL).build()
        )
                .append(this.delegate.visitType(extendedType, parameter));
    }

    @Override
    public String toString() {
        return "MetaTypePropertyFieldVisitor{" +
                "namingConfig=" + this.namingConfig +
                ", variableName=" + this.variableName +
                ", delegate=" + this.delegate +
                "}";
    }
}
