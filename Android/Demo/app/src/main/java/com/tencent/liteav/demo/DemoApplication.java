package com.tencent.liteav.demo;

import android.os.Build;
import android.os.StrictMode;
import androidx.multidex.MultiDexApplication;

import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.rtmp.TXLiveBase;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

//import com.squareup.leakcanary.LeakCanary;
//import com.squareup.leakcanary.RefWatcher;


public class DemoApplication extends MultiDexApplication {

    //    private RefWatcher mRefWatcher;
    private static DemoApplication instance;

    // 如何获取License? 请参考官网指引 https://cloud.tencent.com/document/product/454/34750
    String licenceUrl = "http://license.vod2.myqcloud.com/license/v1/b0bd0f2084d456f0932db0a98be905d4/TXLiveSDK.licence";
    String licenseKey = "fedf3107141a791fac5e03af31c9b350";

    @Override
    public void onCreate() {

        super.onCreate();

        instance = this;

        TXLiveBase.setConsoleEnabled(true);
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
        strategy.setAppVersion(TXLiveBase.getSDKVersionStr());
        CrashReport.initCrashReport(getApplicationContext(), strategy);

        TXLiveBase.getInstance().setLicence(instance, licenceUrl, licenseKey);

        // 短视频licence设置
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
        closeAndroidPDialog();
    }

    //    public static RefWatcher getRefWatcher(Context context) {
    //        DemoApplication application = (DemoApplication) context.getApplicationContext();
    //        return application.mRefWatcher;
    //    }

    public static DemoApplication getApplication() {
        return instance;
    }

    private void closeAndroidPDialog() {
        try {
            Class       aClass              = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class  cls            = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread         = declaredMethod.invoke(null);
            Field  mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}