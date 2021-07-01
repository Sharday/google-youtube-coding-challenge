package com.google;

import java.util.*;
import java.util.stream.Collectors;
import java.util.Scanner;

public class VideoPlayer {

  private final VideoLibrary videoLibrary;

  private Video playing;
  private boolean paused = false;
  private List<VideoPlaylist> playlists;

  public VideoPlayer() {

    this.videoLibrary = new VideoLibrary();
    this.playlists = new ArrayList<>();
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  public void showAllVideos() {
    System.out.println("Here's a list of all available videos:");
    ArrayList<Video> videos = (ArrayList<Video>) videoLibrary.getVideos();
    Collections.sort(videos, new VideoComparator());
    for (Video video : videos) {
      String title = video.getTitle();
      String id = video.getVideoId();
      List<String> tags = video.getTags();
      String tagList = tags.toString().replaceAll(",","");

      if (video.getFlag() != null) {
        System.out.println(title + " ("+ id + ") " + tagList+" - FLAGGED (reason: "+video.getFlag()+")");
      } else {
        System.out.println(title + " ("+ id + ") " + tagList);
      }

    }
  }

  public void playVideo(String videoId) {
    Video playNext = videoLibrary.getVideo(videoId);
    if (playNext == null) {
      System.out.println("Cannot play video: Video does not exist");
      return;
    } else if (playNext.getFlag() != null) {
      System.out.println("Cannot play video: Video is currently flagged (reason: "+playNext.getFlag()+")");
      return;
    } else {
      if (playing != null) {
        System.out.println("Stopping video: " + playing.getTitle());
      }
      playing = playNext;
      System.out.println("Playing video: "+playing.getTitle());
      paused = false;
    }

  }

  public void stopVideo() {

    if (playing == null) {
      System.out.println("Cannot stop video: No video is currently playing");
    } else {

      System.out.println("Stopping video: "+playing.getTitle());
      playing = null;

    }
  }

  public void playRandomVideo() {
    List<Video> videos = videoLibrary.getVideos();

    Iterator<Video> it = videos.iterator();

    //remove flagged videos
    while (it.hasNext()) {
      Video video = it.next();
      if (video.getFlag() != null) {
        it.remove();
      }
    }

    if (videos.isEmpty()) {
      System.out.println("No videos available");
      return;
    }
    List<String> ids = videos.stream()
            .map(Video::getVideoId)
            .collect(Collectors.toList());
    Random rand = new Random();
    String randomId = ids.get(rand.nextInt(ids.size()));
    this.playVideo(randomId);

  }

  public void pauseVideo() {
    if (playing == null) {
      System.out.println("Cannot pause video: No video is currently playing");
    } else if (paused) {
      System.out.println("Video already paused: "+playing.getTitle());
    } else {
      System.out.println("Pausing video: "+playing.getTitle());
      paused = true;
    }
  }

  public void continueVideo() {
    if (playing == null) {
      System.out.println("Cannot continue video: No video is currently playing");
    } else if (!paused) {
      System.out.println("Cannot continue video: Video is not paused");
    } else {
      System.out.println("Continuing video: "+playing.getTitle());
      paused = false;
    }
  }

  public void showPlaying() {
    if (playing == null) {
      System.out.println("No video is currently playing");
    } else {
      String title = playing.getTitle();
      String id = playing.getVideoId();
      List<String> tags = playing.getTags();
      String tagList = tags.toString().replaceAll(",","");
      if (paused) {
        System.out.println("Currently playing: "+title + " ("+ id + ") " + tagList + " - PAUSED");
      } else {
        System.out.println("Currently playing: "+title + " ("+ id + ") " + tagList);
      }

    }
  }

  public void createPlaylist(String playlistName) {


    for (VideoPlaylist elem : playlists) {
      if (elem.getPlaylistName().toLowerCase(Locale.ROOT).equals(playlistName.toLowerCase())) {
        System.out.println("Cannot create playlist: A playlist with the same name already exists");
        return;
      }
    }
      VideoPlaylist playlist = new VideoPlaylist(playlistName);
      playlists.add(playlist);
      System.out.println("Successfully created new playlist: "+playlist.getPlaylistName());

  }

  public int getIndexOfPlaylist (String playlistName){
    for (int i=0; i<this.playlists.size(); i++) {
      if (this.playlists.get(i).getPlaylistName().toLowerCase(Locale.ROOT).equals(playlistName.toLowerCase(Locale.ROOT))) {
        return i;
      }
    }
    return -1; //not found

  }

  public void addVideoToPlaylist(String playlistName, String videoId) {

    int pos = getIndexOfPlaylist(playlistName);
    if (pos == -1) {
      System.out.println("Cannot add video to "+playlistName+": Playlist does not exist");
      return;
    }
    Video video = this.videoLibrary.getVideo(videoId);
    if (video==null) {
      System.out.println("Cannot add video to "+playlistName+": Video does not exist");
      return;
    } else if (video.getFlag() != null) {
      System.out.println("Cannot add video to "+playlistName+": Video is currently flagged (reason: "+video.getFlag()+")");
      return;
    }

    VideoPlaylist playlist = this.playlists.get(pos);

    if (playlist.contains(video)) {
      System.out.println("Cannot add video to "+playlistName+": Video already added");
      return;
    }
    playlist.addVideo(video);
    System.out.println("Added video to "+playlistName + ": "+video.getTitle());
  }

  public void showAllPlaylists() {

    if (this.playlists.isEmpty()) {
      System.out.println("No playlists exist yet");
      return;
    } else {
      Collections.sort(this.playlists, new PlaylistComparator());
      System.out.println("Showing all playlists:");
      for (VideoPlaylist playlist : this.playlists) {
        System.out.println(playlist.getPlaylistName());
      }
    }
  }

  public void showPlaylist(String playlistName) {
    int pos = getIndexOfPlaylist(playlistName);
    if (pos == -1) {
      System.out.println("Cannot show playlist "+playlistName+": Playlist does not exist");
      return;
    }
    VideoPlaylist playlist = this.playlists.get(pos);
    System.out.println("Showing playlist: "+playlistName);
    if (playlist.isEmpty()) {
      System.out.println("No videos here yet");
      return;
    } else {
      playlist.showVideos();
    }


  }

  public void removeFromPlaylist(String playlistName, String videoId) {
    int pos = getIndexOfPlaylist(playlistName);
    if (pos == -1) {
      System.out.println("Cannot remove video from "+playlistName+": Playlist does not exist");
      return;
    }
    Video video = this.videoLibrary.getVideo(videoId);
    if (video==null) {
      System.out.println("Cannot remove video from "+playlistName+": Video does not exist");
      return;
    }

    VideoPlaylist playlist = this.playlists.get(pos);

    if (playlist.isEmpty() || !playlist.contains(video)) {
      System.out.println("Cannot remove video from "+playlistName+": Video is not in playlist");
      return;
    }
    playlist.removeVideo(video);
    System.out.println("Removed video from "+playlistName + ": "+video.getTitle());
  }

  public void clearPlaylist(String playlistName) {
    int pos = getIndexOfPlaylist(playlistName);
    if (pos == -1) {
      System.out.println("Cannot clear playlist "+playlistName+": Playlist does not exist");
      return;
    }

    VideoPlaylist playlist = this.playlists.get(pos);
    playlist.clearPlaylist();
    System.out.println("Successfully removed all videos from "+playlistName);
  }

  public void deletePlaylist(String playlistName) {
    int pos = getIndexOfPlaylist(playlistName);
    if (pos == -1) {
      System.out.println("Cannot delete playlist "+playlistName+": Playlist does not exist");
      return;
    }
    this.playlists.remove(pos);
    System.out.println("Deleted playlist: "+playlistName);
  }


  public void searchVideos(String searchTerm) {
    ArrayList<Video> videoList = (ArrayList<Video>) videoLibrary.getVideos();
    Collections.sort(videoList, new VideoComparator());

    Iterator<Video> it = videoList.iterator();

    //remove flagged videos
    //remove videos not containing search term
    while (it.hasNext()) {
      Video video = it.next();
      String title = video.getTitle();
      if (!(title.toLowerCase(Locale.ROOT).contains(searchTerm.toLowerCase(Locale.ROOT))) || (video.getFlag() != null)) {
        it.remove();
      }


    }
    if (videoList.isEmpty()) {
      System.out.println("No search results for "+searchTerm);
      return;
    }

    System.out.println("Here are the results for "+searchTerm+":");
    for (int i=0; i<videoList.size() ; i++) {
      Video video = videoList.get(i);
      String title = video.getTitle();
      String id = video.getVideoId();
      List<String> tags = video.getTags();
      String tagList = tags.toString().replaceAll(",","");

      System.out.println(i+1 + ") "+title + " ("+ id + ") " + tagList);
    }

    System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
    System.out.println("If your answer is not a valid number, we will assume it's a no.");

    Scanner in = new Scanner(System.in);
    if (in.hasNextInt()) { //user entered an integer
      Integer num = in.nextInt();
      if (num >= 1 && num <= videoList.size()) { //check valid
        int index = num - 1;
        Video video = videoList.get(index);
        System.out.println("Playing video: "+video.getTitle());
      } else {
        return;
      }
    } else {
      return;
    }



  }

  public void searchVideosWithTag(String videoTag) {
    ArrayList<Video> videoList = (ArrayList<Video>) videoLibrary.getVideos();
    Collections.sort(videoList, new VideoComparator());

    Iterator<Video> it = videoList.iterator();

    //remove flagged videos
    //remove videos not containing search tag
    while (it.hasNext()) {
      Video video = it.next();
      List<String> tags = video.getTags();

      if (!tags.contains(videoTag.toLowerCase(Locale.ROOT)) || (video.getFlag() != null)) {
        it.remove();
      }


    }
    if (videoList.isEmpty()) {
      System.out.println("No search results for "+videoTag);
      return;
    }

    System.out.println("Here are the results for "+videoTag+":");
    for (int i=0; i<videoList.size() ; i++) {
      Video video = videoList.get(i);
      String title = video.getTitle();
      String id = video.getVideoId();
      List<String> tags = video.getTags();
      String tagList = tags.toString().replaceAll(",","");

      System.out.println(i+1 + ") "+title + " ("+ id + ") " + tagList);
    }

    System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
    System.out.println("If your answer is not a valid number, we will assume it's a no.");

    Scanner in = new Scanner(System.in);
    if (in.hasNextInt()) { //user entered integer
      Integer num = in.nextInt();
      if (num >= 1 && num <= videoList.size()) { //check valid
        int index = num - 1;
        Video video = videoList.get(index);
        System.out.println("Playing video: "+video.getTitle());
      } else {
        return;
      }
    } else {
      return;
    }
  }

  public void flagVideo(String videoId) {

    Video video = this.videoLibrary.getVideo(videoId);

    if (video==null) {
      System.out.println("Cannot flag video: Video does not exist");
      return;
    }
    if (video.getFlag() != null) {
      System.out.println("Cannot flag video: Video is already flagged");
    } else {
      if (video.equals(playing)) {
        stopVideo();
      }
      video.setFlag("Not supplied");
      System.out.println("Successfully flagged video: "+video.getTitle()+" (reason: "+video.getFlag()+")");
    }
  }

  public void flagVideo(String videoId, String reason) {
    Video video = this.videoLibrary.getVideo(videoId);

    if (video==null) {
      System.out.println("Cannot flag video: Video does not exist");
      return;
    }
    if (video.getFlag() != null) {
      System.out.println("Cannot flag video: Video is already flagged");
    } else {
      if (video.equals(playing)) {
        stopVideo();
      }
      video.setFlag(reason);
      System.out.println("Successfully flagged video: "+video.getTitle()+" (reason: "+video.getFlag()+")");
    }

  }

  public void allowVideo(String videoId) {
    Video video = this.videoLibrary.getVideo(videoId);

    if (video==null) {
      System.out.println("Cannot remove flag from video: Video does not exist");
      return;
    }
    if (video.getFlag() == null) {
      System.out.println("Cannot remove flag from video: Video is not flagged");
    } else {
      video.removeFlag();
      System.out.println("Successfully removed flag from video: "+video.getTitle());
    }
  }
}