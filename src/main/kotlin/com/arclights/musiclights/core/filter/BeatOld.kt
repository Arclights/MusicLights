package com.arclights.musiclights.core.filter

import com.arclights.musiclights.core.LightConfig
import ddf.minim.AudioInput
import ddf.minim.analysis.FFT

class BeatOld(
    private val config: LightConfig,
    private val nbrOfLights: Int
) : Filter {
    private val sense: Float = 2F
    private val E: Array<FloatArray> = Array(32) { FloatArray(43) }
    private val nbrOfSubbands: Int = 32
    private val bandsPerGroup: Int = (32 / nbrOfLights)

    override fun filter(fft: FFT, input: AudioInput): List<Float> {
        val Es: List<Float> = (0 until nbrOfSubbands).map { subBand ->
            (subBand * 16 until (subBand + 1) * 16)
                .map { k -> fft.getBand(k) }
                .sum()
                .div(16)
        }

        val res: List<Float> = (0 until nbrOfLights).map { lightNbr ->
            val (largestAmpInGroupIndex, largestAmpInGroup) = (lightNbr * bandsPerGroup until (lightNbr + 1) * bandsPerGroup)
                .map { it to Es[it] }
                .maxBy { it.second } ?: 0 to 0F

            if (largestAmpInGroup > sense * getAverageBufferEnergy(largestAmpInGroupIndex)) {
                if (config.beatWithLevels) {
                    largestAmpInGroup
                } else {
                    1F
                }
            } else {
                0F
            }
        }

        (0 until 32).forEach { i ->
            shiftBuffer(i)
            E[i][0] = Es[i]
        }

        return res
    }

    private fun getAverageBufferEnergy(band: Int): Float = E[band].sum().div(43)

    private fun shiftBuffer(band: Int) = (nbrOfSubbands - 1 downTo 1).forEach { i -> E[band][i] = E[band][i - 1] }
}
