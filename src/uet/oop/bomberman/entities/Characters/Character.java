package uet.oop.bomberman.entities.Characters;

import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.AnimatedEntity;
import uet.oop.bomberman.Board;
import uet.oop.bomberman.graphics.Screen;

public abstract class Character extends AnimatedEntity {
    protected Board board;
    protected int direct = -1;
    protected boolean alive = true;
    protected boolean moving = false;
    public int timeAfter = 70;

    public Character(int x, int y, Board board) {
        this.x = x;
        this.y = y;
        this.board = board;
    }

    public abstract void update();

    public abstract void render(Screen screen);

    protected abstract void calculateMove();

    protected abstract void move(double xa, double ya);

    public abstract void kill();

    protected abstract void afterKill();

    protected abstract boolean canMove(double x, double y);

    protected double getXMessage() {
        return (x * Game.SCALE_MULTIPLE) + ((double) sprite.SIZE / 2 * Game.SCALE_MULTIPLE);
    }

    protected double getYMessage() {
        return (y * Game.SCALE_MULTIPLE) - ((double) sprite.SIZE / 2 * Game.SCALE_MULTIPLE);
    }
}
