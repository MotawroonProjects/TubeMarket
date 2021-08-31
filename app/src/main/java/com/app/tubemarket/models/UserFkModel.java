package com.app.tubemarket.models;

import java.io.Serializable;

public class UserFkModel implements Serializable {
    private String id;
    private String code;
    private String user_type;
    private String name;
    private String email;
    private String image;
    private String channel_id;
    private String channel_video_name;
    private String channel_video_image;
    private String channel_video_description;
    private String channel_code;
    private String channel_video_link;
    private String channel_description;
    private String channel_name;
    private String channel_image;
    private String google_id;
    private String interested;
    private String is_vip;
    private String coins;
    private String approved_status;
    private String approved_by;
    private String is_blocked;
    private String is_login;
    private String logout_time;
    private String email_verified_at;
    private String confirmation_code;
    private String forget_password_code;
    private String software_type;
    private String deleted_at;

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getUser_type() {
        return user_type;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public String getChannel_video_name() {
        return channel_video_name;
    }

    public String getChannel_video_image() {
        return channel_video_image;
    }

    public String getChannel_video_description() {
        return channel_video_description;
    }

    public String getChannel_code() {
        return channel_code;
    }

    public String getChannel_video_link() {
        return channel_video_link;
    }

    public String getChannel_description() {
        return channel_description;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public String getChannel_image() {
        return channel_image;
    }

    public String getGoogle_id() {
        return google_id;
    }

    public String getInterested() {
        return interested;
    }

    public String getIs_vip() {
        return is_vip;
    }

    public String getCoins() {
        return coins;
    }

    public String getApproved_status() {
        return approved_status;
    }

    public String getApproved_by() {
        return approved_by;
    }

    public String getIs_blocked() {
        return is_blocked;
    }

    public String getIs_login() {
        return is_login;
    }

    public String getLogout_time() {
        return logout_time;
    }

    public String getEmail_verified_at() {
        return email_verified_at;
    }

    public String getConfirmation_code() {
        return confirmation_code;
    }

    public String getForget_password_code() {
        return forget_password_code;
    }

    public String getSoftware_type() {
        return software_type;
    }

    public String getDeleted_at() {
        return deleted_at;
    }
}
