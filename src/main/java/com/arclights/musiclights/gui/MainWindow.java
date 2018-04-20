package com.arclights.musiclights.gui;

import com.arclights.musiclights.core.LightRig;
import com.arclights.musiclights.gui.light.LightControl;
import com.arclights.musiclights.gui.masterControl.MasterControl;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.stream.IntStream;

public class MainWindow extends Application {
    private LightRig lightRig;

    private Timeline timeline;

    private final Color[] colors = new Color[]{Color.RED, Color.GREEN, Color.YELLOW,
            Color.BLUE, Color.ORANGE, Color.ORANGE, Color.BLUE, Color.YELLOW,
            Color.GREEN, Color.RED};

    public MainWindow() {
        this.lightRig = new LightRig(10);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("MusicLights");
        GridPane root = new GridPane();
        root.setHgap(1);

        Scene scene = new Scene(root);


        // For Arduino
//        Serial s = new Serial(monitor);
//        s.initialize();
//        s.start();

        // The controls
        IntStream.range(0, lightRig.getNbrOfLights()).forEach(i -> {
            LightControl control = new LightControl(i, lightRig, colors[i]);
            root.add(control, i, 0);
        });

        // Master Control
        MasterControl mc = new MasterControl(lightRig, primaryStage);
        root.add(mc, lightRig.getNbrOfLights(), 0);

        root.autosize();

        int drawFrequencyPerSecond = 24;
        timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(0),
                        event -> lightRig.update()
                ),
                new KeyFrame(Duration.millis(1000 / drawFrequencyPerSecond))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        timeline.stop();
        lightRig.stop();
    }

    public static void main(String[] args) {
        MainWindow.launch(args);
    }

}
