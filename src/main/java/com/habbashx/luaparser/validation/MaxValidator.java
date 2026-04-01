package com.habbashx.luaparser.validation;

import com.habbashx.luaparser.annotation.LuaMax;
import com.habbashx.luaparser.exception.LuaValidationException;
import org.jetbrains.annotations.NotNull;

public class MaxValidator implements FieldValidator{

    @Override
    public void validate(@NotNull final ValidationContext validationContext) {

        if (validationContext.getValue() == null) return;

        final long max = validationContext.getField().getAnnotation(LuaMax.class).value();

        if (validationContext.getValue() instanceof Number number) {
            if (number.longValue() > max) {
                throw new LuaValidationException("Field " + validationContext.getField().getName() + " must be <= " + max);
            }
        }
    }
}
