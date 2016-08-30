package com.example.parsler.parsler.Components.Fonts;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;

public final class FontsOverride {

    /**
     * Function to set default font family
     * @param context
     * @param staticTypefaceFieldName
     * @param fontAssetName
     */
    public static void setDefaultFont(Context context, String staticTypefaceFieldName, String fontAssetName) {

        final Typeface regular =
                Typeface.createFromAsset(context.getAssets(), fontAssetName);
        replaceFont(staticTypefaceFieldName, regular);
    }


    /**
     * Function to replace default font family with custom family
     * @param staticTypefaceFieldName
     * @param newTypeface
     */
    protected static void replaceFont(String staticTypefaceFieldName, final Typeface newTypeface) {
        try {

            final Field staticField =
                    Typeface.class.getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);

        } catch (NoSuchFieldException e) {

            e.printStackTrace();

        } catch (IllegalAccessException e) {

            e.printStackTrace();
        }
    }
}