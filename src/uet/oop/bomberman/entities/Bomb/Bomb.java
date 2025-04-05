package uet.oop.bomberman.entities.Bomb;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.entities.AnimatedEntity;
import uet.oop.bomberman.entities.Characters.Bomber;
import uet.oop.bomberman.entities.Characters.Character;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.Level.Coordinates;

public class Bomb extends AnimatedEntity {
    protected double timeToExplode = 120;
    public int timeAfter = 20;
    protected Board board;
    protected Flame[] flames;
    protected boolean exploded = false;

    public Bomb(int x, int y, Board board) {
        this.x = x;
        this.y = y;
        this.board = board;
        sprite = Sprite.bomb;
    }

    @Override
    public void update() {
        if (timeToExplode > 0) timeToExplode--;
        else {
            if (!exploded) explode();
            else updateFlames();
            if (timeAfter > 0) timeAfter--;
            else remove();
        }
        animate();
    }

    @Override
    public void render(Screen screen) {
        if (exploded) {
            sprite = Sprite.movingSprite(Sprite.bomb_exploded, Sprite.bomb_exploded1, Sprite.bomb_exploded2, animate, 20);
            renderFlames(screen);
        } else sprite = Sprite.movingSprite(Sprite.bomb, Sprite.bomb_1, Sprite.bomb_2, animate, 40);
        screen.renderEntity((int) x << 4, (int) y << 4, this);
    }

    public void renderFlames(Screen screen) {
        for (Flame flame : flames) flame.render(screen);
    }

    public void updateFlames() {
        for (Flame flame : flames) flame.update();
    }

    protected void explode() {
        Game.playSE(2);
        timeToExplode = 0;
        exploded = true;

        flames = new Flame[4];
        flames[0] = new Flame((int) x, (int) y - 1, 0, Game.getBombRadius(), board);
        flames[1] = new Flame((int) x + 1, (int) y, 1, Game.getBombRadius(), board);
        flames[2] = new Flame((int) x, (int) y + 1, 2, Game.getBombRadius(), board);
        flames[3] = new Flame((int) x - 1, (int) y, 3, Game.getBombRadius(), board);

        Character ch = board.getCharacterAtExcluding((int) x, (int) y, null);
        if (ch != null) ch.kill();
    }

    public FlameSegment flameAt(int x, int y) {
        if (!exploded) return null;
        for (Flame flame : flames) {
            if (flame == null) return null;
            FlameSegment e = flame.flameSegmentAt(x, y);
            if (e != null) return e;
        }
        return null;
    }

    @Override
    public boolean collide(Entity e) {
        if (e instanceof FlameSegment && !exploded) explode();
        return true;
    }
}