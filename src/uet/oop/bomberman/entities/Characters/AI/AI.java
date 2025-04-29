package uet.oop.bomberman.entities.Characters.AI;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;

import java.util.*;

public abstract class AI {
    // Hằng số di chuyển theo 4 hướng (phải, xuống, lên, trái)
    protected static final int[] DX = {1, 0, 0, -1};
    protected static final int[] DY = {0, 1, -1, 0};

    // Các tập hợp ô đặc biệt
    protected static final Set<Character> BLOCKING_TILES = new HashSet<>(Arrays.asList(
            '*', 'p', 'x', '2', '4', '5', '6', '7', '8'
    ));
    protected static final Set<Character> ENEMY_TILES = new HashSet<>(Arrays.asList(
            '2', '4', '5', '6'
    ));
    protected static final Set<Character> BOMB_TILES = new HashSet<>(Arrays.asList(
            '7', '8'
    ));

    // Các trường thành viên
    protected final Random random = new Random();
    protected final Board board;
    protected final HashMap<Character, Boolean> canGo;
    protected final int height, width;
    protected char[][] map;
    protected boolean[][] inDanger;
    protected int[][] dangerDistance, distanceFromEnemy;

    // Constructor
    public AI(Board board) {
        this.board = board;
        this.height = Game.levelHeight;
        this.width = Game.levelWidth;
        this.canGo = initializeCanGoMap();
    }

    // Khởi tạo bản đồ các ô có thể đi được
    private HashMap<Character, Boolean> initializeCanGoMap() {
        HashMap<Character, Boolean> map = new HashMap<>();
        for (char c : new char[]{'#', ' ', '*', 'x', 'p', '1', '2', '3', '4', '5', '6', '7', '8', 'b', 'f', 's'}) {
            map.put(c, false);
        }
        return map;
    }

    // Kiểm tra vị trí hợp lệ
    protected boolean isValidPosition(int x, int y) {
        return x >= 0 && x < height && y >= 0 && y < width;
    }

    // Tính toán vùng nguy hiểm từ bom
    private void calculateDangerZones() {
        inDanger = new boolean[height][width];
        Queue<Pair> queue = new LinkedList<>();
        // Khởi tạo vị trí bom
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (BOMB_TILES.contains(map[i][j])) {
                    queue.add(new Pair(i, j));
                    inDanger[i][j] = true;
                }
            }
        }

        // Lan truyền vùng nguy hiểm
        while (!queue.isEmpty()) {
            Pair current = queue.poll();
            int x = current.getX(), y = current.getY();

            for (int dir = 0; dir < 4; dir++) {
                for (int dist = 1; dist <= Game.getBombRadius(); dist++) {
                    int nx = x + DX[dir] * dist;
                    int ny = y + DY[dir] * dist;

                    if (!isValidPosition(nx, ny) || map[nx][ny] == '#') {
                        break;
                    }
                    inDanger[nx][ny] = true;
                    if (BLOCKING_TILES.contains(map[nx][ny])) {
                        break;
                    }
                }
            }
        }
    }

    // Phương thức trừu tượng để tính khoảng cách nguy hiểm
    abstract public void calcDangerDistance();

    // Tính khoảng cách từ kẻ địch và bom
    private void calculateEnemyDistances() {
        distanceFromEnemy = new int[height][width];
        Queue<Pair> queue = new LinkedList<>();

        // Khởi tạo khoảng cách
        for (int i = 0; i < height; i++) {
            Arrays.fill(distanceFromEnemy[i], -1);
        }

        // Xử lý vị trí kẻ địch
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (ENEMY_TILES.contains(map[i][j])) {
                    queue.add(new Pair(i, j));
                    distanceFromEnemy[i][j] = 0;
                }
            }
        }

        // BFS cho khoảng cách từ kẻ địch
        processDistances(queue, new HashSet<>(Arrays.asList('2', '4', '5', '6', '7', '#', '*')));

        queue = new LinkedList<>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (map[i][j] == '7') {
                    queue.add(new Pair(i, j));
                    distanceFromEnemy[i][j] = 0;
                }
            }
        }

        // BFS cho khoảng cách từ bom
        processDistances(queue, new HashSet<>(Arrays.asList('2', '4', '5', '6', '7', '#')));
    }

    // Xử lý BFS chung cho khoảng cách
    private void processDistances(Queue<Pair> queue, Set<Character> blockedTiles) {
        while (!queue.isEmpty()) {
            Pair current = queue.poll();
            int x = current.getX(), y = current.getY();
            int currentDist = distanceFromEnemy[x][y];

            for (int i = 0; i < 4; i++) {
                int nx = x + DX[i], ny = y + DY[i];

                if (!isValidPosition(nx, ny) || blockedTiles.contains(map[nx][ny])) {
                    continue;
                }
                if (distanceFromEnemy[nx][ny] != -1 && distanceFromEnemy[nx][ny] <= currentDist + 1) {
                    continue;
                }

                distanceFromEnemy[nx][ny] = currentDist + 1;
                queue.add(new Pair(nx, ny));
            }
        }
    }

    // Khởi tạo các khoảng cách
    public void initDistance() {
        calculateDangerZones();
        calcDangerDistance();
        calculateEnemyDistances();
    }

    // Cập nhật bản đồ hiện tại
    public void calcCurrentMap() throws NullPointerException {
        if (board == null) {
            throw new NullPointerException("Board is null");
        }
        char[][] temp = board.reviveMap();
        if (temp == null) {
            map = null;
            return;
        }
        map = new char[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                map[i][j] = temp[j][i];
            }
        }
    }

    // Phương thức trừu tượng để tính hướng di chuyển
    public abstract int calculateDirection();
}
