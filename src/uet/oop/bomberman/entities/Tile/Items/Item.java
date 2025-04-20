package uet.oop.bomberman.entities.Tile.Items;

import uet.oop.bomberman.entities.Tile.Tile;
import uet.oop.bomberman.graphics.Sprite;

public abstract class Item extends Tile {
    public Item(int x, int y, Sprite sprite) {
        super(x, y, sprite);
    }
}
