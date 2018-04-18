package com.arclights.musiclights.gui;

import com.arclights.musiclights.core.LightRig;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MasterControl extends GridPane {
    public static final int BY_GROUP = 0;
    public static final int BASS = 1;
    public static final int ALT_BASS = 2;
    public static final int DIFF = 3;
    public static final int BY_GROUP_AVRAGE = 4;

    private LightRig lightRig;
    private Slider slider;
    private Button reset;

    // Filters
    private RadioButton byGroup;
    private RadioButton diff;
    private RadioButton beat;
    private CheckBox beatBox;
    private RadioButton byGroupAvrage;

    // Amplification
    private RadioButton fromFile;
    private Button openFile;
    private RadioButton adaptAmp;
    private Button resetAdaptAmpButton;

    MasterControl(LightRig lightRig, Stage stage) {
        this.lightRig = lightRig;
        setVgap(25);

        add(getLevelerChoices(stage), 0, 0);

        add(getFilterChoices(), 0, 1);

        reset = new Button("Reset");
        reset.setOnAction(event -> slider.setValue(5));
        GridPane.setHalignment(reset, HPos.CENTER);
        add(reset, 0, 2);

        slider = new Slider(0, 1, 0.1);
        TitledPane pane = new TitledPane("Amplification", slider);
        pane.setCollapsible(false);
        slider.setOrientation(Orientation.VERTICAL);
        slider.setMajorTickUnit(0.1);
        slider.setMinorTickCount(1);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.valueProperty().addListener((obsVal,oldVal,newVal) -> lightRig.setMasterLevel(newVal.floatValue()));
        GridPane.setHalignment(slider, HPos.CENTER);
        add(pane, 0, 3);
    }

    private Control getFilterChoices() {
        GridPane filters = new GridPane();
        TitledPane pane = new TitledPane("Filters", filters);
        pane.setCollapsible(false);
        ToggleGroup group = new ToggleGroup();

        byGroup = new RadioButton("By group");
        byGroup.setOnAction(event -> {
            beatBox.setDisable(true);
            lightRig.setFilter(MasterControl.BY_GROUP);
        });
        byGroup.setToggleGroup(group);
        filters.add(byGroup, 0, 0);
        byGroup.setSelected(true);

        byGroupAvrage = new RadioButton("By group avrage");
        byGroupAvrage.setOnAction(event -> {
            beatBox.setDisable(true);
            lightRig.setFilter(MasterControl.BY_GROUP_AVRAGE);
        });
        byGroupAvrage.setToggleGroup(group);
        filters.add(byGroupAvrage, 1, 0);

        diff = new RadioButton("Difference");
        diff.setOnAction(event -> {
            beatBox.setDisable(true);
            lightRig.setFilter(MasterControl.DIFF);
        });
        diff.setToggleGroup(group);
        filters.add(diff, 0, 1);

        filters.add(createBeatChoice(group), 1, 1);

        return pane;
    }

    private GridPane createBeatChoice(ToggleGroup tg) {
        GridPane beatGroup = new GridPane();

        beat = new RadioButton("Beat");
        beat.setOnAction(event -> {
            beatBox.setDisable(false);
            lightRig.setFilter(MasterControl.BASS);
        });
        beat.setToggleGroup(tg);
        beatGroup.add(beat, 0, 0);

        beatBox = new CheckBox("Levels");
        beatBox.setDisable(true);
        beatBox.setOnAction(event -> lightRig.setBeatWithLevels(beatBox.isSelected()));
        beatGroup.add(beatBox, 0, 1);
        return beatGroup;
    }

    private Control getLevelerChoices(Stage stage) {
        GridPane leveler = new GridPane();
        TitledPane pane = new TitledPane("Amplification Leveler", leveler);
        pane.setCollapsible(false);
        ToggleGroup group = new ToggleGroup();

        fromFile = new RadioButton("From file");
        fromFile.setOnAction(event -> {
            lightRig.useAdaptiveAmplification(false);
            resetAdaptAmpButton.setDisable(true);
            openFile.setDisable(false);
        });
        fromFile.setSelected(true);
        fromFile.setToggleGroup(group);
        leveler.add(fromFile, 0, 0);

        openFile = new Button("Open");
        openFile.setOnAction(event -> {
            if (openFile.getText().equals("Open")) {
                fileOpen(stage);
                openFile.setText("Reset");
            } else {
                lightRig.resetAmpLeveler();
                openFile.setText("Open");
            }
        });
        leveler.add(openFile, 0, 1);

        GridPane.setMargin(openFile, new Insets(0, 0, 10, 0));

        adaptAmp = new RadioButton("Adaptive Amplification");
        adaptAmp.setOnAction(event -> {
            lightRig.useAdaptiveAmplification(true);
            resetAdaptAmpButton.setDisable(false);
            openFile.setDisable(true);
        });
        adaptAmp.setToggleGroup(group);
        leveler.add(adaptAmp, 0, 2);

        resetAdaptAmpButton = new Button("Reset");
        resetAdaptAmpButton.setOnAction(event -> resetAdaptAmp());
        resetAdaptAmpButton.setDisable(true);
        leveler.add(resetAdaptAmpButton, 0, 3);

        return pane;
    }

    private void resetAdaptAmp() {
        lightRig.resetAdaptiveAmp(true);
    }

    private void fileOpen(Stage stage) {
        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(stage);
        if (file != null) {
            try {
                BufferedReader in = new BufferedReader(new FileReader(file));
                String line = in.readLine();
                line = line.substring(1, line.length() - 1);
                String[] parsedValues = line.split(",");

                float[] values = new float[parsedValues.length];
                for (int i = 0; i < parsedValues.length; i++) {
                    values[i] = Float.parseFloat(parsedValues[i]);
                }

                lightRig.setAmpLeveler(values);
                in.close();

            } catch (IOException e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            }
        }
    }
}
