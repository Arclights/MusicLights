package com.arclights.musiclights.tool

import ddf.minim.AudioInput
import ddf.minim.Minim
import ddf.minim.analysis.FFT
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.input.KeyCode
import javafx.scene.layout.GridPane
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.util.Duration

class AnalyseFreq : Application() {
    lateinit var minim: Minim
    lateinit var input: AudioInput
    lateinit var fft: FFT
    private lateinit var maxAmpPerFreqGroup: Array<Float>
    private lateinit var timeline: Timeline

    private lateinit var canvas: Canvas

    override fun start(primaryStage: Stage) {
        canvas = Canvas(1000.0, 100.0)
        val root = GridPane()
        root.add(canvas, 0, 0)
        val scene = Scene(root)

        minim = Minim(this)
        input = minim.getLineIn(Minim.STEREO)
        fft = FFT(input.bufferSize(), input.sampleRate())

        maxAmpPerFreqGroup = Array(fft.specSize() / 2) { 0F }

        fft.linAverages(32)
        primaryStage.title = "None"

        scene.setOnKeyPressed { event ->
            when (event.code) {
                KeyCode.W -> {
                    // a Hamming window can be used to shape the sample buffer that is
                    // passed to the FFT
                    // this can reduce the amount of noise in the spectrum
                    fft.window(FFT.HAMMING)
                    primaryStage.title = "Hamming"
                }
                KeyCode.E -> {
                    fft.window(FFT.NONE)
                    primaryStage.title = "None"
                }
                KeyCode.S -> {
                }
                else -> {
                }
            }
        }

        val drawFrequencyPerSecond = 24
        timeline = Timeline(
            KeyFrame(
                Duration.seconds(0.0),
                EventHandler { draw() }
            ),
            KeyFrame(Duration.millis(1000.0 / drawFrequencyPerSecond))
        )
        timeline.cycleCount = Timeline.INDEFINITE
        timeline.play()

        primaryStage.scene = scene
        primaryStage.sizeToScene()
        primaryStage.show()
    }

    private fun draw() {
        val gc = canvas.graphicsContext2D
        gc.fill = Color.BLACK
        gc.fillRect(0.0, 0.0, canvas.width, canvas.height)

        gc.stroke = Color.RED

        // perform a forward FFT on the samples in jingle's mix buffer
        // note that if jingle were a MONO file, this would be the same as using
        // jingle.left or jingle.right
        // fft.forward(jingle.mix);
        fft.forward(input.mix)
        val spectrumWidth = fft.specSize() / 2
        (0 until spectrumWidth).forEach { i ->
            if (maxAmpPerFreqGroup[i] < fft.getBand(i)) {
                maxAmpPerFreqGroup[i] = fft.getBand(i)
            }

            when (i) {
                25 -> gc.stroke = Color.GREEN
                50 -> gc.stroke = Color.YELLOW
                75 -> gc.stroke = Color.ORANGE
                100 -> gc.stroke = Color.PINK
                125 -> gc.stroke = Color.CYAN
            }

            gc.strokeLine(
                i.toDouble(),
                canvas.height,
                i.toDouble(),
                canvas.height - fft.getBand(i) * 4
            )
        }
    }


    override fun stop() {
        // always close Minim audio classes when you finish with them
        minim.stop()

        super.stop()
    }

}
