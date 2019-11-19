package org.tttnhung.hien;

public class ImageInfo {

    public String image;

    public String name;

    public ImageInfo() {

    }

    public ImageInfo(String image, String name) {
        this.image = image;
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}