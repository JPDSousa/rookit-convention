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

import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.util.Modules;
import com.squareup.javapoet.TypeVariableName;
import org.rookit.auto.EntityHandler;
import org.rookit.auto.entity.EntityFactory;
import org.rookit.auto.entity.PropertyEntityFactory;
import org.rookit.auto.entity.property.PropertyFlatEntityFactory;
import org.rookit.auto.identifier.BaseEntityIdentifierFactory;
import org.rookit.auto.identifier.EntityIdentifierFactory;
import org.rookit.auto.javapoet.FieldFactory;
import org.rookit.auto.javapoet.method.EntityMethodFactory;
import org.rookit.auto.javapoet.method.MethodFactory;
import org.rookit.auto.javax.property.ExtendedProperty;
import org.rookit.auto.javax.property.PropertyEvaluator;
import org.rookit.auto.naming.AbstractNamingModule;
import org.rookit.auto.naming.BaseJavaPoetNamingFactory;
import org.rookit.auto.naming.NamingFactory;
import org.rookit.auto.source.SingleTypeSourceFactory;
import org.rookit.convention.meta.guice.Metatype;
import org.rookit.convention.property.source.config.ConfigurationModule;
import org.rookit.convention.utils.config.ConventionPropertyConfig;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@SuppressWarnings("MethodMayBeStatic")
public final class SourceModule extends AbstractNamingModule {

    private static final Module MODULE = Modules.override(
            org.rookit.convention.meta.source.SourceModule.getModule()
    ).with(
            new SourceModule(),
            ConfigurationModule.getModule()
    );

    public static Module getModule() {
        return MODULE;
    }

    private SourceModule() {}

    @Override
    protected void configure() {
        bind(EntityIdentifierFactory.class).to(BaseEntityIdentifierFactory.class).in(Singleton.class);
        bind(EntityHandler.class).to(PropertyEntityHandler.class).in(Singleton.class);
        bind(PropertyEntityFactory.class).to(MetatypePropertyEntityFactory.class).in(Singleton.class);
        bind(PropertyEvaluator.class).toInstance(ExtendedProperty::isContainer);
        bind(EntityFactory.class).to(PropertyFlatEntityFactory.class).in(Singleton.class);
        bind(SingleTypeSourceFactory.class).to(PropertySingleTypeSourceFactory.class).in(Singleton.class);
        bind(FieldFactory.class).to(MetatypePropertyFieldFactory.class).in(Singleton.class);
        bind(EntityMethodFactory.class).to(PropertyMetatypeEntityMethodFactory.class).in(Singleton.class);
        bind(MethodFactory.class).to(Key.get(MethodFactory.class, Metatype.class)).in(Singleton.class);
    }

    @Provides
    @Singleton
    NamingFactory namingFactory(final ConventionPropertyConfig config) {
        return BaseJavaPoetNamingFactory.create(config.basePackage(), config.entitySuffix(), EMPTY, EMPTY);
    }

    @Provides
    @Singleton
    TypeVariableName typeVariableName(final ConventionPropertyConfig config) {
        return config.parameterName();
    }
}
