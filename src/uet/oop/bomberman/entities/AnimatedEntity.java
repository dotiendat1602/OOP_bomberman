package uet.oop.bomberman.entities;

public abstract class AnimatedEntity extends Entity {
    protected int animate = 0;
    protected final int MAX_ANIMATE = 7500;

    protected void animate() {
        if (animate < MAX_ANIMATE) animate++;
        else animate = 0;
    }
}
