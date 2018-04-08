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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.cactoos.io.InputStreamOf;
import org.cactoos.list.ListOf;

/**
 * Json for some fields of object.
 *
 * @author Vseslav Sekorin (vssekorin@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public final class SomeFields extends JsonEnvelope {

    /**
     * Ctor.
     * @param object Object
     * @param names Field names
     */
    public SomeFields(final Object object, final String... names) {
        this(object, new ListOf<>(names), "");
    }

    /**
     * Ctor.
     * @param object Object
     * @param names List of field names
     */
    public SomeFields(final Object object, final List<String> names) {
        this(object, names, "");
    }

    /**
     * Ctor.
     * @param object Object
     * @param names List of field names
     * @param indent Indent
     */
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    public SomeFields(final Object object, final List<String> names,
        final String indent) {
        super(() -> () -> {
            final List<InputStream> streams = new ArrayList<>(names.size());
            for (int ind = 0; ind < names.size(); ++ind) {
                final Field field =
                    object.getClass().getDeclaredField(names.get(ind));
                field.setAccessible(true);
                final Object value = field.get(object);
                streams.add(
                    new SequenceInputStream(
                        new FieldJson(field.getName(), value, indent).stream(),
                        new InputStreamOf(
                            //@checkstyle AvoidInlineConditionalsCheck (2 lines)
                            ind == names.size() - 1
                                ? "\n" : ",\n"
                        )
                    )
                );
            }
            return new SequenceInputStream(
                new SequenceInputStream(
                    new InputStreamOf("{\n"),
                    streams.stream()
                        .reduce(SequenceInputStream::new)
                        .orElse(new InputStreamOf(""))
                ),
                new InputStreamOf(String.format("%s%s", indent, "}"))
            );
        });
    }
}
