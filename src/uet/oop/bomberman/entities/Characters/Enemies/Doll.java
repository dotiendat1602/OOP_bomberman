package uet.oop.bomberman.entities.Characters.Enemies;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.entities.Characters.AI.AILow;

public class Doll extends Enemy {
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

    public Doll(int x, int y, Board board) {
        super(x, y, board, Sprite.doll_dead, 1.0, 200);
        sprite = Sprite.doll_right1;
        ai = new AILow();
        direct = ai.calculateDirection();
    }

    @Override
    protected void chooseSprite() {
        DirectionGroup directionGroup = DirectionGroup.fromDirection(direct);

        // Default sprites for non-moving state
        Sprite defaultSprite = (directionGroup == DirectionGroup.RIGHT) ? Sprite.doll_right1 : Sprite.doll_left1;

        // Sprites for moving state
        if (moving) {
            if (directionGroup == DirectionGroup.RIGHT) {
                sprite = Sprite.movingSprite(Sprite.doll_right1, Sprite.doll_right2, Sprite.doll_right3, animate, ANIMATION_FRAME_DURATION);
            } else {
                sprite = Sprite.movingSprite(Sprite.doll_left1, Sprite.doll_left2, Sprite.doll_left3, animate, ANIMATION_FRAME_DURATION);
            }
        } else {
            sprite = defaultSprite;
        }
    }
}