package com.arclights.musiclights.gui.masterControl

import com.arclights.musiclights.core.LightConfig
import com.arclights.musiclights.core.filter.FilterType
import com.arclights.musiclights.core.filter.FilterType.BEAT_BY_ENERGY
import com.arclights.musiclights.core.filter.FilterType.BEAT_BY_FREQ
import com.arclights.musiclights.core.filter.FilterType.BEAT_OLD
import com.arclights.musiclights.core.filter.FilterType.BY_GROUP
import com.arclights.musiclights.core.filter.FilterType.BY_GROUP_AVERAGE
import com.arclights.musiclights.core.filter.FilterType.DIFF
import com.arclights.musiclights.core.filter.FilterType.FFT_BY_BAND
import com.arclights.musiclights.core.filter.FilterType.FFT_LARGEST
import javafx.event.EventHandler
import javafx.scene.control.CheckBox
import javafx.scene.control.RadioButton
import javafx.scene.control.TitledPane
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.GridPane

class FilterChooser(config: LightConfig) : TitledPane() {
    init {
        text = "Filters"
        isCollapsible = false

        val filters = GridPane()
        filters.hgap = 5.0
        filters.vgap = 5.0

        val group = ToggleGroup()

        val levelBox = LevelCheckBox(config)
        val byGroupRadioButton = FilterRadioButton("By group", true, BY_GROUP, levelBox, config)
        byGroupRadioButton.toggleGroup = group
        byGroupRadioButton.isSelected = true
        val byGroupAverageRadioButton = FilterRadioButton("By group average", true, BY_GROUP_AVERAGE, levelBox, config)
        byGroupAverageRadioButton.toggleGroup = group
        val fftLargestRadioButton = FilterRadioButton("FFT largest", true, FFT_LARGEST, levelBox, config)
        fftLargestRadioButton.toggleGroup = group
        val fftByBandRadioButton = FilterRadioButton("FFT by band", true, FFT_BY_BAND, levelBox, config)
        fftByBandRadioButton.toggleGroup = group
        val differenceRadioButton = FilterRadioButton("Difference", true, DIFF, levelBox, config)
        differenceRadioButton.toggleGroup = group
        val beatByFreqRadioButton = FilterRadioButton("Beat by frequency", false, BEAT_BY_FREQ, levelBox, config)
        beatByFreqRadioButton.toggleGroup = group
        val beatByEnergyRadioButton = FilterRadioButton("Beat by energy", false, BEAT_BY_ENERGY, levelBox, config)
        beatByEnergyRadioButton.toggleGroup = group
        val beatOldRadioButton = FilterRadioButton("Beat old", false, BEAT_OLD, levelBox, config)
        beatOldRadioButton.toggleGroup = group

        filters.add(byGroupRadioButton, 0, 0)
        filters.add(byGroupAverageRadioButton, 1, 0)
        filters.add(fftLargestRadioButton, 0, 1)
        filters.add(fftByBandRadioButton, 1, 1)
        filters.add(differenceRadioButton, 0, 2, 1, 2)
        filters.add(beatByFreqRadioButton, 1, 2)
        filters.add(beatByEnergyRadioButton, 1, 3)
        filters.add(beatOldRadioButton, 1, 4)
        filters.add(levelBox, 1, 5)

        content = filters
    }
}

class FilterRadioButton(
    text: String,
    disableLevelBox: Boolean,
    filterType: FilterType,
    levelBox: LevelCheckBox,
    config: LightConfig
) : RadioButton() {
    init {
        this.text = text
        onAction = EventHandler {
            levelBox.isDisable = disableLevelBox
            config.filter = filterType
        }
    }
}

class LevelCheckBox(config: LightConfig) : CheckBox() {
    init {
        text = "Levels"
        isDisable = true
        onAction = EventHandler { config.beatWithLevels = this.isSelected }
    }
}