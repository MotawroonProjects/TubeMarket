package com.tubemarket.models;

import java.io.Serializable;

public class AdsViewModel implements Serializable {
    private String id;
    private String user_id;
    private String advertisement_id;
    private String created_at;
    private String updated_at;
    private AdAds advertisement_fk;

    public String getId() {
        return id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getAdvertisement_id() {
        return advertisement_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public AdAds getAdvertisement_fk() {
        return advertisement_fk;
    }

    public static class AdAds implements Serializable {
        private String id;
        private String user_id;
        private String type;
        private String subscription_limit;
        private String views_number;
        private String likes_limit;
        private String subscriptions_count;
        private String views_count;
        private String likes_count;
        private String total_cost;
        private String link;
        private String status;
        private String watch_time;
        private String estimated_arrive_time_per_day;
        private String profit_coins;
        private String created_at;
        private String updated_at;
        private String channel_name;
        private String channel_image;
        private String timer_limit;

        private OperationModel user_operations_fk;

        public String getId() {
            return id;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getType() {
            return type;
        }

        public String getSubscription_limit() {
            return subscription_limit;
        }

        public String getViews_number() {
            return views_number;
        }

        public String getLikes_limit() {
            return likes_limit;
        }

        public String getSubscriptions_count() {
            return subscriptions_count;
        }

        public String getViews_count() {
            return views_count;
        }

        public String getLikes_count() {
            return likes_count;
        }

        public String getTotal_cost() {
            return total_cost;
        }

        public String getLink() {
            return link;
        }

        public String getStatus() {
            return status;
        }

        public String getWatch_time() {
            return watch_time;
        }

        public String getEstimated_arrive_time_per_day() {
            return estimated_arrive_time_per_day;
        }

        public String getProfit_coins() {
            return profit_coins;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public String getChannel_name() {
            return channel_name;
        }

        public String getChannel_image() {
            return channel_image;
        }

        public String getTimer_limit() {
            return timer_limit;
        }

        public OperationModel getUser_operations_fk() {
            return user_operations_fk;
        }
    }
}
