package com.arclights.musiclights.gui.light

import com.arclights.musiclights.core.Light
import com.arclights.musiclights.core.LightRig
import com.arclights.musiclights.core.listeners.FREQUENCY_UPDATE
import com.arclights.musiclights.core.listeners.FrequencyChangeListener
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import java.beans.PropertyChangeEvent
import java.util.Arrays
import kotlin.math.min


class FrequencyView(
        light: Light,
        private val color: Color
) : FrequencyChangeListener, Canvas() {

    companion object {
        private const val HEIGHT = 200.0
    }

    private val scaleFactor = (HEIGHT / LightRig.MAX_AMPLITUDE).toFloat()

    init {
        height = HEIGHT
        light.addFrequencyChangerListener(this)
    }

    private fun draw(frequencyValues: FloatArray) {
        val spikeWidth = width / frequencyValues.size

        graphicsContext2D.fill = Color.BLACK
        graphicsContext2D.fillRect(0.0, 0.0, width, height)

        graphicsContext2D.fill = color
        frequencyValues.forEachIndexed { spikeNbr, frequencyAmplitude ->
            graphicsContext2D.fillRect(
                    spikeNbr * spikeWidth,
                    height - scaleAmplitude(frequencyAmplitude),
                    spikeWidth,
                    height
            )
        }
    }

    private fun scaleAmplitude(amplitude: Float) = min(amplitude * scaleFactor, LightRig.MAX_AMPLITUDE)

    override fun propertyChange(evt: PropertyChangeEvent?) {
        if (evt?.propertyName == FREQUENCY_UPDATE) {
            println(Arrays.toString(evt.newValue as FloatArray))
            draw(evt.newValue as FloatArray)
        }
    }
}