package uet.oop.bomberman.GUI.Menu;

import uet.oop.bomberman.GUI.Frame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class Game extends JMenu {
    // Frame chính của game
    private Frame frame;

    // Cấu trúc menu game
    private static final String MENU_TITLE = "Game";

    // Các thông tin cho menu items
    private static final class MenuItem {
        final String name;
        final int keyCode;
        final int modifier;

        MenuItem(String name, int keyCode, int modifier) {
            this.name = name;
            this.keyCode = keyCode;
            this.modifier = modifier;
        }
    }

    private static final Map<String, MenuItem> MENU_ITEMS = new HashMap<String, MenuItem>() {{
        put("New", new MenuItem("New", KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        put("Restart", new MenuItem("Restart", KeyEvent.VK_N, KeyEvent.SHIFT_DOWN_MASK));
        put("Pause", new MenuItem("Pause", KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK));
        put("Resume", new MenuItem("Resume", KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK));
    }};

    public Game(Frame frame) {
        super(MENU_TITLE);
        this.frame = frame;

        // Tạo các menu items
        createMenuItems();
    }

    private void createMenuItems() {
        MenuActionListener actionListener = new MenuActionListener(frame);

        for (String key : MENU_ITEMS.keySet()) {
            MenuItem item = MENU_ITEMS.get(key);
            JMenuItem menuItem = new JMenuItem(item.name);
            menuItem.setAccelerator(KeyStroke.getKeyStroke(item.keyCode, item.modifier));
            menuItem.addActionListener(actionListener);
            add(menuItem);
        }
    }

    static class MenuActionListener implements ActionListener {
        private final Frame frame;

        public MenuActionListener(Frame frame) {
            this.frame = frame;
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            String command = event.getActionCommand();

            switch (command) {
                case "New":
                    frame.newGame();
                    break;
                case "Restart":
                    frame.restart();
                    break;
                case "Pause":
                    frame.pause();
                    break;
                case "Resume":
                    frame.resume();
                    break;
            }
        }
    }
}