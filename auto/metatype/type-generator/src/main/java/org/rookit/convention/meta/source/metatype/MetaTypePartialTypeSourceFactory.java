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
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeVariableName;
import org.rookit.auto.javapoet.method.EntityMethodFactory;
import org.rookit.auto.javapoet.naming.JavaPoetNamingFactory;
import org.rookit.auto.javapoet.type.JavaPoetTypeSourceFactory;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.auto.source.spec.SpecFactory;
import org.rookit.convention.guice.MetaType;
import org.rookit.convention.auto.metatype.guice.MetaTypeAPI;
import org.rookit.convention.metatype.AbstractMetaType;
import org.rookit.utils.optional.Optional;
import org.rookit.utils.optional.OptionalFactory;

import java.util.Collection;

final class MetaTypePartialTypeSourceFactory extends AbstractMetaTypeTypeSourceFactory {

    private final OptionalFactory optionalFactory;
    private final TypeVariableName typeVariableName;
    private final TypeName delegateBaseMetaType;
    private final JavaPoetNamingFactory namingFactory;

    @Inject
    private MetaTypePartialTypeSourceFactory(final JavaPoetTypeSourceFactory adapter,
                                             @MetaType final SpecFactory<FieldSpec> fieldFactory,
                                             @MetaType final EntityMethodFactory entityMethodFactory,
                                             @MetaType final SpecFactory<MethodSpec> specFactory,
                                             @MetaType final TypeVariableName typeVariableName,
                                             @MetaTypeAPI final JavaPoetNamingFactory namingFactory,
                                             final OptionalFactory optionalFactory) {
        super(adapter, specFactory, entityMethodFactory, fieldFactory);
        this.optionalFactory = optionalFactory;
        this.typeVariableName = typeVariableName;
        this.delegateBaseMetaType = ParameterizedTypeName.get(ClassName.get(AbstractMetaType.class),
                this.typeVariableName);
        this.namingFactory = namingFactory;
    }

    @Override
    Collection<TypeName> superInterfaces(final ExtendedTypeElement element) {
        return ImmutableList.of(ParameterizedTypeName.get(this.namingFactory.classNameFor(element),
                this.typeVariableName));
    }

    @Override
    Optional<TypeName> superclass(final ExtendedTypeElement element) {
        return this.optionalFactory.of(this.delegateBaseMetaType);
    }

    @Override
    Optional<TypeVariableName> typeVariableName(final ExtendedTypeElement element) {
        return this.optionalFactory.of(TypeVariableName.get(this.typeVariableName.name, ClassName.get(element)));
    }

    @Override
    public String toString() {
        return "MetaTypePartialTypeSourceFactory{" +
                "optionalFactory=" + this.optionalFactory +
                ", typeVariableName=" + this.typeVariableName +
                ", delegateBaseMetaType=" + this.delegateBaseMetaType +
                ", namingFactory=" + this.namingFactory +
                "} " + super.toString();
    }
}
