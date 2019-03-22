package com.yu.common.mvp;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

public abstract class BasePresenter<T extends Viewer> implements Presenter {

  protected T viewer;
  private Handler mHandler = new Handler(Looper.getMainLooper());

  public BasePresenter(T viewer) {
    setViewer(viewer);
  }

  public @Nullable T getViewer() {
    return viewer;
  }

  public void setViewer(@Nullable T viewer) {
    this.viewer = viewer;
  }

  public void postDelay(Runnable runnable, int delay) {
    mHandler.postDelayed(runnable, delay);
  }

  public void runOnUiThread(Runnable runnable) {
    mHandler.postDelayed(runnable, 0);
  }

  public Activity getActivity() {
    if (getViewer() != null) {
      return getViewer().getActivity();
    }
    return null;
  }

  @Override public int priority() {
    return 0;
  }

  @Override public void start() {

  }

  @Override public void stop() {

  }

  @Override public void destroy() {
    mHandler.removeCallbacksAndMessages(null);
    viewer = null;
  }

  @Override public void newIntent(Intent intent) {

  }

  @Override public void activityResult(int requestCode, int resultCode, Intent data) {

  }
}