package com.arclights.musiclights.core.filter;

import ddf.minim.analysis.FFT;

import java.util.ArrayList;

public class FFTFilter {

    static public ArrayList<Float> getLargest(FFT fft, int nbrOfLargest) {
        ArrayList<Float> largest = new ArrayList<Float>();
        float lastLargest = Integer.MAX_VALUE;
        for (int i = 0; i < nbrOfLargest; i++) {
            float currentLargest = 0;
            for (int j = 0; j < 32; j++) {
                if (fft.getAvg(j) > currentLargest
                        && fft.getAvg(j) < lastLargest) {
                    currentLargest = fft.getAvg(j);
                }
            }
            largest.add(currentLargest);
            lastLargest = currentLargest;
        }
        return largest;
    }

    static public ArrayList<Float> getByBand(FFT fft, int nbrOfBands) {
        ArrayList<Float> res = new ArrayList<Float>();
        float bandWidth = fft.getBandWidth();
        int freqPerBand = (int) (bandWidth / nbrOfBands);
        int lastBandsLastFreq = -1;
        float totalLargest = 0;
        int j = 0;
        for (int i = 0; i < bandWidth; i++) {
            float largestForCurrSection = 0;
            for (j = lastBandsLastFreq + 1; j < lastBandsLastFreq
                    + freqPerBand; j++) {
                if (fft.getBand(j) > largestForCurrSection) {
                    largestForCurrSection = fft.getBand(j);
                }
            }
            lastBandsLastFreq = j;
            res.add(largestForCurrSection);
            if (largestForCurrSection > totalLargest) {
                totalLargest = largestForCurrSection;
            }
        }

        return res;

    }
}
