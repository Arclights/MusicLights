package com.arclights.musiclights.gui;

import com.arclights.musiclights.core.LightRig;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Slider;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;

public class LightControl extends GridPane {
    private boolean powerOn = true;

    public LightControl(int controlNbr, LightRig lightRig, String shortcut) {
        setVgap(25);

        Button powerButton = createPowerButton(lightRig, controlNbr);
        Button resetButton = createResetButton();
        Control slider = createSlider(lightRig, controlNbr, resetButton);

        add(powerButton, 0, 0);
        add(resetButton, 0, 1);
        add(slider, 0, 2);
    }

    private Button createPowerButton(LightRig lightRig, int controlNbr) {
        Button power = new Button("OFF");
        power.setOnAction(event -> {
            if (powerOn) {
                powerOn = false;
                lightRig.turnPowerOff(controlNbr);
                power.setText("ON");
            } else {
                powerOn = true;
                lightRig.turnPowerOn(controlNbr);
                power.setText("OFF");
            }
        });
        GridPane.setHalignment(power, HPos.CENTER);

        return power;
    }

    private Button createResetButton() {
        Button reset = new Button("Reset");
        GridPane.setHalignment(reset, HPos.CENTER);

        return reset;
    }

    private Control createSlider(LightRig lightRig, int controlNbr, Button resetButton) {
        Slider slider = new Slider(5, 20, 10);
        slider.setMajorTickUnit(5);
        slider.setMinorTickCount(1);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setOrientation(Orientation.VERTICAL);
        slider.valueProperty().addListener((obsVal,oldVal,newVal) -> lightRig.setLevel(controlNbr, newVal.floatValue() * 0.1f));
        TitledPane sliderPane = new TitledPane("Amplification", slider);
        sliderPane.setCollapsible(false);
        resetButton.setOnAction(event -> slider.setValue(10));

        return sliderPane;
    }

}
