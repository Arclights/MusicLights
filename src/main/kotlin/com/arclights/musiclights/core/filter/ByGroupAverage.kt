package com.arclights.musiclights.core.filter

import ddf.minim.analysis.FFT

class ByGroupAverage {
    companion object {
        fun filter(fft: FFT, nbrOfGroups: Int): List<Float> {
            val spectrumWidth = fft.specSize() / 2
            val bandsPerGroup = spectrumWidth / nbrOfGroups
            return (0 until nbrOfGroups)
                    .map {
                        (it * bandsPerGroup until (it + 1) * bandsPerGroup)
                                .map { fft.getBand(it) }
                                .sum() / bandsPerGroup
                    }
        }
    }
}