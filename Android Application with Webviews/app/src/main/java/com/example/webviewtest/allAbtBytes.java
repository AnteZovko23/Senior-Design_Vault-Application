package com.example.webviewtest;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

/*
static allAbtBytes instance = new allAbtBytes(R.layout.activity_dbdata_bytearray);
    private static Bitmap bitmap;

    public allAbtBytes(int layout)
    {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.smiley);
    }
 */
/**/

public class allAbtBytes
{
    private static allAbtBytes instance = null;
    private static Bitmap bitmap;

    private allAbtBytes(Context context)
    {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.smiley);
    }

    public static byte[] createByteArray(String text)
    {
        // if we need to do anything special depending on
        //  type of input string or otherwise, put here
        return text.getBytes(StandardCharsets.UTF_8);
    }

    protected String encodedImgStr()
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);
        byte[] b = baos.toByteArray();

        String encodeImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encodeImage;
    }
/*
    new AsyncTask<Void, Void, String>() {
    @Override
    protected String doInBackground(Void... voids) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);
        byte[] b = baos.toByteArray();

        String encodeImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encodeImage;
    }

    @Override
    protected void onPostExecute(String s) {
        txtV.setText(s);
    }
}.execute();
/**/
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

    public static Bitmap getBitmap()
    {
        return bitmap;
    }

    public static allAbtBytes makeInstance(Context context)
    {
        if (instance == null)
        {
            instance = new allAbtBytes(context);
        }
        return instance;
    }
}
