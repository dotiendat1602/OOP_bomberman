package uet.oop.bomberman;

import uet.oop.bomberman.entities.Characters.Character;

import uet.oop.bomberman.Level.FileLevelLoader;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.Input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Board {
    protected FileLevelLoader fileLevelLoader;
    protected Game game;
    protected Keyboard input;
    protected Screen screen;

    public Entity[] entities;
    public List<Character> characters = new ArrayList<>();

    private int screenToShow = -1;

    private int time = Game.TIME;
    private int points = Game.POINTS;
    private int lives = Game.LIVES;

    private final char[][] map;

    public Board(Game game, Keyboard input, Screen screen) {
        this.game = game;
        this.input = input;
        this.screen = screen;
        loadLevel(1);
        map = new char[fileLevelLoader.getWidth()][fileLevelLoader.getHeight()];
    }

    public void update() {
        if (game.isPaused()) return;

        updateEntities();
        updateCharacters();
        detectEndGame();

        for (int i = 0; i < characters.size(); i++) {
            Character a = characters.get(i);
            if (a.isRemoved()) characters.remove(i);
        }
    }

    public void render(Screen screen) {
        if (game.isPaused()) return;

        int x0 = Screen.xOffset >> 4;
        int x1 = (Screen.xOffset + screen.getWidth() + Game.TILE_SIZE) / Game.TILE_SIZE;
        int y0 = Screen.yOffset >> 4;
        int y1 = (Screen.yOffset + screen.getHeight()) / Game.TILE_SIZE;

        for (int y = y0; y < y1; y++)
            for (int x = x0; x < x1; x++) entities[x + y * fileLevelLoader.getWidth()].render(screen);


        renderCharacter(screen);
    }

    public void nextLevel() {
        loadLevel(fileLevelLoader.getLevel() + 1);
    }

    public void loadLevel(int level) {
        time = Game.TIME;
        screenToShow = 2;
        game.resetScreenDelay();
        game.pause();
        characters.clear();

        Screen.setOffset(0, 0);
        Game.playSE(7);
        try {
            fileLevelLoader = new FileLevelLoader(this, level);
            entities = new Entity[fileLevelLoader.getHeight() * fileLevelLoader.getWidth()];
            fileLevelLoader.createEntities();
        } catch (NullPointerException e) {
            finishGame();
        } catch (Exception e) {
            endGame();
        }
    }

    protected void detectEndGame() {
        if (time <= 0) endGame();
    }

    public void newGame() {
        loadLevel(1);
//        resetProperties();
        lives = Game.LIVES;
    }

    public void endGame() {
        screenToShow = 1;
        game.resetScreenDelay();
        Game.playSE(9);
        game.pause();
    }

    public void finishGame() {
        screenToShow = 4;
        game.resetScreenDelay();
        Game.playSE(8);
        game.pause();
    }

    public void gamePause() {
        game.resetScreenDelay();
        if (screenToShow <= 0) screenToShow = 3;
        game.pause();
    }

    public void gameResume() {
        game.resetScreenDelay();
        screenToShow = -1;
        game.resume();
    }



    public void drawScreen(Graphics g) {
        switch (screenToShow) {
            case 1 -> screen.drawEndGame(g, points);
            case 2 -> screen.drawChangeLevel(g, fileLevelLoader.getLevel());
            case 3 -> screen.drawPaused(g);
            case 4 -> screen.drawFinishGame(g, points);
        }
    }

    public Entity getEntity(double x, double y, Character m) {
        Entity res;
        res = getEntityAt((int) x, (int) y);
        return res;
    }

    public Character getCharacterAtExcluding(int x, int y, Character a) {
        Iterator<Character> itr = characters.iterator();
        Character cur;
        while (itr.hasNext()) {
            cur = itr.next();
            if (cur == a) continue;
            if (cur.getXTile() == x && cur.getYTile() == y) return cur;
        }
        return null;
    }


    public Entity getEntityAt(double x, double y) {
        return entities[(int) x + (int) y * fileLevelLoader.getWidth()];
    }

    public void addEntity(int pos, Entity e) {
        entities[pos] = e;
    }

    public void addCharacter(Character e) {
        characters.add(e);
    }


    protected void renderCharacter(Screen screen) {
        for (Character character : characters) character.render(screen);
    }


    protected void updateEntities() {
        if (game.isPaused()) return;
        for (Entity entity : entities) entity.update();
    }

    protected void updateCharacters() {
        if (game.isPaused()) return;
        Iterator<Character> itr = characters.iterator();
        while (itr.hasNext() && !game.isPaused()) itr.next().update();
    }


    public int subtractTime() {
        if (game.isPaused()) return this.time;
        else return this.time--;
    }

    public Keyboard getInput() {
        return input;
    }

    public FileLevelLoader getLevel() {
        return fileLevelLoader;
    }

    public Game getGame() {
        return game;
    }

    public int getShow() {
        return screenToShow;
    }

    public void setShow(int i) {
        screenToShow = i;
    }

    public int getTime() {
        return time;
    }

    public int getPoints() {
        return points;
    }

    public int getLives() {
        return lives;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public void addLives(int lives) {
        this.lives += lives;
    }

    public int getWidth() {
        return fileLevelLoader.getWidth();
    }

}