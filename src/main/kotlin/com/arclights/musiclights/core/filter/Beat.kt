package com.arclights.musiclights.core.filter

import com.arclights.musiclights.core.LightRig
import ddf.minim.AudioInput
import ddf.minim.analysis.BeatDetect

class Beat(val lightRig: LightRig, val input: AudioInput) {
    private val beatDetector = BeatDetect(input.bufferSize(), input.sampleRate())

    fun filter(nbrOfLights: Int): List<Float> {
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