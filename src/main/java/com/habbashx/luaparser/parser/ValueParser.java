package com.habbashx.luaparser.parser;

import org.luaj.vm2.LuaValue;

/**
 * Functional interface for converting LuaValue into Java type.
 *
 * @param <T> target type
 */
public interface ValueParser<T> {
    /**
     * Converts LuaValue into Java object.
     *
     * @param value Lua value
     * @return Java object
     */
    T parse(LuaValue value);
}
