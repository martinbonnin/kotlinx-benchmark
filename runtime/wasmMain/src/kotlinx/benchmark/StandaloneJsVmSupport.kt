package kotlinx.benchmark

import kotlin.time.Duration.Companion.milliseconds

private fun browserEngineReadFile(path: String): String =
    js("globalThis.read(path)")

internal abstract class StandaloneJsVmSupport : JsEngineSupport() {
    override fun writeFile(path: String, text: String) =
        print("<FILE:$path>$text<ENDFILE>")

    override fun readFile(path: String): String =
        browserEngineReadFile(path)
}

private fun getPerformance(): JsAny =
    js("(typeof self !== 'undefined' ? self : globalThis).performance")

private fun performanceNow(performance: JsAny): Double =
    js("performance.now()")

internal inline fun standaloneJsVmMeasureTime(block: () -> Unit): Long {
    val performance = getPerformance()
    val start = performanceNow(performance)
    block()
    val end = performanceNow(performance)
    val startInNs = start.milliseconds.inWholeNanoseconds
    val endInNs = end.milliseconds.inWholeNanoseconds
    return endInNs - startInNs
}