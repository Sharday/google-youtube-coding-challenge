package com.google;

import java.util.*;

/** A class used to represent a Playlist */
class VideoPlaylist {
    Queue<Video> playlist;
    private final String name;

    public VideoPlaylist(String name) {
        this.playlist = new LinkedList<Video>();
        this.name = name;
    }

    public String getPlaylistName() {
        return this.name;
    }

    public void addVideo(Video video) {
        this.playlist.add(video);
    }

    public boolean contains(Video video) {
        return this.playlist.contains(video);
    }

    public boolean isEmpty() {
        return playlist.isEmpty();
    }

    public void showVideos() {
        for (Video video : this.playlist) {

            if (video.getFlag() != null) {
                System.out.println(video.getTitle() + " ("+ video.getVideoId() + ") " + VideoPlayer.createTagList(video.getTags())+" - FLAGGED (reason: "+video.getFlag()+")");
            } else {
                System.out.println(video.getTitle() + " ("+ video.getVideoId() + ") " + VideoPlayer.createTagList(video.getTags()));
            }
        }
    }

    public void removeVideo(Video video) {
        this.playlist.remove(video);
    }

    public void clearPlaylist() {
        this.playlist = new LinkedList<Video>();
    }
}

//for sorting lexicographically by playlist name
class PlaylistComparator implements Comparator<VideoPlaylist> {
    public int compare(VideoPlaylist a, VideoPlaylist b)
    {
        return a.getPlaylistName().compareTo(b.getPlaylistName());
    }
}
