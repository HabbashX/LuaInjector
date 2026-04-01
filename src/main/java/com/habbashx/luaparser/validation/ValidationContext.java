package com.habbashx.luaparser.validation;

import com.habbashx.luaparser.injector.LuaInjector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class ValidationContext {

    private final Object target;
    private final Object value;
    private final Field field;
    private final LuaInjector luaInjector;

    public ValidationContext(Object target,Object value, Field field, LuaInjector luaInjector) {
        this.value = value;
        this.field = field;
        this.luaInjector = luaInjector;
    }

    public Object getTarget() {
        return target;
    }

    public Object getValue() {
        return value;
    }

    public Field getField() {
        return field;
    }

    public LuaInjector getLuaInjector() {
        return luaInjector;
    }

    public <A extends Annotation> A annotation(Class<A> type) {
        return field.getAnnotation(type);
    }
}
