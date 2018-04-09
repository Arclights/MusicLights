package com.arclights.musiclights.gui;

import com.arclights.musiclights.core.LightRig;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Observable;
import java.util.Observer;

public class FreqView extends Canvas implements Observer {
    private LightRig lightRig;
    private final float BAR_SCALE_FACTOR;

    public FreqView(LightRig lightRig) {
        super(1000, 100);
        BAR_SCALE_FACTOR = ((float) getHeight()) / 1024;
        this.lightRig = lightRig;
        lightRig.addObserver(this);
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, getWidth(), getHeight());

        float spikeWidth = (float) (getWidth() / lightRig.getNbrOfLights() * 2)
                / (float) (lightRig.getSpectrumSize() / lightRig.getNbrOfLights());
        for (int i = 0; i < lightRig.getSpectrumSize() / 2; i++) {
            if (i == 0) {
                gc.setFill(Color.RED);
            } else if (i == 25) {
                gc.setFill(Color.GREEN);
            } else if (i == 52) {
                gc.setFill(Color.YELLOW);
            } else if (i == 77) {
                gc.setFill(Color.BLUE);
            } else if (i == 102) {
                gc.setFill(Color.ORANGE);
            } else if (i == 128) {
                gc.setFill(Color.ORANGE);
            } else if (i == 152) {
                gc.setFill(Color.BLUE);
            } else if (i == 178) {
                gc.setFill(Color.YELLOW);
            } else if (i == 204) {
                gc.setFill(Color.GREEN);
            } else if (i == 230) {
                gc.setFill(Color.RED);
            }
            gc.fillRect(i * spikeWidth, getHeight() - (lightRig.getBand(i) * BAR_SCALE_FACTOR), spikeWidth, getHeight());
        }

    }

    @Override
    public void update(Observable o, Object arg) {
        draw(getGraphicsContext2D());
    }

}
