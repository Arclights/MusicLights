package com.arclights.musiclights.gui;

import com.arclights.musiclights.monitor.Monitor;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ControlPanel extends JPanel {
    private ArrayList<LightControl> controls;
    private String[] shortcuts = {"control I", "2", "3", "4", "5", "6", "7", "8", "9",
            "0"};

    public ControlPanel(int cellWidth, Monitor monitor) {
        setFocusable(true);

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        controls = new ArrayList<LightControl>();
        int j = 0;
        for (int i = 0; i < monitor.getNbrOfLights(); i++) {
            LightControl control = new LightControl(cellWidth, i, monitor, shortcuts[i]);
            control.setPreferredSize(new Dimension(cellWidth - 1, 500));
            c.gridx = j;
            add(control, c);

            c.gridx = ++j;
            JSeparator sep = new JSeparator(JSeparator.VERTICAL);
            sep.setPreferredSize(new Dimension(1, 500));
            add(sep, c);

            controls.add(control);
            j++;
        }

    }
}
