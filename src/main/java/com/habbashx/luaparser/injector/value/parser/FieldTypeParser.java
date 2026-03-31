package com.habbashx.luaparser.injector.value.parser;

import com.habbashx.luaparser.injector.LuaInjector;
import org.luaj.vm2.LuaValue;

import java.lang.reflect.Field;

/**
 * Strategy interface for handling field injection logic.
 *
 * <p>Each implementation handles a specific type category:
 * <ul>
 *     <li>Primitive types</li>
 *     <li>Lists / Arrays</li>
 *     <li>Maps</li>
 *     <li>Nested Objects</li>
 * </ul>
 */
public interface FieldTypeParser {

    /**
     * Checks whether this parser supports the given field type.
     *
     * @param type field type
     * @return true if supported
     */
    boolean supports(Class<?> type);


    /**
     * Injects Lua value into the target field.
     *
     * @param target Java object
     * @param field field to inject into
     * @param value Lua value
     * @param injector main injector (used for recursion)
     */
    void inject(Object target, Field field, LuaValue value, LuaInjector injector);

}
