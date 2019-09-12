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

import com.google.inject.Inject;
import org.rookit.auto.javax.ExtendedElement;
import org.rookit.auto.javax.ExtendedElementFactory;
import org.rookit.auto.javax.executable.ExtendedExecutableElement;
import org.rookit.auto.javax.pack.ExtendedPackageElement;
import org.rookit.auto.javax.parameter.ExtendedTypeParameterElement;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.auto.javax.type.ExtendedTypeMirrorFactory;
import org.rookit.auto.javax.variable.ExtendedVariableElement;
import org.rookit.convention.auto.property.ExtendedPropertyExtractor;
import org.rookit.convention.auto.property.Property;
import org.rookit.utils.optional.OptionalFactory;
import org.rookit.utils.primitive.VoidUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import java.util.Collection;

final class ConventionTypeElementFactoryImpl implements ConventionTypeElementFactory {

    private final ExtendedElementFactory delegate;
    private final OptionalFactory optionalFactory;
    private final ConventionElementUtils utils;
    private final ExtendedTypeMirrorFactory mirrorFactory;
    private final ExtendedPropertyExtractor extractor;
    private final VoidUtils voidUtils;

    @Inject
    private ConventionTypeElementFactoryImpl(final ExtendedElementFactory delegate,
                                             final OptionalFactory optionalFactory,
                                             final ConventionElementUtils utils,
                                             final ExtendedTypeMirrorFactory mirrorFactory,
                                             final ExtendedPropertyExtractor extractor,
                                             final VoidUtils voidUtils) {
        this.delegate = delegate;
        this.optionalFactory = optionalFactory;
        this.utils = utils;
        this.mirrorFactory = mirrorFactory;
        this.extractor = extractor;
        this.voidUtils = voidUtils;
    }

    @Override
    public ExtendedElement extend(final Element element) {
        return this.delegate.extend(element);
    }

    @Override
    public ExtendedElement extendAsSubType(final Element element) {
        return element.accept(new ElementVisitor<ExtendedElement, Void>() {
            @Override
            public ExtendedElement visit(final Element e, final Void aVoid) {
                return extend(e);
            }

            @Override
            public ExtendedElement visit(final Element e) {
                return extend(e);
            }

            @Override
            public ExtendedElement visitPackage(final PackageElement e, final Void aVoid) {
                return extendPackage(e);
            }

            @Override
            public ExtendedElement visitType(final TypeElement e, final Void aVoid) {
                return extendType(e);
            }

            @Override
            public ExtendedElement visitVariable(final VariableElement e, final Void aVoid) {
                return extendVariable(e);
            }

            @Override
            public ExtendedElement visitExecutable(final ExecutableElement e, final Void aVoid) {
                return extendExecutable(e);
            }

            @Override
            public ExtendedElement visitTypeParameter(final TypeParameterElement e, final Void aVoid) {
                return extendParameter(e);
            }

            @Override
            public ExtendedElement visitUnknown(final Element e, final Void aVoid) {
                return extend(e);
            }
        }, this.voidUtils.returnVoid());
    }

    @Override
    public ConventionTypeElement extendType(final TypeElement baseElement) {
        final ExtendedTypeElement extended = this.delegate.extendType(baseElement);
        return fromExtendedTypeElement(extended);
    }

    @Override
    public ExtendedExecutableElement extendExecutable(final ExecutableElement element) {
        return this.delegate.extendExecutable(element);
    }

    @Override
    public ExtendedTypeParameterElement extendParameter(final TypeParameterElement element) {
        return this.delegate.extendParameter(element);
    }

    @Override
    public ExtendedVariableElement extendVariable(final VariableElement element) {
        return this.delegate.extendVariable(element);
    }

    @Override
    public ExtendedPackageElement extendPackage(final PackageElement packageElement) {
        return this.delegate.extendPackage(packageElement);
    }

    private ConventionTypeElement fromExtendedTypeElement(final ExtendedTypeElement extended) {
        final Collection<Property> properties = this.extractor.fromTypeAsCollection(extended);

        return new ConventionTypeElementImpl(
                extended,
                this.optionalFactory,
                this.utils,
                properties,
                this.mirrorFactory,
                this);
    }

    @Override
    public ConventionTypeElement changeElementProperties(final ConventionTypeElement original,
                                                         final Collection<Property> newProperties) {
        return new PropertyOverridingExtendedTypeElement(original, newProperties);
    }

    @Override
    public String toString() {
        return "ConventionTypeElementFactoryImpl{" +
                "delegate=" + this.delegate +
                ", optionalFactory=" + this.optionalFactory +
                ", utils=" + this.utils +
                ", mirrorFactory=" + this.mirrorFactory +
                ", extractor=" + this.extractor +
                ", voidUtils=" + this.voidUtils +
                "}";
    }
}
