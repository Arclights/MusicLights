package com.arclights.musiclights.gui.masterControl

import com.arclights.musiclights.core.LightConfig
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
        text = "Amplifier Leveler"
        isCollapsible = false

        val leveler = GridPane()

        val group = ToggleGroup()

        val resetAdaptiveAmpButton = ResetAdaptiveAmpButton(lightRig)
        val openFileButton = OpenFileButton(stage, lightRig.config)

        val fromFile = LevelerRadioButton(
            text = "From file",
            useAdaptiveAmplification = false,
            disableResetButton = true,
            disableOpenFileButton = false,
            resetAdaptiveAmpButton = resetAdaptiveAmpButton,
            openFileButton = openFileButton,
            config = lightRig.config
        )
        fromFile.isSelected = true
        fromFile.toggleGroup = group

        val adaptiveAmplification = LevelerRadioButton(
            text = "Adaptive Amplifier",
            useAdaptiveAmplification = true,
            disableResetButton = false,
            disableOpenFileButton = true,
            resetAdaptiveAmpButton = resetAdaptiveAmpButton,
            openFileButton = openFileButton,
            config = lightRig.config
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
    config: LightConfig
) : RadioButton() {
    init {
        this.text = text
        onAction = EventHandler {
            config.useAdaptiveAmp = useAdaptiveAmplification
            resetAdaptiveAmpButton.isDisable = disableResetButton
            openFileButton.isDisable = disableOpenFileButton
        }
    }
}

class ResetAdaptiveAmpButton(lightRig: LightRig) : Button() {
    init {
        text = "Reset"
        isDisable = true
        onAction = EventHandler { lightRig.resetAdaptiveAmp(true) }
    }
}

class OpenFileButton(stage: Stage, config: LightConfig) : Button() {
    init {
        text = "Open"
        onAction = EventHandler {
            text = if (text == "Open") {
                openFile(stage, config)
                "Reset"
            } else {
                config.getAmplifier().reset()
                "Open"
            }
        }
    }

    private fun openFile(stage: Stage, config: LightConfig) {
        with(FileChooser().showOpenDialog(stage)) {
            when (this) {
                null -> return
                else -> {
                    this.bufferedReader().useLines { lines ->
                        lines
                            .first()
                            .substring(1, lines.first().length - 1)
                            .split(",")
                            .map(String::toFloat)
                            .let { config.setAmpLeveler(it.toFloatArray()) }
                    }
                }
            }
        }

    }
}