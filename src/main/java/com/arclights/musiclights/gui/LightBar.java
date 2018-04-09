package com.arclights.musiclights.gui;

import com.arclights.musiclights.core.LightRig;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Observable;
import java.util.Observer;

public class LightBar extends Canvas implements Observer {
    private LightRig lightRig;
    private int cellWidth;
    final Color[] colors = new Color[]{Color.RED, Color.GREEN, Color.YELLOW,
            Color.BLUE, Color.ORANGE, Color.ORANGE, Color.BLUE, Color.YELLOW,
            Color.GREEN, Color.RED};
    private FreqView fv;

    public LightBar(LightRig lightRig, FreqView fv) {
        super(1000, 100);
        this.lightRig = lightRig;
        this.cellWidth = (int) Math.round(getWidth() / lightRig.getNbrOfLights());
        this.fv = fv;
        lightRig.addObserver(this);
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, getWidth(), getHeight());

        int i = 0;
        for (float level : lightRig.getCurrentLevels()) {
            gc.setFill(colors[i]);
            gc.setGlobalAlpha(level);
            gc.fillRect(i * cellWidth, 0, cellWidth, 100);
            i++;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        draw(getGraphicsContext2D());
    }

}
