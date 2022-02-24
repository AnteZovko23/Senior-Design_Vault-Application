package com.example.webviewtest;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class allAbtBytes
{
    private static allAbtBytes instance = new allAbtBytes();

    private allAbtBytes(){}

    public static byte[] createByteArray(String text)
    {
        // if we need to do anything special depending on
        //  type of input string or otherwise, put here
        return text.getBytes();
    }

    public static BitmapDrawable getPic(byte[] bytearray, Resources res)
    {
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytearray,0,bytearray.length);
        // final drawable bitmap of image to return
        BitmapDrawable finalPic = new BitmapDrawable(res, bitmap);

        return finalPic;
    }

    public static allAbtBytes getInstance()
    {
        return instance;
    }
}
