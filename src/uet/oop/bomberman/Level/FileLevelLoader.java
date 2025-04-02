package uet.oop.bomberman.Level;

import uet.oop.bomberman.Board;

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
                for (int i = 0; i < line.length(); i++) map[i][rowNum] = line.charAt(i);
                rowNum++;
            }

            stream.close();
            br.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
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
