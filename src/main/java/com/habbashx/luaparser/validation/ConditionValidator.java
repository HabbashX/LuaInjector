package com.habbashx.luaparser.validation;

import com.habbashx.luaparser.annotation.Condition;
import com.habbashx.luaparser.exception.ConditionException;
import org.jetbrains.annotations.NotNull;

public class ConditionValidator implements FieldValidator {

    private static final String EQUAL_CONDITION = "==";
    private static final String NOT_EQUAL_CONDITION = "!=";

    @Override
    public void validate(@NotNull ValidationContext validationContext) {


        try {
            final Condition condition = validationContext.annotation(Condition.class);

            final String stringCondition = condition.value();

            if (stringCondition.contains(EQUAL_CONDITION)) {
                performCondition(validationContext, stringCondition, EQUAL_CONDITION);
            } else if (stringCondition.contains(NOT_EQUAL_CONDITION)) {
                performCondition(validationContext, stringCondition, NOT_EQUAL_CONDITION);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void performCondition(@NotNull ValidationContext validationContext, @NotNull String stringCondition, String condtion) throws IllegalAccessException {
        String[] parts = stringCondition.split(condtion);
        String expected = parts[1].trim().replace("'", "");

        Object actualValue = validationContext.getField().get(validationContext.getTarget());

        if (!expected.equalsIgnoreCase(String.valueOf(actualValue))) {
            throw new ConditionException("there`s no equality");
        }
    }
}
