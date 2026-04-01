package com.habbashx.luaparser.injector.value.parser;

import com.habbashx.luaparser.adapter.TypeAdapter;
import com.habbashx.luaparser.annotation.AdaptType;
import com.habbashx.luaparser.injector.LuaInjector;
import org.luaj.vm2.LuaValue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.UUID;

public class AdaptTypeParser implements FieldTypeParser {

    @Override
    public boolean supports(Class<?> type) {
        return UUID.class.isAssignableFrom(type);
    }

    @Override
    public Object parse(Field field, LuaValue value, LuaInjector injector) {

        try {
            AdaptType adaptType = field.getAnnotation(AdaptType.class);

            if (adaptType == null) return null;

            final Class<? extends TypeAdapter<?>> adapter = adaptType.value();

            final Constructor<? extends TypeAdapter<?>> constructor =adapter.getConstructor();
            constructor.setAccessible(true);

            TypeAdapter<?> adapterInstance = constructor.newInstance();

            return adapterInstance.adpat(value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to adapt field "+field.getName());
        }
    }
}
