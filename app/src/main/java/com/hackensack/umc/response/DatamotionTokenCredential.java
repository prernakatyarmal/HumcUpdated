package com.hackensack.umc.response;

/**
 * Created by prerana_katyarmal on 2/3/2016.
 */
public class DatamotionTokenCredential {
    private String username;
    private String password;

    public DatamotionTokenCredential(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
