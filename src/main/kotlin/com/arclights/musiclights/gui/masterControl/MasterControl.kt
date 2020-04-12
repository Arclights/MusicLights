package com.arclights.musiclights.gui.masterControl

import com.arclights.musiclights.core.LightConfig
import com.arclights.musiclights.core.LightRig
import javafx.scene.layout.GridPane
import javafx.stage.Stage

class MasterControl(
    config: LightConfig,
    stage: Stage
) : GridPane() {

    init {
        vgap = 25.0

        val levelerChooser = LevelerChooser(stage, config)
        val filterChooser = FilterChooser(config)
        val amplitudeSlider = AmplitudeSlider(config)

        add(levelerChooser, 0, 0)
        add(filterChooser, 0, 1)
        add(amplitudeSlider, 0, 2)
    }

}
