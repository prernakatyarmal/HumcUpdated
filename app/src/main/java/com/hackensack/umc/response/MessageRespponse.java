package com.hackensack.umc.response;

/**
 * Created by prerana_katyarmal on 10/28/2015.
 */
public class MessageRespponse {
    String Message;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public MessageRespponse(String message) {
        Message = message;
    }
}
