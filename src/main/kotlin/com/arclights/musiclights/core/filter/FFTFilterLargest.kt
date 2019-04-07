package com.arclights.musiclights.core.filter

import ddf.minim.AudioInput
import ddf.minim.analysis.FFT

class FFTFilterLargest(private val nbrOfGroups: Int) : Filter {
    override fun filter(fft: FFT, input: AudioInput): List<Float> = (0 until nbrOfGroups).map { fft.getAvg(it) }

}
