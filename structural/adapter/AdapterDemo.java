package structural.adapter;

public class AdapterDemo {
    public static void main(String[] args) {
        System.out.println("=== Adapter Pattern Demo ===\n");
        
        AudioPlayer audioPlayer = new AudioPlayer();
        
        audioPlayer.play("mp3", "song.mp3");
        audioPlayer.play("mp4", "video.mp4");
        audioPlayer.play("vlc", "movie.vlc");
        audioPlayer.play("avi", "documentary.avi");
    }
}
