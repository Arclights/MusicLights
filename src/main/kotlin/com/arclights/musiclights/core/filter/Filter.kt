package com.arclights.musiclights.core.filter

import ddf.minim.AudioInput
import ddf.minim.analysis.FFT

interface Filter {
    fun filter(fft: FFT, input: AudioInput): List<Float>
}