package com.example.catl.hulibarry;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;


import com.example.catl.hulibarry.interface_ulite.IActivityExtend;
import com.example.catl.hulibarry.my_ui.DialogThridUtils;
import com.example.catl.hulibarry.my_ui.WeiboDialogUtils;
import com.example.catl.hulibarry.ulite.AndroidUtils;
import com.example.catl.hulibarry.ulite.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 123 on 2018/1/4.
 */

public abstract class BaseAppActivity extends AppCompatActivity implements IActivityExtend {
    public String TAG = this.getClass().getSimpleName();
    final List<Fragment> mFragments = new ArrayList<Fragment>();
    private static int diaLogType = 2;
    private WeiboDialogUtils mWeiboDialog;
    private DialogThridUtils mDialog;

    protected abstract void initData();

    protected abstract void init();

    protected abstract void launchMeForResult(Activity activity);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setContentView(R.layout.default_fragment_layout);
        initData();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    /**
     * 添加fragment
     *
     * @param containerViewId
     * @param fragment
     */
    protected void addFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        if (!fragment.isAdded()) {
            fragmentTransaction.add(containerViewId, fragment);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * 重置fragment
     *
     * @param containerViewId
     * @param fragment
     */
    protected void replaceFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        if (!fragment.isAdded()) {
            fragmentTransaction.replace(containerViewId, fragment);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * 显示fragment
     *
     * @param containerViewId
     * @param fragment
     */
    protected void showFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        if (!fragment.isAdded()) {
            fragmentTransaction.add(containerViewId, fragment);
        }
        fragmentTransaction.show(fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (popBackStackImmediate())
            return;
        super.onBackPressed();
    }

    public boolean popBackStackImmediate() {
        if (getSupportFragmentManager() != null)
            return getSupportFragmentManager().popBackStackImmediate();
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogUtil.i(TAG, "onKeyDown: " + keyCode + "; " + event.getRepeatCount());
        if (onFragmentsKeyDown(keyCode, event, getSupportFragmentManager()))
            return true;
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected boolean onFragmentsKeyDown(int keyCode, KeyEvent event, FragmentManager manager) {
        if (mFragments != null && !mFragments.isEmpty()) {
            for (Fragment fragment : mFragments) {
                if (fragment == null || !fragment.isResumed())
                    continue;
                if (fragment instanceof BaseAppFragment) {
                    if (((BaseAppFragment) fragment).onKeyDown(keyCode, event)) {
                        return true;
                    }
                    if (((BaseAppFragment) fragment).isAddToBackStack() && keyCode == KeyEvent.KEYCODE_BACK
                            && event.getRepeatCount() == 0) {
                        break;
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }
        super.onDestroy();
    }


    /**
     * 判断字符串是否为null或者空字符串
     */
    public static boolean isStrEmpty(String str) {
        if (str != null && !str.isEmpty() && !str.equals("") && !str.equals("null")) return true;
        else return false;
    }

    /**
     * 判断集合是否为null或者长度为0
     */
    public static boolean isListNull(List<?> list) {
        if (list != null && list.size() > 0) return true;
        else return false;
    }

    /**
     * 判断两个字符串是否相等
     */
    public static boolean isStrCompare(String str1, String str2) {
        if (isStrEmpty(str1) && isStrEmpty(str2) && str1.equals(str2)) return true;
        else return false;
    }

    /**
     * 判断字符串是否为纯数字
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("-?[0-9]+");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 判断是否有对象为null
     */
    public static boolean isObjectNull(Object... objects) {
        for (Object object : objects) {
            if (object == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否有对象为null
     */
    public static boolean isStringNull(String... strings) {
        for (String string : strings) {
            if (!isStrEmpty(string)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isObjectNullAndListNull(List<?> list, Object... objects) {
        for (Object object : objects) {
            if (object == null) {
                return false;
            }
        }
        if (list == null || list.size() == 0) return false;
        else return true;
    }


    /**
     * 判断是否包含特定字符串
     */
    public static boolean isContains(String message, String... str) {
        if (isStrEmpty(message) && isStringNull(str)) {
            for (String string : str) {
                if (!message.contains(string)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void showLoadingIndicatorDialog() {
        showLoadingIndicatorDialog("加载中...", 30000);
    }

    @Override
    public void showLoadingIndicatorDialog(String message) {
        showLoadingIndicatorDialog(message, 30000);
    }

    @Override
    public void showLoadingIndicatorDialog(int time) {
        showLoadingIndicatorDialog("加载中...", time);
    }

    @Override
    public void showLoadingIndicatorDialog(final String message, final int time) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (diaLogType) {
                    case 0:
                        mWeiboDialog.createLoadingDialog(message);
                        break;
                    case 1:
                        mDialog.showWaitDialog(message, false, false);
                        break;
                    case 2:
                        mDialog.showWaitDialog(message, true, false);
                        break;
                    default:
                        mWeiboDialog.createLoadingDialog(message);
                        break;
                }
                if (time > 0) {
                    updateHideLoadingIndicatorDialogRunnable(time);
                }
            }
        });
    }

    private void updateHideLoadingIndicatorDialogRunnable(long showingTime) {
        AndroidUtils.MainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LogUtil.i(TAG, "hideLoadingIndicatorDialogRunnable executed");
                showToast(getParent(), "请求超时，请确认网络环境!!!");
                hideLoadingIndicatorDialog();
            }
        }, showingTime);
    }

    public static void showToast(Activity context, String toast) {
        AndroidUtils.showToast(context, toast);
    }

    @Override
    public void hideLoadingIndicatorDialog() {
        switch (diaLogType) {
            case 0:
                mWeiboDialog.closeDialog();
                break;
            case 1:
            case 2:
                mDialog.closeDialog();
                break;
            default:
                mWeiboDialog.closeDialog();
                break;
        }
    }

    @Override
    public void setDiaLogType(int diaLogType) {
        this.diaLogType = diaLogType;
        switch (diaLogType) {
            case 0:
                mWeiboDialog = new WeiboDialogUtils(this);
                break;
            case 1:
            case 2:
                mDialog = new DialogThridUtils(this);
                break;
            default:
                mWeiboDialog = new WeiboDialogUtils(this);
                break;
        }
    }
}
