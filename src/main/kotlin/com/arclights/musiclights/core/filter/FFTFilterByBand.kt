package com.arclights.musiclights.core.filter

import ddf.minim.AudioInput
import ddf.minim.analysis.FFT

class FFTFilterByBand(private val nbrOfGroups: Int) : Filter {
    override fun filter(fft: FFT, input: AudioInput): List<Float> {
        val spectrumWidth = fft.specSize() / 2
        val bandsPerGroup = spectrumWidth / nbrOfGroups
        var lastBandLastFreq = 0
        return (0 until nbrOfGroups)
                .map {
                    val largestForCurrentSection =
                            (lastBandLastFreq until lastBandLastFreq + bandsPerGroup)
                                    .map { fft.getBand(it) }
                                    .max()!!
                    lastBandLastFreq += bandsPerGroup
                    largestForCurrentSection
                }
    }

}
