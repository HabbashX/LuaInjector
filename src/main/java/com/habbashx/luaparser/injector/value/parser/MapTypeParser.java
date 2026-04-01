package com.habbashx.luaparser.injector.value.parser;

import com.habbashx.luaparser.exception.LuaParserException;
import com.habbashx.luaparser.injector.LuaInjector;
import com.habbashx.luaparser.parser.ValueParserFactory;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles Map<K, V> injection from Lua tables.
 *
 * <p>Lua table keys become Map keys,
 * and values are converted recursively or via primitive parser.
 */
public class MapTypeParser implements FieldTypeParser{

    @Override
    public boolean supports(Class<?> type) {
        return Map.class.isAssignableFrom(type);
    }

    @Override
    public Object parse(final Field field, final LuaValue value, final LuaInjector injector) {

        try {
            if (!value.istable()) throw new LuaParserException("Expected Lua table for map field: "+field.getName());

            if (!(field.getGenericType() instanceof ParameterizedType parameterizedType)) {
                throw new RuntimeException("Map field must be parameterized: " + field.getName());
            }

            final Class<?> keyType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
            final Class<?> valueType = (Class<?>) parameterizedType.getActualTypeArguments()[1];

            final Map<Object, Object> map = new HashMap<>();
            LuaValue k = LuaValue.NIL;

            while (true) {
                final Varargs n = value.next(k);
                k = n.arg1();

                if (k.isnil()) break;

                final LuaValue v = n.arg(2);
                final Object key = ValueParserFactory.parse(keyType, k);
                final Object val;

                if (injector.isComplexType(valueType)) {
                    final Object nested = valueType.getDeclaredConstructor().newInstance();
                    injector.injectObject(nested, v);
                    val = nested;
                } else {
                    val = ValueParserFactory.parse(valueType, v);
                }
                map.put(key,val);
            }
            return map;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
