package com.arclights.musiclights.gui

import com.arclights.musiclights.core.LightRig
import com.arclights.musiclights.gui.light.LightControl
import com.arclights.musiclights.gui.masterControl.MasterControl
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import javafx.scene.paint.Color
import javafx.stage.Stage

class MainWindow : Application() {
    private val colors = arrayOf(Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE, Color.ORANGE)

    private val drawFrequencyPerSecond = 24

    private val lightRig = LightRig(10, 30)

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
        val masterControl = MasterControl(lightRig.config, primaryStage)
        root.add(masterControl, lightRig.nbrOfLights, 0)

        primaryStage.scene = scene
        primaryStage.sizeToScene()
        primaryStage.show()
    }

    override fun stop() {
        super.stop()
        lightRig.stop()
    }

}

fun main(args: Array<String>) {
    Application.launch(MainWindow::class.java, *args)
}