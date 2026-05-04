import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import javax.inject.Inject

abstract class MyJavaExecTask @Inject constructor(
    private val execOperations: ExecOperations,
) : DefaultTask() {

    @get:Input
    abstract val mainClass: Property<String>

    @TaskAction
    fun run() {
        execOperations.javaexec {
            // `executable` is a Property<String> on JavaExecSpec.
            // Without `import org.gradle.kotlin.dsl.assign`, this line fails
            // to compile and the IDE should — but does not — offer the
            // "Import assign operator" quick fix.
            executable = "/usr/bin/java"
            mainClass.set("does.not.Matter")
        }
    }
}
