package com.qrhunter;

import android.graphics.Bitmap;
import android.util.Pair;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * A QR code scanned alongside an optional photo and location.
 */
public class Collectable {
    private String id = "";
    private Long score = 0L;
    private Bitmap photo;
    private Pair<Double, Double> location = new Pair<>(0.0, 0.0);      // lat, long
    private ArrayList<String> comments = new ArrayList<>();


    /**
     * Returns the score for the collectable.
     * @return the score.
     */
    public Long getScore() {return score;}


    /**
     * Return the ID for the collectable.
     * @return The ID.
     */
    public String getId() {return id;}


    /**
     * Returns the photo for the collectable.
     * @return The BitMap.
     */
    public Bitmap getPhoto() {return photo;}


    /**
     * Returns the location as a Pair, lat first, long second.
     * @return The location.
     */
    public Pair<Double, Double> getLocation() {return location;}


    /**
     * Returns the list of comments.
     * @return The comments.
     */
    public ArrayList<String> getComments() {return comments;}


    /**
     * Sets the score for the collectable.
     * @param new_score The score.
     * @throws AssertionError if the score is negative.
     */
    public void setScore(Long new_score) {
        assert(new_score >= 0);
        score = new_score;
    }

    /**
     * Sets the ID for the collectable.
     * @param new_id The new id for the collectable.
     */
    public void setId(String new_id) {
        id = new_id;
    }

    /**
     * Sets the photo for the collectable.
     * @param new_photo The photo
     */
    public void setPhoto(Bitmap new_photo) {
        photo = new_photo;
    }


    /**
     * Sets the location of the collectable.
     * @param new_location The new location.
     */
    public void setLocation(Pair<Double, Double> new_location) {location = new_location;}


    /**
     * Sets a new arraylist for the comments. Don't use this to update information, as it will
     * only be seen locally, and won't syncronize with the firestore.
     * @param new_comments The comments.
     */
    public void setComments(ArrayList<String> new_comments) {comments = new_comments;}


    /**
     * Sets an ImageView to the image in the Collectable.
     * @param view the ImageView to add to.
     */
    public void viewPhoto(ImageView view) {
        view.setImageBitmap(photo);
    }
}

