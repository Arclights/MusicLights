package com.arclights.musiclights.monitor;

import com.arclights.musiclights.gui.MasterControl;

public class Monitor {
    private float[] levels;
    private boolean[] powerOn;
    private int nbrOfLights;
    private int masterLevel;
    private int filter;
    private float[] ampLeveler;
    private boolean useAdapAmp;
    private boolean resetAdaptLeveler;
    private boolean beatWithLevels;

    /**
     * Used by Arduino
     */
    private int message;

    public Monitor(int nbrOfLights) {
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
        filter = MasterControl.BY_GROUP;

        message = -1;
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
     * Gets the level for a specific light
     *
     * @param lightNbr The number of the light the level is requested for
     * @return the level for the light
     */
    public float getLevel(int lightNbr) {
        return levels[lightNbr];
    }

    /**
     * Sets the level a specific light
     *
     * @param lightNbr The number of the light for which the level is going to be set
     * @param level    The level the light is going t be set to
     */
    public void setLevel(int lightNbr, float level) {
        levels[lightNbr] = level;
    }

    /**
     * Sets the level for the master control
     *
     * @param level
     */
    public void setMasterLevel(int level) {
        masterLevel = level;
    }

    /**
     * Gets the level of the master control
     *
     * @return The level
     */
    public int getMasterLevel() {
        return masterLevel;
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
     * Returns whether the specified light is on or off, the power status
     *
     * @param lightNbr The number of the light in question
     * @return The power status of the light
     */
    public boolean getPowerStatus(int lightNbr) {
        return powerOn[lightNbr];
    }

    /**
     * Sets which main.java.filter should be used to visualize the audio. The available
     * filters are: BY_GROUP = 0; BASS = 1; ALT_BASS = 2;
     *
     * @param filter The main.java.filter to use
     */
    public void setFilter(int filter) {
        this.filter = filter;
    }

    /**
     * Returns the main.java.filter currently used to visualize the audio
     *
     * @return The main.java.filter
     */
    public int getFilter() {
        return filter;
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

    /**
     * Returns the main.java.amplification of the light, band group, in question
     *
     * @param bandGroup The band group
     * @return The main.java.amplification of the band group
     */
    public float getAmpLeveler(int bandGroup) {
        return ampLeveler[bandGroup];
    }

    public void useAdaptiveAmplification(boolean use) {
        useAdapAmp = use;
    }

    public boolean usingAdaptiveAmp() {
        return useAdapAmp;
    }

    public void resetAdaptiveAmp(boolean reset) {
        resetAdaptLeveler = reset;
    }

    public boolean isResettingAdaptAmp() {
        return resetAdaptLeveler;
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
