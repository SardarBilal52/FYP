package com.example.bloodapp;

public class Messages
{
    private String from, message, type;

    public Messages()
    {

    }

    public Messages(String from, String message, String type) {
        this.from = from;
        this.message = message;
        this.type = type;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
