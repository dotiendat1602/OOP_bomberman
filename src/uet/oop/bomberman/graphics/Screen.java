package uet.oop.bomberman.graphics;


import uet.oop.bomberman.entities.Characters.Bomber;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Screen {
    private final int TRANSPARENT_COLOR = 0xffff00ff;
    protected int width, height;
    public int[] pixels;

    private BufferedImage background = null;
    private Image backgroundFixed = null;

    private BufferedImage aboutImage = null;

    public static int xOffset = 0, yOffset = 0;

    public Screen(int width, int height) {
        this.width = width;
        this.height = height;
        pixels = new int[width * height];

        try {
            background = ImageIO.read(new File("res/textures/menu.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        backgroundFixed = background.getScaledInstance(Game.WIDTH * Game.SCALE_MULTIPLE, Game.HEIGHT * Game.SCALE_MULTIPLE, Image.SCALE_DEFAULT);

        try {
            aboutImage = ImageIO.read(new File("res/textures/about-table.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        Arrays.fill(pixels, 0);
    }

    public void renderEntity(int xp, int yp, Entity entity) {
        xp -= xOffset;
        yp -= yOffset;
        for (int y = 0; y < entity.getSprite().getSize(); y++) {
            int ya = y + yp;
            for (int x = 0; x < entity.getSprite().getSize(); x++) {
                int xa = x + xp;
                if (xa < - entity.getSprite().getSize() || xa >= width || ya < 0 || ya >= height) break;
                if (xa < 0) xa = 0;
                int color = entity.getSprite().getPixel(x + y * entity.getSprite().getSize());
                if (color != TRANSPARENT_COLOR) pixels[xa + ya * width] = color;
            }
        }
    }

    public void renderEntityWithBelowSprite(int xp, int yp, Entity entity, Sprite below) {
        xp -= xOffset;
        yp -= yOffset;
        for (int y = 0; y < entity.getSprite().getSize(); y++) {
            int ya = y + yp;
            for (int x = 0; x < entity.getSprite().getSize(); x++) {
                int xa = x + xp;
                if (xa < - entity.getSprite().getSize() || xa >= width || ya < 0 || ya >= height) break;
                if (xa < 0) xa = 0;
                int color = entity.getSprite().getPixel(x + y * entity.getSprite().getSize());
                if(color != TRANSPARENT_COLOR) pixels[xa + ya * width] = color;
                else pixels[xa + ya * width] = below.getPixel(x + y * below.getSize());
            }
        }
    }

    public static void setOffset(int xO, int yO) {
        xOffset = xO;
        yOffset = yO;
    }

    public static int calculateXOffset(Board board, Bomber bomber) {
        if (bomber == null) return 0;
        int temp = xOffset;
        double bomberX = bomber.getX() / 16;
        double complement = 0.5;
        int firstBreakpoint = board.getWidth() / 4;
        int lastBreakpoint = board.getWidth() - firstBreakpoint;
        if (bomberX > firstBreakpoint + complement && bomberX < lastBreakpoint - complement) temp = (int) bomber.getX()  - (Game.WIDTH / 2);
        return temp;
    }

    /* Vẽ các màn hình game */
    public void drawEndGame(Graphics g, int points, int highscore, int level) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("res/textures/score-table.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int targetWidth = image.getWidth() * Game.SCALE_MULTIPLE / 4;
        int targetHeight = image.getHeight() * Game.SCALE_MULTIPLE / 4;
        Image scoreTable = image.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        Font font = new Font("Arial", Font.PLAIN, 12 * Game.SCALE_MULTIPLE);
        g.setFont(font);
        g.setColor(Color.white);
        drawCenteredImage(scoreTable, targetWidth, targetHeight, getRealWidth(), getRealHeight(), g);
        drawCenteredString("Level " + level, getRealWidth(), getRealHeight() - targetHeight + 140 / Game.SCALE_MULTIPLE, g);
        drawCenteredString("Your score: " + points, getRealWidth(), getRealHeight() - targetHeight + 700 / Game.SCALE_MULTIPLE, g);
        drawCenteredString("High score: " + highscore, getRealWidth(), getRealHeight() - targetHeight + 1000 / Game.SCALE_MULTIPLE, g);
        drawCenteredString("Retry", getRealWidth() + 10, getRealHeight() - targetHeight + 1524 / Game.SCALE_MULTIPLE, g);
    }

    public void drawChangeLevel(Graphics g, int level) {
        g.setColor(Color.black);
        g.fillRect(0, 0, getRealWidth(), getRealHeight());

        Font font = new Font("Arial", Font.PLAIN, 20 * Game.SCALE_MULTIPLE);
        g.setFont(font);
        g.setColor(Color.white);
        drawCenteredString("LEVEL " + level, getRealWidth(), getRealHeight(), g);
    }

    public void drawPaused(Graphics g) {
        Font font = new Font("Arial", Font.PLAIN, 20 * Game.SCALE_MULTIPLE);
        g.setFont(font);
        g.setColor(Color.white);
        drawCenteredString("PAUSED", getRealWidth(), getRealHeight(), g);
    }

    public void drawMenu(Graphics g) {
        g.drawImage(backgroundFixed, 0, 0, null);
    }

    public void drawAbout(Graphics g) {
        g.drawImage(aboutImage, Game.WIDTH - 175, Game.HEIGHT - 100, null);
    }

    public void drawFinishGame(Graphics g, int points) {
        g.setColor(Color.black);
        g.fillRect(0, 0, getRealWidth(), getRealHeight());

        Font font = new Font("Arial", Font.PLAIN, 20 * Game.SCALE_MULTIPLE);
        g.setFont(font);
        g.setColor(Color.white);
        drawCenteredString("VICTORY", getRealWidth(), getRealHeight(), g);

        font = new Font("Arial", Font.PLAIN, 10 * Game.SCALE_MULTIPLE);
        g.setFont(font);
        g.setColor(Color.yellow);
        drawCenteredString("SCORE: " + points, getRealWidth(), getRealHeight() + (Game.TILE_SIZE * 2) * Game.SCALE_MULTIPLE, g);
    }

    public void drawCenteredString(String s, int w, int h, Graphics g) {
        FontMetrics fm = g.getFontMetrics();
        g.drawString(s, (w - fm.stringWidth(s)) / 2, (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2));
    }

    public void drawCenteredImage(Image image, int imageWidth, int imageHeight,
                                  int gameWidth, int gameHeight, Graphics g) {
        int x = (gameWidth - imageWidth) / 2;
        int y = (gameHeight - imageHeight) / 2;
        g.drawImage(image, x, y, null);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getRealWidth() {
        return width * Game.SCALE_MULTIPLE;
    }

    public int getRealHeight() {
        return height * Game.SCALE_MULTIPLE;
    }
}
