package com.townsquare.ui.main.models;

import com.google.gson.annotations.SerializedName;

public class Message {

    @SerializedName("content")
    private String content;

    @SerializedName("sender")
    private String sender;
    private String when;
    private String images;
    private boolean isOwner;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    @Override
    public String toString() {
        return "content = " + getContent() + " sender = " + getSender() + " when = " + getWhen() + " images = " + getImages() + " isOwner = " + isOwner();
    }
}
