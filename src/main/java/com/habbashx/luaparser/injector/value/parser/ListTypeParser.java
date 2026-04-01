package com.habbashx.luaparser.injector.value.parser;

import com.habbashx.luaparser.exception.LuaParserException;
import com.habbashx.luaparser.injector.LuaInjector;
import com.habbashx.luaparser.parser.ValueParserFactory;
import org.jetbrains.annotations.NotNull;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles List<T> and Array injection from Lua tables.
 *
 * <p>Supports:
 * <ul>
 *     <li>Java arrays</li>
 *     <li>Generic Lists</li>
 * </ul>
 *
 * <p>Lua tables are treated as indexed arrays.
 */
public class ListTypeParser implements FieldTypeParser {

    @Override
    public boolean supports(Class<?> type) {
        return List.class.isAssignableFrom(type) || type.isArray();
    }

    @Override
    public Object parse(final Field field, final LuaValue value, final LuaInjector injector) {

        try {
            if (!value.istable()) throw new LuaParserException("Expected Lua table for array or list field "+field.getName());

            final Class<?> type = field.getType();

            if (type.isArray()) return injectArray(value,type);

           return injectList(field,value);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object injectArray(final LuaValue value , final Class<?> type)  {

        final Class<?> component = type.getComponentType();
        final int size = value.length();
        final Object array = Array.newInstance(component,size);

        for (int i = 1 ; i<= size ; i++) {
            final LuaValue val = value.get(i);
            final Object parsedObject = ValueParserFactory.parse(component, val);
            Array.set(array, i - 1, parsedObject);
        }
        return array;
    }

    private Object injectList(@NotNull final Field field, final LuaValue value) throws IllegalAccessException {

        if (!(field.getGenericType() instanceof ParameterizedType pt)) {
            throw new RuntimeException("List field must be parameterized: " + field.getName());
        }

        final Class<?> actualType =
                (Class<?>) pt.getActualTypeArguments()[0];

        final int size = value.length();
        final List<Object> list = new ArrayList<>(size);

        for (int i = 1; i <= size; i++) {
            LuaValue v = value.get(i);

            Object parsed = ValueParserFactory.parse(actualType, v);
            list.add(parsed);
        }

        return list;
    }
}
