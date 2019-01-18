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
package org.rookit.convention.api.source;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.util.Modules;
import org.rookit.auto.javax.ExtendedExecutableElementFactory;
import org.rookit.auto.javax.guice.JavaxModule;
import org.rookit.auto.javax.property.BasePropertyExtractor;
import org.rookit.auto.javax.property.ExtendedPropertyFactory;
import org.rookit.auto.javax.property.PropertyExtractor;
import org.rookit.auto.javax.property.PropertyTypeResolver;
import org.rookit.auto.javax.property.UnwrapperPropertyFactory;
import org.rookit.convention.api.guice.Container;
import org.rookit.convention.api.source.config.ConfigurationModule;
import org.rookit.convention.api.source.entity.EntityModule;
import org.rookit.convention.api.source.method.MethodModule;
import org.rookit.convention.api.source.naming.NamingModule;
import org.rookit.convention.api.source.type.TypeSpecModule;
import org.rookit.convention.utils.ConventionPropertyResolver;
import org.rookit.utils.guice.UtilsModule;
import org.rookit.utils.optional.OptionalFactory;

@SuppressWarnings("MethodMayBeStatic")
public final class SourceModule extends AbstractModule {

    private static final Module MODULE = Modules.combine(new SourceModule(),
            UtilsModule.getModule(),
            MethodModule.getModule(),
            TypeSpecModule.getModule(),
            NamingModule.getModule(),
            ConfigurationModule.getModule(),
            EntityModule.getModule(),
            JavaxModule.getModule());

    public static Module getModule() {
        return MODULE;
    }

    private SourceModule() {}

    @Override
    protected void configure() {
        bind(ExtendedPropertyFactory.class).annotatedWith(Container.class)
                .to(UnwrapperPropertyFactory.class).in(Singleton.class);
    }

    @Singleton
    @Provides
    PropertyExtractor methodExtractor(@Container final ExtendedPropertyFactory propertyFactory,
                                      final ExtendedExecutableElementFactory executableFactory) {
        return BasePropertyExtractor.create(propertyFactory, executableFactory);
    }

    @Provides
    @Singleton
    PropertyTypeResolver propertyTypeResolver(final OptionalFactory optionalFactory) {
        return ConventionPropertyResolver.create(optionalFactory);
    }
}
