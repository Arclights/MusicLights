package com.arclights.musiclights.core.amplification

import ddf.minim.analysis.FFT

interface Amplifier {
    fun update(fft: FFT)
    fun getLevel(index: Int): Float
    fun reset()
    fun size(): Int
    fun setLevel(index: Int, level: Float)
}