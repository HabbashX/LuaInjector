package com.habbashx.luaparser.injector.value.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Registry that holds all available FieldTypeParsers.
 *
 * <p>Responsible for selecting the correct parser for a field type.
 */
public class ParserRegistry {

    List<FieldTypeParser> parsers = new ArrayList<>();

    /**
     * Registers a new parser strategy.
     *
     * @param handler parser implementation
     */
    public void register(final FieldTypeParser parser) {
        parsers.add(parser);
    }

    /**
     * Resolves the correct parser for a given type.
     *
     * @param type field type
     * @return matching parser
     * @throws RuntimeException if no parser found
     */
    public FieldTypeParser resolve(final Class<?> type) {
        for (final FieldTypeParser parser : parsers) {
            if (parser.supports(type)) {
                return parser;
            }
        }
        throw new RuntimeException("No handler for type: " + type.getName());
    }
}