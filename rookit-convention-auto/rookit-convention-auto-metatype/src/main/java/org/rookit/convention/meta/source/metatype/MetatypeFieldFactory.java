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
package org.rookit.convention.meta.source.metatype;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeVariableName;
import one.util.streamex.StreamEx;
import org.rookit.auto.javapoet.FieldFactory;
import org.rookit.auto.javapoet.naming.JavaPoetPropertyNamingFactory;
import org.rookit.auto.javax.element.ExtendedTypeElement;
import org.rookit.auto.javax.property.ExtendedProperty;
import org.rookit.convention.meta.guice.Metatype;

import java.util.Collection;
import java.util.stream.Stream;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;

final class MetatypeFieldFactory implements FieldFactory {

    private final JavaPoetPropertyNamingFactory namingFactory;
    private final ClassName metatypeName;
    private final TypeVariableName variableName;

    @Inject
    private MetatypeFieldFactory(@Metatype final JavaPoetPropertyNamingFactory namingFactory,
                                 @Metatype final TypeVariableName variableName) {
        this.namingFactory = namingFactory;
        this.variableName = variableName;
        this.metatypeName = ClassName.get(org.rookit.convention.Metatype.class);
    }

    @Override
    public Collection<FieldSpec> filterCompatible(final ExtendedTypeElement owner,
                                                  final Collection<ExtendedProperty> properties) {
        final TypeName param = owner.isPartialEntity() ? this.variableName : ClassName.get(owner);
        final ParameterizedTypeName metatypeType = ParameterizedTypeName.get(this.metatypeName, param);
        return ImmutableList.<FieldSpec>builder()
                .add(FieldSpec.builder(metatypeType, "metatype", PRIVATE, FINAL).build())
                .addAll(createPropertyFields(owner, properties))
                .build();
    }

    private Collection<FieldSpec> createPropertyFields(final ExtendedTypeElement owner,
                                                       final Collection<ExtendedProperty> properties) {
        return StreamEx.of(properties)
                .filter(this::isCompatible)
                .flatMap(property -> create(owner, property))
                .toImmutableSet();
    }

    @Override
    public Stream<FieldSpec> create(final ExtendedTypeElement owner, final ExtendedProperty property) {
        final TypeName type = this.namingFactory.typeNameFor(owner, property);
        final FieldSpec field = FieldSpec.builder(type, property.name(), PRIVATE, FINAL)
                .build();

        return StreamEx.of(field);
    }

    @Override
    public boolean isCompatible(final ExtendedProperty property) {
        // all properties are welcome
        return true;
    }

    @Override
    public String toString() {
        return "MetatypeFieldFactory{" +
                "namingFactory=" + this.namingFactory +
                ", metatypeName=" + this.metatypeName +
                ", variableName=" + this.variableName +
                "}";
    }
}
