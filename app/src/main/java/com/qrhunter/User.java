package com.qrhunter;

/**
 * Base user class.
 */
class User {
    private String username = "";
    private String password = "";

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * returns the username of the player (same as object ID)
     * @return the player's username
     */

    public String getUsername() {
        return username;
    }

    /**
     * returns the password of the player
     * @return the player's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets a users username.
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets a users password.
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
