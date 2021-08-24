package com.app.tubemarket.models;

import java.io.Serializable;

public class UserModel extends StatusResponse implements Serializable {
    private String id;
    private String google_id;
    private String email;
    private String name;
    private String image;
    private String coins="0";
    private String code;
    private String user_type;
    private String is_vip;
    private String token;
    private ChannelModel channelModel;
    private VideoModel videoModel;
    private InterestsModel interestsModel;

    public UserModel(String id, String google_id, String email, String name, String image, String coins, String code, String user_type, String is_vip, String token, ChannelModel channelModel, VideoModel videoModel, InterestsModel interestsModel) {
        this.id = id;
        this.google_id = google_id;
        this.email = email;
        this.name = name;
        this.image = image;
        this.coins = coins;
        this.code = code;
        this.user_type = user_type;
        this.is_vip = is_vip;
        this.token = token;
        this.channelModel = channelModel;
        this.videoModel = videoModel;
        this.interestsModel = interestsModel;
    }

    public String getGoogle_id() {
        return google_id;
    }

    public void setGoogle_id(String google_id) {
        this.google_id = google_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getIs_vip() {
        return is_vip;
    }

    public void setIs_vip(String is_vip) {
        this.is_vip = is_vip;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setVideoModel(VideoModel videoModel) {
        this.videoModel = videoModel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }

    public VideoModel getVideoModel() {
        return videoModel;
    }

    public ChannelModel getChannelModel() {
        return channelModel;
    }

    public void setChannelModel(ChannelModel channelModel) {
        this.channelModel = channelModel;
    }

    public InterestsModel getInterestsModel() {
        return interestsModel;
    }

    public void setInterestsModel(InterestsModel interestsModel) {
        this.interestsModel = interestsModel;
    }

    public static class ChannelModel implements Serializable{
        private String id;
        private String title;
        private String descriptions;
        private String url;

        public ChannelModel(String id, String title, String descriptions, String url) {
            this.id = id;
            this.title = title;
            this.descriptions = descriptions;
            this.url = url;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescriptions() {
            return descriptions;
        }

        public void setDescriptions(String descriptions) {
            this.descriptions = descriptions;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class VideoModel implements Serializable{
        private String id;
        private String title;
        private String descriptions;
        private String url;

        public VideoModel(String id, String title, String descriptions, String url) {
            this.id = id;
            this.title = title;
            this.descriptions = descriptions;
            this.url = url;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescriptions() {
            return descriptions;
        }

        public void setDescriptions(String descriptions) {
            this.descriptions = descriptions;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

}

