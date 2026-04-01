package com.habbashx.luaparser.validation;

import org.jetbrains.annotations.NotNull;

public interface FieldValidator {
    void validate(@NotNull final ValidationContext validationContext);
}
