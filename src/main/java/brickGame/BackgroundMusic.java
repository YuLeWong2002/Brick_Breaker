package brickGame;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

/**
 * The {@code BackgroundMusic} class represents a utility class for managing background music in a JavaFX application.
 * It utilizes the {@link javafx.scene.media.MediaPlayer} class to play, pause, resume, stop, and check the playing
 * status of background music.
 */
public class BackgroundMusic {

    /**
     * Media player for controlling background music.
     */
    private final MediaPlayer mediaPlayer;

    /**
     * Constructs a new BackgroundMusic instance.
     *
     * @param mediaPath The file path to the media resource.
     */
    public BackgroundMusic(String mediaPath) {
        // Convert the file path to a URI with the "file" scheme
        File file = new File(mediaPath);
        String uriString = file.toURI().toString();

        Media media = new Media(uriString);
        mediaPlayer = new MediaPlayer(media);
        // Set an event handler to restart the music when it reaches the end
        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
    }

    /**
     * Starts playing the background music.
     */
    public void play() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    /**
     * Pauses the background music.
     */
    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    /**
     * Resumes the background music if it was previously paused.
     */
    public void resume() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
            mediaPlayer.play();
        }
    }

    /**
     * Stops the background music.
     */
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    /**
     * Checks if the background music is currently playing.
     *
     * @return {@code true} if the background music is playing, {@code false} otherwise.
     */
    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

}
