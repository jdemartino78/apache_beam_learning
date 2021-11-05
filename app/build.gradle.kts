/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    id("com.google.looker.java-application-conventions")
}

dependencies {
    implementation(project(":transforms"))
}

//application {
//    // Define the main class for the application.
//    mainClass.set("com.google.looker.app.App")
//}
val appName = "hello"
val charPool : List<Char> = ('a'..'z') + ('0'..'9')
var stringLength=10
val googleCloudSecretName: String by project
val googleCloudProjectName: String by project

val randomString = (1..stringLength)
    .map { kotlin.random.Random.nextInt(0, charPool.size) }
    .map(charPool::get)
    .joinToString("")

var dataflowBaseArgs = mutableListOf(
    "--appName=$appName",
    "--region=us-east1",
    "--maxNumWorkers=1",
    "--workerMachineType=n1-standard-1",
    "--runner=DataflowRunner"
)
var jobArgs = mutableListOf(
    "--project=$googleCloudProjectName",
    "--tempLocation=gs://$googleCloudProjectName-temp/dataflow",
    "--jobName=$appName-$randomString",
    "--secretName=$googleCloudSecretName"
)

var dataflowArgs = jobArgs.toMutableList()
dataflowArgs.addAll(dataflowBaseArgs)

task("local", JavaExec::class){
    group = "application"
    mainClass.set("com.google.looker.app.App")
    classpath = sourceSets["main"].runtimeClasspath
    args = jobArgs
}
task("dataflow", JavaExec::class){
    group = "application"
    mainClass.set("com.google.looker.app.App")
    classpath = sourceSets["main"].runtimeClasspath
    args = dataflowArgs
}
task("localHelp", JavaExec::class) {
    group = "help"
    mainClass.set("com.google.looker.app.App")
    classpath = sourceSets["main"].runtimeClasspath
    args = listOf("--help=org.apache.beam.sdk.extensions.gcp.options.GcpOptions")
}