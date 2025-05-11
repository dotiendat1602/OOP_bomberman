package uet.oop.bomberman.Sound;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Sound {
    Clip clip;
    URL[] soundURL = new URL[30];

    public Sound() {
        soundURL[0] = getClass().getResource("/Sound/music.wav");
        soundURL[1] = getClass().getResource("/Sound/walk.wav");
        soundURL[2] = getClass().getResource("/Sound/drop.wav");
        soundURL[3] = getClass().getResource("/Sound/destroy.wav");
        soundURL[4] = getClass().getResource("/Sound/die.wav");
        soundURL[5] = getClass().getResource("/Sound/item.wav");
        soundURL[6] = getClass().getResource("/Sound/kill.wav");
        soundURL[7] = getClass().getResource("/Sound/level.wav");
        soundURL[8] = getClass().getResource("/Sound/win.wav");
        soundURL[9] = getClass().getResource("/Sound/lose.wav");
    }

    public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.flush();
            clip.setFramePosition(0);
        }
    }

    public static void main(String[] args) {
        try {
            Sound sound = new Sound();
            sound.setFile(0);
            sound.loop();

            // Phát nhạc trong 5 giây
            Thread.sleep(5000);

            // Dừng nhạc
            sound.stop();

            // Đợi thêm 2 giây để kiểm tra
            Thread.sleep(2000);

            // Phát lại nhạc nếu muốn
            sound.play();

            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}