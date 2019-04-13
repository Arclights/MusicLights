package com.arclights.musiclights.core

import com.arclights.musiclights.core.amplification.Amplifier
import com.arclights.musiclights.core.listeners.FREQUENCY_UPDATE
import com.arclights.musiclights.core.listeners.FrequencyChangeListener
import com.arclights.musiclights.core.listeners.LIGHT_UPDATE
import com.arclights.musiclights.core.listeners.LightChangeListener
import ddf.minim.analysis.FFT
import java.beans.PropertyChangeSupport

class Light(
        private val fft: FFT,
        private val lightNbr: Int
) {
    private val spectrumStart = fft.specSize() / 2 / 10 * lightNbr
    private val spectrumEnd = fft.specSize() / 2 / 10 * (lightNbr + 1)
    private val spectrumSize = spectrumEnd - spectrumStart

    private val spectrum = (spectrumStart until spectrumEnd)
    private val turnedOffSpectrumValues = FloatArray(spectrumSize) { 0.0f }
    private var isPoweredOn: Boolean = true
    private var individualAdjustmentLevel: Float = 1.0f
    private var level: Float = 0.0f
    private val spectrumValues = turnedOffSpectrumValues

    private val propertyChangeSupport = PropertyChangeSupport(this)

    fun addLightChangeListener(listener: LightChangeListener) {
        propertyChangeSupport.addPropertyChangeListener(listener)
    }

    fun addFrequencyChangerListener(listener: FrequencyChangeListener) {
        propertyChangeSupport.addPropertyChangeListener(listener)
    }

    fun updateLevel(masterLevel: Float, filterLevels: List<Float>, amplifier: Amplifier) {
        val tempLevel = level
        val tempSpectrum = spectrumValues.copyOf()
        if (isPoweredOn) {
            level = filterLevels[lightNbr] * masterLevel * individualAdjustmentLevel * amplifier.getLevel(lightNbr)
            updateSpectrumValues()
        } else {
            level = 0.0f
            spectrumValues.fill(0.0f)
        }
        propertyChangeSupport.firePropertyChange(LIGHT_UPDATE, tempLevel, level)
        propertyChangeSupport.firePropertyChange(FREQUENCY_UPDATE, tempSpectrum, spectrumValues)
    }

    private fun updateSpectrumValues() {
        spectrum.forEachIndexed { index, spec ->
            spectrumValues[index] = fft.getBand(spec)
        }
    }

    fun setIndividualAdjustmentLevel(level: Float) {
        this.individualAdjustmentLevel = level
    }

    fun turnOff() {
        isPoweredOn = false
    }

    fun turnOn() {
        isPoweredOn = true
    }
}