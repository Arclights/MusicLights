package com.arclights.musiclights.core.amplification

import ddf.minim.analysis.FFT

class ConstantAmplifier(nbrOfLights: Int) : Amplifier {

    private val levels = MutableList(nbrOfLights) { 1f }

    override fun update(fft: FFT): List<Float> = levels

    override fun reset() = levels.fill(1f)

    override fun size(): Int = levels.size

    override fun setLevel(index: Int, level: Float) {
        levels[index] = level
    }
}
