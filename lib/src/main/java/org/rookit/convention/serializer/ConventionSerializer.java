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
package org.rookit.convention.serializer;

import org.apache.commons.lang3.SerializationException;
import org.rookit.convention.ConventionUtils;
import org.rookit.convention.MetaType;
import org.rookit.convention.instance.InstanceBuilder;
import org.rookit.convention.instance.InstanceBuilderFactory;
import org.rookit.convention.property.ImmutableCollectionPropertyModel;
import org.rookit.convention.property.ImmutableOptionalPropertyModel;
import org.rookit.convention.property.ImmutablePropertyModel;
import org.rookit.convention.property.PropertyModel;
import org.rookit.serialization.Serializer;
import org.rookit.serialization.TypeReader;
import org.rookit.serialization.TypeWriter;
import org.rookit.utils.optional.OptionalFactory;

import java.util.Optional;


public final class ConventionSerializer<T> implements Serializer<T> {

    public static <S> Serializer<S> create(final MetaType<S> metaType,
                                           final InstanceBuilderFactory<S> instanceFactory,
                                           final ConventionUtils conventionUtils,
                                           final OptionalFactory optionalFactory) {
        return new ConventionSerializer<>(metaType, instanceFactory, conventionUtils, optionalFactory);
    }

    private final MetaType<T> properties;
    private final InstanceBuilderFactory<T> instanceBuilderFactory;
    private final ConventionUtils conventionUtils;
    private final OptionalFactory optionalFactory;

    private ConventionSerializer(final MetaType<T> properties,
                                 final InstanceBuilderFactory<T> instanceFactory,
                                 final ConventionUtils conventionUtils,
                                 final OptionalFactory optionalFactory) {
        this.properties = properties;
        this.instanceBuilderFactory = instanceFactory;
        this.conventionUtils = conventionUtils;
        this.optionalFactory = optionalFactory;
    }

    @Override
    public void write(final TypeWriter writer, final T value) {
        writer.startObject();
        for (final PropertyModel<?> property : this.properties.properties()) {
            writer.name(property.propertyName());
            // TODO tech debt incoming!!!!
            if (property instanceof ImmutablePropertyModel) {
                this.conventionUtils.write(writer, (ImmutablePropertyModel<T, ?>) property, value);
            }
            else if (property instanceof ImmutableOptionalPropertyModel) {
                this.conventionUtils.writeOptional(writer, (ImmutableOptionalPropertyModel<T, ?>) property, value);
            }
            else if (property instanceof ImmutableCollectionPropertyModel) {
                this.conventionUtils.writeCollection(writer, (ImmutableCollectionPropertyModel<T, ?>) property, value);
            }
            else {
                final String errorMessage = String.format("Unknown property type while deserializing %s: %s",
                        this.properties.modelType(), property);
                throw new IllegalStateException(errorMessage);
            }
        }
        writer.endObject();
    }

    @Override
    public T read(final TypeReader reader) {
        InstanceBuilder<T> builder = this.instanceBuilderFactory.create();

        reader.startDocument();
        builder = readProperties(reader, builder);
        reader.endDocument();

        if (!builder.isReady()) {
            // TODO maybe provide more information here on what the actual data is.
            throw new SerializationException("Source object has not enough data to build a consistent genre.");
        }
        return builder.getInstance();
    }

    @Override
    public org.rookit.utils.optional.Optional<T> readOptional(final TypeReader reader) {
        return reader.hasNext() ? this.optionalFactory.of(read(reader)) : this.optionalFactory.empty();
    }

    private InstanceBuilder<T> readProperties(final TypeReader reader, final InstanceBuilder<T> builder) {
        InstanceBuilder<T> builderCopy = builder;
        while(reader.hasNext()) {
            final String propertyName = reader.name();
            final Optional<PropertyModel<?>> propertyOrNone = this.properties.property(propertyName);

            if (propertyOrNone.isPresent()) {
                final PropertyModel<?> property = propertyOrNone.get();
                // TODO tech debt incoming!!!!
                if (property instanceof ImmutablePropertyModel) {
                    builderCopy = this.conventionUtils.read(reader, builder, (ImmutablePropertyModel<T, ?>) property);
                }
                else if (property instanceof ImmutableOptionalPropertyModel) {
                    builderCopy = this.conventionUtils.readOptional(reader, builder,
                            (ImmutableOptionalPropertyModel<T, ?>) property);
                }
                else if (property instanceof ImmutableCollectionPropertyModel) {
                    builderCopy = this.conventionUtils.readCollection(reader, builder,
                            (ImmutableCollectionPropertyModel<T, ?>) property);
                }
            }
            else {
                // TODO uncomment and handle log.
                // logger.trace("Cannot find property with name {}. Ignoring.", propertyName);
            }
        }
        return builderCopy;
    }

    @Override
    public Class<T> serializationClass() {
        return this.properties.modelType();
    }

    @Override
    public String toString() {
        return "ConventionSerializer{" +
                "properties=" + this.properties +
                ", instanceBuilderFactory=" + this.instanceBuilderFactory +
                ", conventionUtils=" + this.conventionUtils +
                ", optionalFactory=" + this.optionalFactory +
                "}";
    }
}
