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

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeVariableName;
import org.rookit.auto.javapoet.FieldFactory;
import org.rookit.auto.javax.element.ExtendedTypeElement;
import org.rookit.auto.javax.property.ExtendedProperty;
import org.rookit.convention.config.NamingConfig;
import org.rookit.convention.guice.Metatype;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;

final class MetatypePropertyFieldFactory implements FieldFactory {

    private final NamingConfig namingConfig;
    private final TypeVariableName variableName;
    private final FieldFactory delegate;

    @Inject
    private MetatypePropertyFieldFactory(final NamingConfig namingConfig,
                                         @Metatype final TypeVariableName variableName,
                                         @Metatype final FieldFactory delegate) {
        this.namingConfig = namingConfig;
        this.variableName = variableName;
        this.delegate = delegate;
    }

    @Override
    public Collection<FieldSpec> filterCompatible(final ExtendedTypeElement owner,
                                                  final Collection<ExtendedProperty> properties) {
        final ParameterizedTypeName function = ParameterizedTypeName.get(
                ClassName.get(Function.class),
                this.variableName,
                ClassName.get(owner)
        );

        return ImmutableList.<FieldSpec>builder()
                .add(FieldSpec.builder(String.class, this.namingConfig.propertyName(), PRIVATE, FINAL).build())
                .add(FieldSpec.builder(function, this.namingConfig.getter(), PRIVATE, FINAL).build())
                .addAll(this.delegate.filterCompatible(owner, properties))
                .build();
    }


    @Override
    public Stream<FieldSpec> create(final ExtendedTypeElement owner, final ExtendedProperty property) {
        return this.delegate.create(owner, property);
    }

    @Override
    public boolean isCompatible(final ExtendedProperty property) {
        return this.delegate.isCompatible(property);
    }

    @Override
    public String toString() {
        return "MetatypePropertyFieldFactory{" +
                "namingConfig=" + this.namingConfig +
                ", variableName=" + this.variableName +
                ", delegate=" + this.delegate +
                "}";
    }
}
