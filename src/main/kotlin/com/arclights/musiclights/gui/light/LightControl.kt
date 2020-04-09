package com.arclights.musiclights.gui.light

import com.arclights.musiclights.core.Light
import javafx.event.EventHandler
import javafx.geometry.HPos
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Slider
import javafx.scene.control.TitledPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color

class LightControl(light: Light, color: Color) : GridPane() {

    init {
        vgap = 25.0

        val freqView = FrequencyView(light, color)
        freqView.widthProperty().bind(widthProperty())
        val lightView = LightView(light, color)
        lightView.widthProperty().bind(widthProperty())
        val powerButton = PowerButton(light)
        val slider = AmplificationSlider(light)

        add(freqView, 0, 0)
        add(lightView, 0, 1)
        add(powerButton, 0, 2)
        add(slider, 0, 3)
    }
}

class PowerButton(light: Light) : Button() {
    private var powerOn = true

    init {
        text = "OFF"
        onAction = EventHandler {
            if (powerOn) {
                powerOn = false
                light.turnOff()
                text = "ON"
            } else {
                powerOn = true
                light.turnOn()
                text = "OFF"
            }
        }
        GridPane.setHalignment(this, HPos.CENTER)
    }
}

class ResetButton(slider: Slider) : Button() {
    init {
        text = "Reset"
        GridPane.setHalignment(this, HPos.CENTER)
        onAction = EventHandler { slider.value = 10.0 }
    }
}

class AmplificationSlider(light: Light) : TitledPane() {
    init {
        val slider = Slider(5.0, 20.0, 10.0)
        slider.majorTickUnit = 5.0
        slider.minorTickCount = 1
        slider.isShowTickMarks = true
        slider.isShowTickLabels = true
        slider.orientation = Orientation.VERTICAL
        slider.valueProperty()
            .addListener { _, _, newVal -> light.setIndividualAdjustmentLevel(newVal.toFloat() * 0.1f) }

        val box = VBox()
        box.children.addAll(ResetButton(slider), slider)
        box.alignment = Pos.CENTER

        text = "Amplifier"
        content = box
        isCollapsible = false
    }
}