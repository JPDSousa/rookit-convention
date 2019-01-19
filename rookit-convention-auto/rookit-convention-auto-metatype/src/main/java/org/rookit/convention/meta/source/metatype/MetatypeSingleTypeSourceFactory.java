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
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeVariableName;
import org.rookit.auto.javapoet.FieldFactory;
import org.rookit.auto.javapoet.method.EntityMethodFactory;
import org.rookit.auto.javapoet.method.MethodFactory;
import org.rookit.auto.javapoet.naming.JavaPoetNamingFactory;
import org.rookit.auto.javapoet.type.TypeSourceAdapter;
import org.rookit.auto.javax.element.ExtendedTypeElement;
import org.rookit.auto.javax.property.PropertyExtractor;
import org.rookit.convention.meta.guice.Metatype;
import org.rookit.convention.metatype.AbstractMetatype;
import org.rookit.convention.utils.guice.MetatypeAPI;
import org.rookit.utils.optional.Optional;
import org.rookit.utils.optional.OptionalFactory;

import java.util.Collection;

final class MetatypeSingleTypeSourceFactory extends AbstractMetatypeTypeSourceFactory {

    private final JavaPoetNamingFactory namingFactory;
    private final ClassName delegateBaseMetatype;
    private final OptionalFactory optionalFactory;

    @Inject
    private MetatypeSingleTypeSourceFactory(final TypeSourceAdapter adapter,
                                            @Metatype final MethodFactory methodFactory,
                                            @Metatype final EntityMethodFactory entityMethodFactory,
                                            @Metatype final FieldFactory fieldFactory,
                                            final PropertyExtractor extractor,
                                            @MetatypeAPI final JavaPoetNamingFactory namingFactory,
                                            final OptionalFactory optionalFactory) {
        super(adapter, methodFactory, entityMethodFactory, fieldFactory, extractor);
        this.namingFactory = namingFactory;
        this.optionalFactory = optionalFactory;
        this.delegateBaseMetatype = ClassName.get(AbstractMetatype.class);
    }

    @Override
    Collection<TypeName> superInterfaces(final ExtendedTypeElement element) {
        return ImmutableList.of(this.namingFactory.classNameFor(element));
    }

    @Override
    Optional<TypeName> superclass(final ExtendedTypeElement element) {
        return this.optionalFactory.of(ParameterizedTypeName.get(this.delegateBaseMetatype, ClassName.get(element)));
    }

    @Override
    Optional<TypeVariableName> typeVariableName(final ExtendedTypeElement element) {
        return this.optionalFactory.empty();
    }

    @Override
    public String toString() {
        return "MetatypeSingleTypeSourceFactory{" +
                "namingFactory=" + this.namingFactory +
                ", delegateBaseMetatype=" + this.delegateBaseMetatype +
                ", optionalFactory=" + this.optionalFactory +
                "} " + super.toString();
    }
}
