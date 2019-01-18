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

import org.rookit.convention.instance.InstanceBuilder;
import org.rookit.convention.property.ImmutableCollectionPropertyModel;
import org.rookit.convention.property.ImmutableOptionalPropertyModel;
import org.rookit.convention.property.ImmutablePropertyModel;
import org.rookit.serialization.TypeReader;
import org.rookit.serialization.TypeWriter;

public interface ConventionUtils {

    <P, M> void write(TypeWriter writer, ImmutablePropertyModel<M, P> property, M model);

    <P, M> void writeOptional(TypeWriter writer,
                              ImmutableOptionalPropertyModel<M, P> property,
                              M model);

    <P, M> void writeCollection(TypeWriter writer,
                                ImmutableCollectionPropertyModel<M, P> property,
                                M model);

    <P, M> InstanceBuilder<M> read(TypeReader reader,
                                   InstanceBuilder<M> builder,
                                   ImmutablePropertyModel<M, P> property);

    <P, M> InstanceBuilder<M> readOptional(TypeReader reader,
                                           InstanceBuilder<M> builder,
                                           ImmutableOptionalPropertyModel<M, P> property);

    <P, M> InstanceBuilder<M> readCollection(TypeReader reader,
                                             InstanceBuilder<M> builder,
                                             ImmutableCollectionPropertyModel<M, P> property);
}
