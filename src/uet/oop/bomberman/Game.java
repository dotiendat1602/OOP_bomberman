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
    private boolean isAboutPane = false;

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

    @Override
    public void mouseClicked(MouseEvent e) {
        Rectangle playButton = new Rectangle(Game.WIDTH + 50, Game.HEIGHT + 130, 150, 50);
        if (playButton.contains(e.getX(), e.getY()) && menu) {
            menu = false;
            running = true;
        }

//        Rectangle optionButton = new Rectangle(Game.WIDTH + 50, Game.HEIGHT + 210, 150, 50);
//        if (optionButton.contains(e.getX(), e.getY()) && _menu && !isSetting) {
//            isSetting = true;
//            getBoard().setShow(5);
//        }

        Rectangle aboutButton = new Rectangle(Game.WIDTH + 50, Game.HEIGHT + 280, 150, 50);
        if (aboutButton.contains(e.getX(), e.getY()) && menu) {
            isAboutPane = true;
            getBoard().setShow(6);
        }

        Rectangle exitAboutButton = new Rectangle(Game.WIDTH + 370, Game.HEIGHT - 100, 60, 60);
        if (exitAboutButton.contains(e.getX(), e.getY()) && isAboutPane) {
            isAboutPane = false;
            getBoard().setShow(5);
        }

//        Rectangle exitSettingButton = new Rectangle(Game.WIDTH + 300, Game.HEIGHT - 60, 50, 50);
//        if (exitSettingButton.contains(e.getX(), e.getY()) && (_paused || isSetting)) {
//            if (_menu) {
//                isSetting = false;
//                getBoard().setShow(4);
//            } else {
//                isSetting = false;
//                getBoard().gameResume();
//            }
//            if (isClickChangeMap) {
//                if(screen.isBasicMap){
//                    map.modifySpriteSheet("/textures/miramar.png", 64);
//                    changeMap();
//                    _frame.get_infopanel().changeBackground(desertColor);
//                    screen.isBasicMap = false;
//                }else{
//                    map.modifySpriteSheet("/textures/erangel.png", 64);
//                    changeMap();
//                    _frame.get_infopanel().changeBackground(basicColor);
//                    screen.isBasicMap = true;
//                }
//                isClickChangeMap = false;
//            }
//        }
//
//        Rectangle changeMapButton = new Rectangle(Game.WIDTH + 270, Game.HEIGHT + 40, 30, 30);
//        Rectangle changeMapButton_1 = new Rectangle(Game.WIDTH + 70, Game.HEIGHT + 40, 30, 30);
//
//        if (changeMapButton.contains(e.getX(), e.getY()) && isSetting) {
//            isClickChangeMap = true;
//            if (screen.isBasicMap) {
//                map.modifySpriteSheet("/textures/miramar.png", 64);
//                screen.isBasicMap = false;
//            } else {
//                map.modifySpriteSheet("/textures/erangel.png", 64);
//                screen.isBasicMap = true;
//            }
//        }
//
//        if (changeMapButton_1.contains(e.getX(), e.getY()) && isSetting) {
//            isClickChangeMap = true;
//            if (screen.isBasicMap) {
//                map.modifySpriteSheet("/textures/miramar.png", 64);
//                screen.isBasicMap = false;
//            } else {
//                map.modifySpriteSheet("/textures/erangel.png", 64);
//                screen.isBasicMap = true;
//            }
//        }
//
//        Rectangle codeButton = new Rectangle(Game.WIDTH - 60, Game.HEIGHT + 140, 120, 50);
//        if (codeButton.contains(e.getX(), e.getY()) && isSetting) {
//            codePane.setVisible(true);
//        }
//
//        Rectangle okButton = new Rectangle(Game.WIDTH + 90, Game.HEIGHT + 240, 100, 50);
//        if (okButton.contains(e.getX(), e.getY()) && (isSetting)) {
//            if (_menu) {
//                isSetting = false;
//                getBoard().setShow(4);
//            } else {
//                isSetting = false;
//                getBoard().gameResume();
//            }
//            if (screen.isBasicMap) {
//                _frame.get_infopanel().changeBackground(basicColor);
//                changeMap();
//            } else {
//                changeMap();
//                _frame.get_infopanel().changeBackground(desertColor);
//            }
//        }
//
//        Rectangle confirmNewGame = new Rectangle(Game.WIDTH + 150, Game.HEIGHT + 100, 100, 40);
//        if (confirmNewGame.contains(e.getX(), e.getY()) && isResetGame) {
//            getBoard().newGame();
//            isResetGame = false;
//        }
//
//        Rectangle exitNewGame = new Rectangle(Game.WIDTH - 10, Game.HEIGHT + 100, 100, 40);
//        if(exitNewGame.contains(e.getX(), e.getY()) && isResetGame){
//            getBoard().gameResume();
//            isResetGame = false;
//        }
//
//        Rectangle replayButton = new Rectangle(Game.WIDTH + 50, Game.HEIGHT + 170, 150, 50);
//        if (replayButton.contains(e.getX(), e.getY()) && isEndgame) {
//            _board.newGame();
//            isEndgame = false;
//        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Rectangle playButton = new Rectangle(Game.WIDTH + 50, Game.HEIGHT + 130, 150, 50);
//        Rectangle optionButton = new Rectangle(Game.WIDTH + 50, Game.HEIGHT + 210, 150, 50);
        Rectangle aboutButton = new Rectangle(Game.WIDTH + 50, Game.HEIGHT + 280, 150, 50);

        if (menu && !isAboutPane) {
            if (playButton.contains(e.getX(), e.getY())
                    || aboutButton.contains(e.getX(), e.getY())) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            } else {
                setCursor(Cursor.getDefaultCursor());
            }
        }

        Rectangle exitAbout = new Rectangle(Game.WIDTH + 370, Game.HEIGHT - 100, 60, 60);
        if (isAboutPane) {
            if (exitAbout.contains(e.getX(), e.getY())) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            } else {
                setCursor(Cursor.getDefaultCursor());
            }
        }


//        Rectangle exitSettingButton = new Rectangle(Game.WIDTH + 300, Game.HEIGHT - 60, 50, 50);
//        Rectangle changeMapButton = new Rectangle(Game.WIDTH + 270, Game.HEIGHT + 40, 30, 30);
//        Rectangle changeMapButton_1 = new Rectangle(Game.WIDTH + 70, Game.HEIGHT + 40, 30, 30);
//        Rectangle okButton = new Rectangle(Game.WIDTH + 90, Game.HEIGHT + 240, 100, 50);
//        Rectangle codeButton = new Rectangle(Game.WIDTH - 60, Game.HEIGHT + 140, 120, 50);
//
//        if (isSetting) {
//            if (exitSettingButton.contains(e.getX(), e.getY())
//                    || changeMapButton.contains(e.getX(), e.getY())
//                    || changeMapButton_1.contains(e.getX(), e.getY())
//                    || codeButton.contains(e.getX(), e.getY())
//                    || okButton.contains(e.getX(), e.getY())) {
//                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//            } else {
//                setCursor(Cursor.getDefaultCursor());
//            }
//            if (_menu) {
//                if (exitSettingButton.contains(e.getX(), e.getY())
//                        || changeMapButton.contains(e.getX(), e.getY())
//                        || changeMapButton_1.contains(e.getX(), e.getY())
//                        || codeButton.contains(e.getX(), e.getY())
//                        || okButton.contains(e.getX(), e.getY())) {
//                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//                } else {
//                    setCursor(Cursor.getDefaultCursor());
//                }
//            }
//        }
//        Rectangle replayButton = new Rectangle(Game.WIDTH + 50, Game.HEIGHT + 170, 150, 50);
//        if (isEndgame) {
//            if (replayButton.contains(e.getX(), e.getY())) {
//                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//            } else {
//                setCursor(Cursor.getDefaultCursor());
//            }
//        }
//        Rectangle confirmNewGame = new Rectangle(Game.WIDTH + 150, Game.HEIGHT + 100, 100, 40);
//        Rectangle exitNewGame = new Rectangle(Game.WIDTH - 10, Game.HEIGHT + 100, 100, 40);
//        if (isResetGame) {
//            if (confirmNewGame.contains(e.getX(), e.getY())
//                    || exitNewGame.contains(e.getX(), e.getY())) {
//                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//            } else {
//                setCursor(Cursor.getDefaultCursor());
//            }
//        }

        if (!menu) {
            setCursor(Cursor.getDefaultCursor());
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
}