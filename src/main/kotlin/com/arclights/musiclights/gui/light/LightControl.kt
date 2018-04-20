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
import javafx.scene.paint.Color

class LightControl(lightNbr: Int, lightRig: LightRig, color: Color) : GridPane() {

    init {
        vgap = 25.0

        val light = Light(color, lightNbr, lightRig)
        light.widthProperty().bind(widthProperty())
        val powerButton = PowerButton(lightRig, lightNbr)
        val slider = AmplificationSlider(lightRig, lightNbr)

        add(light,0,0)
        add(powerButton, 0, 1)
        add(slider, 0, 2)
    }
}

class PowerButton(lightRig: LightRig, lightNbr: Int) : Button() {
    private var powerOn = true

    init {
        text = "OFF"
        onAction = EventHandler { _ ->
            if (powerOn) {
                powerOn = false
                lightRig.turnPowerOff(lightNbr)
                text = "ON"
            } else {
                powerOn = true
                lightRig.turnPowerOn(lightNbr)
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

class AmplificationSlider(lightRig: LightRig, lightNbr: Int) : TitledPane() {
    init {
        val slider = Slider(5.0, 20.0, 10.0)
        slider.majorTickUnit = 5.0
        slider.minorTickCount = 1
        slider.isShowTickMarks = true
        slider.isShowTickLabels = true
        slider.orientation = Orientation.VERTICAL
        slider.valueProperty().addListener({ _, _, newVal -> lightRig.setIndividualLevel(lightNbr, newVal.toFloat() * 0.1f) })

        val box = VBox()
        box.children.addAll(ResetButton(slider), slider)
        box.alignment = Pos.CENTER

        text = "Amplification"
        content = box
        isCollapsible = false
    }
}