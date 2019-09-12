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
import org.rookit.auto.AbstractConfigAwareTypeProcessor;
import org.rookit.auto.config.ProcessorConfig;
import org.rookit.convention.auto.entity.PropertyEntityFactory;
import org.rookit.convention.auto.javax.ConventionTypeElementFactory;
import org.rookit.convention.auto.property.PropertyFactory;
import org.rookit.utils.optional.Optional;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.TypeElement;

final class PropertyTypeProcessor extends AbstractConfigAwareTypeProcessor {

    private final PropertyEntityFactory propertyEntityFactory;
    private final ConventionTypeElementFactory elementFactory;
    private final Filer filer;
    private final PropertyFactory propertyFactory;

    @Inject
    private PropertyTypeProcessor(final ProcessorConfig config,
                                  final Messager messager,
                                  final PropertyEntityFactory propEntityFactory,
                                  final ConventionTypeElementFactory elementFactory,
                                  final Filer filer,
                                  final PropertyFactory propertyFactory) {
        super(config, messager);
        this.propertyEntityFactory = propEntityFactory;
        this.elementFactory = elementFactory;
        this.filer = filer;
        this.propertyFactory = propertyFactory;
    }

    @Override
    protected void doProcessEntity(final TypeElement element) {
        this.elementFactory.extendType(element).properties().stream()
                .map(this.propertyFactory::toContainer)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(this.propertyEntityFactory::create)
                .forEach(entity -> entity.writeTo(this.filer));
    }

    @Override
    public void postProcess() {
        // nothing to do
    }

    @Override
    public String toString() {
        return "PropertyTypeProcessor{" +
                "propertyEntityFactory=" + this.propertyEntityFactory +
                ", elementFactory=" + this.elementFactory +
                ", filer=" + this.filer +
                ", propertyFactory=" + this.propertyFactory +
                "} " + super.toString();
    }
}
