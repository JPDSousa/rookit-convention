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
package org.rookit.convention.meta.source.config;

import com.google.inject.Inject;
import org.rookit.auto.config.ProcessorConfig;
import org.rookit.convention.utils.config.ConventionApiConfig;
import org.rookit.convention.utils.config.ConventionMetatypeConfig;
import org.rookit.convention.utils.config.GuiceConfig;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

final class ProcessorConfigImpl implements ProcessorConfig {

    private final ConventionApiConfig apiConfig;
    private final GuiceConfig guiceConfig;
    private final ConventionMetatypeConfig metaConfig;
    private final Messager messager;

    @Inject
    private ProcessorConfigImpl(final ConventionApiConfig apiConfig,
                                final GuiceConfig guiceConfig,
                                final ConventionMetatypeConfig metaConfig,
                                final Messager messager) {
        this.apiConfig = apiConfig;
        this.guiceConfig = guiceConfig;
        this.metaConfig = metaConfig;
        this.messager = messager;
    }

    @Override
    public boolean isEnabled() {
        final boolean apiEnabled = this.apiConfig.isEnabled();
        final boolean guiceEnabled = this.guiceConfig.isEnabled();
        final boolean metaEnabled = this.metaConfig.isEnabled();

        if (metaEnabled && !apiEnabled) {
            this.messager.printMessage(Diagnostic.Kind.WARNING, "Considering metatype processor disabled, as " +
                    "metatype api processor is disabled.");
        } else if (metaEnabled && !guiceEnabled) {
            this.messager.printMessage(Diagnostic.Kind.WARNING, "Considering metatype processor disabled, as " +
                    "guice processor is disabled.");
        }

        return apiEnabled && guiceEnabled && metaEnabled;
    }

    @Override
    public String toString() {
        return "ProcessorConfigImpl{" +
                "apiConfig=" + this.apiConfig +
                ", guiceConfig=" + this.guiceConfig +
                ", metaConfig=" + this.metaConfig +
                ", messager=" + this.messager +
                "}";
    }
}
