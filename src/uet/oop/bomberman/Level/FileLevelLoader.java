package uet.oop.bomberman.Level;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.entities.Characters.Bomber;
import uet.oop.bomberman.entities.Tile.Grass;
import uet.oop.bomberman.entities.Tile.Destroyable.Brick;
import uet.oop.bomberman.entities.Tile.Items.BombItem;
import uet.oop.bomberman.entities.Tile.Items.FlameItem;
import uet.oop.bomberman.entities.Tile.Items.SpeedItem;
import uet.oop.bomberman.entities.Tile.Portal;
import uet.oop.bomberman.entities.Tile.Wall;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.entities.LayeredEntity;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class FileLevelLoader {
    private int width, height, level;
    private Board board;

    private static char[][] map;

    public FileLevelLoader(Board board, int level) throws Exception {
        loadLevel(level);
        this.board = board;
    }

    public void loadLevel(int level) throws Exception {
        try {
            Class<?> c = Class.forName("uet.oop.bomberman.Level.FileLevelLoader");
            InputStream stream = c.getResourceAsStream("/Levels/level" + level + ".txt");
            Reader r = new InputStreamReader(Objects.requireNonNull(stream), StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(r);

            String line;
            line = br.readLine();
            String[] sizes = line.split("\\s");
            this.level = Integer.parseInt(sizes[0]);
            height = Integer.parseInt(sizes[1]);
            width = Integer.parseInt(sizes[2]);
            map = new char[width][height];

            int rowNum = 0;
            while ((line = br.readLine()) != null) {
                for (int i = 0; i < line.length(); i++) {
                    map[i][rowNum] = line.charAt(i);
                }
                rowNum++;
            }

            stream.close();
            br.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Không tìm thấy file map");
            e.printStackTrace();
        }
    }

    public void createEntities() {
        Game.levelHeight = height;
        Game.levelWidth = width;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pos = x + y * width;
                System.out.print(map[x][y]);
                switch (map[x][y]) {
                    case 'p' -> {
                        board.addEntity(pos, new Grass(x, y, Sprite.grass));
                        board.addCharacter(new Bomber(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILE_SIZE, board));
                    }
                    case '#' -> board.addEntity(pos, new Wall(x, y, Sprite.wall));
                    case '*' -> board.addEntity(pos, new LayeredEntity(x, y, new Grass(x, y, Sprite.grass), new Brick(x, y, Sprite.brick)));
                    case 'x' -> board.addEntity(pos, new LayeredEntity(x, y, new Grass(x, y, Sprite.grass), new Brick(x, y, Sprite.brick)));
                    case 'b' -> board.addEntity(pos, new LayeredEntity(x, y, new Grass(x, y, Sprite.grass), new BombItem(x, y, Sprite.powerup_bombs)));
                    case 'f' -> board.addEntity(pos, new LayeredEntity(x, y, new Grass(x, y, Sprite.grass), new FlameItem(x, y, Sprite.powerup_flames)));
                    case 's' -> board.addEntity(pos, new LayeredEntity(x, y, new Grass(x, y, Sprite.grass), new SpeedItem(x, y, Sprite.powerup_speed)));
                    case '1' -> {
                        board.addEntity(pos, new Grass(x, y, Sprite.grass));
                    }
                    case '2' -> {
                        board.addEntity(pos, new Grass(x, y, Sprite.grass));
                    }
                    case '3' -> {
                        board.addEntity(pos, new Grass(x, y, Sprite.grass));
                    }
                    case '4' -> {
                        board.addEntity(pos, new Grass(x, y, Sprite.grass));
                    }
                    case '5' -> {
                        board.addEntity(pos, new Grass(x, y, Sprite.grass));
                    }
                    case '6' -> {
                        board.addEntity(pos, new Grass(x, y, Sprite.grass));
                    }
                    default -> board.addEntity(pos, new Grass(x, y, Sprite.grass));
                }
            }
            System.out.println();
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLevel() {
        return level;
    }
}
