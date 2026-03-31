package com.habbashx.luaparser.injector.value.parser;

import com.habbashx.luaparser.injector.LuaInjector;
import com.habbashx.luaparser.parser.ValueParserFactory;
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
    public void inject(final Object target, final Field field, final LuaValue value, final LuaInjector injector) {

        try {

            if (!value.istable()) return;

            final Class<?> type = field.getType();

            if (type.isArray()) {

                final Class<?> component = type.getComponentType();
                final int size = value.length();
                final Object array = Array.newInstance(component,size);

                for (int i = 1 ; i<= size ; i++) {
                    final LuaValue val = value.get(i);
                    final Object parsedObject = ValueParserFactory.parse(component,val);
                    Array.set(array,i - 1 ,parsedObject);
                }
                field.set(target,array);
            }


            final ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
            final Class<?> actualGenericsType = (Class<?>) parameterizedType.getActualTypeArguments()[0];

            final List<Object> list = new ArrayList<>();

            LuaValue k = LuaValue.NIL;

            while (true) {
                final Varargs n = value.next(k);

                k = n.arg1();
                if (k.isnil()) break;

                final LuaValue v = n.arg(2);
                final Object parsedObject = ValueParserFactory.parse(actualGenericsType,v);
                list.add(parsedObject);
            }
            field.set(target,list);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
