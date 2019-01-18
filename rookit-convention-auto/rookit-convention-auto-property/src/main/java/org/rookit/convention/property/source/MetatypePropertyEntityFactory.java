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
package org.rookit.convention.property.source;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.rookit.auto.entity.Entity;
import org.rookit.auto.entity.EntityFactory;
import org.rookit.auto.entity.PropertyEntityFactory;
import org.rookit.auto.javax.property.ExtendedProperty;

final class MetatypePropertyEntityFactory implements PropertyEntityFactory {

    // TODO not cool
    private final Provider<EntityFactory> entityFactory;

    @Inject
    private MetatypePropertyEntityFactory(final Provider<EntityFactory> entityFactory) {
        this.entityFactory = entityFactory;
    }

    @Override
    public Entity create(final ExtendedProperty property) {
        return property.typeAsElement()
                .map(this.entityFactory.get()::create)
                .orElseThrow(() -> new RuntimeException("Cannot create an entity for a non property container."));
    }

    @Override
    public String toString() {
        return "MetatypePropertyEntityFactory{" +
                "entityFactory=" + this.entityFactory +
                "}";
    }
}
