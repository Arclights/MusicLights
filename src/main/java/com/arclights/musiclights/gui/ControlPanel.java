package com.arclights.musiclights.gui;

import com.arclights.musiclights.core.LightRig;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class ControlPanel extends GridPane {
    private ArrayList<LightControl> controls;
    private String[] shortcuts = {"control I", "2", "3", "4", "5", "6", "7", "8", "9", "0"};

    public ControlPanel( LightRig lightRig) {
        controls = new ArrayList<>();
        setHgap(1);
        for (int i = 0; i < lightRig.getNbrOfLights(); i++) {
            LightControl control = new LightControl(i, lightRig, shortcuts[i]);
            add(control, i, 0);
            controls.add(control);
        }
        autosize();
    }
}
