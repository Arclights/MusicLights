package com.arclights.musiclights.core;

import com.arclights.musiclights.core.amplification.AdaptiveAmplification;
import com.arclights.musiclights.core.filter.*;
import ddf.minim.AudioInput;
import ddf.minim.Minim;
import ddf.minim.analysis.FFT;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Observable;

import static com.arclights.musiclights.core.filter.FilterType.BY_GROUP;

public class LightRig extends Observable {
    private float[] levels;
    private boolean[] powerOn;
    private int nbrOfLights;
    private float masterLevel;
    private FilterType filter;
    private float[] ampLeveler;
    private boolean useAdapAmp;
    private boolean resetAdaptLeveler;
    private boolean beatWithLevels;
    private Minim minim;
    private AudioInput in;
    private FFT fft;
    private Diff diff;
    private Beat beat;
    private AdaptiveAmplification adaptAmp;

    private float[] currentFinalLevels;

    public static final float MAX_AMPLITUDE = 400;

    /**
     * Used by Arduino
     */
    private int message;

    public LightRig(int nbrOfLights) {
        this.nbrOfLights = nbrOfLights;
        levels = new float[nbrOfLights];
        powerOn = new boolean[nbrOfLights];
        ampLeveler = new float[nbrOfLights];
        for (int i = 0; i < nbrOfLights; i++) {
            levels[i] = 1;
            powerOn[i] = true;
            ampLeveler[i] = 1;
        }
        masterLevel = 5;
        useAdapAmp = false;
        resetAdaptLeveler = false;
        beatWithLevels = false;
        filter = BY_GROUP;

        minim = new Minim(this);
        in = minim.getLineIn(Minim.STEREO);
        diff = new Diff(in);
        adaptAmp = new AdaptiveAmplification(nbrOfLights);
        fft = new FFT(in.bufferSize(), in.sampleRate());
        fft.linAverages(32);
        beat = new Beat(in, this);

        message = -1;

        currentFinalLevels = new float[nbrOfLights];
    }

    public void update() {
        fft.forward(in.mix);
        ArrayList<Float> largest = null;
        switch (filter) {
            case BY_GROUP:
                largest = ByGroup.filter(fft, nbrOfLights);
                break;
            case BASS:
                largest = beat.filter(fft, nbrOfLights);
                break;
            case DIFF:
                largest = diff.filter(fft, nbrOfLights);
                break;
            case BY_GROUP_AVRAGE:
                largest = ByGroupAvrage.filter(fft, nbrOfLights);
        }

//        fv.setFFT(fft);

        // For Arduino
//        float f = largest.get(0);
//        System.out.println(f);
//        if (f <= 0) {
//            monitor.setMessage(0);
//        } else {
//            monitor.setMessage(1);
//        }

        if (resetAdaptLeveler) {
            adaptAmp.reset();
            resetAdaptLeveler = false;
        }

        if (useAdapAmp) {
            adaptAmp.setLeveler(fft);
        }

        for (int i = 0; i < nbrOfLights; i++) {
            if (powerOn[i]) {

                float leveler;
                if (useAdapAmp) {
                    leveler = adaptAmp.getLeveler(i);
                } else {
                    leveler = ampLeveler[i];
                }

                float level = largest.get(i) * masterLevel
                        * levels[i] * leveler;

                currentFinalLevels[i] = level;
            } else {
                currentFinalLevels[i] = 0;
            }
        }

        setChanged();
        notifyObservers();
    }

    public int getSpectrumSize() {
        return fft.specSize();
    }

    public float getBand(int i) {
        return fft.getBand(i);
    }

    public float[] getCurrentLevels() {
        return currentFinalLevels;
    }

    public void stop() {
        minim.stop();
    }

    public String sketchPath(String fileName) {
        return "";
    }

    public InputStream createInput(String fileName) throws FileNotFoundException {
        return new FileInputStream(fileName);
    }

    /**
     * Returns the number of lights that is currently shown in the LightBar
     *
     * @return the number of lights
     */
    public int getNbrOfLights() {
        return nbrOfLights;
    }

    /**
     * Sets the level a specific light
     *
     * @param lightNbr The number of the light for which the level is going to be set
     * @param level    The level the light is going t be set to
     */
    public void setIndividualLevel(int lightNbr, float level) {
        levels[lightNbr] = level;
    }

    /**
     * Sets the level for the master control
     *
     * @param level
     */
    public void setMasterLevel(float level) {
        masterLevel = level;
    }

    /**
     * Turns on the specified light so it is seen in the LightBar
     *
     * @param lightNbr The number of the light in question
     */
    public void turnPowerOn(int lightNbr) {
        powerOn[lightNbr] = true;
    }

    /**
     * Turns off the specified light
     *
     * @param lightNbr The number of the light in question
     */
    public void turnPowerOff(int lightNbr) {
        powerOn[lightNbr] = false;
    }

    /**
     * Sets which main.java.filter should be used to visualize the audio. The available
     * filters are: BY_GROUP = 0; BASS = 1; ALT_BASS = 2;
     *
     * @param filter The main.java.filter to use
     */
    public void setFilter(FilterType filter) {
        this.filter = filter;
    }

    /**
     * Sets the amplifier leveler for all the lights. The input is adjusted to
     * fit the amount of lights used by taking the largest main.java.amplification of the
     * bands belonging to each light and assigning them to respective light
     *
     * @param leveler The levelers for each band
     */
    public void setAmpLeveler(float[] leveler) {
        int bandsPerGroup = leveler.length / ampLeveler.length;
        for (int i = 0; i < ampLeveler.length; i++) {
            float largestAmpInGroup = 0;
            for (int j = i * bandsPerGroup; j < (i + 1) * bandsPerGroup; j++) {
                if (leveler[j] > largestAmpInGroup) {
                    largestAmpInGroup = leveler[j];
                }
            }
            ampLeveler[i] = 255 / largestAmpInGroup;
        }
    }

    public void resetAmpLeveler() {
        for (int i = 0; i < nbrOfLights; i++) {
            ampLeveler[i] = 1;
        }
    }

    public void useAdaptiveAmplification(boolean use) {
        useAdapAmp = use;
    }

    public void resetAdaptiveAmp(boolean reset) {
        resetAdaptLeveler = reset;
    }

    public void setBeatWithLevels(boolean bool) {
        beatWithLevels = bool;
    }

    public boolean getBeatWithLevels() {
        return beatWithLevels;
    }

    /**
     * Used by the communication thread to the arduino
     */

    public synchronized void setMessage(int i) {
        message = i;
        notifyAll();
    }

    public synchronized int getMessage() {
        while (message == -1) {
            try {
                wait();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        int temp = message;
        message = -1;
        return temp;
    }

}
