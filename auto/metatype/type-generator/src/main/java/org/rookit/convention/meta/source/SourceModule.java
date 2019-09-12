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
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.util.Modules;
import org.rookit.convention.auto.ConventionLibModule;
import org.rookit.convention.auto.property.ExtendedPropertyExtractor;
import org.rookit.convention.auto.property.ExtendedPropertyExtractorFactory;
import org.rookit.convention.meta.source.config.ConfigurationModule;
import org.rookit.convention.meta.source.metatype.MetaTypeModule;
import org.rookit.failsafe.FailsafeModule;
import org.rookit.io.path.PathModule;
import org.rookit.utils.guice.UtilsModule;

@SuppressWarnings("MethodMayBeStatic")
public final class SourceModule extends AbstractModule {

    private static final Module MODULE = Modules.combine(
            new SourceModule(),
            ConventionLibModule.getModule(),
            MetaTypeModule.getModule(),
            ConfigurationModule.getModule(),
            FailsafeModule.getModule(),
            PathModule.getModule(),
            UtilsModule.getModule()
    );

    public static Module getModule() {
        return MODULE;
    }

    private SourceModule() {}

    @Provides
    @Singleton
    ExtendedPropertyExtractor propertyExtractor(final ExtendedPropertyExtractorFactory factory) {
        final ExtendedPropertyExtractor baseExtractor = factory.create(executableElement -> true);
        return factory.createRecursive(baseExtractor);
    }

}
