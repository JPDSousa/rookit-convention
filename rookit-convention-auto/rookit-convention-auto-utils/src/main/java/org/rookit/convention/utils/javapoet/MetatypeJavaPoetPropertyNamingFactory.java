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
package org.rookit.convention.utils.javapoet;

import com.google.inject.Inject;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeVariableName;
import org.rookit.auto.javapoet.naming.JavaPoetNamingFactory;
import org.rookit.auto.javapoet.naming.JavaPoetPropertyNamingFactory;
import org.rookit.auto.javax.ExtendedTypeMirror;
import org.rookit.auto.javax.JavaxKeyedRepetition;
import org.rookit.auto.javax.element.ElementUtils;
import org.rookit.auto.javax.element.ExtendedTypeElement;
import org.rookit.auto.javax.property.ExtendedProperty;
import org.rookit.auto.javax.property.PropertyTypeResolver;
import org.rookit.utils.repetition.Repetition;

import javax.lang.model.type.TypeMirror;

public final class MetatypeJavaPoetPropertyNamingFactory implements JavaPoetPropertyNamingFactory {

    public static JavaPoetPropertyNamingFactory create(final PropertyTypeResolver propertyTypeResolver,
                                                       final ElementUtils utils,
                                                       final TypeVariableName variableName,
                                                       final JavaPoetNamingFactory namingFactory,
                                                       final boolean varEntity) {
        return new MetatypeJavaPoetPropertyNamingFactory(namingFactory, propertyTypeResolver,
                utils, variableName, varEntity);
    }

    private final JavaPoetNamingFactory namingFactory;
    private final PropertyTypeResolver propertyTypeResolver;
    private final ElementUtils utils;
    private final TypeVariableName variableName;
    private final boolean varEntity;

    @Inject
    private MetatypeJavaPoetPropertyNamingFactory(final JavaPoetNamingFactory namingFactory,
                                                  final PropertyTypeResolver propertyTypeResolver,
                                                  final ElementUtils utils,
                                                  final TypeVariableName variableName,
                                                  final boolean varEntity) {
        this.namingFactory = namingFactory;
        this.propertyTypeResolver = propertyTypeResolver;
        this.utils = utils;
        this.variableName = variableName;
        this.varEntity = varEntity;
    }

    @Override
    public TypeName typeNameFor(final ExtendedTypeElement owner, final ExtendedProperty property) {
        final TypeName typeName = getPropertiesParam(owner);
        if (property.isContainer()) {
            final ClassName baseType = createMetaType(property);
            return ParameterizedTypeName.get(baseType, typeName);
        }
        final TypeMirror type = property.type().boxIfPrimitive();
        final TypeName propType = TypeName.get(type);
        final ClassName className = ClassName.get(this.propertyTypeResolver.resolve(property));
        final Repetition repetition = property.repetition();
        if (repetition instanceof JavaxKeyedRepetition) {
            final ExtendedTypeMirror keyType = ((JavaxKeyedRepetition) repetition).unwrapKey(property.type());
            // TODO this has to be changed, as it is boxing stuff
            final TypeName keyClass = TypeName.get(keyType.boxIfPrimitive());
            return ParameterizedTypeName.get(className, keyClass, propType);
        }
        return ParameterizedTypeName.get(className, typeName, propType);
    }

    private TypeName getPropertiesParam(final ExtendedTypeElement element) {
        if (!element.isPropertyContainer() && (this.varEntity || !element.isEntity())) {
            return this.variableName;
        }
        return TypeName.get(element.asType());
    }

    private ClassName createMetaType(final ExtendedProperty property) {
        return property.typeAsElement()
                .map(this.namingFactory::classNameFor)
                .orElseThrow(() ->
                        new RuntimeException("Cannot build className for primitive type: " + property.type()));
    }

    @Override
    public String toString() {
        return "MetatypeJavaPoetPropertyNamingFactory{" +
                "namingFactory=" + this.namingFactory +
                ", propertyTypeResolver=" + this.propertyTypeResolver +
                ", utils=" + this.utils +
                ", variableName=" + this.variableName +
                ", varEntity=" + this.varEntity +
                "}";
    }
}
