package com.qrhunter;

class User {
    private String username;
    private String password;

    public User() {

    }

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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
