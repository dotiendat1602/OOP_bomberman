package uet.oop.bomberman.entities.Characters.AI;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.entities.Characters.Enemies.Enemy;
import java.util.LinkedList;
import java.util.Queue;
import java.util.HashMap;

public class AIMedium extends AIEnemy {
    private static final char[] SAFE_TILES = {'0'}; // Tiles that are safe to start BFS from
    private static final char[] BLOCKING_TILES = {'#', '*', '1', '2', '3', '4', '5', '6'}; // Tiles that block movement in BFS initialization
    private static final char[] OBSTACLE_TILES = {'#', '*', '1', '3', '6', '7', '8'}; // Tiles that block movement in BFS pathfinding
    private static final char[] ALLOWED_SPEED_TILES = {'2', '4', '5'}; // Tiles that allow speed changes

    private final Enemy enemy;
    private final boolean canChangeSpeed;
    private final Board board;
    protected int[][] dangerDistance;
    protected char[][] map;
    protected boolean[][] inDanger;
    protected int m, n;
    protected HashMap<Character, Boolean> canGo;
    protected int[] hX = {0, 0, 1, -1}; // Directions: right, left, down, up
    protected int[] hY = {1, -1, 0, 0};
    protected boolean allowSpeedUp;

    public AIMedium(Board board, Enemy enemy, boolean canChangeSpeed) {
        super(canChangeSpeed);
        this.board = board;
        this.enemy = enemy;
        this.canChangeSpeed = canChangeSpeed;
        initializeAllowedTiles();
    }

    private void initializeAllowedTiles() {
        for (char tile : ALLOWED_SPEED_TILES) {
            canGo.replace(tile, true);
        }
    }

    @Override
    public void calcDangerDistance() {
        dangerDistance = new int[m][n];
        Queue<Pair> queue = new LinkedList<>(); // Using Java's default LinkedList as Queue

        // Initialize distances: 0 for safe positions, -1 for unsafe or blocked
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (isSafeStartingPosition(i, j)) {
                    queue.add(new Pair(i, j));
                    dangerDistance[i][j] = 0;
                } else {
                    dangerDistance[i][j] = -1;
                }
            }
        }

        // BFS to calculate danger distances
        while (!queue.isEmpty()) {
            Pair current = queue.remove();
            int x = current.getX();
            int y = current.getY();

            for (int i = 0; i < 4; i++) {
                int newX = x + hX[i];
                int newY = y + hY[i];

                if (!validate(newX, newY) || isBlockingTile(newX, newY) || dangerDistance[newX][newY] >= 0) {
                    continue;
                }

                dangerDistance[newX][newY] = dangerDistance[x][y] + 1;
                queue.add(new Pair(newX, newY));
            }
        }
    }

    @Override
    public int calculateDirection() {
        calcCurrentMap();
        if (map == null) {
            return 1; // Default direction if map is null
        }

        initDistance();
        adjustEnemySpeed();
        return bestDirection(enemy.getXTile(), enemy.getYTile());
    }

    // Helper method to check if a position is safe to start BFS
    private boolean isSafeStartingPosition(int x, int y) {
        if (inDanger[x][y]) {
            return false;
        }
        for (char safeTile : SAFE_TILES) {
            if (map[x][y] == safeTile) {
                return true;
            }
        }
        for (char blockingTile : BLOCKING_TILES) {
            if (map[x][y] == blockingTile) {
                return false;
            }
        }
        return false;
    }

    // Helper method to check if a tile blocks movement in BFS
    private boolean isBlockingTile(int x, int y) {
        for (char obstacle : OBSTACLE_TILES) {
            if (map[x][y] == obstacle) {
                return true;
            }
        }
        return false;
    }

    // Helper method to adjust enemy speed based on conditions
    private void adjustEnemySpeed() {
        if (canChangeSpeed) {
            enemy.setSpeed(allowSpeedUp ? 1.0 : 0.5);
        }
    }

    // Placeholder for validate method (assumed to exist in the superclass or elsewhere)
    protected boolean validate(int x, int y) {
        return x >= 0 && x < m && y >= 0 && y < n;
    }

    protected int bestDirection(int x, int y) {
        // Implementation assumed in superclass
        return 1;
    }
}