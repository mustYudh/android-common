package com.yu.common.framework;

import com.yu.common.mvp.BasePresenter;
import com.yu.common.mvp.Viewer;


public abstract class BaseViewPresenter<T extends Viewer> extends BasePresenter<T> {

    public BaseViewPresenter(T viewer) {
        super(viewer);
    }

}
