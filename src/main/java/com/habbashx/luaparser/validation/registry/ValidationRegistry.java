package com.habbashx.luaparser.validation.registry;

import com.habbashx.luaparser.injector.LuaInjector;
import com.habbashx.luaparser.validation.FieldValidator;
import com.habbashx.luaparser.validation.ValidationContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

public class ValidationRegistry {

    private final Map<Class<? extends Annotation>, FieldValidator> registry = new HashMap<>();

    public <A extends Annotation> void register(
            Class<A> annotation,
            FieldValidator validator
    ) {
        registry.put(annotation, validator);
    }

    public void validate(Object target,Object value, Field field, LuaInjector injector) {

        final ValidationContext ctx = new ValidationContext(target,value, field, injector);

        for (final Map.Entry<Class<? extends Annotation>, FieldValidator> entry : registry.entrySet()) {

            final Annotation annotation = field.getAnnotation(entry.getKey());

            if (annotation != null) {
                entry.getValue().validate(ctx);
            }
        }
    }
}