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
package org.rookit.convention.module.source;

import com.google.inject.Inject;
import org.rookit.auto.AbstractConfigAwareTypeProcessor;
import org.rookit.auto.config.ProcessorConfig;
import org.rookit.auto.javax.ExtendedElementFactory;
import org.rookit.auto.source.CodeSource;
import org.rookit.auto.source.spec.ExtendedElementAggregator;
import org.rookit.failsafe.Failsafe;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

final class ModuleTypeProcessor extends AbstractConfigAwareTypeProcessor {

    private final ExtendedElementAggregator<Collection<CodeSource>> aggregator;
    private final ExtendedElementFactory elementFactory;
    private final Filer filer;
    private final Messager messager;
    private final Failsafe failsafe;

    @Inject
    private ModuleTypeProcessor(final ProcessorConfig config,
                                final Messager messager,
                                final ExtendedElementAggregator<Collection<CodeSource>> aggregator,
                                final ExtendedElementFactory elementFactory,
                                final Filer filer,
                                final Failsafe failsafe) {
        super(config, messager);
        this.aggregator = aggregator;
        this.elementFactory = elementFactory;
        this.filer = filer;
        this.messager = messager;
        this.failsafe = failsafe;
    }

    @Override
    protected void doProcessEntity(final TypeElement element) {
        this.aggregator.accept(this.elementFactory.extendType(element));
    }


    @Override
    public void postProcess() {
        try {
            this.aggregator.writeTo(this.filer).get();
        } catch (final InterruptedException e) {
            final String errMsg = "Generation process was interrupted.";
            this.messager.printMessage(Diagnostic.Kind.ERROR, errMsg);
            Thread.currentThread().interrupt();
            throw new IllegalStateException(errMsg);
        } catch (final ExecutionException e) {
            this.failsafe.handleException().executionException(e);
        }
    }

    @Override
    public String toString() {
        return "ModuleTypeProcessor{" +
                "aggregator=" + this.aggregator +
                ", elementFactory=" + this.elementFactory +
                ", filer=" + this.filer +
                ", messager=" + this.messager +
                ", failsafe=" + this.failsafe +
                "} " + super.toString();
    }
}
