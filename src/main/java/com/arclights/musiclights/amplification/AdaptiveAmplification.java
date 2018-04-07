package com.arclights.musiclights.amplification;

import ddf.minim.AudioInput;
import ddf.minim.analysis.FFT;

import java.util.ArrayList;

public class AdaptiveAmplification {
    ArrayList<Float> levelers;
    private int nbrOfGroups;
    double[] noise_line = new double[]{454.1341, 985.9705, 936.9573,
            2589.1023, 2526.0164, 4455.165, 3321.9583, 4226.0024, 10840.993,
            11465.399};

    public AdaptiveAmplification(AudioInput in, int nbrOfGroups) {
        levelers = new ArrayList<Float>(nbrOfGroups);
        this.nbrOfGroups = nbrOfGroups;
        for (int i = 0; i < nbrOfGroups; i++) {
            levelers.add((float) 0);
        }
    }

    public void setLeveler(FFT fft) {
        int spectrumWidth = fft.specSize() / 2;
        int bandsPerGroup = (int) (spectrumWidth / nbrOfGroups);
        for (int i = 0; i < nbrOfGroups; i++) {
            float largestAmpInGroup = 0;
            for (int j = i * bandsPerGroup; j < (i + 1) * bandsPerGroup; j++) {
                if (fft.getBand(j) > largestAmpInGroup) {
                    largestAmpInGroup = fft.getBand(j);
                }
            }
            if (255 / largestAmpInGroup < levelers.get(i)) {
                levelers.set(i, 255 / largestAmpInGroup);
            } else if (levelers.get(i) == 0
                    && 255 / largestAmpInGroup < noise_line[i]) {
                levelers.set(i, 255 / largestAmpInGroup);
            }
        }
    }

    public float getLeveler(int index) {
        return levelers.get(index);
    }

    public void reset() {
        for (int i = 0; i < nbrOfGroups; i++) {
            levelers.set(i, (float) 0);
        }
    }

}
