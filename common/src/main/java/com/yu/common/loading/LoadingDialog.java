package com.yu.common.loading;

import android.app.Activity;
import android.os.Build;
import android.view.View;

public class LoadingDialog {

    private static LoadingProgress pDialog = null;

    private static int hashCode = 0;

    public static void showLoading(Activity activity, View contentView) {
        showLoading(activity, contentView, "", true);
    }

    public static void showLoading(Activity activity, View contentView, boolean isCancelable) {
        showLoading(activity, contentView, "", isCancelable);
    }

    private static boolean isActivityAttached(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return activity != null && !activity.isFinishing() && !activity.isDestroyed();
        } else {
            return activity != null && !activity.isFinishing();
        }
    }

    public static void showLoading(final Activity activity, final View contentView, final String text,
                                   final boolean isCancelable) {
        RunnablePost.post(new Runnable() {
            @Override
            public void run() {
                if (!isActivityAttached(activity)) {
                    return;
                }
                if (hashCode != activity.hashCode()) {
                    destroyDialog();
                }
                if (null == pDialog) {
                    pDialog = LoadingProgressFactory.createLoadingProgress(activity, contentView);
                    hashCode = activity.hashCode();
                }
                pDialog.setCancelable(isCancelable);
                if (pDialog != null) {
                    pDialog.showLoading(text);
                }
            }
        });
    }

    public static void dismissLoading(Activity activity) {
        if (activity != null && hashCode == activity.hashCode()) {
            destroyDialog();
        }
    }

    public static void dismissLoading() {
        destroyDialog();
    }

    private static void destroyDialog() {
        RunnablePost.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (pDialog != null) {
                        pDialog.dismissLoading();
                        pDialog = null;
                    }
                    hashCode = 0;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
