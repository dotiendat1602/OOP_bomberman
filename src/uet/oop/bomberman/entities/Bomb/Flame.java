package uet.oop.bomberman.entities.Bomb;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.entities.Characters.Character;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.Tile.Destroyable.DestroyableTile;
import uet.oop.bomberman.entities.Tile.Wall;
import uet.oop.bomberman.graphics.Screen;

public class Flame extends Entity {
    protected Board board;
    protected int direction;
    protected int xOrigin, yOrigin;
    protected FlameSegment[] flameSegments;
    private final int radius;

    public Flame(int x, int y, int direction, int radius, Board board) {
        this.xOrigin = x;
        this.yOrigin = y;
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.radius = radius;
        this.board = board;
        createFlameSegments();
    }

    private void createFlameSegments() {
        int length = calculatePermittedDistance();
        flameSegments = new FlameSegment[length];

        for (int i = 0; i < length; i++) {
            boolean isLast = (i == length - 1);
            int fx = xOrigin;
            int fy = yOrigin;

            switch (direction) {
                case 0: fy = yOrigin - i; break; // UP
                case 1: fx = xOrigin + i; break; // RIGHT
                case 2: fy = yOrigin + i; break; // DOWN
                case 3: fx = xOrigin - i; break; // LEFT
            }

            flameSegments[i] = new FlameSegment(fx, fy, direction, isLast, board);
            flameSegments[i].collide(board.getCharacterAtExcluding(fx, fy, null));
            flameSegments[i].collide(board.getBombAt(fx, fy));
        }
    }

    private int calculatePermittedDistance() {
        for (int i = 0; i < radius; i++) {
            int tx = xOrigin;
            int ty = yOrigin;

            switch (direction) {
                case 0: ty = yOrigin - i; break; // UP
                case 1: tx = xOrigin + i; break; // RIGHT
                case 2: ty = yOrigin + i; break; // DOWN
                case 3: tx = xOrigin - i; break; // LEFT
            }

            Entity entity = board.getEntityAt(tx, ty);
            if (entity instanceof Wall) {
                return i;
            }

            if (entity instanceof LayeredEntity) {
                Entity top = ((LayeredEntity) entity).getTopEntity();
                if (top instanceof DestroyableTile) {
                    if (!((DestroyableTile) top).isDestroyed()) {
                        ((DestroyableTile) top).destroy();
                        return i;
                    }
                }
            }
        }
        return radius;
    }

    public FlameSegment flameSegmentAt(int x, int y) {
        for (FlameSegment segment : flameSegments) {
            if (segment.getX() == x && segment.getY() == y) return segment;
        }
        return null;
    }

    @Override
    public void update() {
        for (FlameSegment segment : flameSegments) {
            segment.update();
        }
    }

    @Override
    public void render(Screen screen) {
        for (FlameSegment segment : flameSegments) {
            segment.render(screen);
        }
    }

    @Override
    public boolean collide(Entity e) {
        if (e instanceof Character) {
            ((Character) e).kill();
        }
        return false;
    }
}
