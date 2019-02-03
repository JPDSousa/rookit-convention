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
package org.rookit.convention;

import com.google.inject.Inject;
import org.rookit.convention.instance.InstanceBuilder;
import org.rookit.convention.property.ImmutableCollectionPropertyModel;
import org.rookit.convention.property.ImmutableOptionalPropertyModel;
import org.rookit.convention.property.ImmutablePropertyModel;
import org.rookit.serialization.TypeReader;
import org.rookit.serialization.TypeWriter;

import java.util.Collection;

final class ConventionUtilsImpl implements ConventionUtils {

    @Inject
    private ConventionUtilsImpl() {}

    @Override
    public <P, M> void write(final TypeWriter writer, final ImmutablePropertyModel<M, P> property, final M model) {
        final P value = property.get(model);
        property.modelSerializer().write(writer, value);
    }

    @Override
    public <P, M> void writeOptional(final TypeWriter writer,
                                     final ImmutableOptionalPropertyModel<M, P> property,
                                     final M model) {
        property.get(model).ifPresent(value -> property.modelSerializer().write(writer, value));
    }

    @Override
    public <P, M> void writeCollection(final TypeWriter writer,
                                       final ImmutableCollectionPropertyModel<M, P> property,
                                       final M model) {
        final Collection<P> values = property.get(model);
        if (!values.isEmpty()) {
            property.modelSerializer().writeCollection(writer, values);
        }
    }

    @Override
    public <P, M> InstanceBuilder<M> read(final TypeReader reader,
                                          final InstanceBuilder<M> builder,
                                          final ImmutablePropertyModel<M, P> property) {
        final P value = property.modelSerializer().read(reader);
        return builder.withProperty(property, value);
    }

    @Override
    public <P, M> InstanceBuilder<M> readOptional(final TypeReader reader,
                                                  final InstanceBuilder<M> builder,
                                                  final ImmutableOptionalPropertyModel<M, P> property) {
        return property.modelSerializer()
                .readOptional(reader)
                .map(value -> builder.withOptionalProperty(property, value))
                .orElse(builder);
    }

    @Override
    public <P, M> InstanceBuilder<M> readCollection(final TypeReader reader,
                                                    final InstanceBuilder<M> builder,
                                                    final ImmutableCollectionPropertyModel<M, P> property) {
        final Collection<P> items = property.modelSerializer().readCollection(reader);
        return builder.withCollectionProperty(property, items);
    }

}
