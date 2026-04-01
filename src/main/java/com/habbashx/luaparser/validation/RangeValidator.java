package com.habbashx.luaparser.validation;

import com.habbashx.luaparser.annotation.LuaRange;
import com.habbashx.luaparser.exception.LuaValidationException;
import org.jetbrains.annotations.NotNull;

public class RangeValidator implements FieldValidator {

    @Override
    public void validate(@NotNull final ValidationContext validationContext) {


        final LuaRange luaRange = validationContext.getField().getAnnotation(LuaRange.class);

        if (luaRange == null || validationContext.getValue() == null) {
            return;
        }

        if (!(validationContext.getValue() instanceof Number number)) return;

        final long v = number.longValue();

        if (v < luaRange.min() || v > luaRange.max()) {
            throw new LuaValidationException("Field "+validationContext.getField().getName()+
                    " must be between "+luaRange.min() + " and "+ luaRange.max());
        }

    }
}
