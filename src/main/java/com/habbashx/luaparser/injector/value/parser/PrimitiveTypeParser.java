package com.habbashx.luaparser.injector.value.parser;

import com.habbashx.luaparser.injector.LuaInjector;
import com.habbashx.luaparser.parser.ValueParserFactory;
import org.jetbrains.annotations.NotNull;
import org.luaj.vm2.LuaValue;

import java.lang.reflect.Field;

/**
 * Handles injection of primitive and simple wrapper types.
 *
 * <p>Supports:
 * <ul>
 *     <li>int, float, double, long</li>
 *     <li>boolean, char</li>
 *     <li>String</li>
 *     <li>Wrapper classes</li>
 * </ul>
 */
public class PrimitiveTypeParser implements FieldTypeParser {

    @Override
    public boolean supports(@NotNull final Class<?> type) {
        return type.isPrimitive()
                || type == String.class
                || Number.class.isAssignableFrom(type)
                || type == Boolean.class
                || type == Character.class;
    }

    @Override
    public void inject(final Object target, final Field field, final LuaValue value, final LuaInjector injector) {

        try {
            final Object parsedValue = ValueParserFactory.parse(field.getType(),value);
            field.setAccessible(true);
            field.set(target,parsedValue);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
