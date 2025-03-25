package uet.oop.bomberman.entities;

import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.Level.Coordinates;

public abstract class Entity {
    //Tọa độ X tính từ góc trái trên trong Canvas
    protected double x;

    //Tọa độ Y tính từ góc trái trên trong Canvas
    protected double y;

    protected boolean removed = false;

    // Hình ảnh của entity
    protected Sprite sprite;


    public abstract void update();

    public abstract void render(Screen screen);

    public void remove() {
        removed = true;
    }

    public boolean isRemoved() {
        return removed;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public abstract boolean collide(Entity e);

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }


    // Chuyển tọa độ từ pixel sang các ô trên map
    public int getXTile() {
        return Coordinates.pixelToTile(x + (double) sprite.SIZE / 2);
    }

    public int getYTile() {
        return Coordinates.pixelToTile(y - (double) sprite.SIZE / 2);
    }
}
