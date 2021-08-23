package com.app.tubemarket.models;

import java.io.Serializable;

public class UserModel extends StatusResponse implements Serializable {
    private String id;
    private String email;
    private String name;
    private String image;
    private String coins="0";
    private ChannelModel channelModel;
    private InterestsModel interestsModel;

    public UserModel(String id, String email, String name, String image) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.image = image;
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
}

