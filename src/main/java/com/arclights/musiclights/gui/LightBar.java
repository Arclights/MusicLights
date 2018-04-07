package com.arclights.musiclights.gui;

import com.arclights.musiclights.amplification.AdaptiveAmplification;
import com.arclights.musiclights.filter.Beat;
import com.arclights.musiclights.filter.ByGroup;
import com.arclights.musiclights.filter.ByGroupAvrage;
import com.arclights.musiclights.filter.Diff;
import com.arclights.musiclights.monitor.Monitor;
import ddf.minim.AudioInput;
import ddf.minim.Minim;
import ddf.minim.analysis.FFT;
import processing.core.PApplet;

import java.awt.*;
import java.util.ArrayList;

public class LightBar extends PApplet {
    private Monitor monitor;
    private Minim minim;
    private AudioInput in;
    private FFT fft;
    private int cellWidth;
    private int nbrOfLights;
    final Color[] colors = new Color[]{Color.RED, Color.GREEN, Color.YELLOW,
            Color.BLUE, Color.ORANGE, Color.ORANGE, Color.BLUE, Color.YELLOW,
            Color.GREEN, Color.RED};
    private Diff diff;
    private Beat beat;
    private FreqView fv;
    private AdaptiveAmplification adaptAmp;

    public LightBar(int cellwidth, int nbrOfLights, Monitor monitor, FreqView fv) {
        this.monitor = monitor;
        this.cellWidth = cellwidth;
        this.nbrOfLights = nbrOfLights;
        this.fv = fv;
    }

    public void setup() {
        size(1000, 100);
        minim = new Minim(this);
        in = minim.getLineIn(Minim.STEREO);
        diff = new Diff(in);
        adaptAmp = new AdaptiveAmplification(in, nbrOfLights);
        fft = new FFT(in.bufferSize(), in.sampleRate());
        fft.linAverages(32);
        beat = new Beat(in, monitor);
    }

    public void draw() {
        background(0);
        stroke(0);

        ArrayList<Float> largest = null;
        fft.forward(in.mix);
        switch (monitor.getFilter()) {
            case MasterControl.BY_GROUP:
                largest = ByGroup.filter(fft, nbrOfLights);
                break;
            case MasterControl.BASS:
                largest = beat.filter(fft, nbrOfLights);
                break;
            case MasterControl.DIFF:
                largest = diff.filter(fft, nbrOfLights);
                break;
            case MasterControl.BY_GROUP_AVRAGE:
                largest = ByGroupAvrage.filter(fft, nbrOfLights);
        }

        fv.setFFT(fft);

        if (monitor.isResettingAdaptAmp()) {
            adaptAmp.reset();
            monitor.resetAdaptiveAmp(false);
        }

        if (monitor.usingAdaptiveAmp()) {
            adaptAmp.setLeveler(fft);
        }

        // For Arduino
        float f = largest.get(0);
        System.out.println(f);
        if (f <= 0) {
            monitor.setMessage(0);
        } else {
            monitor.setMessage(1);
        }

        for (int i = 0; i < nbrOfLights; i++) {
            if (monitor.getPowerStatus(i)) {

                float leveler;
                if (monitor.usingAdaptiveAmp()) {
                    leveler = adaptAmp.getLeveler(i);
                } else {
                    leveler = monitor.getAmpLeveler(i);
                }

                float level = largest.get(i) * monitor.getMasterLevel()
                        * monitor.getLevel(i) * leveler;
                fill((float) colors[i].getRed(), (float) colors[i].getGreen(),
                        (float) colors[i].getBlue(), level);
                rect(i * cellWidth, 0, cellWidth, 100);
            }
        }
    }

    public void stop() {
        // always close Minim audio classes when you finish with them
        in.close();
        minim.stop();

        super.stop();
    }

}
