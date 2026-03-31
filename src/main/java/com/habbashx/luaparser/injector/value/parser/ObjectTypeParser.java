package com.habbashx.luaparser.injector.value.parser;

import com.habbashx.luaparser.injector.LuaInjector;
import org.luaj.vm2.LuaValue;

import java.lang.reflect.Field;

/**
 * Handles nested object injection.
 *
 * <p>Creates a new instance of the field type and recursively injects its fields.
 */
public class ObjectTypeParser implements FieldTypeParser {

    @Override
    public boolean supports(Class<?> type) {
        return false;
    }

    @Override
    public void inject(Object target, Field field, LuaValue value, LuaInjector injector) {

        try {
            final Object nestedObject = field.getType().getConstructor().newInstance();

            injector.injectObject(nestedObject,value);
            field.set(target,nestedObject);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
