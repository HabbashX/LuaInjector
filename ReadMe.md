# LuaInjector 🚀

A lightweight Java library that maps Lua tables directly into Java objects using reflection and a flexible strategy-based parsing system.

It supports primitives, nested objects, lists, arrays, and maps — making Lua a powerful configuration format for Java applications.

---

## ✨ Features

- Automatic Lua → Java object mapping
- Supports primitive types (`int`, `boolean`, `String`, etc.)
- Supports `List` and arrays
- Supports `Map<K, V>`
- Nested object injection (recursive)
- Strategy-based parser system (extensible)
- Reflection-based field binding
- Custom type parsers support

---

## 📦 Example Lua Config

```lua
return {
    port = 8080,

    database = {
        host = "localhost",
        port = 3306
    },

    tags = {"dev", "prod", "test"},

    settings = {
        debug = true,
        timeout = 5000
    }
}
```

## ☕ Java Model

```java
public class Config {
    public int port;
    public Database database;
    public List tags;
    public Map settings;
}

class Database {
    public String host;
    public int port;
}
```

---

## 🚀 Usage

### Load from Lua file

```java
LuaInjector injector = new LuaInjector("config.lua");

Config config = new Config();
injector.inject(config);
```

### Load from LuaValue

```java
LuaValue table = ...;

LuaInjector injector = new LuaInjector(table);

Config config = new Config();
injector.inject(config);
```

---

## 🧠 How It Works

LuaInjector uses a strategy-based architecture:

```
LuaInjector
  → ParserRegistry
    → FieldTypeParser (Strategy)
      → Primitive / List / Map / Object Parsers
        → ValueParserFactory (primitive conversion)
```

Each field is handled by a dedicated parser.

---

## 🧩 Supported Types

### Primitive Types

- `int`, `long`, `float`, `double`
- `boolean`, `char`
- `String`
- Wrapper classes

### Collections

**List**
```java
List values;
```

**Array**
```java
int[] values;
```

**Map**
```java
Map data;
```

### Nested Objects

```java
class Database {
    public String host;
    public int port;
}
```

---

## ⚙️ Architecture

### LuaInjector
Main engine responsible for:
- Loading Lua scripts
- Injecting values into Java objects
- Managing the parser system

### ParserRegistry
Stores and resolves type handlers dynamically.

### FieldTypeParser
Strategy interface for type-specific injection logic.

### ValueParserFactory
Converts `LuaValue` → Java primitive types.

---

## 🧪 Injection Process

1. Load Lua table
2. Match Lua keys with Java fields
3. Resolve parser based on field type
4. Convert `LuaValue` → Java type
5. Inject via reflection
6. Recursively inject nested objects

---

## 🔥 Example Flow

**Lua:**
```lua
db = {
    host = "localhost"
}
```

**Java:**
```java
Database db;
```

**Flow:**
```
Lua table → ObjectTypeParser → new Database() → recursive injection → field assignment
```

---

## 🧩 Extending the System

```java
public interface FieldTypeParser {
    boolean supports(Class type);
    void inject(Object target, Field field, LuaValue value, LuaInjector injector);
}
```

**Register your custom parser:**

```java
parserRegistry.register(new CustomParser());
```

---

## ⚡ Performance Notes

- Reflection-based injection
- Strategy lookup per field
- Designed for configuration use cases
- Not intended for high-frequency runtime execution

---

## 🛠 Requirements

- Java 17
- LuaJ library

## 📦 Maven Dependency

```xml

    org.luaj
    luaj-jse
    3.0.1

```

---

## 🚀 Future Improvements

- Annotation mapping (`@LuaNode`)
- Field caching for performance
- Strict validation mode
- Hot reload system
- Schema validation
- Plugin-based parser system

---

## 📜 License

MIT License

---

## ⭐ Purpose

LuaInjector was built to make Lua a clean, flexible, and powerful configuration format for Java systems — with minimal boilerplate and maximum extensibility.

---

## 🤝 Contributing

- Add new parsers
- Improve performance
- Add validation systems
- Build plugins