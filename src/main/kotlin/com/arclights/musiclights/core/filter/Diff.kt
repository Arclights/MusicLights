package com.arclights.musiclights.core.filter

import com.arclights.musiclights.core.LightRig
import ddf.minim.analysis.FFT

class Diff(val fft: FFT) {
    private val prevLevel = FloatArray(fft.specSize() / 2, { _ -> 0.0f })

    fun filter(nbrOfGroups: Int): List<Float> {
        val spectrumWidth = fft.specSize() / 2
        val bandsPerGroup = spectrumWidth / nbrOfGroups
        return (0 until nbrOfGroups)
                .map {
                    var level = 0.0f
                    (it * bandsPerGroup until (it + 1) * bandsPerGroup)
                            .forEach {
                                level = if (fft.getBand(it) > prevLevel[it]) LightRig.MAX_AMPLITUDE else level
                                prevLevel[it] = fft.getBand(it)
                            }
                    level
                }
    }
}