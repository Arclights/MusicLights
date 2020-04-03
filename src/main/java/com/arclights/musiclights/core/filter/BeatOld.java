package com.arclights.musiclights.core.filter;

import com.arclights.musiclights.core.LightConfig;
import ddf.minim.AudioInput;
import ddf.minim.analysis.FFT;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class BeatOld implements Filter {
    private final float sense = (float) 2;
    private LightConfig config;
    private float[][] E;
    private int nbrOfSubbands;
    private final int nbrOfLights;

    public BeatOld(LightConfig config, int nbrOfLights) {
        this.config = config;
        this.nbrOfLights = nbrOfLights;
        E = new float[32][43];
        nbrOfSubbands = 32;
    }

    @NotNull
    @Override
    public List<Float> filter(@NotNull FFT fft, @NotNull AudioInput input) {
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
                if (config.getBeatWithLevels()) {
                    res.add(Es[largestAmpInGroupIndex]);
                } else {
                    res.add((float) 1);
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
}
