package com.arclights.musiclights.filter;

import com.arclights.musiclights.monitor.Monitor;
import ddf.minim.AudioInput;
import ddf.minim.analysis.FFT;

import java.util.ArrayList;

public class Beat {
    // private float[] buffer;
    private final float sense = (float) 2;
    // private final double avrageConst = 0.02321995464852607709750566893424;
    private Monitor monitor;
    private float[][] E;
    private int nbrOfSubbands;
    private final float subbandConst = (float) 0.03125;

    public Beat(AudioInput in, Monitor monitor) {
        // buffer = new float[43];
        this.monitor = monitor;
        E = new float[32][43];
        nbrOfSubbands = 32;
    }

    public ArrayList<Float> filter(FFT fft, int nbrOfLights) {
        // float level = 255;
        float[] Es = new float[nbrOfSubbands];
        for (int i = 0; i < nbrOfSubbands; i++) {
            float sum = 0;
            for (int k = i * 16; k < (i + 1) * 16; k++) {
                sum += fft.getBand(k);
            }
            Es[i] = sum / 16;
        }

        ArrayList<Float> res = new ArrayList<Float>();
        int bandsPerGroup = (int) (32 / nbrOfLights);
        for (int i = 0; i < nbrOfLights; i++) {
            int largestAmpInGroupIndex = 0;
            float largestAmpinGroup = 0;
            for (int j = i * bandsPerGroup; j < (i + 1) * bandsPerGroup; j++) {
                if (Es[j] > largestAmpinGroup) {
                    largestAmpInGroupIndex = j;
                    largestAmpinGroup = Es[j];
                }
            }
            if (Es[largestAmpInGroupIndex] > sense
                    * getAvrageBufferEnergy(largestAmpInGroupIndex)) {
                if (monitor.getBeatWithLevels()) {
                    res.add(Es[largestAmpInGroupIndex]);
                } else {
                    res.add((float) 255);
                }
            } else {
                res.add((float) 0);
            }
        }
        for (int i = 0; i < 32; i++) {
            shiftBuffer(i);
            E[i][0] = Es[i];
        }

        return res;
    }

    private float getAvrageBufferEnergy(int band) {
        float en = 0;
        for (int i = 0; i < nbrOfSubbands; i++) {
            en += E[band][i];
        }

        return en / 43;
    }

    private void shiftBuffer(int band) {
        for (int i = nbrOfSubbands - 1; i > 0; i--) {
            E[band][i] = E[band][i - 1];
        }
    }

    //
    // private double getSense() {
    // double res = 0;
    // for (int i = 0; i < 43; i++) {
    // // System.out.println(buffer[i]
    // +"\t"+getAvrageBufferEnergy()+"\t"+(buffer[i] -
    // getAvrageBufferEnergy()));
    // res += Math.pow(buffer[i] - getAvrageBufferEnergy(), 2);
    // }
    // System.out.println(res /43);
    // return (-0.0025714 * res /43) + 1.5142857;
    // }
    //
    // private void shiftBuffer() {
    // for (int i = 42; i > 0; i--) {
    // buffer[i] = buffer[i - 1];
    // }
    // }
    //
    // private float getAvrageBufferEnergy() {
    // float bufferSum = 0;
    // for (int i = 0; i < 43; i++) {
    // bufferSum += Math.pow(buffer[i], 1);
    // }
    //
    // return (float) (bufferSum * avrageConst);
    // }

    // Tests
    private String printArray(float[] array) {
        String res = "[";
        for (float f : array) {
            res += f + ", ";
        }
        return res + "]";

    }
}
