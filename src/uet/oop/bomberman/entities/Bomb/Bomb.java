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
            if (timeAfter > 0) timeAfter--;
            else remove();
        }
        animate();
    }

    @Override
    public void render(Screen screen) {
        if (exploded) {
            sprite = Sprite.movingSprite(Sprite.bomb_exploded, Sprite.bomb_exploded1, Sprite.bomb_exploded2, animate, 20);
        } else sprite = Sprite.movingSprite(Sprite.bomb, Sprite.bomb_1, Sprite.bomb_2, animate, 40);
        screen.renderEntity((int) x << 4, (int) y << 4, this);
    }

    protected void explode() {
        Game.playSE(2);
        timeToExplode = 0;
        exploded = true;
    }

    @Override
    public boolean collide(Entity e) {
        return true;
    }
}