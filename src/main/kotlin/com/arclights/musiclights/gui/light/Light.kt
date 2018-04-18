package com.arclights.musiclights.gui.light

import com.arclights.musiclights.core.LightRig
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import java.util.*

data class Light(
        private val gc: GraphicsContext,
        private val color: Color,
        private val x: Double,
        private val width: Double,
        private val index: Int,
        private val lightRig: LightRig
) : Observer {

    init {
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
        gc.fill = color
        gc.globalAlpha = alpha
        gc.fillRect(x, 0.0, width, 100.0)
    }

}