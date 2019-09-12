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
package org.rookit.convention.property.source.config;

import com.squareup.javapoet.TypeVariableName;
import org.rookit.auto.javax.pack.ExtendedPackageElement;
import org.rookit.utils.object.DynamicObject;
import org.rookit.convention.auto.config.ConventionConfig;
import org.rookit.convention.auto.config.PropertyConfig;
import org.rookit.convention.auto.config.NamingConfig;
import org.rookit.utils.string.template.Template1;
import org.rookit.utils.string.template.TemplateFactory;

final class PropertyConfigImpl implements PropertyConfig {

    private final DynamicObject configuration;
    private final ConventionConfig parent;
    private final String name;
    private final TemplateFactory templateFactory;

    PropertyConfigImpl(final DynamicObject configuration,
                       final ConventionConfig parent,
                       final String name,
                       final TemplateFactory templateFactory) {
        this.configuration = configuration;
        this.parent = parent;
        this.name = name;
        this.templateFactory = templateFactory;
    }

    @Override
    public ExtendedPackageElement basePackage() {
        return this.parent.basePackage();
    }

    @Override
    public TypeVariableName parameterName() {
        return TypeVariableName.get(this.configuration.getString("parameterName"));
    }

    @Override
    public Template1 entityTemplate() {
        return this.templateFactory.template1(this.configuration.getString("entityTemplate"));
    }

    @Override
    public NamingConfig naming() {
        return new NamingConfigImpl(this.configuration.getDynamicObject("naming"));
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public boolean isEnabled() {
        return this.configuration.getBoolean("enabled");
    }

    @Override
    public String toString() {
        return "PropertyConfigImpl{" +
                "configuration=" + this.configuration +
                ", parent=" + this.parent +
                ", name='" + this.name + '\'' +
                ", templateFactory=" + this.templateFactory +
                "}";
    }
}
