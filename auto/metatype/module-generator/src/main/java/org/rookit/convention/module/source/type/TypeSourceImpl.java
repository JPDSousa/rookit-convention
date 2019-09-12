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
package org.rookit.convention.module.source.type;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.rookit.auto.javax.naming.Identifier;
import org.rookit.auto.javapoet.type.AbstractJavaPoetTypeSource;

import java.util.Collection;
import java.util.concurrent.Executor;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;

final class TypeSourceImpl extends AbstractJavaPoetTypeSource {

    private final Identifier identifier;
    // TODO ideally we would have a Keyed<Signature, MethodSpec>, so that it would be easy to overwrite previous
    // TODO versions of a given method.
    private final Collection<MethodSpec> methods;
    private final Collection<FieldSpec> fields;

    TypeSourceImpl(final Executor executor,
                   final Identifier identifier,
                   final Iterable<MethodSpec> methods,
                   final Iterable<FieldSpec> fields) {
        super(executor);
        this.identifier = identifier;
        this.methods = Lists.newArrayList(methods);
        this.fields = Lists.newArrayList(fields);
    }

    @Override
    protected TypeSpec typeSpec() {
        final ClassName className = ClassName.get(this.identifier.packageElement().fullName().asString(),
                this.identifier.name());

        return TypeSpec.classBuilder(className)
                .addModifiers(PUBLIC, FINAL)
                .superclass(AbstractModule.class)
                .addMethods(this.methods)
                .addFields(this.fields)
                .build();
    }

    @Override
    public Identifier identifier() {
        return this.identifier;
    }

    @Override
    public void addMethod(final MethodSpec method) {
        this.methods.add(method);
    }

    @Override
    public void addField(final FieldSpec field) {
        this.fields.add(field);
    }

    @Override
    public String toString() {
        return "TypeSourceImpl{" +
                "identifier=" + this.identifier +
                ", methods=" + this.methods +
                ", fields=" + this.fields +
                "} " + super.toString();
    }
}
