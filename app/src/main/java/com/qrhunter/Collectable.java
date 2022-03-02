package com.qrhunter;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class Collectable {
    private String id;
    private int score;
    private Bitmap photo;

    public int getScore() {return score;}
    public String getId() {return id;}
    public Bitmap getPhoto() {return photo;}

    public void setScore(int new_score) {
        assert(new_score >= 0);
        score = new_score;
    }
    public void setId(String new_id) {
        id = new_id;
    }
    public void setPhoto(Bitmap new_photo) {
        photo = new_photo;
    }
    public void viewPhoto(ImageView view) {
        view.setImageBitmap(photo);
    }

}

