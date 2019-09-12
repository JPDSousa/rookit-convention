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
package org.rookit.convention.module.source.javapoet;

import com.google.inject.Inject;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import org.rookit.auto.javapoet.method.MethodSpecFactory;
import org.rookit.utils.string.template.Template1;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;
import java.util.Arrays;

import static java.lang.String.format;
import static javax.lang.model.element.Modifier.PROTECTED;

final class ModuleConfigureMethodSpecFactory implements MethodSpecFactory {

    private final Messager messager;

    @Inject
    private ModuleConfigureMethodSpecFactory(final Messager messager) {
        this.messager = messager;
    }


    @Override
    public MethodSpec create(final String propertyName, final ParameterSpec... parameters) {
        final String warnMessage = format("Guice Module's configure method does not support any configurations. " +
                        "Thus, the provided parameters ('%s', '%s') will be ignored.)", propertyName,
                Arrays.toString(parameters));
        this.messager.printMessage(Diagnostic.Kind.WARNING, warnMessage);
        return create();
    }

    @Override
    public MethodSpec create(final String propertyName, final Template1 template, final ParameterSpec... parameters) {
        final String warnMessage = format("Guice Module's configure method does not support any configurations. " +
                        "Thus, the provided parameters ('%s', '%s', '%s') will be ignored.)", propertyName,
                template, Arrays.toString(parameters));
        this.messager.printMessage(Diagnostic.Kind.WARNING, warnMessage);
        return create();
    }

    @Override
    public MethodSpec create() {
        final MethodSpec.Builder configureBuilder = MethodSpec.methodBuilder("configure")
                .addAnnotation(Override.class)
                .addModifiers(PROTECTED);

        return configureBuilder.build();
    }

    @Override
    public String toString() {
        return "ModuleConfigureMethodSpecFactory{" +
                "messager=" + this.messager +
                "}";
    }
}
