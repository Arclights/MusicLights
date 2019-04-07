package com.arclights.musiclights.gui.light

import com.arclights.musiclights.core.LightRig
import com.arclights.musiclights.core.listeners.LIGHT_UPDATE
import com.arclights.musiclights.core.listeners.LightChangeListener
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import java.beans.PropertyChangeEvent

data class LightView(
        private val color: Color,
        private val index: Int,
        private val lightRig: LightRig
) : LightChangeListener, Canvas() {

    private val HEIGHT = 100.0

    init {
        height = HEIGHT
        lightRig.addLightChangeListener(this)
    }

    override fun propertyChange(evt: PropertyChangeEvent?) {
        if (evt?.propertyName == LIGHT_UPDATE) {
            draw((evt.newValue as FloatArray)[index].toDouble())
        }
    }

    fun draw(level: Double) {
        draw(Color.BLACK, 1.0)
        draw(color, level)
    }

    private fun draw(color: Color, alpha: Double) {
        graphicsContext2D.fill = color
        graphicsContext2D.globalAlpha = alpha
        graphicsContext2D.fillRect(0.0, 0.0, width, HEIGHT)
    }

}