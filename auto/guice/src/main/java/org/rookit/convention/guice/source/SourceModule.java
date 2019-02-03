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
package org.rookit.convention.guice.source;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.google.inject.util.Modules;
import org.rookit.auto.entity.EntityFactory;
import org.rookit.auto.entity.PartialEntityFactory;
import org.rookit.auto.identifier.PropertyIdentifierFactory;
import org.rookit.auto.javax.guice.JavaxModule;
import org.rookit.convention.guice.Guice;
import org.rookit.convention.guice.source.config.ConfigurationModule;
import org.rookit.convention.guice.source.entity.EntityModule;
import org.rookit.convention.guice.source.naming.NamingModule;
import org.rookit.convention.guice.source.type.TypeModule;
import org.rookit.utils.guice.UtilsModule;

@SuppressWarnings("MethodMayBeStatic")
public final class SourceModule extends AbstractModule {

    private static final Module MODULE = Modules.combine(
            NamingModule.getModule(),
            TypeModule.getModule(),
            JavaxModule.getModule(),
            EntityModule.getModule(),
            UtilsModule.getModule(),
            ConfigurationModule.getModule(),
            new SourceModule()
    );

    public static Module getModule() {
        return MODULE;
    }

    private SourceModule() {}

    @Override
    protected void configure() {
        //noinspection UninstantiableBinding not really
        bind(PropertyIdentifierFactory.class).annotatedWith(Guice.class).to(PropertyIdentifierFactory.class);
        //noinspection UninstantiableBinding not really
        bind(PartialEntityFactory.class).to(EntityFactory.class).in(Singleton.class);
    }

}
