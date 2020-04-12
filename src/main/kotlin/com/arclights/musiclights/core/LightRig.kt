package com.arclights.musiclights.core

import ddf.minim.AudioInput
import ddf.minim.Minim
import ddf.minim.analysis.FFT
import reactor.core.Disposable
import reactor.core.publisher.Flux
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.time.Duration

class LightRig(
    val nbrOfLights: Int,
    sampleFrequency: Int
) {
    private val minim: Minim = Minim(this)
    private val input: AudioInput
    private val fft: FFT
    private val lightStateSubscription: Disposable

    val config: LightConfig

    val lights: List<Light>

    init {
        input = minim.getLineIn(Minim.STEREO)
        fft = FFT(input.bufferSize(), input.sampleRate())
        fft.linAverages(nbrOfLights)
        config = LightConfig(nbrOfLights, input.bufferSize(), input.sampleRate(), fft.specSize())
        lights = (0 until nbrOfLights).map { Light(fft, it) }

        lightStateSubscription = Flux
            .interval(Duration.ofMillis(1000L / sampleFrequency))
            .map { _ ->
                fft.forward(input.mix)

                val filterLevel = config.getFilter().filter(fft, input)
                val amplifierLevels = config.getAmplifier().update(fft)

                zip(
                    filterLevel,
                    amplifierLevels,
                    Float::times
                )
                    .map { it * config.masterLevel }
            }
            .subscribe(this::updateLights)
    }

    private fun zip(l1: List<Float>, l2: List<Float>, f: (a: Float, b: Float) -> Float) = l1.zip(l2, f)

    private fun updateLights(updatedState: List<Float>) {
        lights.forEach { it.updateLevel(updatedState) }
    }

    fun stop() {
        lightStateSubscription.dispose()
        minim.stop()
    }

    @SuppressWarnings
    fun sketchPath(fileName: String): String {
        return ""
    }

    @SuppressWarnings
    @Throws(FileNotFoundException::class)
    fun createInput(fileName: String): InputStream {
        return FileInputStream(fileName)
    }

    companion object {
        const val MAX_AMPLITUDE = 400f
    }

}
