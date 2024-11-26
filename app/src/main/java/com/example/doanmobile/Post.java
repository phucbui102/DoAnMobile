package com.example.doanmobile;

import java.io.Serializable;

public class Post implements Serializable {
    private String content;
    private byte[] image;
    private String _id;

    public Post(String content, byte[] image, String _id) {
        this.content = content;
        this.image = image;
        this._id = _id;
    }

    public Post(String content, byte[] image) {
        this(content, image, null); // Gọi constructor chính với _id = null
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
