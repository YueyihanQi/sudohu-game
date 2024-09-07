package org.example.sudokugame_javafx;

import javax.print.attribute.standard.Media;
import java.io.File;



public class MusicPlayer {
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;  // 跟踪播放状态

    public MusicPlayer(String filePath) {
        File musicFile = new File(filePath);
        Media medias = new Media(musicFile.toURI().toString());
        mediaPlayer = new MediaPlayer(medias);
    }

    // 切换播放和暂停状态
    public void play() {
        if (!isPlaying) {
            mediaPlayer.play();
            isPlaying = true;
        }
    }

    public void pause() {
        if (isPlaying) {
            mediaPlayer.pause();
            isPlaying = false;
        }
    }

    public void togglePlayPause() {
        if (isPlaying) {
            pause();
        } else {
            play();
        }
    }

    public void dispose() {
        mediaPlayer.dispose();
    }
}
