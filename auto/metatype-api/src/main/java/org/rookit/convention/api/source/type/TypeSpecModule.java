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
package org.rookit.convention.api.source.type;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.squareup.javapoet.TypeVariableName;
import org.rookit.convention.api.guice.Container;
import org.rookit.auto.javapoet.method.EntityMethodFactory;
import org.rookit.auto.javapoet.method.MethodFactory;
import org.rookit.auto.javapoet.naming.JavaPoetParameterResolver;
import org.rookit.auto.javapoet.type.BaseTypeSourceAdapter;
import org.rookit.auto.javapoet.type.EmptyLeafTypeSourceFactory;
import org.rookit.auto.javapoet.type.TypeSourceAdapter;
import org.rookit.auto.javax.property.PropertyExtractor;
import org.rookit.auto.source.SingleTypeSourceFactory;
import org.rookit.convention.guice.MetatypeAPI;
import org.rookit.convention.guice.PartialMetatypeAPI;
import org.rookit.utils.primitive.VoidUtils;

@SuppressWarnings("MethodMayBeStatic")
public final class TypeSpecModule extends AbstractModule {

    private static final Module MODULE = new TypeSpecModule();

    public static Module getModule() {
        return MODULE;
    }

    private TypeSpecModule() {}

    @Override
    protected void configure() {

    }

    @Singleton
    @Provides
    @Container
    SingleTypeSourceFactory baseTypeSourceFactory(final PropertyExtractor extractor,
                                                  final MethodFactory methodFactory,
                                                  final EntityMethodFactory entityMethodFactory,
                                                  final TypeSourceAdapter adapter,
                                                  @Container final JavaPoetParameterResolver resolver,
                                                  @MetatypeAPI final TypeVariableName typeVariableName) {
        return new BaseTypeSourceFactory(extractor,
                methodFactory,
                entityMethodFactory,
                adapter,
                resolver,
                typeVariableName);
    }

    @Singleton
    @Provides
    @PartialMetatypeAPI
    SingleTypeSourceFactory partialMetaTypeProvider(final PropertyExtractor extractor,
                                                    final MethodFactory methodFactory,
                                                    final EntityMethodFactory entityMethodFactory,
                                                    final TypeSourceAdapter adapter,
                                                    @PartialMetatypeAPI final JavaPoetParameterResolver resolver,
                                                    @MetatypeAPI final TypeVariableName typeVariableName) {
        return new BaseTypeSourceFactory(extractor,
                methodFactory,
                entityMethodFactory,
                adapter,
                resolver,
                typeVariableName);
    }

    @Singleton
    @Provides
    @MetatypeAPI
    SingleTypeSourceFactory metaTypeProvider(final TypeSourceAdapter adapter,
                                             @MetatypeAPI final JavaPoetParameterResolver resolver) {
        return EmptyLeafTypeSourceFactory.create(adapter, resolver);
    }

    @Singleton
    @Provides
    TypeSourceAdapter typeSourceAdapter(final VoidUtils voidUtils) {
        return BaseTypeSourceAdapter.create(voidUtils);
    }
}
