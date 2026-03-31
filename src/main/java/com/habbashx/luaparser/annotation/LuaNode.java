package com.habbashx.luaparser.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * LuaNode represent the Lua Node inside lua file
 * this class indicate to LuaInjector to inject the specific node value into
 * annotated field
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface LuaNode {

    /**
     * represent the lua node
     * @return node
     */
    String value();
}
