package com.m2m.yafun.view;

public interface Updater extends OnUpdateListener {

    void registerUpdateListener(OnUpdateListener listener);

    void unRegisterUpdateListener(OnUpdateListener listener);
}
