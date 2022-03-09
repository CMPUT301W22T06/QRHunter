package com.qrhunter;

import android.graphics.Bitmap;
import android.util.Pair;
import android.widget.ImageView;

public class Collectable {
    private String id;
    private Long score;
    private Bitmap photo;
    private Pair<Double, Double> location = new Pair<>(0.0, 0.0);      // lat, long

    public Long getScore() {return score;}
    public String getId() {return id;}
    public Bitmap getPhoto() {return photo;}
    public Pair<Double, Double> getLocation() {return location;}

    public void setScore(Long new_score) {
        assert(new_score >= 0);
        score = new_score;
    }
    public void setId(String new_id) {
        id = new_id;
    }
    public void setPhoto(Bitmap new_photo) {
        photo = new_photo;
    }
    public void setLocation(Pair<Double, Double> new_location) {location = new_location;}

    public void viewPhoto(ImageView view) {
        view.setImageBitmap(photo);
    }
}

