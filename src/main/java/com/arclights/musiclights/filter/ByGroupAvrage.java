package com.arclights.musiclights.filter;

import ddf.minim.analysis.FFT;

import java.util.ArrayList;

public class ByGroupAvrage {

    public static ArrayList<Float> filter(FFT fft, int nbrOfGroups) {
        ArrayList<Float> res = new ArrayList<Float>();
        int spectrumWidth = fft.specSize() / 2;
        int bandsPerGroup = (int) (spectrumWidth / nbrOfGroups);
        for (int i = 0; i < nbrOfGroups; i++) {
            float ampTotalInGroup = 0;
            for (int j = i * bandsPerGroup; j < (i + 1) * bandsPerGroup; j++) {
                ampTotalInGroup += fft.getBand(j);
            }
            res.add(ampTotalInGroup / bandsPerGroup);
        }

        return res;
    }

}
