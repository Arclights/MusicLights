package com.arclights.musiclights.gui;

import ddf.minim.analysis.FFT;
import processing.core.PApplet;

public class FreqView extends PApplet {
    private FFT fft;
    private int nbrOfLights;

    public FreqView(int nbrOfLights) {
        this.nbrOfLights = nbrOfLights;
    }

    public void setup() {
        size(1000, 100, P3D);
        // rectMode(CORNERS);
    }

    @Override
    public void draw() {
        background(0);

        if (fft != null) {
            float spikeWidth = (float) (width / nbrOfLights * 2)
                    / (float) (fft.specSize() / nbrOfLights);
            for (int i = 0; i < fft.specSize() / 2; i++) {
                if (i == 0) {
                    // Red
                    fill(255, 0, 0);
                } else if (i == 25) {
                    // Green
                    fill(173, 255, 47);
                } else if (i == 52) {
                    // Yellow
                    fill(255, 255, 0);
                } else if (i == 77) {
                    // Blue
                    fill(0, 0, 255);
                } else if (i == 102) {
                    // Orange
                    fill(255, 140, 0);
                } else if (i == 128) {
                    // Orange
                    fill(255, 140, 0);
                } else if (i == 152) {
                    // Blue
                    fill(0, 0, 255);
                } else if (i == 178) {
                    // Yellow
                    fill(255, 255, 0);
                } else if (i == 204) {
                    // Green
                    fill(173, 255, 47);
                } else if (i == 230) {
                    // Red
                    fill(255, 0, 0);
                }
                rect(i * spikeWidth, height, spikeWidth, fft.getBand(i) * -4);
            }
        }

    }

    public void setFFT(FFT fft) {
        this.fft = fft;
    }

    public void stop() {
        // always close Minim audio classes when you finish with them
        super.stop();
    }

}
