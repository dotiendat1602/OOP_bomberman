package uet.oop.bomberman.GUI.Menu;

import uet.oop.bomberman.GUI.Frame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Level extends JMenu {
    private final Frame frame;

    public Level(Frame frame) {
        super("Level");
        this.frame = frame;

        for (int i = 1; i <= 5; i++) {
            add(createLevelMenuItem(i));
        }
    }

    private JMenuItem createLevelMenuItem(int levelNumber) {
        JMenuItem menuItem = new JMenuItem("Level " + levelNumber);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0 + levelNumber, KeyEvent.CTRL_DOWN_MASK));
        menuItem.addActionListener(new MenuActionListener(frame, levelNumber));
        return menuItem;
    }

    static class MenuActionListener implements ActionListener {
        private final Frame frame;
        private final int level;

        public MenuActionListener(Frame frame, int level) {
            this.frame = frame;
            this.level = level;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            frame.changeLevel(level);
        }
    }
}
