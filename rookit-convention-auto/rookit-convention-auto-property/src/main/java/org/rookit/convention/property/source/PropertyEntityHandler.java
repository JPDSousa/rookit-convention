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
package org.rookit.convention.property.source;

import com.google.inject.Inject;
import org.rookit.auto.AbstractConfigAwareEntityHandler;
import org.rookit.auto.config.ProcessorConfig;
import org.rookit.auto.entity.PropertyEntityFactory;
import org.rookit.auto.javax.element.ExtendedTypeElementFactory;
import org.rookit.auto.javax.property.ExtendedProperty;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.TypeElement;
import java.io.IOException;

final class PropertyEntityHandler extends AbstractConfigAwareEntityHandler {

    private final PropertyEntityFactory propertyEntityFactory;
    private final ExtendedTypeElementFactory elementFactory;
    private final Filer filer;

    @Inject
    private PropertyEntityHandler(final ProcessorConfig config,
                                  final Messager messager,
                                  final PropertyEntityFactory propEntityFactory,
                                  final ExtendedTypeElementFactory elementFactory,
                                  final Filer filer) {
        super(config, messager);
        this.propertyEntityFactory = propEntityFactory;
        this.elementFactory = elementFactory;
        this.filer = filer;
    }

    @Override
    protected void doProcessEntity(final TypeElement element) {
        try {
            for (final ExtendedProperty property : this.elementFactory.extend(element).properties()) {
                if (property.isContainer()) {
                    this.propertyEntityFactory.create(property)
                            .writeTo(this.filer);
                }
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void postProcess() {

    }

    @Override
    public String toString() {
        return "PropertyEntityHandler{" +
                "propertyEntityFactory=" + this.propertyEntityFactory +
                ", elementFactory=" + this.elementFactory +
                ", filer=" + this.filer +
                "} " + super.toString();
    }
}
