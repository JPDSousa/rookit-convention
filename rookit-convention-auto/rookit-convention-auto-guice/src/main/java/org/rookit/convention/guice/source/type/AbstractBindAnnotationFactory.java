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
package org.rookit.convention.guice.source.type;

import com.google.inject.BindingAnnotation;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.TypeSpec;
import org.rookit.auto.identifier.Identifier;
import org.rookit.auto.javapoet.type.TypeSourceAdapter;
import org.rookit.auto.source.TypeSource;

import javax.lang.model.element.Modifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

class AbstractBindAnnotationFactory {

    private final TypeSourceAdapter adapter;

    private final AnnotationSpec retention;
    private final AnnotationSpec bindingAnnotation;
    private final AnnotationSpec target;

    AbstractBindAnnotationFactory(final TypeSourceAdapter adapter) {
        this.adapter = adapter;
        this.retention = AnnotationSpec.builder(Retention.class)
                .addMember("value", "$T.$L", RetentionPolicy.class, RUNTIME)
                .build();
        this.bindingAnnotation = AnnotationSpec.builder(BindingAnnotation.class)
                .build();
        this.target = AnnotationSpec.builder(Target.class)
                .addMember("value", "{$T.$L, $T.$L, $T.$L}",
                        ElementType.class, FIELD,
                        ElementType.class, METHOD,
                        ElementType.class, PARAMETER)
                .build();
    }

    TypeSource create(final Identifier identifier, final String annotationName) {
        final TypeSpec spec = TypeSpec.annotationBuilder(annotationName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(this.retention)
                .addAnnotation(this.bindingAnnotation)
                .addAnnotation(this.target)
                .build();

        return this.adapter.fromTypeSpec(identifier, spec);
    }

    @Override
    public String toString() {
        return "AbstractBindAnnotationFactory{" +
                "adapter=" + this.adapter +
                ", retention=" + this.retention +
                ", bindingAnnotation=" + this.bindingAnnotation +
                ", target=" + this.target +
                "}";
    }
}
