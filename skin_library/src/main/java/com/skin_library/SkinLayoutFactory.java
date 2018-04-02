package com.skin_library;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.skin_library.util.ThemeUtils;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by lizhiyun on 2018/3/21.
 */

public class SkinLayoutFactory implements LayoutInflater.Factory2 ,Observer{
    private static final String[] sClassPrefixList = {
            "android.widget.",
            "android.view.",
            "android.webkit."
    };
    private static final Map<String, Constructor<? extends View>> sConstructorMap = new HashMap<>();

    private static final Class<?>[] sConstructorSignature = new Class[]{
            Context.class, AttributeSet.class
    };
    SkinAttribute mSkinAttribute;
    private Activity mActivity;

    public SkinLayoutFactory(Activity activity, Typeface typeface) {
        this.mActivity = activity;
        this.mSkinAttribute = new SkinAttribute(typeface);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        View view = createViewFromTag(name,context,attrs);
        if (null == view){
            view = createView(name,context,attrs);
        }
        mSkinAttribute.load(view,attrs);
        return view;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return null;
    }

    private View createViewFromTag(String name, Context context, AttributeSet attributeSet) {
        if (-1 != name.indexOf('.')) {
            return null;
        }
        View view = null;
        for (int i = 0; i < sClassPrefixList.length; i++) {
            view =  createView(sClassPrefixList[i] + name, context, attributeSet);
            if (null != view){
                break;
            }
        }
        return view;
    }

    private View createView(String name, Context context, AttributeSet attributeSet) {
        Constructor<? extends View> constructor = sConstructorMap.get(name);
        if (null == constructor) {
            try {
                Class<? extends View> aClass = context.getClassLoader().loadClass(name).asSubclass
                        (View.class);
                constructor = aClass.getConstructor(sConstructorSignature);
                sConstructorMap.put(name, constructor);
            } catch (Exception e) {
            }
        }
        if (null != constructor) {
            try {
                return constructor.newInstance(context, attributeSet);
            } catch (Exception e) {
            }
        }
        return null;
    }

    @Override
    public void update(Observable o, Object arg) {
        ThemeUtils.updateStatusBar(mActivity);
        mSkinAttribute.applySkin(ThemeUtils.getSkinTypeface(mActivity));
    }

    public void updateSkinForNewView(View view) {
        mSkinAttribute.updateSkinForNewView(ThemeUtils.getSkinTypeface(mActivity),view);
    }
    public void release(){
        if (mSkinAttribute != null){
            mSkinAttribute.release();
        }
    }
}
