package uet.oop.bomberman.entities.Characters.Enemies;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.graphics.Sprite;

import uet.oop.bomberman.entities.Characters.AI.AILow;

public class Balloom extends Enemy {
    public Balloom(int x, int y, Board board) {
        super(x, y, board, Sprite.balloom_dead, 0.5, 100);
        sprite = Sprite.balloom_left1;
        ai = new AILow();
        direct = ai.calculateDirection();
    }

    @Override
    protected void chooseSprite() {
        switch (direct) {
            case 0, 1 -> sprite = Sprite.movingSprite(Sprite.balloom_right1, Sprite.balloom_right2, Sprite.balloom_right3, animate, 60);
            case 2, 3 -> sprite = Sprite.movingSprite(Sprite.balloom_left1, Sprite.balloom_left2, Sprite.balloom_left3, animate, 60);
        }
    }
}