package com.habbashx.luaparser.validation;

import com.habbashx.luaparser.annotation.LuaMin;
import com.habbashx.luaparser.exception.LuaValidationException;
import org.jetbrains.annotations.NotNull;

public class MinValidator implements FieldValidator {

    @Override
    public void validate(@NotNull final ValidationContext validationContext) {

        if (validationContext.getValue() == null) return;

        final int min = validationContext.getField().getAnnotation(LuaMin.class).value();

        if (validationContext.getValue() instanceof Number number) {
            if (number.longValue() < min)
                throw new LuaValidationException("Field "+validationContext.getField().getName() + "must be >= "+min);
        }
    }
}
