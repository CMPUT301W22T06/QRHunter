package com.qrhunter;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * A QR code scanned alongside an optional photo and location.
 */
public class Collectable {
    private String id = "";
    private String name = "";
    private Long score = 0L;
    private Bitmap photo;
    private Geolocation location = new Geolocation(0.0, 0.0); // lat, long
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
    public Geolocation getLocation() {return location;}


    /**
     * Returns the list of comments.
     * @return The comments.
     */
    public ArrayList<String> getComments() {return comments;}


    /**
     * Returns the name of the Code.
     * @return The name.
     */
    public String getName() {return name;}


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
    public void setLocation(Geolocation new_location) {location = new_location;}


    /**
     * Sets the name of the collectable.
     * @param new_name The name of the collectable.
     * @throws AssertionError If the new name exceeds 24 characters.
     */
    public void setName(String new_name) {
        assert(new_name.length() <= 24);
        name = new_name;
    }


    /**
     * Sets a new arraylist for the comments. Don't use this to update information, as it will
     * only be seen locally, and won't synchronize with the Firestore.
     * @param new_comments The comments.
     */
    public void setComments(ArrayList<String> new_comments) {comments = new_comments;}


    /**
     * Sets an ImageView to the image in the Collectable.
     * @param view the ImageView to add to.
     */
    public void viewPhoto(ImageView view) {view.setImageBitmap(photo);}
}

