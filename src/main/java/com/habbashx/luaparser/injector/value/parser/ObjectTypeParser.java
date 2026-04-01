package com.habbashx.luaparser.injector.value.parser;

import com.habbashx.luaparser.injector.LuaInjector;
import org.luaj.vm2.LuaValue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Handles nested object injection.
 *
 * <p>Creates a new instance of the field type and recursively injects its fields.
 */
public class ObjectTypeParser implements FieldTypeParser {

    @Override
    public boolean supports(Class<?> type) {
        return  !type.isPrimitive()
                && type != String.class
                && !Number.class.isAssignableFrom(type)
                && type != Boolean.class
                && type != Character.class
                && !type.isEnum()
                && !Map.class.isAssignableFrom(type)
                && !List.class.isAssignableFrom(type)
                && !type.isArray();
    }

    @Override
    public Object parse(Field field, LuaValue value, LuaInjector injector) {

        try {

            Constructor<?> constructor = field.getType().getConstructor();
            constructor.setAccessible(true);
            final Object nestedObject =constructor.newInstance();

            injector.injectObject(nestedObject,value);
            return nestedObject;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
