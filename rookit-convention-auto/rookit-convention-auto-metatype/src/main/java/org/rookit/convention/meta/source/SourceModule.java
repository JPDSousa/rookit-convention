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
package org.rookit.convention.meta.source;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.util.Modules;
import org.rookit.auto.javax.ExtendedExecutableElementFactory;
import org.rookit.auto.javax.JavaxRepetitionFactory;
import org.rookit.auto.javax.element.ElementUtils;
import org.rookit.auto.javax.element.ExtendedTypeElementFactory;
import org.rookit.auto.javax.property.BaseExtendedPropertyFactory;
import org.rookit.auto.javax.property.BasePropertyExtractor;
import org.rookit.auto.javax.property.ExtendedPropertyFactory;
import org.rookit.auto.javax.property.PropertyExtractor;
import org.rookit.auto.javax.property.RecursivePropertyExtractor;
import org.rookit.auto.javax.property.UnwrapperPropertyFactory;
import org.rookit.convention.meta.source.config.ConfigurationModule;
import org.rookit.convention.meta.source.metatype.MetatypeModule;

@SuppressWarnings("MethodMayBeStatic")
public final class SourceModule extends AbstractModule {

    private static final Module MODULE = Modules.override(
            org.rookit.convention.api.source.SourceModule.getModule()
    ).with(
            Modules.override(
                    org.rookit.convention.guice.source.SourceModule.getModule()
            ).with(
                    new SourceModule(),
                    MetatypeModule.getModule(),
                    ConfigurationModule.getModule()
            )
    );

    public static Module getModule() {
        return MODULE;
    }

    private SourceModule() {}

    @Override
    protected void configure() {

    }

    @Provides
    @Singleton
    PropertyExtractor propertyExtractor(final ExtendedPropertyFactory factory,
                                        final ElementUtils utils,
                                        final ExtendedExecutableElementFactory executableFactory) {
        final PropertyExtractor baseExtractor = BasePropertyExtractor.create(factory, executableFactory);
        return RecursivePropertyExtractor.create(baseExtractor, utils);
    }

    @Provides
    @Singleton
    ExtendedPropertyFactory extendedPropertyFactory(final JavaxRepetitionFactory repetitionFactory,
                                                    final ElementUtils utils,
                                                    final Provider<ExtendedTypeElementFactory> elementFactory) {
        final ExtendedPropertyFactory baseFactory = BaseExtendedPropertyFactory.create(repetitionFactory,
                utils,
                elementFactory);

        return UnwrapperPropertyFactory.create(baseFactory, repetitionFactory);
    }

}
