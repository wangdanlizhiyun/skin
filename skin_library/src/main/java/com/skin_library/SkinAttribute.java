package com.skin_library;

import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

import com.skin_library.util.SkinResources;
import com.skin_library.util.ThemeUtils;

import java.util.ArrayList;
import java.util.List;

public class SkinAttribute {
    private static final ArrayList<TagAndNamePair> sTagAndNamePair;
    private static class TagAndNamePair{
        String name;
        int tagId;

        public TagAndNamePair(String name, int tagId) {
            this.name = name;
            this.tagId = tagId;
        }
    }
    static {
        sTagAndNamePair = new ArrayList<>();
        sTagAndNamePair.add(new TagAndNamePair("background",R.id.tag_background));
        sTagAndNamePair.add(new TagAndNamePair("src",R.id.tag_src));
        sTagAndNamePair.add(new TagAndNamePair("textColor",R.id.tag_textColor));
        sTagAndNamePair.add(new TagAndNamePair("drawableLeft",R.id.tag_drawableLeft));
        sTagAndNamePair.add(new TagAndNamePair("drawableTop",R.id.tag_drawableTop));
        sTagAndNamePair.add(new TagAndNamePair("drawableRight",R.id.tag_drawableRight));
        sTagAndNamePair.add(new TagAndNamePair("drawableBottom",R.id.tag_drawableBottom));
        sTagAndNamePair.add(new TagAndNamePair("skinTypeface",R.id.tag_skinTypeface));
    }
    private boolean isHasAttributeName(String attributeName){
        for (TagAndNamePair tagAndNamePair:sTagAndNamePair
             ) {
            if (tagAndNamePair.name.equals(attributeName)){
                return true;
            }
        }
        return false;
    }



    public SkinAttribute(Typeface typeface) {
        this.typeface = typeface;


    }
    private Typeface typeface;

    List<SkinView> mSkinViews = new ArrayList<>();

    public SkinView load(View view, AttributeSet attrs) {
        List<SkinPair> skinPairs = new ArrayList<>();
        if (attrs != null){
            for (int i = 0; i < attrs.getAttributeCount(); i++) {
                String attributeName = attrs.getAttributeName(i);
                if (isHasAttributeName(attributeName)) {
                    String attributeValue = attrs.getAttributeValue(i);
                    if (attributeValue.startsWith("#")) {
                        continue;
                    }
                    int resId;
                    if (attributeValue.startsWith("?")) {
                        int attrId = Integer.parseInt(attributeValue.substring(1));
                        resId = ThemeUtils.getResId(view.getContext(), new int[]{attrId})[0];
                    } else {
                        resId = Integer.parseInt(attributeValue.substring(1));
                    }
                    if (resId != 0) {
                        skinPairs.add(new SkinPair(attributeName, resId));
                    }
                }
            }
        }else {
            //非xml生成的view
            for (TagAndNamePair tagAndNamePair:sTagAndNamePair
                    ) {
                Object tag = view.getTag(tagAndNamePair.tagId);
                if (null != tag){
                    if (tag instanceof Integer){
                        int resId = (int) tag;
                        if (resId != 0){
                            skinPairs.add(new SkinPair(tagAndNamePair.name, resId));
                        }
                    }
                }
            }

        }
        if (!skinPairs.isEmpty() || view instanceof  TextView || view instanceof SkinViewSupport) {
            SkinView skinView = new SkinView(view, skinPairs);
            skinView.applySkin(typeface);
            mSkinViews.add(skinView);
            return skinView;
        }
        return null;
    }

    public void applySkin(Typeface typeface) {
        for (SkinView skinView : mSkinViews
                ) {
            skinView.applySkin(typeface);
        }
    }

    public void updateSkinForNewView(Typeface skinTypeface, View view) {
        SkinView skinView = load(view,null);
        if (null != skinView){
            skinView.applySkinTypeface(skinTypeface);
        }

    }

    static class SkinView {
        View view;
        List<SkinPair> skinPairs;

        public SkinView(View view, List<SkinPair> skinPairs) {
            this.view = view;
            this.skinPairs = skinPairs;
        }

        public void applySkin(Typeface typeface) {
            applySkinTypeface(typeface);
            for (SkinPair skinPairs : skinPairs
                    ) {
                try {

                    Drawable left = null, top = null, right = null, bottom = null;
                    switch (skinPairs.attributeName) {
                        case "background":
                            Object background = SkinResources.getInstance().getBackground(skinPairs.resId);
                            if (background instanceof Integer) {
                                view.setBackgroundColor((Integer) background);
                            } else {
                                ViewCompat.setBackground(view, (Drawable) background);
                            }
                            break;
                        case "src":
                            if (view instanceof ImageView) {

                                background = SkinResources.getInstance().getBackground(skinPairs.resId);
                                ;
                                if (background instanceof Integer) {
                                    ((ImageView) view).setImageDrawable(new ColorDrawable((Integer) background));
                                } else {
                                    ((ImageView) view).setImageDrawable((Drawable) background);
                                }
                            }
                            break;
                        case "textColor":
                            ((TextView) view).setTextColor(SkinResources.getInstance().getColorStateList(skinPairs.resId));
                            break;
                        case "drawableLeft":
                            left = SkinResources.getInstance().getDrawable(skinPairs.resId);
                            break;
                        case "drawableTop":
                            top = SkinResources.getInstance().getDrawable(skinPairs.resId);
                            break;
                        case "drawableRight":
                            right = SkinResources.getInstance().getDrawable(skinPairs.resId);
                            break;
                        case "drawableBottom":
                            bottom = SkinResources.getInstance().getDrawable(skinPairs.resId);
                            break;
                        case "skinTypeface":
                            Typeface typeface1 = SkinResources.getInstance().getTypeface(skinPairs.resId);
                            applySkinTypeface(typeface1);
                            break;
                        default:
                            break;

                    }
                    if (null != left || null != top || null != right || null != bottom) {
                        ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            //对于自定义view其他属性的换肤支持放在最后面，避免被通用的几个case覆盖
            applySkinViewSupport();
        }
        private void applySkinViewSupport() {
            if (view instanceof SkinViewSupport) {
                ((SkinViewSupport) view).applySkin();
            }
        }
        private void applySkinTypeface(Typeface typeface) {
            if (view instanceof TextView) {
                ((TextView) view).setTypeface(typeface);
            }
        }
    }

    static class SkinPair {
        String attributeName;
        int resId;

        public SkinPair(String attributeName, int resId) {
            this.attributeName = attributeName;
            this.resId = resId;
        }
    }
    public void release(){
        mSkinViews.clear();
        mSkinViews = null;
    }
}
