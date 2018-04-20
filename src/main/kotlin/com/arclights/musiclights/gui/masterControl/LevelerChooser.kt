package com.arclights.musiclights.gui.masterControl

import com.arclights.musiclights.core.LightRig
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.control.RadioButton
import javafx.scene.control.TitledPane
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.GridPane
import javafx.stage.FileChooser
import javafx.stage.Stage

class LevelerChooser(stage: Stage, lightRig: LightRig) : TitledPane() {
    init {
        text = "Amplification Leveler"
        isCollapsible = false

        val leveler = GridPane()

        val group = ToggleGroup()

        val resetAdaptiveAmpButton = ResetAdaptiveAmpButton(lightRig)
        val openFileButton = OpenFileButton(stage, lightRig)

        val fromFile = LevelerRadioButton(
                "From file",
                false,
                true,
                false,
                resetAdaptiveAmpButton,
                openFileButton,
                lightRig
        )
        fromFile.isSelected = true
        fromFile.toggleGroup = group

        val adaptiveAmplification = LevelerRadioButton(
                "Adaptive Amplification",
                true,
                false,
                true,
                resetAdaptiveAmpButton,
                openFileButton,
                lightRig
        )
        adaptiveAmplification.toggleGroup = group

        leveler.add(fromFile, 0, 0)
        leveler.add(openFileButton, 0, 1)
        leveler.add(adaptiveAmplification, 0, 2)
        leveler.add(resetAdaptiveAmpButton, 0, 3)

        GridPane.setMargin(openFileButton, Insets(0.0, 0.0, 10.0, 0.0))

        content = leveler
    }
}

class LevelerRadioButton(
        text: String,
        useAdaptiveAmplification: Boolean,
        disableResetButton: Boolean,
        disableOpenFileButton: Boolean,
        resetAdaptiveAmpButton: ResetAdaptiveAmpButton,
        openFileButton: OpenFileButton,
        lightRig: LightRig
) : RadioButton() {
    init {
        this.text = text
        onAction = EventHandler { _ ->
            lightRig.useAdaptiveAmplification(useAdaptiveAmplification)
            resetAdaptiveAmpButton.isDisable = disableResetButton
            openFileButton.isDisable = disableOpenFileButton
        }
    }
}

class ResetAdaptiveAmpButton(lightRig: LightRig) : Button() {
    init {
        text = "Reset"
        isDisable = true
        onAction = EventHandler { _ -> lightRig.resetAdaptiveAmp(true) }
    }
}

class OpenFileButton(stage: Stage, lightRig: LightRig) : Button() {
    init {
        text = "Open"
        onAction = EventHandler { _ ->
            text = if (text.equals("Open")) {
                openFile(stage, lightRig)
                "Reset"
            } else {
                lightRig.resetAmpLeveler()
                "Open"
            }
        }
    }

    private fun openFile(stage: Stage, lightRig: LightRig) {
        with(FileChooser().showOpenDialog(stage)) {
            when (this) {
                null -> return
                else -> {
                    this.bufferedReader().useLines {
                        it
                                .first()
                                .substring(1, it.first().length - 1)
                                .split(",")
                                .map { it.toFloat() }
                                .let { lightRig.setAmpLeveler(it.toFloatArray()) }
                    }
                }
            }
        }

    }
}