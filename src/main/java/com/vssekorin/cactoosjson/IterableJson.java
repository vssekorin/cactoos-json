/**
 * MIT License
 *
 * Copyright (c) 2018 Vseslav Sekorin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.vssekorin.cactoosjson;

import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.Iterator;
import org.cactoos.io.InputStreamOf;

/**
 * Json for Iterable field.
 *
 * @author Vseslav Sekorin (vssekorin@gmail.com)
 * @version $Id$
 * @since 0.1
 */
@SuppressWarnings(
    {
        "PMD.CallSuperInConstructor",
        "PMD.ConstructorOnlyInitializesOrCallOtherConstructors"
    }
)
public final class IterableJson extends JsonEnvelope {

    /**
     * Ctor.
     * @param obj Iterable value
     */
    @SuppressWarnings("unchecked")
    public IterableJson(final Object obj) {
        this((Iterable<Object>) obj);
    }

    /**
     * Ctor.
     * @param iterable Iterable value
     */
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    public IterableJson(final Iterable<Object> iterable) {
        super(() -> () -> {
            InputStream stream = new InputStreamOf("");
            final Iterator<Object> iterator = iterable.iterator();
            while (iterator.hasNext()) {
                stream = new SequenceInputStream(
                    stream,
                    new ValueJson(iterator.next()).stream()
                );
                if (iterator.hasNext()) {
                    stream = new SequenceInputStream(
                        stream,
                        new InputStreamOf(", ")
                    );
                }
            }
            return new SequenceInputStream(
                new InputStreamOf("["),
                new SequenceInputStream(
                    stream,
                    new InputStreamOf("]")
                )
            );
        });
    }
}
