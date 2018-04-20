package com.arclights.musiclights.core.amplification

import com.arclights.musiclights.core.LightRig
import ddf.minim.analysis.FFT

class AdaptiveAmplification(val nbrOfGroups: Int) {
    private val noiseLine = doubleArrayOf(454.1341, 985.9705, 936.9573, 2589.1023, 2526.0164, 4455.165, 3321.9583, 4226.0024, 10840.993, 11465.399)

    private val levelers = FloatArray(nbrOfGroups, { _ -> 0.0f })

    fun setLeveler(fft: FFT) {
        val spectrumWidth = fft.specSize() / 2
        val bandsPerGroup = spectrumWidth / nbrOfGroups
        (0 until nbrOfGroups).forEach {
            val startBand = it * bandsPerGroup
            val endBand = startBand + bandsPerGroup
            val largestAmpInGroup = (startBand until endBand)
                    .map { fft.getBand(it) }
                    .max()!!

            val normalizedAmpInGroup = LightRig.MAX_AMPLITUDE / largestAmpInGroup
            if (normalizedAmpInGroup < levelers[it]) {
                levelers[it] = normalizedAmpInGroup
            } else if (levelers[it] == 0.0f && normalizedAmpInGroup < noiseLine[it]) {
                levelers[it] = normalizedAmpInGroup
            }
        }
    }

    fun getLeveler(index: Int) = levelers[index]

    fun reset() = levelers.fill(0.0f)
}