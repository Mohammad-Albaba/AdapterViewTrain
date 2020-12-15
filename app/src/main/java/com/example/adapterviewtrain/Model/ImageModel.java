package com.example.adapterviewtrain.Model;

import java.util.UUID;

public class ImageModel {
    private String text;
    private int imageId;
    private String id;

    public ImageModel() {
        this.id = UUID.randomUUID().toString();
    }


    public ImageModel(String text, int imageId) {
        this.text = text;
        this.imageId = imageId;
        this.id = UUID.randomUUID().toString();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
