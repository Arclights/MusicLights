package com.arclights.musiclights.core.filter;

import ddf.minim.analysis.FFT;

import java.util.ArrayList;

public class ByGroup {

    public static ArrayList<Float> filter(FFT fft, int nbrOfGroups) {
        ArrayList<Float> res = new ArrayList<Float>();
        int spectrumWidth = fft.specSize() / 2;
        int bandsPerGroup = (int) (spectrumWidth / nbrOfGroups);
        for (int i = 0; i < nbrOfGroups; i++) {
            float largestAmpInGroup = 0;
            for (int j = i * bandsPerGroup; j < (i + 1) * bandsPerGroup; j++) {
                if (fft.getBand(j) > largestAmpInGroup) {
                    largestAmpInGroup = fft.getBand(j);
                }
            }
            res.add(largestAmpInGroup);
        }

        return res;
    }

}
