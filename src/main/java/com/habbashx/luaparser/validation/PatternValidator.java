package com.habbashx.luaparser.validation;

import com.habbashx.luaparser.annotation.LuaPattern;
import com.habbashx.luaparser.exception.LuaValidationException;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class PatternValidator implements FieldValidator{


    @Override
    public void validate(@NotNull final ValidationContext validationContext) {

        if (validationContext.getValue() == null) return;

        final String regex = validationContext.getField().getAnnotation(LuaPattern.class).value();
        if (!(validationContext.getValue() instanceof String)) {
            throw new LuaValidationException(
                    "Field "+validationContext.getField().getName() + " must be a string for pattern validation"
            );
        }

        if (!Pattern.matches(regex,(String)validationContext.getValue())) {
            throw new LuaValidationException("Field "+validationContext.getField().getName() + "does not match pattern "+regex);
        }
    }
}
