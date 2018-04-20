package com.arclights.musiclights.gui;

import com.arclights.musiclights.core.LightRig;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainWindow extends Application {
    private LightRig lightRig;

    private Timeline timeline;

    public MainWindow() {
        this.lightRig = new LightRig(10);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("MusicLights");
        GridPane root = new GridPane();
        Scene scene = new Scene(root);


        // For Arduino
//        Serial s = new Serial(monitor);
//        s.initialize();
//        s.start();

        // The controls
        ControlPanel cp = new ControlPanel(lightRig);
        root.add(cp, 0, 0);

        // Master Control
        MasterControl mc = new MasterControl(lightRig, primaryStage);
        root.add(mc, 1, 0);

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
