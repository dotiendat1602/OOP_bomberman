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

public class FlameSegment extends AnimatedEntity {
    private final boolean isLast;
    private final int direction;
    private final Board board;

    public FlameSegment(int x, int y, int direction, boolean isLast, Board board) {
        this.x = x;
        this.y = y;
        this.isLast = isLast;
        this.direction = direction;
        this.board = board;
        updateSprite();
    }

    @Override
    public void render(Screen screen) {
        updateSprite();
        screen.renderEntity((int) x << 4, (int) y << 4, this);
    }

    private void updateSprite() {
        sprite = switch (direction) {
            case 0 -> isLast ? Sprite.movingSprite(Sprite.explosion_vertical_top_last, Sprite.explosion_vertical_top_last1, Sprite.explosion_vertical_top_last2, animate, 20)
                    : Sprite.movingSprite(Sprite.explosion_vertical, Sprite.explosion_vertical1, Sprite.explosion_vertical2, animate, 20);
            case 1 -> isLast ? Sprite.movingSprite(Sprite.explosion_horizontal_right_last, Sprite.explosion_horizontal_right_last1, Sprite.explosion_horizontal_right_last2, animate, 20)
                    : Sprite.movingSprite(Sprite.explosion_horizontal, Sprite.explosion_horizontal1, Sprite.explosion_horizontal2, animate, 20);
            case 2 -> isLast ? Sprite.movingSprite(Sprite.explosion_vertical_down_last, Sprite.explosion_vertical_down_last1, Sprite.explosion_vertical_down_last2, animate, 20)
                    : Sprite.movingSprite(Sprite.explosion_vertical, Sprite.explosion_vertical1, Sprite.explosion_vertical2, animate, 20);
            case 3 -> isLast ? Sprite.movingSprite(Sprite.explosion_horizontal_left_last, Sprite.explosion_horizontal_left_last1, Sprite.explosion_horizontal_left_last2, animate, 20)
                    : Sprite.movingSprite(Sprite.explosion_horizontal, Sprite.explosion_horizontal1, Sprite.explosion_horizontal2, animate, 20);
            default -> sprite;
        };
    }

    @Override
    public void update() {
        animate();
    }

    @Override
    public boolean collide(Entity e) {
        Bomber b = board.getBomber();

        int leftX = Coordinates.pixelToTile(b.getX() + Game.TILE_SIZE / 6.0);
        int rightX = Coordinates.pixelToTile(b.getX() + Game.TILE_SIZE * 4.0 / 6.0);
        int bottomY = Coordinates.pixelToTile(b.getY() - Game.TILE_SIZE / 6.0);
        int topY = Coordinates.pixelToTile(b.getY() - Game.TILE_SIZE * 4.0 / 6.0);

        if ((leftX == x && b.getYTile() == y) || (rightX == x && b.getYTile() == y)
                || (b.getXTile() == x && bottomY == y) || (b.getXTile() == x && topY == y)) {
            b.kill();
        }

        if (e instanceof Character) ((Character) e).kill();
        if (e instanceof Bomb) e.collide(this);

        return false;
    }
}
