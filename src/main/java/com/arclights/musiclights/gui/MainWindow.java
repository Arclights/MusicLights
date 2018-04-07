package com.arclights.musiclights.gui;

import com.arclights.musiclights.Communication.Serial;
import com.arclights.musiclights.monitor.Monitor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MainWindow extends JFrame implements ActionListener {
    private LightBar lb;
    private ControlPanel cp;
    private int cellWidth;
    private JMenuBar menu;
    private Monitor monitor;

    public MainWindow() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 100);
        setTitle("MusicLights");
        int nbrOfLights = 10;

        // menu = createMenuBar();
        // setJMenuBar(menu);

        cellWidth = getWidth() / nbrOfLights;
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;

        monitor = new Monitor(nbrOfLights);

        // For Arduino
        Serial s = new Serial(monitor);
        s.initialize();
        s.start();

        // Frequency view
        FreqView fv = new FreqView(nbrOfLights);
        fv.init();
        add(fv, c);

        // The lights
        lb = new LightBar(cellWidth, nbrOfLights, monitor, fv);
        lb.init();
        c.gridy++;
        add(lb, c);

        // The controls
        cp = new ControlPanel(cellWidth, monitor);
        c.gridy++;
        add(cp, c);

        // Master Control
        MasterControl mc = new MasterControl(monitor, 500 + 2 * cellWidth);
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 3;
        add(mc, c);

        pack();
        setMinimumSize(getSize());
        setVisible(true);

    }

    // private JMenuBar createMenuBar() {
    // JMenuBar menuBar = new JMenuBar();
    //
    // JMenu file = new JMenu("File");
    // file.setMnemonic(KeyEvent.VK_F);
    // menuBar.add(file);
    //
    // JMenuItem open = new JMenuItem("Open Amp. config file", KeyEvent.VK_O);
    // open.addActionListener(this);
    // file.add(open);
    //
    // return menuBar;
    // }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == menu.getMenu(0).getMenuComponent(0)) {
            JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    BufferedReader in = new BufferedReader(new FileReader(fc
                            .getSelectedFile().getAbsoluteFile()));
                    String line = in.readLine();
                    line = line.substring(1, line.length() - 1);
                    String[] parsedValues = line.split(",");

                    float[] values = new float[parsedValues.length];
                    for (int i = 0; i < parsedValues.length; i++) {
                        values[i] = Float.parseFloat(parsedValues[i]);
                    }

                    monitor.setAmpLeveler(values);

                } catch (FileNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                }
            }
        }

    }

    public static void main(String[] args) {
        // Threadsafe way to create a Swing GUI
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainWindow();
            }
        });
    }
}
