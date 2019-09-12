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
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import org.rookit.auto.javapoet.method.EntityMethodFactory;
import org.rookit.auto.javapoet.type.JavaPoetTypeSourceFactory;
import org.rookit.auto.javax.naming.Identifier;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.auto.source.spec.SpecFactory;
import org.rookit.auto.source.type.SingleTypeSourceFactory;
import org.rookit.auto.source.type.TypeSource;
import org.rookit.utils.optional.Optional;

import javax.lang.model.element.Modifier;
import java.util.Collection;

abstract class AbstractMetaTypeTypeSourceFactory implements SingleTypeSourceFactory {

    private final JavaPoetTypeSourceFactory adapter;
    private final SpecFactory<FieldSpec> fieldFactory;
    private final EntityMethodFactory entityMethodFactory;
    private final SpecFactory<MethodSpec> methodFactory;

    AbstractMetaTypeTypeSourceFactory(final JavaPoetTypeSourceFactory adapter,
                                      final SpecFactory<MethodSpec> methodFactory,
                                      final EntityMethodFactory entityMethodFactory,
                                      final SpecFactory<FieldSpec> fieldFactory) {
        this.adapter = adapter;
        this.methodFactory = methodFactory;
        this.entityMethodFactory = entityMethodFactory;
        this.fieldFactory = fieldFactory;
    }

    @Override
    public TypeSource create(final Identifier identifier, final ExtendedTypeElement element) {
        final ClassName className = ClassName.get(identifier.packageElement().fullName().asString(), identifier.name());

        final TypeSpec.Builder builder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        typeVariableName(element).ifPresent(builder::addTypeVariable);
        superclass(element).ifPresent(builder::superclass);

        final TypeSpec.Builder spec = builder
                .addSuperinterfaces(superInterfaces(element))
                .addMethods(this.entityMethodFactory.create(element))
                .addFields(this.fieldFactory.create(element))
                .addMethods(this.methodFactory.create(element));

        return this.adapter.fromTypeSpec(identifier, spec.build());
    }

    abstract Collection<TypeName> superInterfaces(ExtendedTypeElement element);

    abstract Optional<TypeName> superclass(ExtendedTypeElement element);

    abstract Optional<TypeVariableName> typeVariableName(ExtendedTypeElement element);

    @Override
    public String toString() {
        return "AbstractMetaTypeTypeSourceFactory{" +
                "adapter=" + this.adapter +
                ", fieldFactory=" + this.fieldFactory +
                ", entityMethodFactory=" + this.entityMethodFactory +
                ", methodFactory=" + this.methodFactory +
                "}";
    }
}
