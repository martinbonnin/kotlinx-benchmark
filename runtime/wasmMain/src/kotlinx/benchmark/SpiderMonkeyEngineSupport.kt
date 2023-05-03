package kotlinx.benchmark

private fun spiderMonkeyArguments(): String =
    js("globalThis.scriptArgs.join(' ')")

internal object SpiderMonkeyEngineSupport : StandaloneJsVmSupport() {
    override fun arguments(): Array<out String> =
        spiderMonkeyArguments().split(' ').toTypedArray()
}

internal fun isSpiderMonkeyEngine(): Boolean =
    js("typeof globalThis.inIon !== 'undefined'")