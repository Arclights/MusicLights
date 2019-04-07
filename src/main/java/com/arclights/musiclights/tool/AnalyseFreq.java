package com.arclights.musiclights.tool;

import ddf.minim.AudioInput;
import ddf.minim.Minim;
import ddf.minim.analysis.FFT;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AnalyseFreq extends Application {
    Minim minim;
    AudioInput in;
    FFT fft;
    float[] maxAmpPerFreqGroup;
    private Timeline timeline;

    private Canvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception {
        canvas = new Canvas(1000, 100);
        GridPane root = new GridPane();
        root.add(canvas, 0, 0);
        Scene scene = new Scene(root);

        minim = new Minim(this);
        in = minim.getLineIn(Minim.STEREO);
        fft = new FFT(in.bufferSize(), in.sampleRate());

        maxAmpPerFreqGroup = new float[fft.specSize() / 2];

        fft.linAverages(32);
        primaryStage.setTitle("None");

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case W:
                    // a Hamming window can be used to shape the sample buffer that is
                    // passed to the FFT
                    // this can reduce the amount of noise in the spectrum
                    fft.window(FFT.HAMMING);
                    primaryStage.setTitle("Hamming");
                case E:
                    fft.window(FFT.NONE);
                    primaryStage.setTitle("None");
                case S:
//            JFileChooser fc = new JFileChooser();
//            int returnVal = fc.showSaveDialog(this);
//            if (returnVal == JFileChooser.APPROVE_OPTION) {
//                try {
//                    FileWriter fstream = new FileWriter(fc.getSelectedFile()
//                            .getAbsoluteFile());
//                    BufferedWriter out = new BufferedWriter(fstream);
//                    out.write(Arrays.toString(maxAmpPerFreqGroup));
//                    out.close();
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
            }
        });

        int drawFrequencyPerSecond = 24;
        timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(0),
                        event -> draw()
                ),
                new KeyFrame(Duration.millis(1000 / drawFrequencyPerSecond))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    public void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setStroke(Color.RED);

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
                gc.setStroke(Color.GREEN);
            } else if (i == 50) {
                gc.setStroke(Color.YELLOW);
            } else if (i == 75) {
                gc.setStroke(Color.ORANGE);
            } else if (i == 100) {
                gc.setStroke(Color.PINK);
            } else if (i == 125) {
                gc.setStroke(Color.CYAN);
            }
            gc.strokeLine(i, canvas.getHeight(), i, canvas.getHeight() - fft.getBand(i) * 4);
        }

    }


    public void stop() throws Exception {
        // always close Minim audio classes when you finish with them
        // jingle.close();
        minim.stop();

        super.stop();
    }

    public String sketchPath(String fileName) {
        return "";
    }

    public InputStream createInput(String fileName) throws FileNotFoundException {
        return new FileInputStream(fileName);
    }
}
