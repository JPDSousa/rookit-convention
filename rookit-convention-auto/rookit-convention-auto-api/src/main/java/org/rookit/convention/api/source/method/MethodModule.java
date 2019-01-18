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
package org.rookit.convention.api.source.method;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.squareup.javapoet.TypeVariableName;
import org.rookit.auto.javapoet.method.EntityMethodFactory;
import org.rookit.auto.javapoet.method.MethodFactory;
import org.rookit.auto.javapoet.naming.JavaPoetNamingFactory;
import org.rookit.auto.javapoet.naming.JavaPoetPropertyNamingFactory;
import org.rookit.auto.javax.element.ElementUtils;
import org.rookit.auto.javax.property.PropertyTypeResolver;
import org.rookit.convention.utils.guice.MetatypeAPI;
import org.rookit.convention.utils.javapoet.MetatypeJavaPoetPropertyNamingFactory;

@SuppressWarnings("MethodMayBeStatic")
public final class MethodModule extends AbstractModule {

    private static final Module MODULE = new MethodModule();

    public static Module getModule() {
        return MODULE;
    }

    private MethodModule() {}

    @Override
    protected void configure() {
        bind(MethodFactory.class).to(EntityPropertiesMethodFactory.class).in(Singleton.class);
        bind(EntityMethodFactory.class).to(EntityPropertiesEntityMethodFactory.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    @MetatypeAPI
    JavaPoetPropertyNamingFactory javaPoetPropertyNamingFactory(
            final ElementUtils utils,
            @MetatypeAPI final TypeVariableName variableName,
            final PropertyTypeResolver propertyTypeResolver,
            @MetatypeAPI final JavaPoetNamingFactory namingFactory) {
        return MetatypeJavaPoetPropertyNamingFactory.create(propertyTypeResolver, utils, variableName, namingFactory, true);
    }
}
