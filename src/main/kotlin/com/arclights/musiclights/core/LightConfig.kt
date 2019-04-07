package com.arclights.musiclights.core

import com.arclights.musiclights.core.amplification.AdaptiveAmplifier
import com.arclights.musiclights.core.amplification.Amplifier
import com.arclights.musiclights.core.amplification.ConstantAmplifier
import com.arclights.musiclights.core.filter.BeatByEnergy
import com.arclights.musiclights.core.filter.BeatByFrequency
import com.arclights.musiclights.core.filter.ByGroup
import com.arclights.musiclights.core.filter.ByGroupAverage
import com.arclights.musiclights.core.filter.Diff
import com.arclights.musiclights.core.filter.FFTFilterByBand
import com.arclights.musiclights.core.filter.FFTFilterLargest
import com.arclights.musiclights.core.filter.Filter
import com.arclights.musiclights.core.filter.FilterType

class LightConfig(
        private val nbrOfLights: Int,
        bufferSize: Int,
        sampleRate: Float,
        specSize: Int
) {
    val powerOn: BooleanArray = BooleanArray(nbrOfLights) { true }


    var masterLevel = 5f
    var filter = FilterType.BY_GROUP
    var useAdapAmp: Boolean = false
    var beatWithLevels: Boolean = false

    private val byGroup = ByGroup(nbrOfLights)
    private val beatByFrequency = BeatByFrequency(nbrOfLights, bufferSize, sampleRate)
    private val beatByEnergy = BeatByEnergy(nbrOfLights)
    private val diff = Diff(nbrOfLights, specSize)
    private val byGroupAverage = ByGroupAverage(nbrOfLights)
    private val fftFilterLargest = FFTFilterLargest(nbrOfLights)
    private val fftFilterByBand = FFTFilterByBand(nbrOfLights)

    private val adaptiveAmplifier = AdaptiveAmplifier(nbrOfLights)
    private val constantAmplifier = ConstantAmplifier(nbrOfLights)

    fun getFilter(): Filter = when (filter) {
        FilterType.BY_GROUP -> byGroup
        FilterType.BEAT_BY_FREQ -> beatByFrequency
        FilterType.BEAT_BY_ENERGY -> beatByEnergy
        FilterType.DIFF -> diff
        FilterType.BY_GROUP_AVRAGE -> byGroupAverage
        FilterType.FFT_LARGEST -> fftFilterLargest
        FilterType.FFT_BY_BAND -> fftFilterByBand
        FilterType.ALT_BASS -> TODO()
    }

    /**
     * Turns on the specified light so it is seen input the LightBar
     *
     * @param lightNbr The number of the light input question
     */
    fun turnPowerOn(lightNbr: Int) {
        powerOn[lightNbr] = true
    }

    /**
     * Turns off the specified light
     *
     * @param lightNbr The number of the light input question
     */
    fun turnPowerOff(lightNbr: Int) {
        powerOn[lightNbr] = false
    }

    fun getAmplifier(): Amplifier = if (useAdapAmp) {
        adaptiveAmplifier
    } else {
        constantAmplifier
    }

    /**
     * Sets the amplifier leveler for all the lights. The input is adjusted to
     * fit the amount of lights used by taking the largest main.java.amplification of the
     * bands belonging to each light and assigning them to respective light
     *
     * @param leveler The levelers for each band
     */
    fun setAmpLeveler(leveler: FloatArray) {
        val amplifier = getAmplifier()
        val bandsPerGroup = leveler.size / amplifier.size()
        (0 until amplifier.size()).forEach {
            var largestAmpInGroup = 0f
            for (j in it * bandsPerGroup until (it + 1) * bandsPerGroup) {
                if (leveler[j] > largestAmpInGroup) {
                    largestAmpInGroup = leveler[j]
                }
            }
            amplifier.setLevel(it, 255 / largestAmpInGroup)
        }
    }

}