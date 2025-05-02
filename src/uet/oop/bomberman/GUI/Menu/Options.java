package uet.oop.bomberman.GUI.Menu;

import uet.oop.bomberman.GUI.Frame;
import uet.oop.bomberman.Game;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Options extends JMenu implements ChangeListener {
    private static final int MIN_SCALE = 1;
    private static final int MAX_SCALE = 5;
    private static final int INITIAL_SCALE = 3;

    private final Frame frame;

    public Options(Frame frame) {
        super("Options");
        this.frame = frame;
        setupScaleSlider();
    }

    private void setupScaleSlider() {
        add(new JLabel("Size"));

        JSlider scaleSlider = new JSlider(JSlider.HORIZONTAL, MIN_SCALE, MAX_SCALE, INITIAL_SCALE);
        scaleSlider.setMajorTickSpacing(1);
        scaleSlider.setMinorTickSpacing(1);
        scaleSlider.setPaintTicks(true);
        scaleSlider.setPaintLabels(true);
        scaleSlider.addChangeListener(this);

        add(scaleSlider);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider slider = (JSlider) e.getSource();
        if (!slider.getValueIsAdjusting()) {
            Game.SCALE_MULTIPLE = slider.getValue();
            frame.gamePanel.changeSize();
            frame.revalidate();
            frame.pack();
        }
    }
}
