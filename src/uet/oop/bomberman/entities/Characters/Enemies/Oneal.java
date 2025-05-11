package uet.oop.bomberman.entities.Characters.Enemies;


import uet.oop.bomberman.entities.Characters.AI.AIMedium;
import uet.oop.bomberman.Board;
import uet.oop.bomberman.graphics.Sprite;


public class Oneal extends Enemy {
    private static final double ONEAL_SPEED = 0.5;
    private static final int ONEAL_POINTS = 400;
    private static final int ANIMATION_CYCLE = 60;

    private final AIMedium ai;
    private int direct;

    public Oneal(int x, int y, Board board) {
        super(x, y, board, Sprite.oneal_dead, ONEAL_SPEED, ONEAL_POINTS);
        this.sprite = Sprite.oneal_left1;
        this.ai = new AIMedium(board, this, false);
        this.direct = ai.calculateDirection();
    }

    @Override
    protected void chooseSprite() {
        // Default to left sprite when not moving
        sprite = Sprite.oneal_left1;

        if (moving) {
            if (direct == 0 || direct == 1) {
                // Moving right: animate between right-facing sprites
                sprite = Sprite.movingSprite(Sprite.oneal_right1, Sprite.oneal_right2, Sprite.oneal_right3, animate, ANIMATION_CYCLE);
            } else if (direct == 2 || direct == 3) {
                // Moving left: animate between left-facing sprites
                sprite = Sprite.movingSprite(Sprite.oneal_left1, Sprite.oneal_left2, Sprite.oneal_left3, animate, ANIMATION_CYCLE);
            }
        }
    }
}
