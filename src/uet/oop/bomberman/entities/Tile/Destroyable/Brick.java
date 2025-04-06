package uet.oop.bomberman.entities.Tile.Destroyable;

import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.Level.Coordinates;

public class Brick extends DestroyableTile {
    public Brick(int x, int y, Sprite sprite) {
        super(x, y, sprite);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void render(Screen screen) {
        if (destroyed) {
            sprite = movingSprite(Sprite.brick_exploded, Sprite.brick_exploded1, Sprite.brick_exploded2);
            screen.renderEntityWithBelowSprite(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y), this, belowSprite);
        } else screen.renderEntity(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y), this);
    }
}