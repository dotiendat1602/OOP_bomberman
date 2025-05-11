package uet.oop.bomberman.GUI.Menu;

import uet.oop.bomberman.GUI.Frame;
import uet.oop.bomberman.Sound.Sound;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Music extends JMenu {
    private final Frame frame;
    private final Sound sound;

    public Music(Frame frame, Sound sound) {
        super("Sound");
        this.frame = frame;
        this.sound = sound;

        // Thêm các mục menu cho điều khiển âm thanh
        add(createPlayMusicItem());
        add(createStopMusicItem());
    }

    private JMenuItem createPlayMusicItem() {
        JMenuItem menuItem = new JMenuItem("Play Music");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK)); // Phím tắt: Ctrl+P
        menuItem.addActionListener(new MenuActionListener(frame, sound, "play"));
        return menuItem;
    }

    private JMenuItem createStopMusicItem() {
        JMenuItem menuItem = new JMenuItem("Stop Music");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK)); // Phím tắt: Ctrl+S
        menuItem.addActionListener(new MenuActionListener(frame, sound, "stop"));
        return menuItem;
    }

    static class MenuActionListener implements ActionListener {
        private final Frame frame;
        private final Sound sound;
        private final String action;

        public MenuActionListener(Frame frame, Sound sound, String action) {
            this.frame = frame;
            this.sound = sound;
            this.action = action;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (action) {
                case "play":
                    frame.getGame().setSoundEnabled(true);
                    break;
                case "stop":
                    frame.getGame().setSoundEnabled(false);
                    break;
                default:
                    break;
            }
        }
    }
}