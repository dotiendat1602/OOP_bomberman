package uet.oop.bomberman.entities.Characters.AI;

import uet.oop.bomberman.entities.Characters.AI.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public abstract class AIEnemy extends AI {
    protected boolean speed;
    protected boolean allowSpeedUp = false;

    public AIEnemy(boolean speed) {
        this.speed = speed;
        // Khởi tạo các ô AI có thể đi qua
        char[] canGoThrought = {' ', 'x', 'p', '8', 'b', 'f', 's'};
        for (char c : canGoThrought) canGo.replace(c, true);
    }

    protected int bestDirection(int yy, int xx) {
        // Tìm vị trí mục tiêu (người chơi hoặc mục tiêu đặc biệt)
        Pair target = findTarget();
        if (target == null) return 0; // Trả về hướng mặc định nếu không tìm thấy mục tiêu

        // Tính khoảng cách từ mục tiêu đến mọi điểm trên bản đồ
        int[][] distance = calculateDistances(target);

        // Chọn hướng đi dựa trên trạng thái nguy hiểm hoặc an toàn
        return inDanger[xx][yy] ? handleDanger(xx, yy, distance) : handleSafe(xx, yy, distance);
    }

    // Tìm vị trí mục tiêu
    private Pair findTarget() {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (map[i][j] == 'p' || map[i][j] == '8') {
                    return new Pair(i, j);
                }
            }
        }
        return null;
    }

    // Tính khoảng cách từ mục tiêu đến mọi ô bằng BFS
    private int[][] calculateDistances(Pair start) {
        int[][] distance = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                distance[i][j] = -1;
            }
        }

        Queue<Pair> queue = new LinkedList<>();
        distance[start.getX()][start.getY()] = 0;
        queue.add(start);

        while (!queue.isEmpty()) {
            Pair u = queue.remove();
            for (int i = 0; i < 4; i++) {
                int x = u.getX() + hX[i];
                int y = u.getY() + hY[i];
                if (!validate(x, y) || distance[x][y] != -1 || !canGo.get(map[x][y])) continue;
                distance[x][y] = distance[u.getX()][u.getY()] + 1;
                queue.add(new Pair(x, y));
            }
        }
        return distance;
    }

    // Xử lý khi AI ở trong vùng nguy hiểm
    private int handleDanger(int xx, int yy, int[][] distance) {
        int curDistance = dangerDistance[xx][yy];
        if (curDistance == -1) return 0;

        int bestDirection = -1;
        int minDangerDistance = curDistance;
        int minDistanceToTarget = m * n;

        for (int i = 0; i < 4; i++) {
            int x = xx + hX[i];
            int y = yy + hY[i];
            if (!validate(x, y) || dangerDistance[x][y] == -1) continue;
            if (dangerDistance[x][y] < minDangerDistance) {
                minDangerDistance = dangerDistance[x][y];
                bestDirection = i;
                minDistanceToTarget = distance[x][y];
            } else if (dangerDistance[x][y] == minDangerDistance && distance[x][y] < minDistanceToTarget) {
                bestDirection = i;
                minDistanceToTarget = distance[x][y];
            }
        }

        allowSpeedUp = true;
        return bestDirection == -1 ? random.nextInt(4) : bestDirection;
    }

    // Xử lý khi AI ở vị trí an toàn
    private int handleSafe(int xx, int yy, int[][] distance) {
        int curDistance = distance[xx][yy];
        int bestDirection = -1;
        int[] die = new int[4]; // 0: an toàn, 1: không hợp lệ, 2: nguy hiểm

        for (int i = 0; i < 4; i++) {
            int x = xx + hX[i];
            int y = yy + hY[i];
            if (!validate(x, y)) {
                die[i] = 1;
                continue;
            }
            if (inDanger[x][y]) {
                die[i] = 2;
                continue;
            }
            if (distance[x][y] == -1) continue;
            if (distance[x][y] < curDistance) {
                curDistance = distance[x][y];
                bestDirection = i;
            }
        }

        allowSpeedUp = curDistance < 4;
        if (bestDirection == -1) {
            for (int i = 0; i < 4; i++) {
                if (die[i] == 0) return i;
            }
            for (int i = 0; i < 4; i++) {
                if (die[i] == 1) return i;
            }
            return 0;
        }
        return bestDirection;
    }
}