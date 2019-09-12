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
package org.rookit.convention.module.source.config;

import com.google.common.collect.ImmutableSet;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.util.Modules;
import org.rookit.auto.config.DependencyAwareProcessorConfig;
import org.rookit.auto.config.ProcessorConfig;
import org.rookit.convention.auto.config.ConventionConfig;
import org.rookit.convention.auto.config.ConventionMetatypeConfig;
import org.rookit.guice.auto.config.GuiceConfig;
import org.rookit.convention.auto.config.MetatypeApiConfig;
import org.rookit.convention.auto.config.MetatypeModuleConfig;
import org.rookit.convention.auto.config.PropertyConfig;
import org.rookit.utils.string.template.TemplateFactory;

import javax.annotation.processing.Messager;

public final class ConfigurationModule extends AbstractModule {

    private static final Module MODULE = Modules.combine(
            new ConfigurationModule(),
            org.rookit.auto.config.ConfigurationModule.getModule()
    );

    public static Module getModule() {
        return MODULE;
    }

    private ConfigurationModule() {}

    @SuppressWarnings("TypeMayBeWeakened") // due to guice
    @Provides
    @Singleton
    ProcessorConfig processorConfig(final MetatypeModuleConfig delegate,
                                    final MetatypeApiConfig apiDependency,
                                    final ConventionMetatypeConfig metatypeDependency,
                                    final GuiceConfig guiceDependency,
                                    final PropertyConfig propertyDependency,
                                    final Messager messager) {
        return DependencyAwareProcessorConfig.create(
                delegate,
                ImmutableSet.of(apiDependency, guiceDependency, metatypeDependency, propertyDependency),
                messager
        );
    }

    @Provides
    @Singleton
    MetatypeModuleConfig config(final ConventionConfig parent, final TemplateFactory templateFactory) {
        final String name = "module";
        return new MetatypeModuleConfigImpl(parent.getProcessorConfig(name), parent, name, templateFactory);
    }

}
