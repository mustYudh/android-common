package com.yu.common.framework;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.yu.common.R;
import com.yu.common.launche.LauncherHelper;
import com.yu.common.utils.NetWorkUtil;

/**
 * @author yudneghao
 * @date 2019/3/5
 */
public abstract class BasicFragment extends AbstractExtendsFragment {

    private boolean isVisibleToUser = true;
    private boolean isViewPrepared = false;
    private View mNetworkErrorView;
    protected View mContentView;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
                             Bundle savedInstanceState) {
        View rootView;
        if ((rootView = getView()) == null) {
            rootView = onReplaceRootView(inflater, viewGroup);
        }
        mContentView = View.inflate(getActivity(), getContentViewId(),
                (ViewGroup) rootView.findViewById(R.id.content_container));
        return rootView;
    }

    protected View onReplaceRootView(LayoutInflater inflater, ViewGroup viewGroup) {
        View rootView = inflater.inflate(R.layout.activity_base_content, viewGroup, false);
        mNetworkErrorView = View.inflate(getActivity(), R.layout.inclue_networ_error_view,
                (ViewGroup) rootView.findViewById(R.id.network_error));
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setView(savedInstanceState);
        isViewPrepared = true;

        if (!inFragmentPageAdapter() || isVisibleToUser) {
            pageLoadData();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        this.isVisibleToUser = isVisibleToUser;
        super.setUserVisibleHint(isVisibleToUser);

        if (inFragmentPageAdapter() && isVisibleToUser) {
            pageLoadData();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            onPageInTop();
        }
    }


    protected void onPageInTop() {
    }


    protected boolean needCheckNetWork() {
        return false;
    }

    private void pageLoadData() {
        if (isViewPrepared) {
            isViewPrepared = false;
            if (needCheckNetWork()) {
                checkNetworkLoadData();
            } else {
                loadData();
                onPageInTop();
            }
        }
    }

    protected abstract void handleNetWorkError(View view);

    @Override
    public Context getContext() {
        Context context = super.getContext();
        return context == null ? getActivity() : context;
    }


    protected void checkNetworkLoadData() {
        if (NetWorkUtil.isNetworkAvailable(getActivity())) {
            loadData();
            onPageInTop();
        } else {
            if (mNetworkErrorView != null && mContentView != null) {
                mContentView.setVisibility(View.GONE);
                mNetworkErrorView.setVisibility(View.VISIBLE);
                handleNetWorkError(mNetworkErrorView);
            }
        }
    }

    protected void reload() {
        if (NetWorkUtil.isNetworkAvailable(getActivity())) {
            mContentView.setVisibility(View.VISIBLE);
            mNetworkErrorView.setVisibility(View.GONE);
            loadData();
        }
    }


    /**
     * 是否存在于ViewPager中
     * 返回true则根据UserVisibleHint的值懒加载
     */
    protected boolean inFragmentPageAdapter() {
        return false;
    }

    /***
     * 获取当前界面的XML layout Id
     */
    protected abstract int getContentViewId();

    /**
     * 数据，界面处理
     */
    protected abstract void setView(@Nullable Bundle savedInstanceState);

    protected abstract void loadData();


    protected LauncherHelper getLaunchHelper() {
        return LauncherHelper.from(getActivity());
    }

}
