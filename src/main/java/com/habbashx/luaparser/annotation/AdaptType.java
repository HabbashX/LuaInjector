package com.habbashx.luaparser.annotation;

import com.habbashx.luaparser.adapter.TypeAdapter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface AdaptType {
    Class<? extends TypeAdapter<?>> value();
}
