// Wires the custom task into a project so the build-logic compiles end-to-end.
tasks.register("runMyJavaExec", MyJavaExecTask::class.java) {
    mainClass = "does.not.Matter"
}
