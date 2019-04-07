package com.arclights.musiclights.gui.light

import com.arclights.musiclights.core.LightRig
import com.arclights.musiclights.core.listeners.LIGHT_UPDATE
import com.arclights.musiclights.core.listeners.LightChangeListener
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import java.beans.PropertyChangeEvent
import java.util.*
import kotlin.math.min


class FrequencyView(
        private val lightRig: LightRig,
        private val lightNbr: Int,
        private val color: Color
) : LightChangeListener, Canvas() {
    private val HEIGHT = 200.0
    private val scaleFactor = (HEIGHT / LightRig.MAX_AMPLITUDE).toFloat()

    init {
        height = HEIGHT
        lightRig.addLightChangeListener(this)
    }

    private fun draw() {
        val spikeWidth = width / spectrumSize()

        graphicsContext2D.fill = Color.BLACK
        graphicsContext2D.fillRect(0.0, 0.0, width, height)

        graphicsContext2D.fill = color
        (spectrumStart()..spectrumEnd()).forEachIndexed { spikeNbr, spectrumIndex ->
            val frequencyAmplitude = lightRig.getBand(spectrumIndex)
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
        if(evt?.propertyName == LIGHT_UPDATE){
            draw()
        }
    }

    @Deprecated("Get from Light object instead", ReplaceWith("light.?"))
    private fun spectrumStart() = lightRig.spectrumSize / 2 / 10 * lightNbr

    @Deprecated("Get from Light object instead", ReplaceWith("light.?"))
    private fun spectrumEnd() = lightRig.spectrumSize / 2 / 10 * (lightNbr + 1)

    private fun spectrumSize() = spectrumEnd() - spectrumStart()
}