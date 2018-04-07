package com.arclights.musiclights.gui;

import com.arclights.musiclights.monitor.Monitor;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

public class LightControl extends JPanel implements ActionListener,
        ChangeListener {
    private int controlNbr;
    private Monitor monitor;
    private JButton power;
    private JButton reset;
    private boolean powerOn;
    private JSlider slider;
    Hashtable<Integer, JLabel> sliderLabels;

    public LightControl(int cellWidth, int controlNbr, Monitor monitor, String shortcut) {
        getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("control I"), "power");
        Action powerAction = new powerAction();
        getActionMap().put("power", powerAction);
        setFocusable(true);

        this.monitor = monitor;
        this.controlNbr = controlNbr;
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        power = new JButton("OFF");
        power.addActionListener(this);
        add(power, c);

        c.gridy++;
        add(Box.createRigidArea(new Dimension(0, 25)), c);

        c.gridy++;
        reset = new JButton("Reset");
        reset.addActionListener(this);
        add(reset, c);

        c.gridy++;
        add(Box.createRigidArea(new Dimension(0, 25)), c);

        sliderLabels = new Hashtable<Integer, JLabel>();
        sliderLabels.put(new Integer(5), new JLabel("0.5"));
        sliderLabels.put(new Integer(10), new JLabel("1"));
        sliderLabels.put(new Integer(15), new JLabel("1.5"));
        sliderLabels.put(new Integer(20), new JLabel("2"));
        slider = new JSlider(JSlider.VERTICAL, 5, 20, 10);
        slider.setBorder(BorderFactory.createTitledBorder("Amplification"));
        slider.setPreferredSize(new Dimension(90, 200));
        slider.setMajorTickSpacing(5);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);
        slider.setLabelTable(sliderLabels);
        slider.addChangeListener(this);
        c.gridy++;
        add(slider, c);

        powerOn = true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == power) {
            if (powerOn) {
                powerOn = false;
                monitor.turnPowerOff(controlNbr);
                power.setText("ON");
            } else {
                powerOn = true;
                monitor.turnPowerOn(controlNbr);
                power.setText("OFF");
            }
        } else if (e.getSource() == reset) {
            slider.setValue(10);
        }

    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == slider) {
            monitor.setLevel(controlNbr, (float) (slider.getValue() * 0.1));
        }

    }

    class powerAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Control " + controlNbr);

        }

    }
}
