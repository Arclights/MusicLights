package com.arclights.musiclights.core

import com.arclights.musiclights.core.amplification.AdaptiveAmplifier
import ddf.minim.AudioInput
import ddf.minim.Minim
import ddf.minim.analysis.FFT
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream

class LightRig(val nbrOfLights: Int) {
    private var resetAdaptLeveler = false
    private val minim: Minim = Minim(this)
    private val input: AudioInput
    private val fft: FFT
    private val adaptAmp: AdaptiveAmplifier

    val config: LightConfig

    private val currentLevels: FloatArray
    val lights: List<Light>

    /**
     * Used by Arduino
     */
    private var message: Int = 0

    init {
        input = minim.getLineIn(Minim.STEREO)
        fft = FFT(input.bufferSize(), input.sampleRate())
        fft.linAverages(nbrOfLights)
        adaptAmp = AdaptiveAmplifier(nbrOfLights)
        config = LightConfig(nbrOfLights, input.bufferSize(), input.sampleRate(), fft.specSize())
        lights = (0 until nbrOfLights).map { Light(fft, it) }

        message = -1

        currentLevels = FloatArray(nbrOfLights)
    }

    fun update() {
        fft.forward(input.mix)

        val filter = config.getFilter()

        val filterLevel = filter.filter(fft, input)

        if (resetAdaptLeveler) {
            adaptAmp.reset()
            resetAdaptLeveler = false
        }

        val amplifier = config.getAmplifier()

        amplifier.update(fft)

        lights.forEach { it.updateLevel(config.masterLevel, filterLevel, amplifier) }
    }

    fun stop() {
        minim.stop()
    }

    fun sketchPath(fileName: String): String {
        return ""
    }

    @Throws(FileNotFoundException::class)
    fun createInput(fileName: String): InputStream {
        return FileInputStream(fileName)
    }

    fun resetAdaptiveAmp(reset: Boolean) {
        resetAdaptLeveler = reset
    }


    companion object {
        const val MAX_AMPLITUDE = 400f
    }

}
