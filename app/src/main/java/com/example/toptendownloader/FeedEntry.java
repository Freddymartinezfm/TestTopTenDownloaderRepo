package com.example.toptendownloader;


public class FeedEntry {
    private String name;
    private String artist;
    private String summary;
    private String imageUrl;
    private String releaseDate;

    public String getName() { return name; }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setName(String name) { this.name = name; }

    public String getArtist() {return artist;}

    public void setArtist(String artist) {this.artist = artist; }

    public String getSummary() {return summary; }

    public void setSummary(String summary) {this.summary = summary; }

    public String getImageUrl() {return imageUrl; }

    public void setImageUrl(String imageUrl) {this.imageUrl = imageUrl; }

    @Override
    public String toString() {
        return  "name: " + this.name + "\n" +
                "artist: " + this.artist + "\n" +
                "imageUrl: " + this.imageUrl + "\n";
    }
}
