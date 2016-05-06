package com.hackensack.umc.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;

/**
 * Created by prerana_katyarmal on 10/25/2015.
 */
public class Base64Converter {
    private Context context;

    public static String combineImageAndGetBase64(Bitmap c,Bitmap s,Context context){
            Bitmap cs = null;

            int width, height = 0;

            if(c.getWidth() > s.getWidth()) {
                width = c.getWidth() + s.getWidth();
                height = c.getHeight();
            } else {
                width = s.getWidth() + s.getWidth();
                height = c.getHeight();
            }

            cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            Canvas comboImage = new Canvas(cs);

            comboImage.drawBitmap(c, 0f, 0f, null);
            comboImage.drawBitmap(s, c.getWidth(), 0f, null);

            return createBase64StringFromBitmap(cs,context);

    }

    public static String createBase64StringFroImage(String uri ,Context context){
        Bitmap bitmap= null;
        try {
            bitmap = decodeUri(uri,context);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
     return null;
    }
    public static String createBase64StringFromBitmap(Bitmap bitmap ,Context context){
       // Bitmap bitmap= null;
        try {
          //  bitmap = decodeUri(uri,context);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            if(bitmap.getDensity() > 320){
                bitmap.compress(Bitmap.CompressFormat.PNG, 40, byteArrayOutputStream);
           }else{
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
           }
            byte[] byteArray = byteArrayOutputStream .toByteArray();

            //Log.v("Length of Base64",String.valueOf(Base64.encodeToString(byteArray, Base64.DEFAULT).length()));
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String createBase64StringFroImageNew(String uri ,Context context){
        Bitmap bitmap= null;
        try {
            bitmap = decodeUri(uri,context);
          /*  ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);*/
            //byte[] byteArray = byteArrayOutputStream .toByteArray();
            int bytes = bitmap.getByteCount();
//or we can calculate bytes this way. Use a different value than 4 if you don't use 32bit images.
//int bytes = b.getWidth()*b.getHeight()*4;

            ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
            bitmap.copyPixelsToBuffer(buffer); //Move the byte data to the buffer

            byte[] byteArray = buffer.array();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String createBase64StringFromBitmapNew(Bitmap bitmap ,Context context){
        // Bitmap bitmap= null;
        try {
            //  bitmap = decodeUri(uri,context);
         /*   ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);*/
            int bytes = bitmap.getByteCount();
//or we can calculate bytes this way. Use a different value than 4 if you don't use 32bit images.
//int bytes = b.getWidth()*b.getHeight()*4;

            ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
            bitmap.copyPixelsToBuffer(buffer);
            // byte[] byteArray = byteArrayOutputStream .toByteArray();//Move the byte data to the buffer

            byte[] byteArray = buffer.array();


            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap decodeUri(String selectedImage,Context context) throws FileNotFoundException {
        Uri uri=Uri.parse(selectedImage);
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(
                context.getContentResolver().openInputStream(uri), null, o);

        final int REQUIRED_SIZE = 300;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(
                context.getContentResolver().openInputStream(uri), null, o2);
    }
    public static String createBase64StringFroImageFromFile(String uri ,Context context){
        Bitmap bitmap = BitmapFactory.decodeFile(uri);
        try {
            bitmap = decodeUri(uri,context);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
