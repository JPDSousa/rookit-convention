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

import com.google.inject.Inject;
import org.rookit.auto.config.ProcessorConfig;
import org.rookit.convention.utils.config.ConventionApiConfig;
import org.rookit.convention.utils.config.ConventionPropertyConfig;
import org.rookit.convention.utils.config.GuiceConfig;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

final class ProcessorConfigImpl implements ProcessorConfig {

    private final ConventionPropertyConfig propConfig;
    private final GuiceConfig guiceConfig;
    private final ConventionApiConfig apiConfig;
    private final Messager messager;

    @Inject
    private ProcessorConfigImpl(final ConventionPropertyConfig propConfig,
                                final GuiceConfig guiceConfig,
                                final ConventionApiConfig apiConfig,
                                final Messager messager) {
        this.propConfig = propConfig;
        this.guiceConfig = guiceConfig;
        this.apiConfig = apiConfig;
        this.messager = messager;
    }


    @Override
    public boolean isEnabled() {
        final boolean propEnabled = this.propConfig.isEnabled();
        final boolean guiceEnabled = this.guiceConfig.isEnabled();
        final boolean apiEnabled = this.apiConfig.isEnabled();

        if (propEnabled && !guiceEnabled) {
            this.messager.printMessage(Diagnostic.Kind.WARNING, "Considering convention property propcessor as " +
                    "disabled, since guice processor is disabled.");
        } else if (propEnabled && !apiEnabled) {
            this.messager.printMessage(Diagnostic.Kind.WARNING, "Considering convention property propcessor as " +
                    "disabled, since convention api processor is disabled.");
        }

        return apiEnabled && propEnabled && guiceEnabled;
    }

    @Override
    public String toString() {
        return "ProcessorConfigImpl{" +
                "propConfig=" + this.propConfig +
                ", guiceConfig=" + this.guiceConfig +
                ", apiConfig=" + this.apiConfig +
                ", messager=" + this.messager +
                "}";
    }
}
