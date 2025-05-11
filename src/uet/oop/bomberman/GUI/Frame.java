package uet.oop.bomberman.GUI;

import uet.oop.bomberman.GUI.Menu.Menu;
import uet.oop.bomberman.Game;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {
    public GamePanel gamePanel;
    private final InfoPanel infoPanel;

    private final Game game;

    public Frame() {


        JPanel containerPanel = new JPanel(new BorderLayout());
        gamePanel = new GamePanel(this);
        game = gamePanel.getGame();
        infoPanel = new InfoPanel(gamePanel.getGame());

        setJMenuBar(new Menu(this));

        containerPanel.add(infoPanel, BorderLayout.NORTH);
        containerPanel.add(gamePanel, BorderLayout.CENTER);

//        game = gamePanel.getGame();
        infoPanel.setVisible(false);

        add(containerPanel);

        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        game.start();
    }

    public void changeLevel(int i) {
        game.getBoard().loadLevel(i);
    }

    public void newGame() {
        game.getBoard().newGame();
    }

    public void restart() {
        game.getBoard().restart();
    }

    public void pause() {
        game.getBoard().gamePause();
    }

    public void resume() {
        game.getBoard().gameResume();
    }


    public void setTime(int time) {
        infoPanel.setTime(time);
    }

    public void setLives(int lives) {
        infoPanel.setLives(lives);
    }

    public void setPoints(int points) {
        infoPanel.setPoints(points);
    }

    public InfoPanel get_infopanel() {
        return infoPanel;
    }

    public Game getGame() {
        return game;
    }


}
