package com.arclights.musiclights.core.filter

import com.arclights.musiclights.core.LightRig
import ddf.minim.AudioInput
import ddf.minim.analysis.BeatDetect
import ddf.minim.analysis.FFT

class BeatByEnergy(private val nbrOfLights: Int) : Filter {
    private val beatDetector = BeatDetect()

    override fun filter(fft: FFT, input: AudioInput): List<Float> {
        beatDetector.detect(input.mix)
        val bandsPerLight = beatDetector.detectSize() / nbrOfLights
        return (0 until nbrOfLights)
            .map {
                if (beatDetector.isRange(it * bandsPerLight, (it + 1) * bandsPerLight - 1, 2))
                    LightRig.MAX_AMPLITUDE
                else
                    0.0f
            }
    }
}