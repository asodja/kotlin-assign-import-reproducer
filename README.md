# `Import assign operator` quick fix missing in `.kt` files

Minimal reproducer for the missing IntelliJ quick fix when assigning to a
Gradle `Property<T>` with `=` in a Kotlin source file (`.kt`) inside a
Gradle `build-logic` included build.

## Layout

```
.
├── build.gradle.kts                 # applies the convention plugin
├── settings.gradle.kts              # includes build-logic
└── build-logic/
    ├── build.gradle.kts             # kotlin-dsl
    ├── settings.gradle.kts
    └── src/main/kotlin/
        ├── my-conventions.gradle.kts
        └── MyJavaExecTask.kt        # <-- the file to edit
```

## Steps to reproduce

1. Import this directory in IntelliJ IDEA and wait for Gradle sync to finish.
2. Open `build-logic/src/main/kotlin/MyJavaExecTask.kt`.
3. Change the line

   ```kotlin
   mainClass.set("does.not.Matter")
   ```

   to

   ```kotlin
   mainClass = "does.not.Matter"
   ```

4. The IDE flags the line as `Unresolved reference 'assign'`.
5. Place the cursor on `mainClass` (the property name) and press `Alt+Enter`.
6. Place the cursor on `=` and press `Alt+Enter`.

### Expected

`Import assign operator` is offered as a quick fix in **both** cursor
positions, adding:

```kotlin
import org.gradle.kotlin.dsl.assign
```

Discoverability matters: a quick fix offered only on the `=` sign is
easy to miss because the cursor is rarely parked there. Operator-only
quick fixes are a long-standing usability issue. Offering the same fix
on the property name (`mainClass`) — where the cursor naturally lands —
makes it discoverable.

### Actual

The quick fix is missing in both positions. Only unrelated suggestions
appear, e.g.:

- `Add explicit 'this'`
- `Converts the assignment statement to an expression`
- `Add full qualifier`
- `Enable the option 'Implicit receivers...'`

## Notes

`./gradlew help` succeeds either way — the `kotlin-dsl` plugin
auto-applies the Kotlin assignment compiler plugin, which rewrites
`=` on `Property<T>` at compile time. The bug is in the IDE only.
Adding `import org.gradle.kotlin.dsl.assign` manually silences the
red highlighting in the IDE.
