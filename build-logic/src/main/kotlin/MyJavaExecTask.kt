import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import javax.inject.Inject

// Reproducer for missing "Import assign operator" quick fix in .kt files.
// See README.md for the steps.
abstract class MyJavaExecTask @Inject constructor(
    private val execOperations: ExecOperations,
) : DefaultTask() {

    @get:Input
    abstract val mainClass: Property<String>

    @TaskAction
    fun run() {
        execOperations.javaexec {
            // Change the line below to:
            //
            //     mainClass = "does.not.Matter"
            //
            // The IDE flags it as `Unresolved reference 'assign'`,
            // but does not offer the `Import assign operator` quick fix
            // (which would add `import org.gradle.kotlin.dsl.assign`).
            mainClass.set("does.not.Matter")
        }
    }
}
