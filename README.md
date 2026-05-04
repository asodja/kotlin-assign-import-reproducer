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
        └── MyJavaExecTask.kt        # <-- the file to open in the IDE
```

## Steps to reproduce

1. Open this directory in IntelliJ IDEA and wait for Gradle sync to finish.
2. Open `build-logic/src/main/kotlin/MyJavaExecTask.kt`.
3. Place the cursor on `executable = "/usr/bin/java"` (currently flagged
   as `Unresolved reference 'assign'`).
4. Press `Alt+Enter` to open the quick fix list.

### Expected

`Import assign operator` is offered, which would add:

```kotlin
import org.gradle.kotlin.dsl.assign
```

### Actual

The quick fix list shows only unrelated suggestions, e.g.:

- `Add explicit 'this'`
- `Converts the assignment statement to an expression`
- `Add full qualifier`
- `Enable the option 'Implicit receivers...'`

The same `=` assignment in a `.gradle.kts` script does offer the import
quick fix, so the issue is specific to plain `.kt` files compiled by
`kotlin-dsl`.

## Notes

`./gradlew help` succeeds without any changes — the `kotlin-dsl` plugin
auto-applies the Kotlin assignment compiler plugin, which rewrites `=`
on `Property<T>` at compile time. The bug is in the IDE only: it flags
the line as `Unresolved reference 'assign'` and fails to offer the
correct quick fix. Adding `import org.gradle.kotlin.dsl.assign`
manually silences the error in the IDE.
