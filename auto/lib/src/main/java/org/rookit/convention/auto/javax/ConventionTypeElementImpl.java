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

import com.google.common.collect.ImmutableList;
import one.util.streamex.StreamEx;
import org.rookit.auto.javax.ExtendedElement;
import org.rookit.auto.javax.executable.ExtendedExecutableElement;
import org.rookit.auto.javax.pack.ExtendedPackageElement;
import org.rookit.auto.javax.parameter.ExtendedTypeParameterElement;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.auto.javax.type.ExtendedTypeMirror;
import org.rookit.auto.javax.type.ExtendedTypeMirrorFactory;
import org.rookit.convention.annotation.Entity;
import org.rookit.convention.annotation.EntityExtension;
import org.rookit.convention.annotation.PartialEntity;
import org.rookit.convention.annotation.PropertyContainer;
import org.rookit.convention.auto.property.Property;
import org.rookit.utils.optional.Optional;
import org.rookit.utils.optional.OptionalFactory;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

final class ConventionTypeElementImpl implements ConventionTypeElement {

    private final ExtendedTypeElement delegate;
    private final OptionalFactory optionalFactory;
    private final ConventionElementUtils utils;
    private final Collection<Property> properties;
    private final ExtendedTypeMirrorFactory mirrorFactory;
    private final ConventionTypeElementFactory factory;

    ConventionTypeElementImpl(final ExtendedTypeElement delegate,
                              final OptionalFactory optionalFactory,
                              final ConventionElementUtils utils,
                              final Collection<Property> properties,
                              final ExtendedTypeMirrorFactory mirrorFactory,
                              final ConventionTypeElementFactory factory) {
        this.delegate = delegate;
        this.optionalFactory = optionalFactory;
        this.utils = utils;
        this.mirrorFactory = mirrorFactory;
        this.factory = factory;
        this.properties = ImmutableList.copyOf(properties);
    }


    @Override
    public Collection<Property> properties() {
        //noinspection AssignmentOrReturnOfFieldWithMutableType as already immutable
        return this.properties;
    }

    @Override
    public boolean isConvention() {
        return this.utils.isConventionElement(this);
    }

    @Override
    public boolean isTopLevel() {
        return !conventionInterfaces()
                .findFirst()
                .isPresent();
    }

    @Override
    public StreamEx<ConventionTypeElement> conventionInterfaces() {
        return StreamEx.of(getInterfaces())
                .map(this.mirrorFactory::create)
                .map(ExtendedTypeMirror::toElement)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .select(TypeElement.class)
                .filter(this.utils::isConventionElement)
                .map(this.factory::extendType);
    }

    @Override
    public boolean isEntity() {
        return Objects.nonNull(this.getAnnotation(Entity.class))
                || isEntityExtension();
    }

    @Override
    public boolean isPartialEntity() {
        return Objects.nonNull(getAnnotation(PartialEntity.class));
    }

    @Override
    public boolean isEntityExtension() {
        return Objects.nonNull(getAnnotation(EntityExtension.class));
    }

    @Override
    public boolean isPropertyContainer() {
        return Objects.nonNull(getAnnotation(PropertyContainer.class));
    }

    @Override
    public Optional<ConventionTypeElement> upstreamEntity() {
        // TODO All this stuff implies that we should have multiple implementation according to the type
        // TODO of entity we're dealing with.
        if (isTopLevel()) {
            return this.optionalFactory.empty();
        }
        if (isEntityExtension()) {
            return this.optionalFactory.fromJavaOptional(conventionInterfaces()
                    .map(ConventionTypeElement::upstreamEntity)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .findFirst());
        }
        if (isEntity()) {
            return this.optionalFactory.of(this);
        }

        return this.optionalFactory.empty();
    }

    @Override
    public ExtendedPackageElement packageInfo() {
        return this.delegate.packageInfo();
    }

    @Override
    public List<? extends ExtendedTypeMirror> getInterfaces() {
        return this.delegate.getInterfaces();
    }

    @Override
    public List<? extends ExtendedTypeParameterElement> getTypeParameters() {
        return this.delegate.getTypeParameters();
    }

    @Override
    public ExtendedElement getEnclosingElement() {
        return this.delegate.getEnclosingElement();
    }

    @Override
    public List<? extends ExtendedElement> getEnclosedElements() {
        return this.delegate.getEnclosedElements();
    }

    @Override
    public List<? extends AnnotationMirror> getAnnotationMirrors() {
        return this.delegate.getAnnotationMirrors();
    }

    @Override
    public <A extends Annotation> A getAnnotation(final Class<A> annotationType) {
        return this.delegate.getAnnotation(annotationType);
    }

    @Override
    public <A extends Annotation> A[] getAnnotationsByType(final Class<A> annotationType) {
        return this.delegate.getAnnotationsByType(annotationType);
    }

    @Override
    public <R, P> R accept(final ElementVisitor<R, P> v, final P p) {
        return this.delegate.accept(v, p);
    }

    @Override
    public NestingKind getNestingKind() {
        return this.delegate.getNestingKind();
    }

    @Override
    public Name getQualifiedName() {
        return this.delegate.getQualifiedName();
    }

    @Override
    public Name getSimpleName() {
        return this.delegate.getSimpleName();
    }

    @Override
    public ExtendedTypeMirror getSuperclass() {
        return this.delegate.getSuperclass();
    }

    @Override
    public ExtendedTypeMirror asType() {
        return this.delegate.asType();
    }

    @Override
    public ElementKind getKind() {
        return this.delegate.getKind();
    }

    @Override
    public Set<Modifier> getModifiers() {
        return this.delegate.getModifiers();
    }

    @Override
    public Optional<ExtendedExecutableElement> getMethod(final String name) {
        return this.delegate.getMethod(name);
    }

    @Override
    public String toString() {
        return "ConventionTypeElementImpl{" +
                "delegate=" + this.delegate +
                ", optionalFactory=" + this.optionalFactory +
                ", utils=" + this.utils +
                ", properties=" + this.properties +
                ", mirrorFactory=" + this.mirrorFactory +
                ", factory=" + this.factory +
                "}";
    }
}
