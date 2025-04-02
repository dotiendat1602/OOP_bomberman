package uet.oop.bomberman.entities.Characters;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Tile.*;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.Input.Keyboard;
import uet.oop.bomberman.Level.Coordinates;

import java.awt.*;
import java.util.Iterator;
import java.util.List;


public class Bomber extends Character{
    private final int time = 15;
    protected Keyboard input;
    protected int finalAnimation = 30;

    private int countTime = 0;
    private boolean render = true;
    private int direction;
    private final int maxSteps;
    private int steps;

}
