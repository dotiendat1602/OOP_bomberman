package uet.oop.bomberman.GUI.Menu;

import uet.oop.bomberman.GUI.Frame;

import javax.swing.*;

public class Menu extends JMenuBar {
    public Menu(Frame frame) {
        add(new Game(frame));
        add(new Player(frame));
    }
}