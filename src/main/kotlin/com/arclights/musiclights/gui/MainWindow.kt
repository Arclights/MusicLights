package com.arclights.musiclights.gui

import com.arclights.musiclights.core.LightRig
import com.arclights.musiclights.gui.light.LightControl
import com.arclights.musiclights.gui.masterControl.MasterControl
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.util.Duration

class MainWindow : Application() {
    private val colors = arrayOf(Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE, Color.ORANGE)

    private val drawFrequencyPerSecond = 24

    private val lightRig = LightRig(10)

    private val timeline = Timeline(
        KeyFrame(
            Duration.seconds(0.0),
            EventHandler { lightRig.update() }
        ),
        KeyFrame(Duration.millis((1000 / drawFrequencyPerSecond).toDouble()))
    )

    override fun start(primaryStage: Stage) {
        primaryStage.title = "MusicLights"
        val root = GridPane()
        root.hgap = 1.0

        val scene = Scene(root)

        // The controls
        lightRig.lights.forEachIndexed { index, light ->
            root.add(LightControl(light, colors[index % colors.size]), index, 0)
        }

        // Master Control
        val masterControl = MasterControl(lightRig, primaryStage)
        root.add(masterControl, lightRig.nbrOfLights, 0)

        timeline.cycleCount = Timeline.INDEFINITE
        timeline.play()

        primaryStage.scene = scene
        primaryStage.sizeToScene()
        primaryStage.show()
    }

    override fun stop() {
        super.stop()
        timeline.stop()
        lightRig.stop()
    }

}

fun main(args: Array<String>) {
    Application.launch(MainWindow::class.java, *args)
}