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
package org.rookit.convention.auto.javax;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import org.rookit.auto.javax.ElementUtils;
import org.rookit.convention.annotation.LaConvention;

import javax.lang.model.AnnotatedConstruct;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

final class ConventionElementUtilsImpl implements ConventionElementUtils {

    private final ElementUtils delegate;
    private final Collection<Class<? extends Annotation>> annotations;

    @SuppressWarnings("TypeMayBeWeakened") // due to guice
    @Inject
    private ConventionElementUtilsImpl(final ElementUtils delegate,
                             @LaConvention final Set<Class<? extends Annotation>> annotations) {
        this.delegate = delegate;
        this.annotations = ImmutableSet.copyOf(annotations);
    }

    @Override
    public boolean isConventionElement(final AnnotatedConstruct element) {
        for (final Class<? extends Annotation> annotation : this.annotations) {
            if (Objects.nonNull(element.getAnnotation(annotation))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public <T extends TypeMirror> Collection<T> intersection(final Collection<T> typesA, final Collection<T> typesB) {
        return this.delegate.intersection(typesA, typesB);
    }

    @Override
    public TypeMirror fromClassErasured(final Class<?> clazz) {
        return this.delegate.fromClassErasured(clazz);
    }

    @Override
    public String toString() {
        return "ConventionElementUtilsImpl{" +
                "delegate=" + this.delegate +
                ", annotations=" + this.annotations +
                "}";
    }
}
