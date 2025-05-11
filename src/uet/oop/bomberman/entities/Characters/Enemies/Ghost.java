package uet.oop.bomberman.entities.Characters.Enemies;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.entities.Characters.AI.AIMedium;

import uet.oop.bomberman.Level.Coordinates;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Tile.Wall;

public class Ghost extends Enemy {
    private static final int ANIMATION_FRAME_DURATION = 60;

    // Enum for directions to replace magic numbers
    private enum DirectionGroup {
        RIGHT(0, 1),
        LEFT(2, 3);

        private final int[] directions;

        DirectionGroup(int... directions) {
            this.directions = directions;
        }

        public boolean contains(int direction) {
            for (int d : directions) {
                if (d == direction) return true;
            }
            return false;
        }

        public static DirectionGroup fromDirection(int direction) {
            if (RIGHT.contains(direction)) return RIGHT;
            if (LEFT.contains(direction)) return LEFT;
            throw new IllegalArgumentException("Invalid direction: " + direction);
        }
    }

    public Ghost(int x, int y, Board board) {
        super(x, y, board, Sprite.ghost_dead, 0.5, 800);
        sprite = Sprite.ghost_right1;
        ai = new AIMedium(board, this, true);
        direct = ai.calculateDirection();
    }

    @Override
    public boolean canMove(double x, double y) {
        // Define corner positions for collision checking
        double upperLeftY = y - Game.TILE_SIZE;
        double upperRightY = y - Game.TILE_SIZE;
        double lowerLeftY = y - 1;
        double lowerRightY = y - 1;
        double upperRightX = x - 1 + Game.TILE_SIZE;
        double lowerRightX = x - 1 + Game.TILE_SIZE;

        // Convert pixel coordinates to tile coordinates
        int tileUpperLeftX = Coordinates.pixelToTile(x);
        int tileUpperLeftY = Coordinates.pixelToTile(upperLeftY);
        int tileUpperRightX = Coordinates.pixelToTile(upperRightX);
        int tileUpperRightY = Coordinates.pixelToTile(upperRightY);
        int tileLowerLeftX = Coordinates.pixelToTile(x);
        int tileLowerLeftY = Coordinates.pixelToTile(lowerLeftY);
        int tileLowerRightX = Coordinates.pixelToTile(lowerRightX);
        int tileLowerRightY = Coordinates.pixelToTile(lowerRightY);

        // Get entities at the four corners
        Entity entityUpperLeft = board.getEntity(tileUpperLeftX, tileUpperLeftY, this);
        Entity entityUpperRight = board.getEntity(tileUpperRightX, tileUpperRightY, this);
        Entity entityLowerLeft = board.getEntity(tileLowerLeftX, tileLowerLeftY, this);
        Entity entityLowerRight = board.getEntity(tileLowerRightX, tileLowerRightY, this);

        // Check for wall collision at any corner
        if (entityUpperLeft instanceof Wall || entityUpperRight instanceof Wall ||
                entityLowerLeft instanceof Wall || entityLowerRight instanceof Wall) {
            return false;
        }

        // Check if any corner collides with an entity
        return !collide(entityUpperLeft) && !collide(entityUpperRight) &&
                !collide(entityLowerLeft) && !collide(entityLowerRight);
    }

    @Override
    protected void chooseSprite() {
        DirectionGroup directionGroup = DirectionGroup.fromDirection(direct);

        // Default sprites for non-moving state
        Sprite defaultSprite = (directionGroup == DirectionGroup.RIGHT) ? Sprite.ghost_right1 : Sprite.ghost_left1;

        // Sprites for moving state
        if (moving) {
            if (directionGroup == DirectionGroup.RIGHT) {
                sprite = Sprite.movingSprite(Sprite.ghost_right1, Sprite.ghost_right2, Sprite.ghost_right3, animate, ANIMATION_FRAME_DURATION);
            } else {
                sprite = Sprite.movingSprite(Sprite.ghost_left1, Sprite.ghost_left2, Sprite.ghost_left3, animate, ANIMATION_FRAME_DURATION);
            }
        } else {
            sprite = defaultSprite;
        }
    }
}