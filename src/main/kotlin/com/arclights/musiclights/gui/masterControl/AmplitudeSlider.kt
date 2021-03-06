package com.arclights.musiclights.gui.masterControl

import com.arclights.musiclights.core.LightConfig
import javafx.event.EventHandler
import javafx.geometry.HPos
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Slider
import javafx.scene.control.TitledPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.VBox

class AmplitudeSlider(config: LightConfig) : TitledPane() {
    private val slider: Slider = Slider(0.0, 1.0, 0.1)

    init {
        text = "Amplifier"
        isCollapsible = false

        slider.orientation = Orientation.VERTICAL
        slider.majorTickUnit = 0.1
        slider.minorTickCount = 1
        slider.isShowTickMarks = true
        slider.isShowTickLabels = true
        slider.valueProperty().addListener { _, _, newVal -> config.masterLevel = newVal.toFloat() }

        val box = VBox()
        box.children.addAll(ResetButton(this), slider)
        box.alignment = Pos.CENTER

        content = box
    }

    fun reset() {
        slider.value = 0.1
    }
}

class ResetButton(amplitudeSlider: AmplitudeSlider) : Button() {
    init {
        text = "Reset"
        onAction = EventHandler { amplitudeSlider.reset() }
        GridPane.setHalignment(this, HPos.CENTER)
    }
}