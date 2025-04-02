package uet.oop.bomberman.GUI.Menu;

import uet.oop.bomberman.GUI.Frame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Class Player tạo menu tùy chọn kiểu điều khiển người chơi với các lựa chọn Auto và Manual.
 * Mỗi tùy chọn được gán sự kiện tương ứng để chuyển đổi giữa chế độ chơi tự động và thủ công.
 */
public class Player extends JMenu {
    Frame frame;

    private static final String MENU_TITLE = "Player";

    // Định nghĩa các tùy chọn menu
    private static final String[] PLAYER_MODES = {"Auto", "Manual"};

    /**
     * Khởi tạo menu Player với các tùy chọn điều khiển
     * @param frame Frame chính của game
     */
    public Player(Frame frame) {
        super(MENU_TITLE);
        this.frame = frame;

        // Tạo các menu items
        createMenuItems();
    }

    /**
     * Tạo tất cả các menu items cho việc điều khiển người chơi
     */
    private void createMenuItems() {
        MenuActionListener actionListener = new MenuActionListener(frame);

        for (String mode : PLAYER_MODES) {
            JMenuItem menuItem = new JMenuItem(mode);
            menuItem.addActionListener(actionListener);
            add(menuItem);
        }
    }

    /**
     * Lớp xử lý sự kiện cho các tùy chọn điều khiển người chơi
     */
    static class MenuActionListener implements ActionListener {
        public Frame frame;

        /**
         * @param frame Frame chính để thực hiện các hành động
         */
        public MenuActionListener(Frame frame) {
            this.frame = frame;
        }

        /**
         * Xử lý sự kiện khi người dùng chọn một chế độ điều khiển
         * @param e Sự kiện được kích hoạt
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            switch (command) {
                case "Auto":
//                    frame.auto();
                    break;
                case "Manual":
//                    frame.manual();
                    break;
            }
        }
    }
}