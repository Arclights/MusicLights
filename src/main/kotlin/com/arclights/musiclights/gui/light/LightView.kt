package com.arclights.musiclights.gui.light

import com.arclights.musiclights.core.Light
import com.arclights.musiclights.core.listeners.LIGHT_UPDATE
import com.arclights.musiclights.core.listeners.LightChangeListener
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import java.beans.PropertyChangeEvent

class LightView(
    light: Light,
    private val color: Color
) : LightChangeListener, Canvas() {

    companion object {
        private const val HEIGHT = 100.0
    }

    init {
        height = HEIGHT
        light.addLightChangeListener(this)
    }

    override fun propertyChange(evt: PropertyChangeEvent?) {
        if (evt?.propertyName == LIGHT_UPDATE) {
            draw((evt.newValue as Float).toDouble())
        }
    }

    private fun draw(level: Double) {
        draw(Color.BLACK, 1.0)
        draw(color, level)
    }

    private fun draw(color: Color, alpha: Double) {
        graphicsContext2D.fill = color
        graphicsContext2D.globalAlpha = alpha
        graphicsContext2D.fillRect(0.0, 0.0, width, HEIGHT)
    }

}