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
package org.rookit.convention.meta.source.metatype;

import com.google.inject.Inject;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeVariableName;
import one.util.streamex.StreamEx;
import org.rookit.auto.javapoet.method.EntityMethodFactory;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.auto.source.spec.SpecFactory;
import org.rookit.convention.guice.MetaType;
import org.rookit.utils.guice.Separator;

final class MetaTypeEntityMethodFactory implements EntityMethodFactory {

    private final TypeVariableName variableName;

    @Inject
    private MetaTypeEntityMethodFactory(
            @MetaType(includeAnnotations = true) final SpecFactory<ParameterSpec> annParamFactory,
            @MetaType final TypeVariableName variableName,
            @Separator final String separator) {
        this.variableName = variableName;
    }

    @Override
    public StreamEx<MethodSpec> create(final ExtendedTypeElement element) {
        return StreamEx.of(
//                createFactoryMethod(element),
//                createConstructor(element),
//                createModelType(element),
//                createModelSerializer(element)
        );
    }

//    private MethodSpec createModelSerializer(final TypeElement element) {
//        // TODO this can be an injected field
//        // TODO the propertyName method className should not be hardcoded here
//        return MethodSpec.methodBuilder("modelSerializer")
//                .addModifiers(PUBLIC)
//                .addAnnotation(Override.class)
//                .returns(ParameterizedTypeName.get(ClassName.get(Serializer.class), ClassName.get(element)))
//                .addStatement("return this.$L.modelSerializer()", this.metatypeVarName)
//                .build();
//    }

//    private Collection<ParameterSpec> entityParameters(final ExtendedTypeElement element) {
//        final TypeName param = element.isPartialEntity() ? this.variableName : ClassName.get(element);
//        final ParameterizedTypeName metatypeType = ParameterizedTypeName.get(this.metatypeName, param);
//
//        return ImmutableSet.of(
//                ParameterSpec.builder(metatypeType, this.metatypeVarName, FINAL).build()
//        );
//    }

}
