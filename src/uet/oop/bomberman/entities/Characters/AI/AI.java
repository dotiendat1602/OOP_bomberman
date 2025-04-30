package uet.oop.bomberman.entities.Characters.AI;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;

import java.util.ArrayList;
import uet.oop.bomberman.entities.Characters.AI.Pair;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public abstract class AI {
    protected Random random = new Random();
    protected Board board;
    protected HashMap<Character, Boolean> canGo;
    protected char[][] map;
    protected int[] hX = {1, 0, 0, -1};
    protected int[] hY = {0, 1, -1, 0};
    protected int m, n;
    protected boolean[][] inDanger;
    protected int[][] dangerDistance, distanceFromEnemy;

    public AI() {
        m = Game.levelHeight;
        n = Game.levelWidth;
        canGo = new HashMap<>();
        // Khởi tạo các ký tự trên bản đồ với giá trị mặc định là không đi qua được
        char[] items = {'#', ' ', '*', 'x', 'p', '1', '2', '3', '4', '5', '6', '7', '8', 'b', 'f', 's'};
        for (char c : items) canGo.put(c, false);
    }

    protected boolean validate(int u, int v) {
        return (0 <= u && u < Game.levelHeight && 0 <= v && v < Game.levelWidth);
    }

    // Tính toán các ô nguy hiểm dựa trên bom và vụ nổ
    private void calcInDanger() {
        inDanger = new boolean[m][n];
        Queue<Pair> queue = new LinkedList<>();
        // Tìm các ô chứa bom hoặc vụ nổ
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (map[i][j] == '7' || map[i][j] == '8') {
                    queue.add(new Pair(i, j));
                    inDanger[i][j] = true;
                }
            }
        }

        // Tính vùng ảnh hưởng của bom
        while (!queue.isEmpty()) {
            Pair top = queue.remove();
            int u = top.getX(), v = top.getY();
            for (int j = 0; j < 4; j++) {
                for (int i = 1; i <= Game.getBombRadius(); i++) {
                    int x = u + hX[j] * i;
                    int y = v + hY[j] * i;
                    if (!validate(x, y) || map[x][y] == '#') break;
                    inDanger[x][y] = true;
                    if (isBlockingTile(map[x][y])) break;
                }
            }
        }
    }

    // Kiểm tra ô có chặn vụ nổ không
    private boolean isBlockingTile(char tile) {
        return tile == '*' || tile == 'p' || tile == 'x' || tile == '2' || tile == '4' || tile == '5' || tile == '6' || tile == '7' || tile == '8';
    }

    // Tính khoảng cách từ các ô đến kẻ thù
    private void calcDistanceFromEnemy() {
        distanceFromEnemy = new int[m][n];
        Queue<Pair> queue = new LinkedList<>();
        // Khởi tạo khoảng cách cho các ô chứa kẻ thù
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (isEnemyTile(map[i][j])) {
                    queue.add(new Pair(i, j));
                    distanceFromEnemy[i][j] = 0;
                } else {
                    distanceFromEnemy[i][j] = -1;
                }
            }
        }

        // BFS để tính khoảng cách từ kẻ thù
        processDistanceQueue(queue, this::isEnemyBlockingTile);

        // Cập nhật khoảng cách từ bom
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (map[i][j] == '7') {
                    queue.add(new Pair(i,j));
                    distanceFromEnemy[i][j] = 0;
                }
            }
        }

        processDistanceQueue(queue, this::isBombBlockingTile);
    }

    // Kiểm tra ô có phải là kẻ thù
    private boolean isEnemyTile(char tile) {
        return tile == '2' || tile == '4' || tile == '5' || tile == '6';
    }

    // Kiểm tra ô chặn đường đi của kẻ thù
    private boolean isEnemyBlockingTile(char tile) {
        return tile == '2' || tile == '4' || tile == '5' || tile == '6' || tile == '7' || tile == '#' || tile == '*';
    }

    // Kiểm tra ô chặn đường đi của bom
    private boolean isBombBlockingTile(char tile) {
        return tile == '2' || tile == '4' || tile == '5' || tile == '6' || tile == '7' || tile == '#';
    }

    // Xử lý hàng đợi BFS cho khoảng cách
    private void processDistanceQueue(Queue<Pair> queue, java.util.function.Predicate<Character> isBlocking) {
        while (!queue.isEmpty()) {
            Pair top = queue.remove();
            int u = top.getX(), v = top.getY();
            for (int i = 0; i < 4; i++) {
                int x = u + hX[i];
                int y = v + hY[i];
                if (!validate(x, y) || isBlocking.test(map[x][y])) continue;
                if (distanceFromEnemy[x][y] != -1 && distanceFromEnemy[x][y] <= distanceFromEnemy[u][v] + 1) continue;
                distanceFromEnemy[x][y] = distanceFromEnemy[u][v] + 1;
                queue.add(new Pair(x, y));
            }
        }
    }

    abstract public void calcDangerDistance();

    public void initDistance() {
        calcInDanger();
        calcDangerDistance();
        calcDistanceFromEnemy();
    }

    public void calcCurrentMap() throws NullPointerException {
        if (this.board == null) throw new NullPointerException();
        char[][] temp = this.board.reviveMap();
        if (temp == null) {
            map = null;
            return;
        }
        map = new char[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                map[i][j] = temp[j][i];
            }
        }
    }

    public abstract int calculateDirection();
}