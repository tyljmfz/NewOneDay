package com.example.newoneday.music.data;

public class Song {

    // 需要显示的信息 歌手名和歌曲名
    private String songName;
    private String songSinger;
    public Integer songId;
    private int duration;
    private Integer albumPicture;
    private boolean liked;

    public Song(String songName, String songSinger, Integer songId){
        this.songId = songId;
        this.songName = songName;
        this.songSinger = songSinger;
//        this.albumPicture = albumPicture;
//        this.liked = liked;
}

    public void setSongId(){
        this.songId = songId;
    }

    public Integer getSongId(){
        return songId;
    }

    public void setSongName(){
        this.songName = songName;
    }

    public String getSongName(){
        return songName;
    }

    public void setSongSinger(){
        this.songSinger = songSinger;
    }

    public String getSongSinger(){
        return songSinger;
    }

    public void setDuration(){
        this.duration = duration;
    }

    public int getDuration(){
        return duration;
    }

    public void setAlbumPicture(){
        this.albumPicture = albumPicture;
    }

    public Integer getAlbumPicture(){
        return albumPicture;
    }

    public void setLiked(){
        this.liked = liked;
    }

    public boolean getLiked(){
        return liked;
    }


}
