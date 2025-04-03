package io.github.RangoUnchained.Model.Firebase;

public class UserInfo {
    public final String uid;
    public final String email;
    public final String username;

    public UserInfo(String uid, String email, String username) {
        this.uid = uid;
        this.email = email;
        this.username = username;
    }

    public String getDisplayName() {
        if (username != null && !username.isEmpty()) return username;
        if (email != null && !email.isEmpty()) return email;
        return "Unknown user";
    }
}

