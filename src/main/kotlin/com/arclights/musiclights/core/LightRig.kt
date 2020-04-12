package com.arclights.musiclights.core

import ddf.minim.AudioInput
import ddf.minim.Minim
import ddf.minim.analysis.FFT
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream

class LightRig(val nbrOfLights: Int) {
    private val minim: Minim = Minim(this)
    private val input: AudioInput
    private val fft: FFT

    val config: LightConfig

    val lights: List<Light>

    init {
        input = minim.getLineIn(Minim.STEREO)
        fft = FFT(input.bufferSize(), input.sampleRate())
        fft.linAverages(nbrOfLights)
        config = LightConfig(nbrOfLights, input.bufferSize(), input.sampleRate(), fft.specSize())
        lights = (0 until nbrOfLights).map { Light(fft, it) }
    }

    fun update() {
        fft.forward(input.mix)

        val filter = config.getFilter()

        val filterLevel = filter.filter(fft, input)

        val amplifier = config.getAmplifier()

        amplifier.update(fft)

        lights.forEach { it.updateLevel(config.masterLevel, filterLevel, amplifier) }
    }

    fun stop() {
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
