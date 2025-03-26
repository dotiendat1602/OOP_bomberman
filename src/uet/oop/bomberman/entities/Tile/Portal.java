package uet.oop.bomberman.entities.Tile;

import uet.oop.bomberman.entities.Characters.Bomber;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.graphics.Sprite;


public class Portal extends Tile {
    public Portal(int x, int y, Sprite sprite) {
        super(x, y, sprite);
    }

    @Override
    public boolean collide(Entity e) {
        if (e instanceof Bomber)
            if (getX() == e.getXTile() && getY() == e.getYTile()) {
                Game.playSE(7);
                return true;
            }
        return false;
    }
}
