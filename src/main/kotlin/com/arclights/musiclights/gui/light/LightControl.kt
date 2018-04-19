package com.arclights.musiclights.gui.light

import com.arclights.musiclights.core.LightRig
import javafx.event.EventHandler
import javafx.geometry.HPos
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Slider
import javafx.scene.control.TitledPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.VBox

class LightControl(controlNbr: Int, lightRig: LightRig) : GridPane() {

    init {
        vgap = 25.0

        val powerButton = PowerButton(lightRig, controlNbr)
        val slider = AmplificationSlider(lightRig, controlNbr)

        add(powerButton, 0, 0)
        add(slider, 0, 1)
    }
}

class PowerButton(lightRig: LightRig, controlNbr: Int) : Button() {
    private var powerOn = true

    init {
        text = "OFF"
        onAction = EventHandler { _ ->
            if (powerOn) {
                powerOn = false
                lightRig.turnPowerOff(controlNbr)
                text = "ON"
            } else {
                powerOn = true
                lightRig.turnPowerOn(controlNbr)
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
        onAction = EventHandler { _ -> slider.value = 10.0 }
    }
}

class AmplificationSlider(lightRig: LightRig, controlNbr: Int) : TitledPane() {
    init {
        val slider = Slider(5.0, 20.0, 10.0)
        slider.majorTickUnit = 5.0
        slider.minorTickCount = 1
        slider.isShowTickMarks = true
        slider.isShowTickLabels = true
        slider.orientation = Orientation.VERTICAL
        slider.valueProperty().addListener({ _, _, newVal -> lightRig.setIndividualLevel(controlNbr, newVal.toFloat() * 0.1f) })

        val box = VBox()
        box.children.addAll(ResetButton(slider), slider)
        box.alignment = Pos.CENTER

        text = "Amplification"
        content = box
        isCollapsible = false
    }
}