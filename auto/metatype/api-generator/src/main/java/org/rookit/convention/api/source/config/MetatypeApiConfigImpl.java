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
package org.rookit.convention.api.source.config;

import com.squareup.javapoet.TypeVariableName;
import org.rookit.auto.javax.pack.ExtendedPackageElement;
import org.rookit.utils.object.DynamicObject;
import org.rookit.convention.auto.config.ConventionConfig;
import org.rookit.convention.auto.config.MetatypeApiConfig;
import org.rookit.utils.string.template.Template1;
import org.rookit.utils.string.template.TemplateFactory;

final class MetatypeApiConfigImpl implements MetatypeApiConfig {

    private final DynamicObject configuration;
    private final String name;
    private final ConventionConfig parent;
    private final TemplateFactory templateFactory;

    MetatypeApiConfigImpl(final DynamicObject configuration,
                          final String name,
                          final ConventionConfig parent,
                          final TemplateFactory templateFactory) {
        this.configuration = configuration;
        this.name = name;
        this.parent = parent;
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
    public Template1 partialEntityTemplate() {
        return this.templateFactory.template1(this.configuration.getString("partialEntityTemplate"));
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
        return "MetatypeApiConfigImpl{" +
                "configuration=" + this.configuration +
                ", name='" + this.name + '\'' +
                ", parent=" + this.parent +
                ", templateFactory=" + this.templateFactory +
                "}";
    }
}
