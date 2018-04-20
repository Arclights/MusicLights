package com.arclights.musiclights.gui.light

import com.arclights.musiclights.core.LightRig
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import java.util.*

data class LightView(
        private val color: Color,
        private val index: Int,
        private val lightRig: LightRig
) : Observer, Canvas() {

    private val HEIGHT = 100.0

    init {
        height = HEIGHT
        lightRig.addObserver(this)
    }

    override fun update(o: Observable?, arg: Any?) {
        draw(lightRig.currentLevels[index].toDouble())
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