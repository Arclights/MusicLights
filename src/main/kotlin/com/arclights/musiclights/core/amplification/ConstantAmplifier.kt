package com.arclights.musiclights.core.amplification;

import ddf.minim.analysis.FFT

class ConstantAmplifier(nbrOfLights: Int) : Amplifier {

    private val levels: FloatArray = FloatArray(nbrOfLights) { 1f }

    override fun update(fft: FFT) {
        // Do nothing
    }

    override fun getLevel(index: Int): Float = levels[index]

    override fun reset() = levels.fill(1f)

    override fun size(): Int = levels.size

    override fun setLevel(index: Int, level: Float) {
        levels[index] = level
    }
}
