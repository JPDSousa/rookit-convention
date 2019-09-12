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
package org.rookit.convention.auto.metatype;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Modules;
import org.rookit.auto.javax.ExtendedElementFactory;
import org.rookit.auto.javax.executable.ExtendedExecutableElement;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.auto.javax.type.ExtendedTypeMirrorFactory;
import org.rookit.convention.auto.guice.MetaTypeModuleMixin;
import org.rookit.convention.auto.metatype.property.PropertyModule;
import org.rookit.convention.guice.MetaType;
import org.rookit.convention.guice.MetaTypeModelSerializer;
import org.rookit.convention.guice.MetaTypeModelType;
import org.rookit.convention.guice.MetaTypeProperties;
import org.rookit.convention.guice.MetaTypeProperty;

import javax.lang.model.element.TypeElement;

import static java.lang.String.format;

public final class MetaTypeModule extends AbstractModule implements MetaTypeModuleMixin<ExtendedExecutableElement> {

    private static final String NO_META_TYPE_METHOD = "Cannot find '%s' method in meta type '%s'.";
    private static final String INVALID_META_TYPE_CLASS = "Something went wrong when using '%s' as a meta type class.";

    private static final Module MODULE = Modules.combine(
            new MetaTypeModule(),
            PropertyModule.getModule()
    );

    public static Module getModule() {
        return MODULE;
    }

    private MetaTypeModule() {}

    @SuppressWarnings({"AnonymousInnerClassMayBeStatic", "AnonymousInnerClass", "EmptyClass"})
    @Override
    protected void configure() {
        bindMetaTypeProperties(binder());
        bind(new TypeLiteral<Class<?>>() {}).annotatedWith(MetaType.class)
                .toInstance(org.rookit.convention.MetaType.class);
    }

    @Provides
    @Singleton
    @MetaTypeModelSerializer
    ExtendedExecutableElement serializerMethod(@MetaType final ExtendedTypeElement metaTypeElement) {
        return getMethodOrFail(metaTypeElement, "modelSerializer");

    }

    @Provides
    @Singleton
    @MetaTypeModelType
    ExtendedExecutableElement typeMethod(@MetaType final ExtendedTypeElement metaTypeElement) {
        return getMethodOrFail(metaTypeElement, "modelType");

    }

    @Provides
    @Singleton
    @MetaTypeProperties
    ExtendedExecutableElement propertiesMethod(@MetaType final ExtendedTypeElement metaTypeElement) {
        return getMethodOrFail(metaTypeElement, "properties");
    }

    @Provides
    @Singleton
    @MetaTypeProperty
    ExtendedExecutableElement propertyMethod(@MetaType final ExtendedTypeElement metaTypeElement) {
        return getMethodOrFail(metaTypeElement, "property");
    }

    @Provides
    @Singleton
    @MetaType
    ExtendedTypeElement metaType(@MetaType final Class<?> metaTypeClass,
                                 final ExtendedTypeMirrorFactory mirrorFactory,
                                 final ExtendedElementFactory elementFactory) {
        return mirrorFactory.createWithErasure(metaTypeClass)
                .toElement()
                .select(TypeElement.class)
                .map(elementFactory::extendType)
                .orElseThrow(() -> new IllegalArgumentException(format(INVALID_META_TYPE_CLASS, metaTypeClass)));
    }

    // TODO this is generic enough to not belong here.
    // TODO also, copied from PropertyModule
    private ExtendedExecutableElement getMethodOrFail(final ExtendedTypeElement typeElement, final String name) {
        return typeElement.getMethod(name)
                .orElseThrow(() -> new IllegalArgumentException(
                        format(NO_META_TYPE_METHOD, name, typeElement.getSimpleName())));
    }

    @Override
    public Key<ExtendedExecutableElement> key() {
        return Key.get(ExtendedExecutableElement.class, MetaType.class);
    }

}
