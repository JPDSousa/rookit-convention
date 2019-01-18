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
import org.rookit.auto.naming.PackageReference;
import org.rookit.auto.naming.PackageReferenceFactory;
import org.rookit.config.Configuration;
import org.rookit.convention.utils.config.ConventionPropertyConfig;

final class ConventionPropertyConfigImpl implements ConventionPropertyConfig {

    private final Configuration configuration;
    private final PackageReferenceFactory packageFactory;

    ConventionPropertyConfigImpl(final Configuration configuration, final PackageReferenceFactory packageFactory) {
        this.configuration = configuration;
        this.packageFactory = packageFactory;
    }

    @Override
    public PackageReference basePackage() {
        return this.packageFactory.create(this.configuration.getString("basePackage"));
    }

    @Override
    public TypeVariableName parameterName() {
        return TypeVariableName.get(this.configuration.getString("parameterName"));
    }

    @Override
    public String entitySuffix() {
        return this.configuration.getString("entitySuffix");
    }

    @Override
    public boolean isEnabled() {
        return this.configuration.getBoolean("enabled");
    }

    @Override
    public String toString() {
        return "ConventionPropertyConfigImpl{" +
                "configuration=" + this.configuration +
                ", packageFactory=" + this.packageFactory +
                "}";
    }
}