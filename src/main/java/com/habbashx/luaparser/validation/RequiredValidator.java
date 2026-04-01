package com.habbashx.luaparser.validation;

import com.habbashx.luaparser.exception.LuaValidationException;
import org.jetbrains.annotations.NotNull;

public class RequiredValidator implements FieldValidator {



    @Override
    public void validate(@NotNull final ValidationContext validationContext) {
        if (validationContext.getValue() == null) throw new LuaValidationException("Field: "+validationContext.getField().getName()+"is required");
    }
}
