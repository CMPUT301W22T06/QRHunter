package com.qrhunter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Pair;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import javax.annotation.Nullable;

/**
 * A database of all Collectables stored by all users. This also facilitates talking to the
 * Firestore.
 */
public class CollectableDatabase {

    // The local copy of the database. Updated when new barcodes are scanned.
    private HashMap<String, Collectable> collectables = new HashMap<>();

    // Firebase related objects
    FirebaseFirestore database;
    FirebaseStorage storage;

    // We need to specific a size, and no image should exceed a megabyte.
    final long ONE_MEGABYTE = 1024 * 1024;


    /**
     * @throws RuntimeException if the database cannot be accessed (Network issues)
     *
     * This function updates the local database. It only downloads those that aren't present, and
     * downloads the entirety of the collectable (Including the photo).
     *
     * When this function is first run, it will be when the Database is constructed, and will down
     * load everything into the local database. Fortunately it's async, so the user can still
     * do stuff while all the data downloads.
     *
     * After that, it's used to watch changes to the Firestore. When a change is made (even by the
     * user who made the change), this function rescans the local library and adds ONLY the
     * collectables that don't exist within the local database.
     *
     * In the future, I'd like to store this on the phone, but I'll need to make Collectable
     * serializable.
     */
    private void updateLocal() {

        // Gets all scanned objects, constructs the collectables.
        database.collection("Scanned").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    Collectable current = new Collectable();

                    // This allows us to not have to re-download the whole thing after each change.
                    if (!collectables.containsKey(document.getId())) {
                        current.setId(document.getId());

                        // The object will NEVER be null, but it prevents the IDE from complaining.
                        Object location_object = document.get("Location");
                        if (location_object != null) {
                            HashMap<String, Object> location = (HashMap<String, Object>) location_object;
                            current.setLocation(new Pair<>(
                                    (Double) location.get("first"),
                                    (Double) location.get("second"))
                            );
                        }

                        // Same issue here.
                        Object score_object = document.get("Score");
                        if (score_object != null) {
                            current.setScore((Long) score_object);
                        }

                        Object comments_object = document.get("Comments");
                        if (comments_object != null) {
                            current.setComments((ArrayList<String>)comments_object);
                        }

                        // Import the image.
                        StorageReference image = storage.getReference(current.getId());
                        image.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
                            current.setPhoto(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                        });

                        // Put the collectable into the local database.
                        collectables.put(document.getId(), current);
                    }
                    // If
                    else {
                        Object comments_object = document.get("Comments");
                        if (comments_object != null) {
                            ArrayList<String> comments = (ArrayList<String>)comments_object;
                            if (current.getComments().size() != comments.size())
                            current.setComments(comments);
                        }
                    }
                }
            }
            else throw new RuntimeException("Network Error:" + task.getException());
        });
    }


    /**
     * Constructs the Database, and downloads all data.
     */
    public CollectableDatabase() {

        // Get the instances.
        database = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        // Initial population
        updateLocal();

        // Hook to update when other people scan codes.
        database.collection("Scanned").addSnapshotListener((queryDocumentSnapshots, error) -> updateLocal());
    }


    /**
     * Returns the firestore database.
     * @return The database.
     */
    public FirebaseFirestore getStore() {return database;}


    /**
     * Returns the entire HashMap containing all Collectables. ID's are the keys.
     * @return The database.
     */
    public HashMap<String, Collectable> getDatabase() {return collectables;}


    /**
     * Adds the provided collectable into the local and firestore databases.
     * @param scanned The scanned collectable.
     * @param callback A callback to the HomeActivity, to resume scanning. This can be null, in
     *                 which case no callback will be called.
     */
    public void add(Collectable scanned, @Nullable HomeActivity callback) {
        // Put it into the local database.
        collectables.put(scanned.getId(), scanned);

        // Our hashmap to upload basic information.
        HashMap<String, Object> pack = new HashMap<>();

        // Photos are special, we use Storage rather than Firestore
        if (scanned.getPhoto() != null) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            // TODO We can change the quality attribute to solve US 09.01.01
            scanned.getPhoto().compress(Bitmap.CompressFormat.JPEG, 100, output);

            // Write it. Since its detached from the main base, use the ID to associate it.
            storage.getReference(scanned.getId())
                    .putBytes(output.toByteArray())
                    .addOnFailureListener(e -> {
                        if (callback != null)
                            callback.resume("Could not upload image!");
                    });
        }

        // Pack all the rest of the information into the hashmap.
        pack.put("Score", scanned.getScore());
        pack.put("Location", scanned.getLocation());
        pack.put("Comments", scanned.getComments());

        // Upload our pack into the Firestore.
        database.collection("Scanned")
                .document(scanned.getId())
                .set(pack)
                .addOnFailureListener(e -> {
                    if (callback != null)
                        callback.resume("Could not upload collectable!");
                })
                .addOnSuccessListener(e -> {
                    if (callback != null)
                        callback.resume("");
                });

    }

    /**
     * Adds comments to a collectable, updates firebase.
     * @param id The id of the collectable.
     * @param comment The comments to add.
     * @throws RuntimeException If the database cannot be accessed (Network Error)
     *
     * This function will add comments to an existing collectable within the database.
     */
    public void addComment(String id, String comment) {
        Collectable selected = collectables.get(id);
        if (selected != null) {
            selected.getComments().add(comment);
            database.collection("Scanned")
                    .document(id)
                    .update("Comments", selected.getComments())
                    .addOnFailureListener(e -> {throw new RuntimeException("Network Error.");});
        }
    }

    /**
     * Deletes a collectable from the database.
     * @param id The id of the collectable.
     * @throws RuntimeException if there was a network error.
     *
     * This function deletes a collectable from the database. It does not throw an exception
     * if the element is not present.
     */
    public void deleteCollectable(String id) {
        if(collectables.containsKey(id)) {
            collectables.remove(id);
            database.collection("Scanned")
                    .document(id)
                    .delete()
                    .addOnFailureListener(e -> {
                        throw new RuntimeException("Network Error.");
                    });
        }
    }

    /**
     * Checks if a particular QR code exists within the database.
     * @param id The id of the code.
     * @return Whether it is present within the database
     */
    public boolean exists(String id) {
        return collectables.containsKey(id);
    }

    /**
     * Returns the collectable associated with the provided ID.
     * @param id The id of the collectable.
     * @return The id
     * @throws RuntimeException If the collectable does not exist.
     */
    public Collectable get(String id) {
        if (exists(id)) {
            return collectables.get(id);
        }
        else throw new RuntimeException("ID not in database");
    }

    /**
     * Returns all collectables.
     * @return Hashmap of all collectables in the database
     */
    public HashMap<String,Collectable> getCollectables() {
        return collectables;
    }
}
