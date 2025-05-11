package uet.oop.bomberman;

import uet.oop.bomberman.GUI.Frame;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.Input.Keyboard;
import uet.oop.bomberman.Sound.Sound;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.*;

public class Game extends Canvas implements MouseListener, MouseMotionListener {
    //Thông số game
    public static final String TITLE = "Bomberman";
    public static final int TILE_SIZE = 16;
    public static final int WIDTH = TILE_SIZE * (31 / 2), HEIGHT = 13 * TILE_SIZE;
    public static int SCALE_MULTIPLE = 3;

    //Thông số mặc định của người chơi
    private static final int BOMB_RATE = 1;
    private static final int BOMB_RADIUS = 1;
    private static final double BOMBER_SPEED = 1.0;


    //Thông số mặc định của game
    public static final int TIME = 200;
    public static final int POINTS = 0;
    public static final int LIVES = 3;

    protected static int SCREEN_DELAY = 3;

    public static int highScore = 0;

    //Thông số riêng của người chơi, có thể thay đổi khi ăn được các item
    protected static int bombRate = BOMB_RATE;
    protected static int bombRadius = BOMB_RADIUS;
    protected static double bomberSpeed = BOMBER_SPEED;


    //Thời gian trong một level
    protected int screenDelay = SCREEN_DELAY;

    public static int levelWidth;
    public static int levelHeight;

    private final Keyboard input;
    static Sound sound = new Sound();

    //Trạng thái game
    private boolean running = false;
    private boolean paused = true;
    private boolean menu = true;
    private boolean isSound = true;
    private boolean isAboutPanel = false;
    public boolean isEndgame = false;

    //Sử dụng để render game
    private final BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private final int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

    private final Board board;
    private final Screen screen;
    private final Frame frame;

    public Game(Frame frame) {
        this.frame = frame;
        this.frame.setTitle(TITLE);

        input = new Keyboard();
        screen = new Screen(WIDTH, HEIGHT);

        board = new Board(this, input, screen);
        addKeyListener(input);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    //Render game, cài đặt để tạo ra FPS lớn nhất có thể tương thích với máy
    private void renderGame() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        screen.clear();
        board.render(screen);

        System.arraycopy(screen.pixels, 0, pixels, 0, pixels.length);

        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        board.renderMessages(g);

        g.dispose();
        bs.show();
    }

    private void renderScreen() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        screen.clear();

        Graphics g = bs.getDrawGraphics();
        board.drawScreen(g);

        g.dispose();
        bs.show();
    }

    //Update các thông số game
    private void update() {
        input.update();
        board.update();
    }

    //Start game
    public void start() {
        readHighscore();
        playMusic(0);
        while (menu) {
            renderScreen();
        }

        board.loadLevel(1);
//        running = true;

        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final double nps = 1000000000.0 / 60.0;
        double delta = 0;
        int frames = 0, updates = 0;

        requestFocus();

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nps;
            lastTime = now;
            while (delta > 1) {
                update();
                updates++;
                delta--;
            }

            if (!paused) {
                frame.get_infopanel().setVisible(true);
            }

            if (paused) {
                if (screenDelay <= 0) {
                    board.setShow(-1);
                    paused = false;
                }
                renderScreen();
            } else renderGame();

            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                frame.setTime(board.subtractTime());
                frame.setPoints(board.getPoints());
                frame.setLives(board.getLives());

                timer += 1000;

                this.frame.setTitle("Bomberman" + " | " + updates + " rate, " + frames + " fps");
                updates = 0;
                frames = 0;

                if (this.board.getShow() == 2) screenDelay--;
            }
        }
    }

    public void readHighscore() {
        BufferedReader read;
        try {
            read = new BufferedReader(new FileReader(new File("res/Data/BestScore.txt")));
            String score = read.readLine().trim();
            if (score == null)
                highScore = 0;
            else
                highScore = Integer.parseInt(score);
            read.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveHighScore() {
        try {
            File file = new File("res/Data/BestScore.txt");
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(String.valueOf(highScore));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Rectangle playButton = new Rectangle(Game.WIDTH + 50, Game.HEIGHT + 130, 150, 50);
        if (playButton.contains(e.getX(), e.getY()) && menu) {
            menu = false;
            running = true;
        }

        Rectangle musicButton = new Rectangle(Game.WIDTH + 50, Game.HEIGHT + 210, 150, 50);
        if (musicButton.contains(e.getX(), e.getY()) && menu) {
            if (isSound) {
                isSound = false;
                sound.stop();
            } else {
                isSound = true;
                sound.play();
                sound.loop();
            }

        }

        Rectangle aboutButton = new Rectangle(Game.WIDTH + 50, Game.HEIGHT + 280, 150, 50);
        if (aboutButton.contains(e.getX(), e.getY()) && menu) {
            isAboutPanel = true;
            getBoard().setShow(6);
        }

        Rectangle exitAboutButton = new Rectangle(Game.WIDTH + 370, Game.HEIGHT - 100, 60, 60);
        if (exitAboutButton.contains(e.getX(), e.getY()) && isAboutPanel) {
            isAboutPanel = false;
            getBoard().setShow(5);
        }

        Rectangle replayButton = new Rectangle(Game.WIDTH + 50, Game.HEIGHT + 170, 150, 50);
        if (replayButton.contains(e.getX(), e.getY()) && isEndgame) {
            board.newGame();
            isEndgame = false;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Rectangle playButton = new Rectangle(Game.WIDTH + 50, Game.HEIGHT + 130, 150, 50);
        Rectangle musicButton = new Rectangle(Game.WIDTH + 50, Game.HEIGHT + 210, 150, 50);
        Rectangle aboutButton = new Rectangle(Game.WIDTH + 50, Game.HEIGHT + 280, 150, 50);

        if (menu && !isAboutPanel) {
            if (playButton.contains(e.getX(), e.getY())
                    || aboutButton.contains(e.getX(), e.getY())
                    || musicButton.contains(e.getX(), e.getY())) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            } else {
                setCursor(Cursor.getDefaultCursor());
            }
        }

        Rectangle exitAbout = new Rectangle(Game.WIDTH + 370, Game.HEIGHT - 100, 60, 60);
        if (isAboutPanel) {
            if (exitAbout.contains(e.getX(), e.getY())) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            } else {
                setCursor(Cursor.getDefaultCursor());
            }
        }

        if (!menu) {
            setCursor(Cursor.getDefaultCursor());
        }

        Rectangle replayButton = new Rectangle(Game.WIDTH + 50, Game.HEIGHT + 170, 150, 50);
        if (isEndgame) {
            if (replayButton.contains(e.getX(), e.getY())) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            } else {
                setCursor(Cursor.getDefaultCursor());
            }
        }
    }


    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }


    public static int getBombRate() {
        return bombRate;
    }

    public static int getBombRadius() {
        return bombRadius;
    }

    public static double getBomberSpeed() {
        return bomberSpeed;
    }

    public static void addBombRate(int i) {
        bombRate += i;
    }

    public static void addBombRadius(int i) {
        bombRadius += i;
    }

    public static void addBomberSpeed(int i) {
        bomberSpeed += i;
    }

    public void resetScreenDelay() {
        screenDelay = SCREEN_DELAY;
    }

    public Board getBoard() {
        return board;
    }

    public int get_highscore() {
        return highScore;
    }

    public void set_highscore(int highscore) {
        highScore = highscore;
    }

    public void resume() {
        this.paused = false;
        screenDelay = -1;
    }

    public boolean isPaused() {
        return paused;
    }

    public void pause() {
        paused = true;
    }

    public void playMusic(int i) {
        sound.setFile(i);
        sound.play();
        sound.loop();
    }

    public static void playSE(int i) {
        sound.setFile(i);
        sound.play();
    }

//    public boolean isSoundEnabled() {
//        return isSound;
//    }
//
//    public void setSoundEnabled(boolean enabled) {
//        this.isSound = enabled;
//        if (!isSound) {
//            sound.stop();
//        } else {
//            sound.play();
//            sound.loop();
//        }
//    }
}