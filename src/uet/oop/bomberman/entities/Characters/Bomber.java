package uet.oop.bomberman.entities.Characters;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.Input.Keyboard;
import uet.oop.bomberman.Level.Coordinates;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

public class Bomber extends Character {
    protected Keyboard input;

    private boolean render = true;
    private final int maxSteps;

    public Bomber(int x, int y, Board board) {
        super(x, y, board);
        timeAfter = 250;
        input = this.board.getInput();
        sprite = Sprite.player_right;
        maxSteps = (int) Math.round(Game.TILE_SIZE / Game.getBomberSpeed());
    }

    @Override
    public void update() {

    }


    @Override
    public void render(Screen screen) {
        calculateXOffset();
        if (alive) chooseSprite();
        else sprite = Sprite.movingSprite(Sprite.player_dead1, Sprite.player_dead2, Sprite.player_dead3, animate, 60);
        if (render) screen.renderEntity((int) x, (int) y - sprite.SIZE, this);
    }

    public void calculateXOffset() {
        int xScroll = Screen.calculateXOffset(board, this);
        Screen.setOffset(xScroll, 0);
    }

    @Override
    public boolean collide(Entity e) {
        return false;
    }

    @Override
    protected void calculateMove() {

    }


    private void soften(double xa, double ya) {
        if (xa != x && y == ya) {
            double near1 = ((int) ya / Game.TILE_SIZE) * Game.TILE_SIZE;
            double near2 = ((int) ya / Game.TILE_SIZE + 1) * Game.TILE_SIZE;
            if (ya - near1 <= 8) {
                if (canMove(xa, near1)) {
                    y--;
                    soften(xa, ya--);
                    if (xa > x) direct = 4;
                    else direct = 3;
                }
            }
            if (near2 - ya <= 8) {
                if (canMove(xa, near2)) {
                    y++;
                    move(xa, ya++);
                    if (xa > x) direct = 4;
                    else direct = 3;
                }
            }
        } else if (xa == x && y != ya) {
            double near1 = ((int) xa / Game.TILE_SIZE) * Game.TILE_SIZE;
            double near2 = ((int) xa / Game.TILE_SIZE + 1) * Game.TILE_SIZE;
            if (xa - near1 <= 8) {
                if (canMove(near1, ya)) {
                    x--;
                    soften(xa--, ya);
                }
            }
            if (near2 - xa <= 8) {
                if (canMove(near2, ya)) {
                    x++;
                    soften(xa++, ya);
                    direct = 1;
                }
            }
        }
    }

    @Override
    public void move(double xa, double ya) {
        if (y < ya) direct = 2;
        if (y > ya) direct = 0;
        if (x > xa) direct = 3;
        if (x < xa) direct = 4;
        if (canMove(xa, ya)) {
            x = xa;
            y = ya;
        } else soften(xa, ya);
    }

    @Override
    public void kill() {

    }

    @Override
    protected void afterKill() {

    }

    @Override
    protected boolean canMove(double x, double y) {
        return false;
    }


    private void chooseSprite() {
        switch (direct) {
            case 0 -> {
                sprite = Sprite.player_up;
                if (moving) sprite = Sprite.movingSprite(Sprite.player_up_1, Sprite.player_up_2, animate, 20);
            }
            case 2 -> {
                sprite = Sprite.player_down;
                if (moving) sprite = Sprite.movingSprite(Sprite.player_down_1, Sprite.player_down_2, animate, 20);
            }
            case 3 -> {
                sprite = Sprite.player_left;
                if (moving) sprite = Sprite.movingSprite(Sprite.player_left_1, Sprite.player_left_2, animate, 20);
            }
            default -> {
                sprite = Sprite.player_right;
                if (moving) sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, animate, 20);
            }
        }
    }
}
