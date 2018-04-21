package com.arclights.musiclights.core.filter

import ddf.minim.analysis.FFT

class FFTFilter {
    companion object {
        fun getLargest(fft: FFT, nbrOfGroups: Int): List<Float> = (0 until nbrOfGroups).map { fft.getAvg(it) }

        fun getByGroup(fft: FFT, nbrOfGroups: Int): List<Float> {
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
}