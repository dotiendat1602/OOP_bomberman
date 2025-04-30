package uet.oop.bomberman.entities.Characters;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Bomb.Bomb;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.Tile.Destroyable.Brick;
import uet.oop.bomberman.entities.Tile.Destroyable.DestroyableTile;
import uet.oop.bomberman.entities.Tile.Items.Item;
import uet.oop.bomberman.entities.Tile.Portal;
import uet.oop.bomberman.entities.Tile.Wall;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.Input.Keyboard;
import uet.oop.bomberman.Level.Coordinates;
import uet.oop.bomberman.entities.Bomb.FlameSegment;
import uet.oop.bomberman.entities.Characters.Enemies.Enemy;

import java.util.Iterator;
import java.util.List;

import java.awt.*;

public class Bomber extends Character {
    private final Keyboard input;

    protected int finalAnimation = 30;

    protected int timeBetweenPutBombs = 0;
    private final List<Bomb> bombs;
    private final int maxSteps;
    private boolean render = true;

    public Bomber(int x, int y, Board board) {
        super(x, y, board);
        bombs = board.getBombs();
        this.input = board.getInput();
        this.sprite = Sprite.player_right;
        this.maxSteps = (int) Math.round(Game.TILE_SIZE / Game.getBomberSpeed());
        this.timeAfter = 250;
    }

    @Override
    public void update() {
        animate();
        clearBombs();
        if (!alive) {
            afterKill();
            return;
        }
        if (timeBetweenPutBombs < -7500) {
            timeBetweenPutBombs = 0;
        }
        else {
            timeBetweenPutBombs--;
        }
        calculateMove();
        detectPlaceBomb();
    }

    @Override
    public void render(Screen screen) {
        calculateXOffset();
        sprite = alive ? chooseSprite() : Sprite.movingSprite(Sprite.player_dead1, Sprite.player_dead2, Sprite.player_dead3, animate, 60);
        if (render) screen.renderEntity((int) x, (int) y - sprite.SIZE, this);
    }

    private void calculateXOffset() {
        Screen.setOffset(Screen.calculateXOffset(board, this), 0);
    }

    @Override
    protected void calculateMove() {
        double xa = 0, ya = 0;

        if (input.up) ya--;
        if (input.down) ya++;
        if (input.left) xa--;
        if (input.right) xa++;

        moving = xa != 0 || ya != 0;
        if (moving) move(x + xa * Game.getBomberSpeed(), y + ya * Game.getBomberSpeed());
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
    public boolean canMove(double x, double y) {
        int[][] points = {
                {Coordinates.pixelToTile(x + 1), Coordinates.pixelToTile(y + 1 - Game.TILE_SIZE)},  // Top-left
                {Coordinates.pixelToTile(x - 1 + (Game.TILE_SIZE * 3 / 4)), Coordinates.pixelToTile(y + 1 - Game.TILE_SIZE)}, // Top-right
                {Coordinates.pixelToTile(x + 1), Coordinates.pixelToTile(y - 1)},  // Bottom-left
                {Coordinates.pixelToTile(x - 1 + (Game.TILE_SIZE * 3 / 4)), Coordinates.pixelToTile(y - 1)} // Bottom-right
        };

        for (int[] p : points) {
            Entity entity = board.getEntity(p[0], p[1], this);
            if (entity instanceof Wall) return false;
            if (entity instanceof LayeredEntity) {
                Entity top = ((LayeredEntity) entity).getTopEntity();
                if (top instanceof Brick) return false;
                if (top instanceof Item) {
                    top.collide(this);
                    return true;
                }
                if (top instanceof Portal && board.detectNoEnemies()) {
                    if (top.collide(this)) board.nextLevel();
                    return true;
                }
            }
        }

        for (int[] p : points) {
            if (collide(board.getEntity(p[0], p[1], this))) {
                return false;
            }
        }

        return true;
    }

    private Sprite chooseSprite() {
        return switch (direct) {
            case 0 -> moving ? Sprite.movingSprite(Sprite.player_up_1, Sprite.player_up_2, animate, 20) : Sprite.player_up;
            case 2 -> moving ? Sprite.movingSprite(Sprite.player_down_1, Sprite.player_down_2, animate, 20) : Sprite.player_down;
            case 3 -> moving ? Sprite.movingSprite(Sprite.player_left_1, Sprite.player_left_2, animate, 20) : Sprite.player_left;
            default -> moving ? Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, animate, 20) : Sprite.player_right;
        };
    }

    private void detectPlaceBomb() {
        if (input.space && timeBetweenPutBombs < 0 && Game.getBombRate() > 0) {
            timeBetweenPutBombs = 30;
            placeBomb(Coordinates.pixelToTile(x + (double) Game.TILE_SIZE / 2), Coordinates.pixelToTile(y - (double) Game.TILE_SIZE / 2));
        }
    }

    protected void placeBomb(int x, int y) {
        Entity downThere = board.getEntity(x, y, this);
        if (downThere instanceof Bomb) return;
        board.addBomb(new Bomb(x, y, board));
        Game.playSE(1);
        Game.addBombRate(-1);
    }

    private void clearBombs() {
        Iterator<Bomb> bs = bombs.iterator();
        Bomb b;
        while (bs.hasNext()) {
            b = bs.next();
            if (b.isRemoved()) {
                bs.remove();
                Game.addBombRate(1);
            }
        }
    }

    @Override
    public boolean collide(Entity e) {
        if (e instanceof FlameSegment) e.collide(this);
        if (e instanceof Bomb) return e.collide(this);
        return false;
    }

    @Override
    public void kill() {
        if (!alive) return;
        Game.playSE(4);
        alive = false;
        board.addLives(-1);
    }

    @Override
    protected void afterKill() {
        if (timeAfter > 0) {
            --timeAfter;
            if (finalAnimation > 0) finalAnimation--;
            else render = false;
        } else {
            if (board.getLives() > 0) board.restart();
            else board.endGame();
        }
    }
}
