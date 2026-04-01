# 📘 LuaInjector – Full Documentation

## 🚀 Overview

LuaInjector is a lightweight, extensible configuration framework that maps Lua tables into Java objects with support for:

- ✅ Primitive type parsing
- ✅ Nested object injection
- ✅ Lists & arrays
- ✅ Maps
- ✅ Enum support
- ✅ Custom type adapters
- ✅ Annotation-driven validation system
- ✅ Conditional validation
- ✅ Extensible parser & validator architecture

---

## 🧠 Core Architecture

```
Lua Script
   ↓
LuaValue (LuaJ)
   ↓
Parser System (FieldTypeParser)
   ↓
Java Object
   ↓
Validation System (Annotation-driven)
   ↓
Final Injected Object
```

---

## ⚙️ Getting Started

### 1. Create Lua Config

```lua
config = {
    port = 8080,
    name = "server",
    database = {
        host = "localhost",
        port = 3306
    },
    users = {"admin", "guest"}
}
```

### 2. Java Model

```java
public class Config {

    public int port;
    public String name;
    public Database database;
    public List<String> users;
}

public class Database {
    public String host;
    public int port;
}
```

### 3. Inject

```java
LuaInjector injector = new LuaInjector("config.lua");

Config config = new Config();
injector.inject(config);
```

---

## 🔌 Parser System

All parsing is handled using: `FieldTypeParser`

### Built-in Parsers

#### 1. `PrimitiveTypeParser`

Supports:
- `int`, `long`, `float`, `double`
- `boolean`
- `char`
- `String`

#### 2. `ListTypeParser`

Supports:
- `List<T>`
- arrays (`T[]`)

> ✔ Uses numeric indexing (`1..n`)  
> ❌ Ignores non-array keys

#### 3. `MapTypeParser`

Supports:
- `Map<K, V>`

> ✔ Uses Lua `next()` iteration  
> ✔ Supports complex values

#### 4. `ObjectTypeParser`

Handles:
- Custom classes (POJOs)

> ✔ Recursively injects fields  
> ✔ Requires default constructor

#### 5. `EnumTypeParser`

Supports Java enums:

```java
enum Role { ADMIN, USER }
```

```lua
role = "ADMIN"
```

> ✔ Case-insensitive matching

#### 6. `AdaptTypeParser` (Custom Adapters)

---

## 🔧 Custom Type Adapters

### Annotation

```java
@AdaptType(UUIDAdapter.class)
UUID id;
```

### Adapter Interface

```java
public interface TypeAdapter<T> {
    T adapt(LuaValue value);
}
```

### Example

```java
public class UUIDAdapter implements TypeAdapter<UUID> {

    @Override
    public UUID adapt(LuaValue value) {
        return UUID.fromString(value.tojstring());
    }
}
```

---

## 🧩 Validation System

### Overview

Validation is:
- ✔ Annotation-driven
- ✔ Extensible
- ✔ Decoupled from parsing
- ✔ Safe (no reflection in validators)

### Validation Flow

```
Parsed Value
   ↓
ValidationRegistry
   ↓
Matching Validators
   ↓
Error handling
```

### `ValidationContext`

Provides safe access:

```java
ctx.value()
ctx.field()
ctx.target()
ctx.annotation(...)
ctx.injector()
```

---

## 🔒 Built-in Validators

### `@Min`

```java
@Min(10)
int port;
```

> ✔ Ensures value ≥ min

### `@Max`

```java
@Max(100)
int port;
```

> ✔ Ensures value ≤ max

### `@Range`

```java
@Range(min = 1, max = 10)
int threads;
```

> ✔ Ensures value within range

### `@Pattern`

```java
@Pattern("\\w+")
String name;
```

> ✔ Regex validation

---

## ⚡ Conditional Validation

### `@Condition`

```java
@Condition("port == 7070") or @Condtion("port != 7070")
@Min(10)
int port;
```

### Behavior

| Condition | Result               |
|-----------|----------------------|
| `false`   | validation skipped   |
| `true`    | validation applied   |

### Example

```java
public class Config {

    public String env;

    @Condition(field = "env", equals = "prod")
    @Min(10)
    public int port;
}
```

---

## 🧠 `ValidationRegistry`

Central dispatcher:

```java
Map<Annotation, FieldValidator>
```

> ✔ Maps annotation → validator  
> ✔ Executes only matching validators  
> ✔ Supports custom validators

---

## 🔌 Creating Custom Validators

### Step 1: Implement

```java
public class MyValidator implements FieldValidator {

    @Override
    public void validate(ValidationContext ctx) {
        // custom logic
    }
}
```

### Step 2: Register

```java
validationRegistry.register(MyAnnotation.class, new MyValidator());
```

### Step 3: Use

```java
@MyAnnotation
String field;
```

---

## 🧠 Parser Registry

Handles parser resolution:

```java
FieldTypeParser resolve(Class<?> type)
```

### Order Matters

| Priority | Parser               |
|----------|----------------------|
| 1        | `AdaptTypeParser`    |
| 2        | `EnumTypeParser`     |
| 3        | `ListTypeParser`     |
| 4        | `MapTypeParser`      |
| 5        | `ObjectTypeParser`   |
| 6        | `PrimitiveTypeParser`|

---

## ⚠️ Important Rules

### Lua Tables

| Structure     | Supported |
|---------------|-----------|
| `{1,2,3}`     | ✅ List   |
| `{a=1}`       | ✅ Map    |
| `{1,2,a=3}`   | ❌ Not supported |

### Arrays vs Maps

- **Arrays** → `length()` + `get(i)`
- **Maps** → `next()`

### Object Requirements

- Must have default constructor
- Fields must be accessible


## 🚀 Advanced Features

#### ✔ Nested Object Injection

```java
class A {
    B b;
}
```

> ✔ Automatically resolved

#### ✔ Nested Collections

```java
List<Database>
Map<String, List<Integer>>
```

> ✔ Supported via recursive parsing

#### ✔ Custom Adapters
> ✔ Plug any type conversion

#### ✔ Conditional Validation
> ✔ Dynamic rules

#### ✔ Extensible Architecture
- Add parsers
- Add validators
- Add adapters

---

## 🧠 Best Practices

**Keep Lua clean:**

```lua
users = {"a", "b", "c"}  -- ✔ good
users = {1, 2, name="bad"} -- ❌ bad
```

- ✔ Always use parameterized collections: `List<String>` not `List`
- ✔ Use adapters for complex types
- ✔ Keep validators small & focused

---

## 🚀 Future Improvements

- 🔥 Dot-path mapping (`db.host`)
- ⚡ Reflection caching
- 🧠 Expression engine for conditions
- 📦 Config reloading
- 🔒 Sandbox Lua execution
- 📊 Schema generation

---

## 🏁 Summary

LuaInjector provides:

- ✔ Clean separation (Parsing / Validation / Injection)
- ✔ High extensibility
- ✔ Annotation-driven configuration
- ✔ Safe and maintainable architecture