package com.lsapp.smarthome.event;

/**
 * Created by Administrator on 2016/8/30.
 */
public class NewFeedEvent {
    boolean isNewsFeed = false;

    public boolean isNewsFeed() {
        return isNewsFeed;
    }

    public void setNewsFeed(boolean newsFeed) {
        isNewsFeed = newsFeed;
    }

    public NewFeedEvent(boolean isNewsFeed) {
        this.isNewsFeed = isNewsFeed;
    }
}
