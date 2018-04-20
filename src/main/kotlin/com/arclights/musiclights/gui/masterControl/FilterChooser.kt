package com.arclights.musiclights.gui.masterControl

import com.arclights.musiclights.core.LightRig
import com.arclights.musiclights.core.filter.FilterType
import com.arclights.musiclights.core.filter.FilterType.*
import javafx.event.EventHandler
import javafx.scene.control.CheckBox
import javafx.scene.control.RadioButton
import javafx.scene.control.TitledPane
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.GridPane

class FilterChooser(lightRig: LightRig) : TitledPane() {
    init {
        text = "Filters"
        isCollapsible = false

        val filters = GridPane()
        filters.hgap = 5.0
        filters.vgap = 5.0

        val group = ToggleGroup()

        val levelBox = LevelCheckBox(lightRig)
        val byGroupRadioButton = FilterRadioButton("By group", true, BY_GROUP, levelBox, lightRig)
        byGroupRadioButton.toggleGroup = group
        byGroupRadioButton.isSelected = true
        val byGroupAverageRadioButton = FilterRadioButton("By group average", true, BY_GROUP_AVRAGE, levelBox, lightRig)
        byGroupAverageRadioButton.toggleGroup = group
        val fftLargestRadioButton = FilterRadioButton("FFT largest", true, FFT_LARGEST, levelBox, lightRig)
        fftLargestRadioButton.toggleGroup = group
        val fftByBandRadioButton = FilterRadioButton("FFT by band", true, FFT_BY_BAND, levelBox, lightRig)
        fftByBandRadioButton.toggleGroup = group
        val differenceRadioButton = FilterRadioButton("Difference", true, DIFF, levelBox, lightRig)
        differenceRadioButton.toggleGroup = group
        val beatRadioButton = FilterRadioButton("Beat", false, BASS, levelBox, lightRig)
        beatRadioButton.toggleGroup = group

        filters.add(byGroupRadioButton, 0, 0)
        filters.add(byGroupAverageRadioButton, 1, 0)
        filters.add(fftLargestRadioButton, 0, 1)
        filters.add(fftByBandRadioButton, 1, 1)
        filters.add(differenceRadioButton, 0, 2, 1, 2)
        filters.add(beatRadioButton, 1, 2)
        filters.add(levelBox, 1, 3)

        content = filters
    }
}

class FilterRadioButton(
        text: String,
        disableLevelBox: Boolean,
        filterType: FilterType,
        levelBox: LevelCheckBox,
        lightRig: LightRig
) : RadioButton() {
    init {
        this.text = text
        onAction = EventHandler { _ ->
            levelBox.isDisable = disableLevelBox
            lightRig.setFilter(filterType)
        }
    }
}

class LevelCheckBox(lightRig: LightRig) : CheckBox() {
    init {
        text = "Levels"
        isDisable = true
        onAction = EventHandler { _ -> lightRig.beatWithLevels = this.isSelected }
    }
}