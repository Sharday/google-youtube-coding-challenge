package com.google;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/** A class used to represent a video. */
class Video {

  private final String title;
  private final String videoId;
  private final List<String> tags;
  private String flag;

  Video(String title, String videoId, List<String> tags) {
    this.title = title;
    this.videoId = videoId;
    this.tags = Collections.unmodifiableList(tags);
  }

  /** Returns the title of the video. */
  String getTitle() {
    return title;
  }

  /** Returns the video id of the video. */
  String getVideoId() {
    return videoId;
  }

  /** Returns a readonly collection of the tags of the video. */
  List<String> getTags() {
    return tags;
  }

  public void setFlag(String reason) {
    this.flag = reason;
  }

  public void removeFlag() {
    this.flag = null;
  }

  public String getFlag() {
    return this.flag;
  }

}

//for sorting lexicographically by video title
class VideoComparator implements Comparator<Video> {
  public int compare(Video a, Video b)
  {
      return a.getTitle().compareTo(b.getTitle());
  }
}