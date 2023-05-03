package kotlinx.benchmark

import kotlin.time.DurationUnit
import kotlin.time.toDuration

private fun nodeJsWriteFile(path: String, text: String): Unit =
    js("require('fs').writeFileSync(path, text, 'utf8')")

private fun nodeJsReadFile(path: String): String =
    js("require('fs').readFileSync(path, 'utf8')")

private fun nodeJsArguments(): String =
    js("process.argv.slice(2).join(' ')")

internal object NodeJsEngineSupport : JsEngineSupport() {
    override fun writeFile(path: String, text: String) =
        nodeJsWriteFile(path, text)

    override fun readFile(path: String): String =
        nodeJsReadFile(path)

    override fun arguments(): Array<out String> =
        nodeJsArguments().split(' ').toTypedArray()
}

private fun hrTimeToNs(hrTime: JsArray<JsNumber>): Long {
    val fromSeconds = hrTime[0]!!.toDouble().toDuration(DurationUnit.SECONDS)
    val fromNanos = hrTime[1]!!.toDouble().toDuration(DurationUnit.NANOSECONDS)
    return (fromSeconds + fromNanos).inWholeNanoseconds
}

private fun getProcess(): JsAny = js("process")

private fun getHrTime(process: JsAny): JsArray<JsNumber> = js("process.hrtime()")

internal inline fun nodeJsMeasureTime(block: () -> Unit): Long {
    val process = getProcess()
    val start = getHrTime(process)
    block()
    val end = getHrTime(process)
    return hrTimeToNs(end) - hrTimeToNs(start)
}

internal fun isNodeJsEngine(): Boolean =
    js("(typeof process !== 'undefined') && (process.release.name === 'node')")