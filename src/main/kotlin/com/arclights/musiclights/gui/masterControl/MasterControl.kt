package com.arclights.musiclights.gui.masterControl

import com.arclights.musiclights.core.LightRig
import javafx.scene.layout.GridPane
import javafx.stage.Stage

class MasterControl(
    lightRig: LightRig,
    stage: Stage
) : GridPane() {

    init {
        vgap = 25.0

        val levelerChooser = LevelerChooser(stage, lightRig)
        val filterChooser = FilterChooser(lightRig.config)
        val amplitudeSlider = AmplitudeSlider(lightRig.config)

        add(levelerChooser, 0, 0)
        add(filterChooser, 0, 1)
        add(amplitudeSlider, 0, 2)
    }

}
