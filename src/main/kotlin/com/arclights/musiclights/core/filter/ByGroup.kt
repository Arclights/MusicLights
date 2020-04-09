package com.arclights.musiclights.core.filter

import ddf.minim.AudioInput
import ddf.minim.analysis.FFT

class ByGroup(private val nbrOfGroups: Int) : Filter {
    override fun filter(fft: FFT, input: AudioInput): List<Float> {
        val spectrumWidth = fft.specSize() / 2
        val bandsPerGroup = spectrumWidth / nbrOfGroups
        return (0 until nbrOfGroups)
            .map {
                (it * bandsPerGroup until (it + 1) * bandsPerGroup)
                    .map(fft::getBand)
                    .max()!!
            }
    }
}