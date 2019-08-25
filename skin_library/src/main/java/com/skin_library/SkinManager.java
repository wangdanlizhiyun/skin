package com.skin_library;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;

import com.skin_library.util.SkinPreference;
import com.skin_library.util.SkinResources;

import java.lang.reflect.Method;
import java.util.Observable;

public class SkinManager extends Observable{
    private static volatile SkinManager sInstance;
    private Application mApplication;


    public static void init(Application application) {
        if (sInstance == null) {
            synchronized (SkinManager.class) {
                if (sInstance == null) {
                    sInstance = new SkinManager(application);
                }
            }
        }
    }
    SkinLifecycle mSkinLifecycle;

    public static SkinManager getInstance() {
        return sInstance;
    }

    private SkinManager(Application application) {
        this.mApplication = application;
        mSkinLifecycle = new SkinLifecycle();
        application.registerActivityLifecycleCallbacks(mSkinLifecycle);
        SkinPreference.init(application);
        SkinResources.init(application);
        loadSkin(SkinPreference.getInstance().getSkin());
    }

    public void loadSkin(String path) {
        if (TextUtils.isEmpty(path)){
            SkinPreference.getInstance().setSkin("");
            SkinResources.getInstance().reset();
        }else {
            try {
                AssetManager assetManager = AssetManager.class.newInstance();
                Method addAssetPath = assetManager.getClass().getMethod("addAssetPath",String.class);
                addAssetPath.invoke(assetManager,path);
                Resources appResources = mApplication.getResources();
                Resources skinResource = new Resources(assetManager,appResources.getDisplayMetrics(),appResources.getConfiguration());
                PackageManager packageManager = mApplication.getPackageManager();
                PackageInfo packageInfo = packageManager.getPackageArchiveInfo(path,PackageManager.GET_ACTIVITIES);
                SkinResources.getInstance().applySkin(skinResource,packageInfo.packageName);
                SkinPreference.getInstance().setSkin(path);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        setChanged();
        notifyObservers();

    }

    public void updateSkin(Activity activity) {
        mSkinLifecycle.updateSkin( activity);
    }

    /**
     * 更新非xml创建的view的皮肤
     * @param activity
     * @param view
     */
    public void updateSkinForNewView(Activity activity, View view) {
        mSkinLifecycle.updateSkinForNewView( activity,view);
    }
}
