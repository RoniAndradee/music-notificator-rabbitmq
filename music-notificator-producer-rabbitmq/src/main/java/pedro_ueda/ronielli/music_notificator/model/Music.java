package pedro_ueda.ronielli.music_notificator.model;

import java.util.UUID;

public class Music {

    private UUID id;
    private String name;
    private String artist;
    private String album;

    public Music() {
    }

    public Music(UUID id, String name, String artist, String album) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.album = album;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }
}
