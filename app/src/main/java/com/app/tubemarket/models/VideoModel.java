package com.app.tubemarket.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class VideoModel implements Serializable {
    private String kind;
    private String etag;
    private String id;
    private List<Item> items;

    public String getKind() {
        return kind;
    }

    public String getEtag() {
        return etag;
    }

    public List<Item> getItems() {
        return items;
    }

    public String getId() {
        return id;
    }

    public static class Default implements Serializable {
        private String url;
        private int width;
        private int height;

        public String getUrl() {
            return url;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }

    public static class Thumbnails implements Serializable {
        @SerializedName("default")
        private Default def;
        private Default medium;
        private Default high;
        private Default standard;
        private Default maxres;

        public Default getDef() {
            return def;
        }

        public Default getMedium() {
            return medium;
        }

        public Default getHigh() {
            return high;
        }

        public Default getStandard() {
            return standard;
        }

        public Default getMaxres() {
            return maxres;
        }
    }

    public static class Localized implements Serializable {
        private String title;
        private String description;

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }
    }

    public static class Snippet implements Serializable {
        private String publishedAt;
        private String channelId;
        private String title;
        private String description;
        private Thumbnails thumbnails;
        private String channelTitle;
        private String categoryId;
        private String liveBroadcastContent;
        private Localized localized;

        public String getPublishedAt() {
            return publishedAt;
        }

        public String getChannelId() {
            return channelId;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public Thumbnails getThumbnails() {
            return thumbnails;
        }

        public String getChannelTitle() {
            return channelTitle;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public String getLiveBroadcastContent() {
            return liveBroadcastContent;
        }

        public Localized getLocalized() {
            return localized;
        }
    }


    public static class ContentDetails implements Serializable {
        private String duration;
        private String dimension;
        private String definition;
        private String caption;
        private boolean licensedContent;
        private String projection;

        public String getDuration() {
            return duration;
        }

        public String getDimension() {
            return dimension;
        }

        public String getDefinition() {
            return definition;
        }

        public String getCaption() {
            return caption;
        }

        public boolean isLicensedContent() {
            return licensedContent;
        }

        public String getProjection() {
            return projection;
        }
    }


    public static class Item implements Serializable {
        private String kind;
        private String etag;
        private String id;
        private Snippet snippet;
        private ContentDetails contentDetails;

        public String getKind() {
            return kind;
        }

        public String getEtag() {
            return etag;
        }

        public String getId() {
            return id;
        }

        public Snippet getSnippet() {
            return snippet;
        }

        public ContentDetails getContentDetails() {
            return contentDetails;
        }
    }


}
