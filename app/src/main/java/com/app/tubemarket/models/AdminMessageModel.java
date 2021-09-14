package com.app.tubemarket.models;

import java.io.Serializable;

public class AdminMessageModel implements Serializable {
    private String id;
    private String from_user_type;
    private String from_user_id;
    private String to_user_id;
    private String admin_room_id;
    private String type;
    private String message;
    private String voice;
    private String image;
    private String created_at;
    private String updated_at;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom_user_type() {
        return from_user_type;
    }

    public void setFrom_user_type(String from_user_type) {
        this.from_user_type = from_user_type;
    }

    public String getFrom_user_id() {
        return from_user_id;
    }

    public void setFrom_user_id(String from_user_id) {
        this.from_user_id = from_user_id;
    }

    public String getTo_user_id() {
        return to_user_id;
    }

    public void setTo_user_id(String to_user_id) {
        this.to_user_id = to_user_id;
    }

    public String getAdmin_room_id() {
        return admin_room_id;
    }

    public void setAdmin_room_id(String admin_room_id) {
        this.admin_room_id = admin_room_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
