package uet.oop.bomberman.entities.Characters.AI;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.entities.Characters.Enemies.Enemy;
import java.util.LinkedList;
import java.util.Queue;
import java.util.HashMap;

public class AIMedium extends AIEnemy {
    private final Enemy enemy;
    private final boolean canChangeSpeed;

    public AIMedium(Board board, Enemy enemy, boolean canChangeSpeed) {
        super(canChangeSpeed);
        this.board = board;
        this.enemy = enemy;
        this.canChangeSpeed = canChangeSpeed;

        enableMovableTiles('2', '4', '5');
    }

    private void enableMovableTiles(char... tiles) {
        for (char tile : tiles) {
            canGo.replace(tile, true);
        }
    }

    @Override
    public void calcDangerDistance() {
        dangerDistance = new int[m][n];
        Queue<Pair> queue = new LinkedList<>();

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (isSafeTile(i, j)) {
                    queue.add(new Pair(i, j));
                    dangerDistance[i][j] = 0;
                } else {
                    dangerDistance[i][j] = -1;
                }
            }
        }

        while (!queue.isEmpty()) {
            Pair top = queue.remove();
            int x = top.getX();
            int y = top.getY();

            for (int i = 0; i < 4; i++) {
                int u = x + hX[i];
                int v = y + hY[i];

                if (!validate(u, v) || isBlockedTile(map[u][v]) || dangerDistance[u][v] >= 0) continue;

                dangerDistance[u][v] = dangerDistance[x][y] + 1;
                queue.add(new Pair(u, v));
            }
        }
    }

    private boolean isSafeTile(int i, int j) {
        char tile = map[i][j];
        return tile != '#' && tile != '*' && !isEnemy(tile) && !inDanger[i][j];
    }

    private boolean isBlockedTile(char tile) {
        return tile == '#' || tile == '*' || tile == '1' || tile == '3' || tile == '6' || tile == '7' || tile == '8';
    }

    private boolean isEnemy(char tile) {
        return tile >= '1' && tile <= '6';
    }

    @Override
    public int calculateDirection() {
        calcCurrentMap();
        if (map == null) return 1;

        initDistance();
        if (canChangeSpeed) {
            enemy.setSpeed(allowSpeedUp ? 1.0 : 0.5);
        }

        return bestDirection(enemy.getXTile(), enemy.getYTile());
    }
}
