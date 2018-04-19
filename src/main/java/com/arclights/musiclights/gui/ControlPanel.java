package com.arclights.musiclights.gui;

import com.arclights.musiclights.core.LightRig;
import javafx.scene.layout.GridPane;
import com.arclights.musiclights.gui.light.LightControl;

import java.util.ArrayList;

class ControlPanel extends GridPane {
    ControlPanel(LightRig lightRig) {
        ArrayList<LightControl> controls = new ArrayList<>();
        setHgap(1);
        for (int i = 0; i < lightRig.getNbrOfLights(); i++) {
            LightControl control = new LightControl(i, lightRig);
            add(control, i, 0);
            controls.add(control);
        }
        autosize();
    }
}
