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
package org.rookit.convention.property.source.javapoet;

import com.google.inject.Inject;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import one.util.streamex.StreamEx;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.auto.javax.visitor.StreamExtendedElementVisitor;
import org.rookit.convention.auto.config.NamingConfig;
import org.rookit.convention.guice.MetaTypeModelSerializer;
import org.rookit.serialization.Serializer;

import javax.lang.model.element.ExecutableElement;

import static javax.lang.model.element.Modifier.PUBLIC;

final class ModelSerializerJavaPoetVisitor<P> implements StreamExtendedElementVisitor<MethodSpec, P> {

    private final NamingConfig namingConfig;
    private final ExecutableElement serializerMethod;

    @Inject
    private ModelSerializerJavaPoetVisitor(final NamingConfig namingConfig,
                                           @MetaTypeModelSerializer final ExecutableElement serializerMethod) {
        this.namingConfig = namingConfig;
        this.serializerMethod = serializerMethod;
    }

    @Override
    public StreamEx<MethodSpec> visitType(final ExtendedTypeElement extendedType, final P parameter) {
        return StreamEx.of(MethodSpec.methodBuilder(this.serializerMethod.getSimpleName().toString())
                .addModifiers(PUBLIC)
                .addAnnotation(Override.class)
                .returns(ParameterizedTypeName.get(ClassName.get(Serializer.class), ClassName.get(extendedType)))
                .addStatement("return this.$L", this.namingConfig.serializer())
                .build());
    }

    @Override
    public String toString() {
        return "ModelSerializerJavaPoetVisitor{" +
                "namingConfig=" + this.namingConfig +
                ", serializerMethod=" + this.serializerMethod +
                "}";
    }
}
