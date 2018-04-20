package com.arclights.musiclights.gui;

import com.arclights.musiclights.core.LightRig;
import com.arclights.musiclights.gui.light.LightControl;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

class ControlPanel extends GridPane {
    private final Color[] colors = new Color[]{Color.RED, Color.GREEN, Color.YELLOW,
            Color.BLUE, Color.ORANGE, Color.ORANGE, Color.BLUE, Color.YELLOW,
            Color.GREEN, Color.RED};

    ControlPanel(LightRig lightRig) {
        ArrayList<LightControl> controls = new ArrayList<>();
        setHgap(1);
        for (int i = 0; i < lightRig.getNbrOfLights(); i++) {
            LightControl control = new LightControl(i, lightRig, colors[i]);
            add(control, i, 0);
            controls.add(control);
        }
        autosize();
    }
}
