package com.arclights.musiclights.tool;

import ddf.minim.AudioInput;
import ddf.minim.Minim;
import ddf.minim.analysis.FFT;
import processing.core.PApplet;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class AnalyseFreq extends PApplet {
    Minim minim;
    AudioInput in;
    FFT fft;
    String windowName;
    float[] maxAmpPerFreqGroup;

    public void setup() {
        size(1000, 100, P3D);
        textMode(SCREEN);

        minim = new Minim(this);
        in = minim.getLineIn(Minim.STEREO);
        fft = new FFT(in.bufferSize(), in.sampleRate());

        maxAmpPerFreqGroup = new float[fft.specSize() / 2];

        rectMode(CORNERS);

        fft.linAverages(32);
        windowName = "None";
    }

    public void draw() {
        background(0);
        // stroke(255);

        // red
        stroke(255, 0, 0);

        // perform a forward FFT on the samples in jingle's mix buffer
        // note that if jingle were a MONO file, this would be the same as using
        // jingle.left or jingle.right
        // fft.forward(jingle.mix);
        fft.forward(in.mix);
        int spectrumWidth = fft.specSize() / 2;
        for (int i = 0; i < spectrumWidth; i++) {

            if (maxAmpPerFreqGroup[i] < fft.getBand(i)) {
                maxAmpPerFreqGroup[i] = fft.getBand(i);
            }

            if (i == 25) {
                // green
                stroke(173, 255, 47);
            } else if (i == 50) {
                // Yellow
                stroke(255, 255, 0);
            } else if (i == 75) {
                // Orange
                stroke(255, 165, 0);
            } else if (i == 100) {
                // Pink
                stroke(255, 20, 147);
            } else if (i == 125) {
                // Cyan
                stroke(0, 255, 255);
            }

            line(i, height, i, height - fft.getBand(i) * 4);
        }

    }

    public void keyReleased() {
        if (key == 'w') {
            // a Hamming window can be used to shape the sample buffer that is
            // passed to the FFT
            // this can reduce the amount of noise in the spectrum
            fft.window(FFT.HAMMING);
            windowName = "Hamming";
        }

        if (key == 'e') {
            fft.window(FFT.NONE);
            windowName = "None";
        }

        if (key == 's') {
            JFileChooser fc = new JFileChooser();
            int returnVal = fc.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    FileWriter fstream = new FileWriter(fc.getSelectedFile()
                            .getAbsoluteFile());
                    BufferedWriter out = new BufferedWriter(fstream);
                    out.write(Arrays.toString(maxAmpPerFreqGroup));
                    out.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public void stop() {
        // always close Minim audio classes when you finish with them
        // jingle.close();
        minim.stop();

        super.stop();
    }
}
