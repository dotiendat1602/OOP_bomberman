package uet.oop.bomberman.entities.Characters.AI;

public class AILow extends AIEnemy {
    public AILow() {
        super(false);
    }

    // Không tính khoảng cách nguy hiểm (AI đơn giản)
    @Override
    public void calcDangerDistance() {}

    // Chọn hướng ngẫu nhiên
    @Override
    public int calculateDirection() {
        return random.nextInt(4);
    }
}