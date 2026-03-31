package com.habbashx.luaparser.injector;


import com.habbashx.luaparser.injector.value.parser.*;
import org.jetbrains.annotations.NotNull;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.lang.reflect.Field;

/**
 * LuaInjector is the core engine that binds Lua tables to Java objects using reflection.
 *
 * <p>It reads a Lua script (or LuaValue table) and injects values into Java fields automatically,
 * supporting:
 * <ul>
 *     <li>Primitive types (int, boolean, String, etc.)</li>
 *     <li>Nested objects</li>
 *     <li>Lists and Arrays</li>
 *     <li>Maps</li>
 * </ul>
 *
 * <p>It uses a strategy-based system (FieldTypeParser) to determine how each field is injected.
 */
public class LuaInjector {

    private final Globals globals = JsePlatform.standardGlobals();

    private final LuaValue root;

    private final ParserRegistry parserRegistry = new ParserRegistry();

    /**
     * Creates an injector and loads a Lua file.
     *
     * @param file Lua script file path
     */
    public LuaInjector(String file) {
        final LuaValue chunk = globals.loadfile(file);
        this.root = chunk.call();

        registerParsers();
    }


    /**
     * Creates an injector from an already evaluated LuaValue (table).
     *
     * @param root Lua table root
     */
    public LuaInjector(LuaValue root) {
        this.root = root;
        registerParsers();
    }


    /**
     * Injects the root Lua table into a Java object.
     *
     * @param target target object to populate
     */
    public void inject(@NotNull Object target) {
        injectObject(target, root);
    }

    /**
     * Recursively injects Lua table values into an object.
     *
     * <p>This method:
     * <ul>
     *     <li>Iterates over all declared fields</li>
     *     <li>Matches Lua keys with field names</li>
     *     <li>Delegates injection to appropriate FieldTypeParser</li>
     * </ul>
     *
     * @param target Java object
     * @param table Lua table
     */
    public void injectObject(@NotNull final Object target, final LuaValue table) {

        Field[] fields = target.getClass().getDeclaredFields();

        try {
            for (Field field : fields) {
                field.setAccessible(true);

                String key = field.getName();

                LuaValue value = table.get(key);

                if (value.isnil()) continue;

                FieldTypeParser handler = parserRegistry.resolve(field.getType());

                handler.inject(target, field, value, this);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Determines whether a type is a complex object (not primitive or wrapper).
     *
     * <p>Used to decide whether to:
     * <ul>
     *     <li>Directly parse value</li>
     *     <li>Recursively inject nested object</li>
     * </ul>
     *
     * @param type field type
     * @return true if complex object
     */
    public boolean isComplexType(@NotNull Class<?> type) {
        return !type.isPrimitive()
                && type != String.class
                && !Number.class.isAssignableFrom(type)
                && type != Boolean.class
                && type != Character.class;
    }

    /**
     * Registers all default field parsers.
     *
     * <p>Order matters:
     * <ul>
     *     <li>Primitive types first</li>
     *     <li>Collections (List/Map)</li>
     *     <li>Objects last (fallback)</li>
     * </ul>
     */
    private void registerParsers() {
        parserRegistry.register(new PrimitiveTypeParser());
        parserRegistry.register(new ListTypeParser());
        parserRegistry.register(new MapTypeParser());
        parserRegistry.register(new ObjectTypeParser());
    }

    /** @return Lua root table */
    public LuaValue getRoot() {
        return root;
    }

    /** @return Lua globals environment */
    public Globals getGlobals() {
        return globals;
    }

    /** @return parser registry used for type resolution */
    public ParserRegistry getParserRegistry() {
        return parserRegistry;
    }
}
