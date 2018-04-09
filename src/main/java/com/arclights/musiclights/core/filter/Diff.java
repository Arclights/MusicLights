package com.arclights.musiclights.core.filter;

import ddf.minim.AudioInput;
import ddf.minim.analysis.FFT;

import java.util.ArrayList;

public class Diff {
    ArrayList<Float> prevLevel;

    public Diff(AudioInput in) {
        prevLevel = new ArrayList<Float>(in.bufferSize() / 4);
        for (int i = 0; i < in.bufferSize() / 4; i++) {
            prevLevel.add((float) 0);
        }
    }

    public ArrayList<Float> filter(FFT fft, int nbrOfGroups) {
        // ArrayList<Float> diff = new ArrayList<Float>(prevLevel.size());
        // for (int i = 0; i < fft.specSize() / 2; i++) {
        // diff.add(fft.getBand(i) - prevLevel.get(i));
        // prevLevel.set(i, fft.getBand(i));
        // }
        //
        // ArrayList<Float> res = new ArrayList<Float>();
        // int spectrumWidth = fft.specSize() / 2;
        // int bandsPerGroup = (int) (spectrumWidth / nbrOfGroups);
        // for (int i = 0; i < nbrOfGroups; i++) {
        // float largestAmpInGroup = 0;
        // for (int j = i * bandsPerGroup; j < (i + 1) * bandsPerGroup; j++) {
        // if (diff.get(j) > largestAmpInGroup) {
        // largestAmpInGroup = diff.get(j);
        // }
        // }
        // res.add(largestAmpInGroup);
        // }

        ArrayList<Float> res = new ArrayList<Float>();
        int spectrumWidth = fft.specSize() / 2;
        int bandsPerGroup = (int) (spectrumWidth / nbrOfGroups);
        for (int i = 0; i < nbrOfGroups; i++) {
            float level = 0;
            for (int j = i * bandsPerGroup; j < (i + 1) * bandsPerGroup; j++) {
                if (fft.getBand(j) > prevLevel.get(j)) {
                    level = 255;
                }
                prevLevel.set(j, fft.getBand(j));
            }
            res.add(level);
        }

        return res;
    }
}
