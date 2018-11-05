package com.example.jorgegonzalezcabrera.outgoing.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class customViewPager extends ViewPager {

    private boolean enabledSwipe;

    public customViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabledSwipe = false;
    }

    public customViewPager(@NonNull Context context) {
        super(context);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.enabledSwipe && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.enabledSwipe && super.onInterceptTouchEvent(event);
    }

    public void setSwipeEnabled(boolean enable) {
        this.enabledSwipe = enable;
    }
}