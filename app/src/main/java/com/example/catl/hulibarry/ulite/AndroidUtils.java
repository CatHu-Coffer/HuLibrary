package com.example.catl.hulibarry.ulite;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.catl.hulibarry.hepler.ReflectHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by HSL on 2020/12/23.
 */

public class AndroidUtils {

    public static final String TAG = AndroidUtils.class.getSimpleName();

    public static final Handler MainHandler = new Handler(Looper.getMainLooper());
    /**
     * 主线程池
     */
    public static Executor MAIN_EXECUTOR = Executors.newFixedThreadPool(5);

    //创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待
    public static ExecutorService MainExecutor = Executors.newFixedThreadPool(5);
    //创建一个定长线程池，支持定时及周期性任务执行。延迟执行示例代码如下
    public static ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);

    public static final void MainExecutorExecute(Runnable runnable) {
        MainExecutor.execute(runnable);
    }

    public static final void MainExecutorSchedule(Runnable runnable){
        scheduledThreadPool.schedule(runnable, (long) 0.5, TimeUnit.SECONDS);
    }

    public static final void MainUIHandler(Runnable runnable) {
        new Thread(runnable).start();
    }

    /**
     * 获取屏幕的分辨率
     * */
    public static DisplayMetrics getActivityDisplayMetrics(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    /**
     * 获取屏幕的物理尺寸（大小）
     * */
    public static Point getActivityDisplaySize(Activity activity) {
        Point outSize = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(outSize);
        return outSize;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Point getActivityDisplayRealSize(Activity activity) {
        Point outSize = new Point();
        activity.getWindowManager().getDefaultDisplay().getRealSize(outSize);
        return outSize;
    }

    public static WindowManager getWindowManager(Context context) {
        return (WindowManager) (context.getSystemService(Activity.WINDOW_SERVICE));
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 提示语
     * */
    public static void showToast(final Activity activity, final String text) {
        LogUtil.d(TAG, "showToast: " + activity + " " + text);
        if (activity == null || text == null) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 根据自己的时间去定义一个Toast 输入时间为毫秒
     *
     * @param c
     * @param info
     * @param time  弹出时间
     */
    public static void setToatBytTime(Context c, String info, int time) {
        final Toast toast = Toast.makeText(c, info, Toast.LENGTH_SHORT);
        toast.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                toast.cancel();
            }
        }, time);
    }

    public static void showToast(final View view, final String text) {
        LogUtil.d(TAG, "showToast: " + view + " " + text);
        view.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(view.getContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static Dialog createDialog(Activity activity, int theme, View view, boolean cancelable,
                                      boolean canceledOnTouchOutSide) {
        Dialog dialog = new Dialog(activity, theme);
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(canceledOnTouchOutSide);
        dialog.setContentView(view);
        return dialog;
    }

    public static Dialog createDialog(Activity activity, View view, boolean cancelable, boolean canceledOnTouchOutSide) {
        int theme = ResourceUtil.getStyleId(activity, "NoFrameDialog");
        return createDialog(activity, theme, view, cancelable, canceledOnTouchOutSide);
    }

    public static boolean isUIThread() {
        return Looper.getMainLooper().getThread().getId() == Thread.currentThread().getId();
    }

    public static Activity getRootActivity(Activity activity) {
        Activity parent;
        while ((parent = activity.getParent()) != null) {
            activity = parent;
        }
        return activity;
    }

    /**
     * 设置Activity启动或退出动画
     */
    public static void setActivityTransitionAnimation(Activity activity, int enterAnimId, int exitAnimId) {
        ReflectHelper.invokeDeclaredMethod(activity, Activity.class.getName(), "overridePendingTransition",
                new Class<?>[]{int.class, int.class}, new Object[]{enterAnimId, exitAnimId});
    }

    public static Intent getCallPhoneIntent(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        if (!TextUtils.isEmpty(phoneNumber))
            intent.setData(Uri.parse("tel:" + phoneNumber));
        return intent;
    }

    /**
     * 调起系统拨号
     */
    public static void callPhone(Activity activity, String phoneNumber) {
        Intent intent = getCallPhoneIntent(phoneNumber);
        activity.startActivity(intent);
    }

    /**
     * 获取系统相机启动Intent
     * <p>
     * 若是captureImageFile指定为null，则会返回缩略图而不是原图
     */
    public static Intent getLaunchCameraCaptureIntent(File captureImageFile) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (captureImageFile != null)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(captureImageFile));
        return intent;
    }

    public static Intent getLaunchCameraCaptureIntent(String captureImagePath) {
        if (TextUtils.isEmpty(captureImagePath))
            return getLaunchCameraCaptureIntent((File) null);
        return getLaunchCameraCaptureIntent(new File(captureImagePath));
    }

    /**
     * 调起系统相机并拍照
     * <p>
     * 若是captureImageFile指定为null，则会返回缩略图而不是原图
     */
    public static void launchCameraCapture(Activity activity, File captureImageFile, int requestCode) {
        Intent intent = getLaunchCameraCaptureIntent(captureImageFile);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void launchCameraCapture(Fragment fragment, File captureImageFile, int requestCode) {
        Intent intent = getLaunchCameraCaptureIntent(captureImageFile);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void launchCameraCapture(Activity activity, String captureImagePath, int requestCode) {
        Intent intent = getLaunchCameraCaptureIntent(captureImagePath);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void launchCameraCapture(Fragment fragment, String captureImagePath, int requestCode) {
        Intent intent = getLaunchCameraCaptureIntent(captureImagePath);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void launchCameraCapture(Activity activity, int requestCode) {
        Intent intent = getLaunchCameraCaptureIntent((File) null);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void launchCameraCapture(Fragment fragment, int requestCode) {
        Intent intent = getLaunchCameraCaptureIntent((File) null);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * 调用系统相册Intent
     */
    public static Intent getLaunchAlbumIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // intent.addCategory(Intent.CATEGORY_OPENABLE);
        // intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // intent.setType("image/*");
        return intent;
    }

    /**
     * 调起系统相册
     */
    public static void launchAlbum(Activity activity, int requestCode) {
        Intent intent = getLaunchAlbumIntent();
        activity.startActivityForResult(intent, requestCode);
    }

    public static void launchAlbum(Fragment fragment, int requestCode) {
        Intent intent = getLaunchAlbumIntent();
        fragment.startActivityForResult(intent, requestCode);
    }

    private static void putImageCropIntentParams(Intent intent, int outputWidth, int outputHeight,
                                                 File outputImageFile, Bitmap.CompressFormat imageFormat) {
        intent.putExtra("crop", "true");
        int aspectY = 1000;
        int aspectX = outputWidth * 1000 / outputHeight;
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", outputWidth);
        intent.putExtra("outputY", outputHeight);
        intent.putExtra("scale", true);
        if (outputImageFile == null) {
            intent.putExtra("return-data", true);
        } else {
            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputImageFile));
        }
        if (imageFormat != null)
            intent.putExtra("outputFormat", imageFormat.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
    }

    /**
     * 调用系统图片裁剪程序Intent
     */
    public static Intent getLaunchImageCropIntent(File imageFile, int outputWidth, int outputHeight,
                                                  File outputImageFile, Bitmap.CompressFormat imageFormat) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(imageFile), "image/*");
        putImageCropIntentParams(intent, outputWidth, outputHeight, outputImageFile, imageFormat);
        return intent;
    }

    /**
     * 调起系统图片裁剪
     */
    public static void launchImageCrop(Activity activity, File imageFile, int cropWidth, int cropHeight,
                                       File cropImageFile, Bitmap.CompressFormat imageFormat, int requestCode) {
        Intent intent = getLaunchImageCropIntent(imageFile, cropWidth, cropHeight, cropImageFile, imageFormat);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void launchImageCrop(Fragment fragment, File imageFile, int cropWidth, int cropHeight,
                                       File cropImageFile, Bitmap.CompressFormat imageFormat, int requestCode) {
        Intent intent = getLaunchImageCropIntent(imageFile, cropWidth, cropHeight, cropImageFile, imageFormat);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * 调用系统相册以及图片裁剪程序Intent
     */
    public static Intent getLaunchAlbumAndCropIntent(int outputWidth, int outputHeight, File outputImageFile,
                                                     Bitmap.CompressFormat imageFormat) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        putImageCropIntentParams(intent, outputWidth, outputHeight, outputImageFile, imageFormat);
        return intent;
    }

    /**
     * 调起系统相册并裁剪图片
     */
    public static void launchAlbumAndCrop(Activity activity, int outputWidth, int outputHeight, File outputImageFile,
                                          Bitmap.CompressFormat imageFormat, int requestCode) {
        Intent intent = getLaunchAlbumAndCropIntent(outputWidth, outputHeight, outputImageFile, imageFormat);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void launchAlbumAndCrop(Fragment fragment, int outputWidth, int outputHeight, File outputImageFile,
                                          Bitmap.CompressFormat imageFormat, int requestCode) {
        Intent intent = getLaunchAlbumAndCropIntent(outputWidth, outputHeight, outputImageFile, imageFormat);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void launchBrowser(Activity activity, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        activity.startActivity(intent);
    }

    public static void launchBrowser(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    /**
     * 返回View在Window中APP可使用部分的绝对坐标
     */
    public static int[] getLocationInWindowVisible(View view, int[] location) {
        view.getLocationInWindow(location);
        // getLocationInWindow返回的坐标包括了顶部状态栏，所以需要减去状态栏高度
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        location[0] -= rect.left;
        location[1] -= rect.top;
        return location;
    }

    /**
     * 安装APK应用程序
     */
    public static void installPackage(Context context, File file) throws FileNotFoundException, Exception {
        LogUtil.d(TAG, "installPackage: " + context + " " + file);
        if (!file.exists()) {
            throw new FileNotFoundException("File is not exists filePath = " + file.getAbsolutePath());
        }
        Intent it = new Intent(Intent.ACTION_VIEW);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        it.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(it);
    }

    /**
     * 安装APK应用程序
     */
    public static void installPackage(Context context, String filePath) throws FileNotFoundException, Exception {
        installPackage(context, new File(filePath));
    }

    /**
     * 根据包名启动一个应用程序
     */
    public static boolean startPackage(Context context, String packageName) {
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        final List<ResolveInfo> apps = context.getPackageManager().queryIntentActivities(mainIntent, 0);
        if (apps != null) {
            // Find all activities that match the packageName
            int count = apps.size();
            for (int i = 0; i < count; i++) {
                final ResolveInfo resolveInfo = apps.get(i);
                final ActivityInfo activityInfo = resolveInfo.activityInfo;
                if (packageName.equals(activityInfo.packageName)) {
                    String className = activityInfo.name;
                    LogUtil.i(TAG, "startPackage: " + packageName + " & " + className);
                    ComponentName cn = new ComponentName(packageName, className);
                    final Intent it = new Intent(Intent.ACTION_MAIN);
                    it.addCategory(Intent.CATEGORY_LAUNCHER);
                    it.setComponent(cn);
                    it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(it);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 解压一个apk文件，以获取PackageInfo
     */
    public static synchronized PackageInfo achievePackageInfo(Context context, String filePath) throws Exception {
        PackageInfo info = context.getPackageManager().getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
        return info;
    }

    /**
     * 手机震动
     */
    public static void vibrate(Context context, long milliseconds) {
        Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vib.cancel();
        vib.vibrate(milliseconds);
    }

    public static void vibrate(Context context, long[] pattern, int repeat) {
        Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vib.cancel();
        vib.vibrate(pattern, repeat);
    }

    public static void cancelVibrate(Context context) {
        Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vib.cancel();
    }

    public static void sendScanFileBroadcast(Activity activity, File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        getRootActivity(activity).sendBroadcast(intent);
    }

    public static void sendScanFileBroadcast(Activity activity, String filePath) {
        sendScanFileBroadcast(activity, new File(filePath));
    }

    public static void sendScanDirectoryBroadcast(Activity activity, File directory) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED);
        intent.setData(Uri.parse("file://" + directory.getAbsolutePath()));
        activity.sendBroadcast(intent);
    }

    public static void sendScenExternalDirectoryBroadcast(Activity activity) {
        sendScanDirectoryBroadcast(activity, Environment.getExternalStorageDirectory());
    }

    /**
     * 转换dp为px
     */
    public static int dp2px(Context context, float dpValue) {
        final float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5f);
    }

    /**
     * 转换px为dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float density = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / density + 0.5f);
    }

    /**
     * 转换pt为px
     */
    public static int pt2px(Context context, float ptValue) {
        final float dpi = context.getResources().getDisplayMetrics().xdpi;
        return (int) (ptValue * 72.0f / dpi + 0.5f);
    }

    /**
     * 转换px为pt
     */
    public static int px2pt(Context context, float pxValue) {
        final float dpi = context.getResources().getDisplayMetrics().xdpi;
        return (int) (pxValue * dpi / 72.0f + 0.5f);
    }
}
