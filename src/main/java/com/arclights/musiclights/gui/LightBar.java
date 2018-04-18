package com.arclights.musiclights.gui;

import com.arclights.musiclights.core.LightRig;
import com.arclights.musiclights.gui.light.Light;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LightBar extends Canvas {
    private int cellWidth;
    final Color[] colors = new Color[]{Color.RED, Color.GREEN, Color.YELLOW,
            Color.BLUE, Color.ORANGE, Color.ORANGE, Color.BLUE, Color.YELLOW,
            Color.GREEN, Color.RED};

    public LightBar(LightRig lightRig) {
        super(1000, 100);
        this.cellWidth = (int) Math.round(getWidth() / lightRig.getNbrOfLights());

        List<Light> lights = IntStream.range(0, lightRig.getNbrOfLights())
                .mapToObj(i -> new Light(getGraphicsContext2D(), colors[i], i * cellWidth, cellWidth, i, lightRig))
                .collect(Collectors.toList());
    }

}
