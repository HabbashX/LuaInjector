package com.habbashx.luaparser.adapter;

import org.luaj.vm2.LuaValue;

public interface TypeAdapter<T>{
    T adpat(LuaValue luaValue);
}
