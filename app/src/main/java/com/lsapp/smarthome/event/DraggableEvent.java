package com.lsapp.smarthome.event;

import android.view.View;

/**
 * Created by Administrator on 2016/12/3.
 */
public class DraggableEvent {
    View.OnTouchListener listener;

    public DraggableEvent(View.OnTouchListener listener) {
        this.listener = listener;
    }

    public View.OnTouchListener getListener() {
        return listener;
    }

    public void setListener(View.OnTouchListener listener) {
        this.listener = listener;
    }
}
