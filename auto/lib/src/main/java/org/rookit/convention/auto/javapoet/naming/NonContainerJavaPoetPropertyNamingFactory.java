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
package org.rookit.convention.auto.javapoet.naming;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import org.rookit.auto.javax.repetition.KeyedRepetitiveTypeMirror;
import org.rookit.auto.javax.repetition.RepetitiveTypeMirror;
import org.rookit.auto.javax.type.ExtendedTypeMirror;
import org.rookit.convention.auto.javax.ConventionTypeElement;
import org.rookit.convention.auto.property.PropertyTypeResolver;
import org.rookit.convention.auto.property.Property;

import javax.lang.model.type.TypeMirror;
import java.util.function.Function;

final class NonContainerJavaPoetPropertyNamingFactory implements JavaPoetPropertyNamingFactory {

    private final PropertyTypeResolver propertyTypeResolver;
    private final Function<ConventionTypeElement, TypeName> paramFunction;

    NonContainerJavaPoetPropertyNamingFactory(
            final PropertyTypeResolver propertyTypeResolver,
            final Function<ConventionTypeElement, TypeName> paramFunction) {
        this.propertyTypeResolver = propertyTypeResolver;
        this.paramFunction = paramFunction;
    }

    @Override
    public TypeName typeNameFor(final ConventionTypeElement owner, final Property property) {
        final TypeName typeName = this.paramFunction.apply(owner);
        final TypeMirror type = property.type().boxIfPrimitive();
        final TypeName propType = unwrapIfRepetitive(type);
        final ClassName className = ClassName.get(this.propertyTypeResolver.resolve(property));

        if (type instanceof KeyedRepetitiveTypeMirror) {
            final ExtendedTypeMirror keyType = ((KeyedRepetitiveTypeMirror) type).unwrapKey();
            // TODO this has to be changed, as it is boxing stuff
            final TypeName keyClass = TypeName.get(keyType.boxIfPrimitive());
            return ParameterizedTypeName.get(className, typeName, keyClass, propType);
        }

        return ParameterizedTypeName.get(className, typeName, propType);
    }

    private TypeName unwrapIfRepetitive(final TypeMirror typeMirror) {
        if (typeMirror instanceof RepetitiveTypeMirror) {
            return TypeName.get(((RepetitiveTypeMirror) typeMirror).unwrapValue());
        }

        return TypeName.get(typeMirror);
    }

    @Override
    public String toString() {
        return "NonContainerJavaPoetPropertyNamingFactory{" +
                "propertyTypeResolver=" + this.propertyTypeResolver +
                ", paramFunction=" + this.paramFunction +
                "}";
    }

}
