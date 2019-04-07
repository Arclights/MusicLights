package com.arclights.musiclights.core

import com.arclights.musiclights.core.amplification.AdaptiveAmplifier
import com.arclights.musiclights.core.listeners.LIGHT_UPDATE
import com.arclights.musiclights.core.listeners.LightChangeListener
import ddf.minim.AudioInput
import ddf.minim.Minim
import ddf.minim.analysis.FFT
import java.beans.PropertyChangeSupport
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream

class LightRig(val nbrOfLights: Int) {
    private val levels: FloatArray = FloatArray(nbrOfLights) { 1f }
    private var resetAdaptLeveler = false
    private val minim: Minim = Minim(this)
    private val input: AudioInput
    private val fft: FFT
    private val adaptAmp: AdaptiveAmplifier

    val config: LightConfig

    private val currentLevels: FloatArray

    private val propertyChangeSupport = PropertyChangeSupport(this)

    /**
     * Used by Arduino
     */
    private var message: Int = 0

    val spectrumSize: Int
        get() = fft.specSize()

    init {
        input = minim.getLineIn(Minim.STEREO)
        fft = FFT(input.bufferSize(), input.sampleRate())
        fft.linAverages(nbrOfLights)
        adaptAmp = AdaptiveAmplifier(nbrOfLights)
        config = LightConfig(nbrOfLights, input.bufferSize(), input.sampleRate(), fft.specSize())

        message = -1

        currentLevels = FloatArray(nbrOfLights)
    }

    fun addLightChangeListener(listener: LightChangeListener) {
        propertyChangeSupport.addPropertyChangeListener(listener)
    }

    fun update() {
        fft.forward(input.mix)

        val filter = config.getFilter()

        val largest = filter.filter(fft, input)

        if (resetAdaptLeveler) {
            adaptAmp.reset()
            resetAdaptLeveler = false
        }

        val amplifier = config.getAmplifier()

        amplifier.update(fft)

        val tempLevels = currentLevels.copyOf()

        for (i in 0 until nbrOfLights) {
            if (config.powerOn[i]) {
                val amplificationLevel = amplifier.getLevel(i)

                currentLevels[i] = (largest[i] * config.masterLevel * levels[i] * amplificationLevel)
            } else {
                currentLevels[i] = 0f
            }
        }

        propertyChangeSupport.firePropertyChange(LIGHT_UPDATE, tempLevels, currentLevels)
    }

    fun getBand(i: Int): Float {
        return fft.getBand(i)
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

    /**
     * Sets the level a specific light
     *
     * @param lightNbr The number of the light for which the level is going to be set
     * @param level    The level the light is going t be set to
     */
    fun setIndividualLevel(lightNbr: Int, level: Float) {
        levels[lightNbr] = level
    }

    fun resetAdaptiveAmp(reset: Boolean) {
        resetAdaptLeveler = reset
    }


    companion object {
        const val MAX_AMPLITUDE = 400f
    }

}
