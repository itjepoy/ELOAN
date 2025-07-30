package com.cremcash.eloan;

import java.io.Serializable;

public class ImageModel implements Serializable {

    String image;

    public ImageModel(){

    }

    public ImageModel(String image){
        this.image = image;
    }

    public  String getImage(){
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
