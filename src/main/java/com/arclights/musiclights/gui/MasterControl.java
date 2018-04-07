package com.arclights.musiclights.gui;

import com.arclights.musiclights.monitor.Monitor;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

public class MasterControl extends JPanel implements ChangeListener,
        ActionListener, ItemListener {
    public static final int BY_GROUP = 0;
    public static final int BASS = 1;
    public static final int ALT_BASS = 2;
    public static final int DIFF = 3;
    public static final int BY_GROUP_AVRAGE = 4;

    private Monitor monitor;
    private JSlider slider;
    private JButton reset;

    // Filters
    private JRadioButton byGroup;
    private JRadioButton diff;
    private JRadioButton beat;
    private JCheckBox beatBox;
    private JRadioButton byGroupAvrage;

    // Amplification
    private JRadioButton fromFile;
    private JButton openFile;
    private JRadioButton adaptAmp;
    private JButton resetAdaptAmp;

    Hashtable<Integer, JLabel> sliderLabels;

    public MasterControl(Monitor monitor, int windowHeight) {
        this.monitor = monitor;
        setPreferredSize(new Dimension(250, windowHeight));
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;

        add(getLevelerChoices(), c);

        c.gridy++;
        add(Box.createRigidArea(new Dimension(0, 25)), c);

        add(getFilterChoices(), c);

        c.gridy++;
        add(Box.createRigidArea(new Dimension(0, 25)), c);

        c.gridy++;
        reset = new JButton("Reset");
        reset.addActionListener(this);
        add(reset, c);

        c.gridy++;
        add(Box.createRigidArea(new Dimension(0, 25)), c);

        c.gridy++;
        slider = new JSlider(JSlider.VERTICAL, 0, 10, 5);
        slider.setBorder(BorderFactory.createTitledBorder("Amplification"));
        slider.setPreferredSize(new Dimension(150, 300));
        slider.setMajorTickSpacing(5);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);
        slider.addChangeListener(this);
        add(slider, c);
    }

    private JPanel getFilterChoices() {
        JPanel filters = new JPanel(new GridBagLayout());
        filters.setBorder(BorderFactory.createTitledBorder("Filters"));
        ButtonGroup group = new ButtonGroup();
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;

        byGroup = new JRadioButton("By group");
        byGroup.addActionListener(this);
        byGroup.setActionCommand("bygroup");
        group.add(byGroup);
        filters.add(byGroup, c);
        byGroup.setSelected(true);

        c.gridx++;
        byGroupAvrage = new JRadioButton("By group avrage");
        byGroupAvrage.addActionListener(this);
        group.add(byGroupAvrage);
        filters.add(byGroupAvrage, c);

        c.gridx = 0;
        c.gridy++;
        diff = new JRadioButton("Difference");
        diff.addActionListener(this);
        diff.setActionCommand("difference");
        group.add(diff);
        filters.add(diff, c);

        c.gridx++;
        filters.add(createBeatChoice(group), c);

        return filters;
    }

    private JPanel createBeatChoice(ButtonGroup bg) {
        JPanel beatGroup = new JPanel();
        beatGroup.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;

        beat = new JRadioButton("Beat");
        beat.addActionListener(this);
        beat.setActionCommand("beat");
        bg.add(beat);
        beatGroup.add(beat, c);

        beatBox = new JCheckBox("Levels");
        beatBox.setEnabled(false);
        beatBox.addItemListener(this);
        c.gridy = 1;
        beatGroup.add(beatBox, c);
        return beatGroup;
    }

    private JPanel getLevelerChoices() {
        JPanel leveler = new JPanel();
        leveler.setBorder(BorderFactory
                .createTitledBorder("Amplification Leveler"));
        ButtonGroup group = new ButtonGroup();

        JPanel filePanel = new JPanel();
        filePanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        fromFile = new JRadioButton("From file");
        fromFile.addActionListener(this);
        fromFile.setActionCommand("fromfile");
        fromFile.setSelected(true);
        group.add(fromFile);
        filePanel.add(fromFile, c);

        c.gridy++;
        openFile = new JButton("Open");
        openFile.addActionListener(this);
        filePanel.add(openFile, c);

        leveler.add(filePanel);

        JPanel adaptAmpPanel = new JPanel();
        adaptAmpPanel.setLayout(new GridBagLayout());
        c.gridx = 0;
        c.gridy = 0;

        adaptAmp = new JRadioButton("Adaptive Amplification");
        adaptAmp.addActionListener(this);
        adaptAmp.setActionCommand("adaptamp");
        group.add(adaptAmp);
        adaptAmpPanel.add(adaptAmp, c);

        c.gridy++;

        Action resetAdaptAmpAction = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("reset");
                resetAdaptAmp();

            }
        };
        resetAdaptAmp = new JButton(resetAdaptAmpAction);
        resetAdaptAmp.setText("Reset");
        resetAdaptAmp.getInputMap().put(KeyStroke.getKeyStroke("ctrl r"),
                "ampReset");
        resetAdaptAmp.getActionMap().put("ampReset", resetAdaptAmpAction);
        resetAdaptAmp.addActionListener(this);
        adaptAmpPanel.add(resetAdaptAmp, c);
        resetAdaptAmp.setEnabled(false);

        leveler.add(adaptAmpPanel);

        return leveler;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == slider) {
            monitor.setMasterLevel(slider.getValue());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == reset) {
            slider.setValue(5);
        } else if (e.getSource() == byGroup) {
            beatBox.setEnabled(false);
            monitor.setFilter(MasterControl.BY_GROUP);
        } else if (e.getSource() == byGroupAvrage) {
            beatBox.setEnabled(false);
            monitor.setFilter(MasterControl.BY_GROUP_AVRAGE);
        } else if (e.getSource() == diff) {
            beatBox.setEnabled(false);
            monitor.setFilter(MasterControl.DIFF);
        } else if (e.getSource() == beat) {
            beatBox.setEnabled(true);
            monitor.setFilter(MasterControl.BASS);
        } else if (e.getSource() == fromFile) {
            monitor.useAdaptiveAmplification(false);
            resetAdaptAmp.setEnabled(false);
            openFile.setEnabled(true);
        } else if (e.getSource() == openFile) {
            if (openFile.getText().equals("Open")) {
                fileOpen();
                openFile.setText("Reset");
            } else {
                monitor.resetAmpLeveler();
                openFile.setText("Open");
            }
        } else if (e.getSource() == adaptAmp) {
            monitor.useAdaptiveAmplification(true);
            resetAdaptAmp.setEnabled(true);
            openFile.setEnabled(false);
        } else if (e.getSource() == resetAdaptAmp) {
            resetAdaptAmp();
        }

    }

    private void resetAdaptAmp() {
        monitor.resetAdaptiveAmp(true);
    }

    private void fileOpen() {
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
                in.close();

            } catch (FileNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IOException e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == beatBox) {
            monitor.setBeatWithLevels(beatBox.isSelected());
        }
    }
}
