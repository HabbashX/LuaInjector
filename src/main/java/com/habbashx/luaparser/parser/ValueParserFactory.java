package com.habbashx.luaparser.parser;

import com.habbashx.luaparser.exception.UnSupportedTypeException;
import org.luaj.vm2.LuaValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory responsible for converting LuaValue into Java primitive types.
 *
 * <p>This class acts as a registry of type converters for fast primitive parsing.
 */
public class ValueParserFactory {

    private static final Map<Class<?>, ValueParser<?>> REGISTRY = new HashMap<>();

    static {
        REGISTRY.put(int.class, LuaValue::toint);
        REGISTRY.put(Integer.class, LuaValue::toint);
        REGISTRY.put(float.class,LuaValue::tofloat);
        REGISTRY.put(Float.class,LuaValue::tofloat);
        REGISTRY.put(double.class,LuaValue::todouble);
        REGISTRY.put(Double.class,LuaValue::todouble);
        REGISTRY.put(short.class, LuaValue::toshort);
        REGISTRY.put(Short.class, LuaValue::toshort);
        REGISTRY.put(long.class, LuaValue::tolong);
        REGISTRY.put(Long.class, LuaValue::tolong);
        REGISTRY.put(char.class, LuaValue::tochar);
        REGISTRY.put(Character.class, LuaValue::tochar);
        REGISTRY.put(boolean.class, LuaValue::toboolean);
        REGISTRY.put(Boolean.class, LuaValue::toboolean);
        REGISTRY.put(String.class, LuaValue::tojstring);
    }



    /**
     * Converts a LuaValue into a Java object of the specified type.
     *
     * @param type target Java type
     * @param luaValue Lua value
     * @return converted Java value
     * @throws UnSupportedTypeException if type is not registered
     */
    @SuppressWarnings("unchecked")
    public static <T> T parse(Class<T> type, LuaValue luaValue) {

        ValueParser<T> valueParser = (ValueParser<T>) REGISTRY.get(type);

        if (valueParser != null) {
            return valueParser.parse(luaValue);
        }

        throw new UnSupportedTypeException("Cannot parse value to type: " + type.getName());
    }
}
