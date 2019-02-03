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

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import org.rookit.auto.identifier.Identifier;
import org.rookit.auto.javapoet.FieldFactory;
import org.rookit.auto.javapoet.method.EntityMethodFactory;
import org.rookit.auto.javapoet.method.MethodFactory;
import org.rookit.auto.javapoet.type.TypeSourceAdapter;
import org.rookit.auto.javax.element.ExtendedTypeElement;
import org.rookit.auto.javax.property.ExtendedProperty;
import org.rookit.auto.javax.property.PropertyExtractor;
import org.rookit.auto.source.SingleTypeSourceFactory;
import org.rookit.auto.source.TypeSource;
import org.rookit.utils.optional.Optional;

import javax.lang.model.element.Modifier;
import java.util.Collection;
import java.util.stream.Collectors;

abstract class AbstractMetatypeTypeSourceFactory implements SingleTypeSourceFactory {

    private final TypeSourceAdapter adapter;
    private final PropertyExtractor extractor;
    private final FieldFactory fieldFactory;
    private final EntityMethodFactory entityMethodFactory;
    private final MethodFactory methodFactory;

    AbstractMetatypeTypeSourceFactory(final TypeSourceAdapter adapter,
                                      final MethodFactory methodFactory,
                                      final EntityMethodFactory entityMethodFactory,
                                      final FieldFactory fieldFactory,
                                      final PropertyExtractor extractor) {
        this.adapter = adapter;
        this.methodFactory = methodFactory;
        this.entityMethodFactory = entityMethodFactory;
        this.fieldFactory = fieldFactory;
        this.extractor = extractor;
    }

    @Override
    public TypeSource create(final Identifier identifier, final ExtendedTypeElement element) {
        final ClassName className = ClassName.get(identifier.packageName().fullName(), identifier.name());
        final Collection<ExtendedProperty> properties = this.extractor.fromType(element)
                .collect(Collectors.toList());

        final TypeSpec.Builder builder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        typeVariableName(element).ifPresent(builder::addTypeVariable);
        superclass(element).ifPresent(builder::superclass);

        final TypeSpec spec = builder
                .addSuperinterfaces(superInterfaces(element))
                .addMethods(this.entityMethodFactory.create(element))
                .addFields(this.fieldFactory.filterCompatible(element, properties))
                .addMethods(this.methodFactory.filterCompatible(element, properties))
                .build();

        return this.adapter.fromTypeSpec(identifier, spec);
    }

    abstract Collection<TypeName> superInterfaces(ExtendedTypeElement element);

    abstract Optional<TypeName> superclass(ExtendedTypeElement element);

    abstract Optional<TypeVariableName> typeVariableName(ExtendedTypeElement element);

}
