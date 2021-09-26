package com.example.catl.hulibarry;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.catl.hulibarry.interface_ulite.IActivityExtend;
import com.example.catl.hulibarry.interface_ulite.SweetAlertDiaLogCall;
import com.example.catl.hulibarry.my_ui.RadiusDialog;
import com.example.catl.hulibarry.ulite.AndroidUtils;
import com.example.catl.hulibarry.ulite.ResourceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by HSL on 2020/12/23.
 */

public abstract class BaseAppFragment extends Fragment {
    public String TAG = this.getClass().getSimpleName();
    private Fragment mParent;
    private List<Fragment> mChildFragments = new ArrayList<>();
    private boolean mAddToBackStack = false;
    public Unbinder unbinder;

    /**
     * 资源layout
     */
    protected abstract int getLayoutId();

    /**
     * Fragment 执行create时的调用
     */
    protected abstract void onFragmentCreate();

    /**
     * Activity和Fragment之间的传值
     */
    protected abstract void setupArguments(Bundle args);

    /**
     * Fragment 执行FragmentViewCreated时的调用
     */
    protected abstract void initData(View view, Bundle savedInstanceState);

    /**
     * 对于自定义View，如果view是静态的，那么没有必要在onActivityCreated 方法去调用，
     * 大多数的自定义的view，初始化时都需要一个context，而activity是context的子类，
     * 所以在onCreateView方法的时候非静态的view初始化调用可能出现异常，
     * 所以对于非静态的view，最好在onActivityCreated方法调用
     */
    protected abstract void onActivityCreated();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onFragmentCreate();
        if (getArguments() != null) {
            setupArguments(getArguments());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onActivityCreated();
    }



    public boolean isAddToBackStack() {
        return mAddToBackStack;
    }

    public static void add(FragmentManager manager, int containerViewId, Fragment fragment, boolean addToBackStack) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.add(containerViewId, fragment);
        if (addToBackStack)
            ft.addToBackStack(null);
        ft.commit();
    }

    public static void replace(FragmentManager manager, int containerViewId, Fragment fragment, boolean addToBackStack) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(containerViewId, fragment);
        if (addToBackStack)
            ft.addToBackStack(null);
        ft.commit();
    }

    public static void add(FragmentActivity activity, int containerViewId, Fragment fragment, boolean addToBackStack) {
        if (fragment instanceof BaseAppFragment) {
            ((BaseAppFragment) fragment).mParent = null;
            ((BaseAppFragment) fragment).mAddToBackStack = addToBackStack;
        }
        add(activity.getSupportFragmentManager(), containerViewId, fragment, addToBackStack);
    }

    public static void add(FragmentActivity activity, Fragment fragment, boolean addToBackStack) {
        int containerViewId = ResourceUtil.getId(activity, "fragment_container");
        add(activity, containerViewId, fragment, addToBackStack);
    }

    public static void add(Fragment fragment, int containerViewId, Fragment child, boolean addToBackStack) {
        if (child instanceof BaseAppFragment) {
            ((BaseAppFragment) child).mParent = fragment;
            ((BaseAppFragment) child).mAddToBackStack = addToBackStack;
        }
        add(fragment.getChildFragmentManager(), containerViewId, child, addToBackStack);
    }

    public static void add(Fragment fragment, Fragment child, boolean addToBackStack) {
        int containerViewId = ResourceUtil.getId(fragment.getActivity(), "fragment_container");
        add(fragment, containerViewId, child, addToBackStack);
    }

    public static void replace(FragmentActivity activity, int containerViewId, Fragment fragment, boolean addToBackStack) {
        if (fragment instanceof BaseAppFragment) {
            ((BaseAppFragment) fragment).mParent = null;
            ((BaseAppFragment) fragment).mAddToBackStack = addToBackStack;
        }
        replace(activity.getSupportFragmentManager(), containerViewId, fragment, addToBackStack);
    }

    public static void replace(FragmentActivity activity, Fragment fragment, boolean addToBackStack) {
        int containerViewId = ResourceUtil.getId(activity, "fragment_container");
        replace(activity, containerViewId, fragment, addToBackStack);
    }

    public static void replace(Fragment fragment, int containerViewId, Fragment child, boolean addToBackStack) {
        if (child instanceof BaseAppFragment) {
            ((BaseAppFragment) child).mParent = fragment;
            ((BaseAppFragment) child).mAddToBackStack = addToBackStack;
        }
        replace(fragment.getChildFragmentManager(), containerViewId, child, addToBackStack);
    }

    public static void replace(Fragment fragment, Fragment child, boolean addToBackStack) {
        int containerViewId = ResourceUtil.getId(fragment.getActivity(), "fragment_container");
        replace(fragment, containerViewId, child, addToBackStack);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (mParent != null && mParent instanceof BaseAppFragment) {
            ((BaseAppFragment) mParent).mChildFragments.add(0, this);
        } else if (getActivity() instanceof BaseAppActivity) {
            ((BaseAppActivity) getActivity()).mFragments.add(0, this);
        }
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mChildFragments != null && !mChildFragments.isEmpty()) {
            for (Fragment fragment : mChildFragments) {
                if (fragment == null || !fragment.isResumed())
                    continue;
                if (fragment instanceof BaseAppFragment) {
                    if (((BaseAppFragment) fragment).onKeyDown(keyCode, event)) {
                        return true;
                    }
                    if (((BaseAppFragment) fragment).isAddToBackStack() && keyCode == KeyEvent.KEYCODE_BACK
                            && event.getRepeatCount() == 0) {
                        return onBackPressed();
                    }
                }
            }
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            return onBackPressed();
        }
        return false;
    }

    public boolean onBackPressed() {
        return popBackStackImmediate();
    }

    public boolean popBackStackImmediate() {
        if (getChildFragmentManager() != null)
            return getChildFragmentManager().popBackStackImmediate();
        return false;
    }

    public void showAlertDiaLogView(String title) {
        showAlertDiaLogView(title, "确定");
    }

    public void showAlertDiaLogView(String title, boolean cancelable) {
        showAlertDiaLogView(title, "确定", cancelable, null);
    }

    public void showAlertDiaLogView(String title, String confirm) {
        showAlertDiaLogView(title, null, confirm, -1, null, -1, false, null);
    }

    public void showAlertDiaLogView(String title, String confirm, SweetAlertDiaLogCall call) {
        showAlertDiaLogView(title, null, confirm, -1, null, -1, false, call);
    }

    public void showAlertDiaLogView(String title, String confirm, boolean cancelable, SweetAlertDiaLogCall call) {
        showAlertDiaLogView(title, null, confirm, -1, null, -1, cancelable, call);
    }

    public void showAlertDiaLogView(String title, String message, String confirm, SweetAlertDiaLogCall call) {
        showAlertDiaLogView(title, message, confirm, -1, null, -1, false, call);
    }

    public void showAlertDiaLogView(String title, String message, String confirm, String cancel, SweetAlertDiaLogCall call) {
        showAlertDiaLogView(title, message, confirm, -1, cancel, -1, false, call);
    }

    public void showAlertDiaLogView(String title, String message, String confirm, int confirmColor, String cancel, int cancelColor, SweetAlertDiaLogCall call) {
        showAlertDiaLogView(title, message, confirm, confirmColor, cancel, cancelColor, false, call);
    }

    /**
     * @param title        标题
     * @param message      内容
     * @param confirm      确认按钮名称
     * @param confirmColor 确认按钮字体颜色
     * @param cancel       取消按钮名称
     * @param cancelColor  取消按钮字体颜色
     * @param cancelable   是否可以点击其他消失
     */
    public void showAlertDiaLogView(String title, String message,
                                    String confirm, int confirmColor,
                                    String cancel, int cancelColor,
                                    boolean cancelable, final SweetAlertDiaLogCall call) {
        final RadiusDialog mdialog = new RadiusDialog(getActivity());
        mdialog.setTitleText(title);
        mdialog.setMessageText(message);
        mdialog.setButtonText(confirm, confirmColor);
        mdialog.setButtonSText(cancel, cancelColor);
        mdialog.setCancelable(cancelable);

        //退出
        mdialog.setOnExitListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果对话框处于显示状态
                if (mdialog.isShowing()) {
                    mdialog.dismiss();//关闭对话框
                    if (call != null)
                        call.onCancel();
                }
            }
        });
        //取消
        mdialog.setOnCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mdialog != null && mdialog.isShowing()) {
                    mdialog.dismiss();
                    if (call != null)
                        call.onConfirm();
                }
            }
        });
        mdialog.show();
    }



    public static void showToast(Activity context, String toast) {
        AndroidUtils.showToast(context, toast);
    }

    public void showLoadingIndicatorDialog() {
        if (getActivity() instanceof IActivityExtend)
            ((IActivityExtend) getActivity()).showLoadingIndicatorDialog();
    }

    public void showLoadingIndicatorDialog(String message) {
        if (getActivity() instanceof IActivityExtend)
            ((IActivityExtend) getActivity()).showLoadingIndicatorDialog(message, 30000);
    }

    public void showLoadingIndicatorDialog(int time) {
        if (getActivity() instanceof IActivityExtend)
            ((IActivityExtend) getActivity()).showLoadingIndicatorDialog("加载中...", time);
    }

    public void showLoadingIndicatorDialog(String message, int time) {
        if (getActivity() instanceof IActivityExtend)
            ((IActivityExtend) getActivity()).showLoadingIndicatorDialog(message, time);
    }

    public void hideLoadingIndicatorDialog() {
        if (getActivity() instanceof IActivityExtend)
            ((IActivityExtend) getActivity()).hideLoadingIndicatorDialog();
    }
}
