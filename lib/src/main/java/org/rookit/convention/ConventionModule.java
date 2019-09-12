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
package org.rookit.convention;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.util.Modules;
import org.rookit.convention.annotation.Entity;
import org.rookit.convention.annotation.EntityExtension;
import org.rookit.convention.annotation.LaConvention;
import org.rookit.convention.annotation.PartialEntity;
import org.rookit.convention.annotation.Property;
import org.rookit.convention.annotation.PropertyContainer;
import org.rookit.convention.property.PropertyModelModule;

import java.lang.annotation.Annotation;

public final class ConventionModule extends AbstractModule {

    private static final Module MODULE = Modules.combine(new ConventionModule(),
            PropertyModelModule.getModule());

    public static Module getModule() {
        return MODULE;
    }

    private ConventionModule() {}

    @Override
    protected void configure() {
        final Multibinder<Class<? extends Annotation>> mBinder = Multibinder
                .newSetBinder(binder(), Key.get(new TypeLiteral<Class<? extends Annotation>>() {}, LaConvention.class));
        mBinder.addBinding().toInstance(Entity.class);
        mBinder.addBinding().toInstance(EntityExtension.class);
        mBinder.addBinding().toInstance(PartialEntity.class);
        mBinder.addBinding().toInstance(Property.class);
        mBinder.addBinding().toInstance(PropertyContainer.class);

        bind(ConventionUtils.class).to(ConventionUtilsImpl.class).in(Singleton.class);
    }
}
