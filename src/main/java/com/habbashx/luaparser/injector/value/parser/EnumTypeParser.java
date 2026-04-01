package com.habbashx.luaparser.injector.value.parser;

import com.habbashx.luaparser.exception.LuaParserException;
import com.habbashx.luaparser.exception.LuaValidationException;
import com.habbashx.luaparser.injector.LuaInjector;
import org.jetbrains.annotations.NotNull;
import org.luaj.vm2.LuaValue;

import java.lang.reflect.Field;

public class EnumTypeParser implements FieldTypeParser {
    @Override
    public boolean supports(@NotNull final Class<?> type) {
        return type.isEnum();
    }

    @Override
    @SuppressWarnings({"unchecked","rawtypes"})
    public Object parse(final Field field, final LuaValue value, final LuaInjector luaInjector) {

        try {
            if (value.isnil()) throw new LuaParserException("expected Lua table for enum in field "+field.getName());

            final String rawValue = value.tojstring();
            final Class<?> enumType = field.getType();

            final Object[] constants = enumType.getEnumConstants();

            for (final Object constant : constants) {
                Enum e = (Enum) constant;

                if (e.name().equalsIgnoreCase(rawValue)) {
                    return e;
                }
            }
            throw new LuaParserException("invalid enum value: "+rawValue + " for field "+field.getName()+" of type "+enumType.getName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
