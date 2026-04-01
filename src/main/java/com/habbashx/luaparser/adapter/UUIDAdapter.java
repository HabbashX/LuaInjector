package com.habbashx.luaparser.adapter;

import org.luaj.vm2.LuaValue;

import java.util.UUID;

public class UUIDAdapter implements TypeAdapter<UUID>{

    @Override
    public UUID adpat(LuaValue luaValue) {
        return UUID.fromString(luaValue.tojstring());
    }
}
