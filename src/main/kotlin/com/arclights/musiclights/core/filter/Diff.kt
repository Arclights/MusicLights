package com.arclights.musiclights.core.filter

import com.arclights.musiclights.core.LightRig
import ddf.minim.AudioInput
import ddf.minim.analysis.FFT

class Diff(private val nbrOfGroups: Int, specSize: Int) : Filter {
    private val prevLevel = FloatArray(specSize / 2) { 0.0f }

    override fun filter(fft: FFT, input: AudioInput): List<Float> {
        val spectrumWidth = fft.specSize() / 2
        val bandsPerGroup = spectrumWidth / nbrOfGroups
        return (0 until nbrOfGroups)
            .map {
                var level = 0.0f
                (it * bandsPerGroup until (it + 1) * bandsPerGroup)
                    .forEach { band ->
                        level = if (fft.getBand(band) > prevLevel[band]) LightRig.MAX_AMPLITUDE else level
                        prevLevel[band] = fft.getBand(band)
                    }
                level
            }
    }
}