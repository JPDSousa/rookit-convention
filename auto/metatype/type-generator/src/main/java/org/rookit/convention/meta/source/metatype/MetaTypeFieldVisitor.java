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

import com.google.inject.Inject;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeVariableName;
import one.util.streamex.StreamEx;
import org.rookit.auto.javax.visitor.StreamExtendedElementVisitor;
import org.rookit.convention.guice.MetaType;
import org.rookit.convention.auto.javapoet.naming.JavaPoetPropertyNamingFactory;
import org.rookit.convention.auto.javax.ConventionTypeElement;
import org.rookit.convention.auto.javax.visitor.ConventionTypeElementVisitor;
import org.rookit.convention.auto.property.Property;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;

final class MetaTypeFieldVisitor implements ConventionTypeElementVisitor<StreamEx<FieldSpec>, Void>,
        StreamExtendedElementVisitor<FieldSpec, Void> {

    private final JavaPoetPropertyNamingFactory namingFactory;
    private final ClassName metaTypeName;
    private final TypeVariableName variableName;

    @Inject
    private MetaTypeFieldVisitor(@MetaType final JavaPoetPropertyNamingFactory namingFactory,
                                 @MetaType final TypeVariableName variableName) {
        this.namingFactory = namingFactory;
        this.variableName = variableName;
        this.metaTypeName = ClassName.get(org.rookit.convention.MetaType.class);
    }

    @Override
    public StreamEx<FieldSpec> visitConventionType(final ConventionTypeElement element, final Void parameter) {
        final TypeName param = element.isPartialEntity() ? this.variableName : ClassName.get(element);
        final ParameterizedTypeName metatypeType = ParameterizedTypeName.get(this.metaTypeName, param);

        // TODO ERROR here!!!!!
        return StreamEx.of(FieldSpec.builder(metatypeType, "metaType", PRIVATE, FINAL).build())
                .append(createPropertyFields(element));
    }

    private StreamEx<FieldSpec> createPropertyFields(final ConventionTypeElement owner) {
        return StreamEx.of(owner.properties())
                .map(property -> createProperty(owner, property));
    }

    private FieldSpec createProperty(final ConventionTypeElement owner, final Property property) {
        final TypeName type = this.namingFactory.typeNameFor(owner, property);
        return FieldSpec.builder(type, property.name(), PRIVATE, FINAL).build();
    }

    @Override
    public String toString() {
        return "MetaTypeFieldVisitor{" +
                "namingFactory=" + this.namingFactory +
                ", metaTypeName=" + this.metaTypeName +
                ", variableName=" + this.variableName +
                "}";
    }
}
