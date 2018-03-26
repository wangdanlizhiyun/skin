package com.skin_library;

import android.app.Activity;
import android.app.Application;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.LayoutInflaterCompat;
import android.view.LayoutInflater;
import android.view.View;

import com.skin_library.util.ThemeUtils;

import java.lang.reflect.Field;

/**
 * Created by lizhiyun on 2018/3/22.
 */

public class SkinLifecycle implements Application.ActivityLifecycleCallbacks {
    ArrayMap<Activity,SkinLayoutFactory> mActivitySkinLayoutFactoryArrayMap = new ArrayMap<>();
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        ThemeUtils.updateStatusBar(activity);
        Typeface typeface = ThemeUtils.getSkinTypeface(activity);
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        try {
            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
            field.setAccessible(true);
            field.setBoolean(layoutInflater,false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SkinLayoutFactory skinLayoutFactory = new SkinLayoutFactory(activity,typeface);
        LayoutInflaterCompat.setFactory2(layoutInflater,skinLayoutFactory);
        SkinManager.getInstance().addObserver(skinLayoutFactory);
        mActivitySkinLayoutFactoryArrayMap.put(activity,skinLayoutFactory);

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        SkinLayoutFactory skinLayoutFactory = mActivitySkinLayoutFactoryArrayMap.remove(activity);
        SkinManager.getInstance().deleteObserver(skinLayoutFactory);
        mActivitySkinLayoutFactoryArrayMap.get(activity).release();
    }

    public void updateSkin(Activity activity) {
        mActivitySkinLayoutFactoryArrayMap.get(activity).update(null, null);
    }
    /**
     * 更新非xml创建的view的皮肤
     * @param activity
     * @param view
     */
    public void updateSkinForNewView(Activity activity, View view) {
        SkinLayoutFactory skinLayoutFactory = mActivitySkinLayoutFactoryArrayMap.get(activity);
        skinLayoutFactory.updateSkinForNewView(view);
    }
}
