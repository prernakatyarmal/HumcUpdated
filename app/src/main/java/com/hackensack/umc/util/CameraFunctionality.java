package com.hackensack.umc.util;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.camera2.CameraCharacteristics;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import static com.hackensack.umc.util.Base64Converter.decodeUri;

/**
 * Created by prerana_katyarmal on 11/24/2015.
 */
public class CameraFunctionality {


    private static String TAG = "CameraFunctionality";

    /**
     * Show camera app of android
     *
     * @param context           -Calling Activity context
     * @param cameraFacing      -Facing of camera
     * @param selectedImageView -Image view for which picture will be taken
     * @return Uri of camera captured image
     */
    public static Uri dispatchTakePictureIntent(Context context, int cameraFacing, int selectedImageView) {
        Uri imageUri = null;

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // Intent intent = new Intent(context, PictureDemo.class);
        if (cameraFacing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            intent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_FRONT);
        } else {
            intent.putExtra("android.intent.extras.CAMERA_FACING",  Camera.CameraInfo.CAMERA_FACING_BACK);
        }
        File photo;
        try {
            // place where to store camera taken picture
            photo = CameraFunctionality.createTemporaryFile("picture_" + selectedImageView, ".png",context);
            // photo.delete();
        } catch (Exception e) {
            Log.v(TAG, "Can't create file to take picture!");
            //Toast.makeText(getActivity(), "Please check SD card! Image shot is impossible!", 10000);
            return imageUri;
        }
        imageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        //start camera intent
        ((Activity) context).startActivityForResult(intent, Constant.FRAGMENT_CONST_REQUEST_IMAGE_CAPTURE);
        return imageUri;
    }

    public static File createTemporaryFile(String part, String ext, Context context) throws Exception {

        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        File tempDir = null;
        if (isSDPresent) {
            tempDir = Environment.getExternalStorageDirectory();
        } else {
            ContextWrapper cw = new ContextWrapper((context.getApplicationContext()));
            // path to /data/data/yourapp/app_data/imageDir
            tempDir = cw.getDir("imageDir", Context.MODE_PRIVATE);
        }
        tempDir = new File(tempDir.getAbsolutePath() + "/Humc");
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }
        File file = new File(tempDir.getAbsolutePath() + "/"+part + ext);
        if (file.exists()) {
            file.delete();
        }
        return new File(tempDir.getAbsolutePath() + "/"+part + ext);
    }

    public static Bitmap checkImageOrientation(Bitmap bitmap, String photoPath, Context context) {
        ExifInterface ei = null;
        Bitmap rotatedBitmap = null;
        Cursor cursor = context.getContentResolver().query(Uri.parse(photoPath), null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        String path = cursor.getString(idx);
        try {
            ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            Log.v("CameraFunctionality", "orientation is::" + String.valueOf(orientation));
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    Log.v("CameraFunctionality", "ORIENTATION_ROTATE_90");
                    rotatedBitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    Log.v("CameraFunctionality", "ORIENTATION_ROTATE_180");
                    rotatedBitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    Log.v("CameraFunctionality", "ORIENTATION_ROTATE_270");
                    rotatedBitmap = rotateImage(bitmap, 270);
                    break;
                default:
                    Log.v("CameraFunctionality", "default orientation is::" + String.valueOf(orientation));
                    rotatedBitmap = bitmap;
                    break;
                // etc.
            }
        } catch (IOException e) {
            e.printStackTrace();
            cursor.close();
            return bitmap;
        }
        cursor.close();
        return rotatedBitmap;

    }

    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static void setPhotoToImageView(String urlString, ImageView mImageView, Context context) {


        try {
            Bitmap bitmap = decodeUri(urlString, context);
            bitmap = CameraFunctionality.checkImageOrientation(bitmap, urlString.toString(), context);
            mImageView.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void setPhotoToImageViewWithoutOrientation(String urlString, ImageView mImageView, Context context) {


        try {
            Bitmap bitmap = decodeUri(urlString, context);
            mImageView.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void setBitMapToImageView(String uri, Bitmap bitmap, ImageView mImageView, Context context) {


        //  try {
        // bitmap = CameraFunctionality.rotateBitmap(bitmap,uri);
        mImageView.setImageBitmap(bitmap);
        /*} catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
    }

    public static Bitmap getBitmapFromFile(String urlString, Context context) {
        Bitmap bitmap = null;
        try {
            bitmap = decodeUri(urlString, context);
            //  bitmap = CameraFunctionality.checkImageOrientation(bitmap, urlString.toString(), context);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static void deleteImageAfterUploading(String imagePath, Context context) {
        try {

            context.getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    MediaStore.Images.Media.DATA
                            + "='"
                            + Uri.parse(imagePath)
                            + "'", null);

        } catch (Exception e) {
            e.printStackTrace();

        }

        /*// to notify change
        getActivity().getContentResolver().notifyChange(
                Uri.parse("file://" + newfilee.getPath()),null);*/
    }

    public static void deleteImage(File image, Context context) {
        File fdelete = image;
        if (image.exists()) {
            if (fdelete.delete()) {
                Log.e("-->", "file Deleted :" + fdelete.getPath());
                callBroadCast(context);
            } else {
                Log.e("-->", "file not Deleted :" + fdelete.getPath());
            }
        }
    }

    public static void callBroadCast(Context context) {
        if (Build.VERSION.SDK_INT >= 14) {
            Log.e("-->", " >= 14");
            MediaScannerConnection.scanFile(context, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                /*
                 *   (non-Javadoc)
                 * @see android.media.MediaScannerConnection.OnScanCompletedListener#onScanCompleted(java.lang.String, android.net.Uri)
                 */
                public void onScanCompleted(String path, Uri uri) {
                    Log.e("ExternalStorage", "Scanned " + path + ":");
                    Log.e("ExternalStorage", "-> uri=" + uri);
                }
            });
        } else {
            Log.e("-->", " < 14");
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
    }

    public static String getRealPathFromURI(Uri uri, Context context) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        String retString = cursor.getString(idx);
        cursor.close();
        File file = new File(retString);

        return Uri.fromFile(file).toString();

    }

    public static Uri saveCropedImage_1(Bitmap bitmap, Context mContext, int seelectedImageView) {
        Uri imageUri = null;

        File photo = null;
        try {
            // place where to store camera taken picture


            photo = CameraFunctionality.createTemporaryFile("picture_cropped" + seelectedImageView, ".png",mContext);
            // photo.delete();


            ByteArrayOutputStream ostream = new ByteArrayOutputStream();

            // save image into gallery
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);

            FileOutputStream fout = new FileOutputStream(photo);
            fout.write(ostream.toByteArray());
            fout.close();
        } catch (Exception e) {
            Log.v(TAG, "Can't create file to take picture!");
            //Toast.makeText(getActivity(), "Please check SD card! Image shot is impossible!", 10000);

        }
        // imageUri = Uri.fromFile(photo);
        ContentValues values = new ContentValues();


        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.MediaColumns.DATA,
                photo.getAbsolutePath());

        Uri uri = mContext.getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //start camera intent
        return uri;
    }


    public static Intent createCameCropInatent(Uri picUri) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(picUri, "image");
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("aspectX", 0);
        cropIntent.putExtra("aspectY", 0);
        cropIntent.putExtra("return-data", false);

        return cropIntent;

    }

    public static Bitmap rotateBitmap(Bitmap bitmap, String uri) {
        int orientation = 0;
        try {
            ExifInterface exif = new ExifInterface(Uri.parse(uri).getPath());
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    static int[][] knl = {
            {0, -1, 0},
            {-1, 5, -1},
            {0, -1, 0}
    };


    protected String convertMediaUriToPath(Uri uri, Context context) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        File file = new File(path);
        return file.getPath();
    }
    public static String saveRoatedSelfie(Bitmap bitmap, int selectedImageView, Context context){
        FileOutputStream out = null;
        File filename= null;
        try {
            filename = CameraFunctionality.createTemporaryFile("picture_" + selectedImageView, ".png", context);

            out = new FileOutputStream(filename);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filename.getAbsolutePath();
    }

}
