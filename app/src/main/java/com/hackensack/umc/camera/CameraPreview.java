package com.hackensack.umc.camera;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.Toast;

import com.hackensack.umc.cropper.BitmapCroppingWorkerTask;
import com.hackensack.umc.cropper.CropImageView;
import com.hackensack.umc.cropper.util.ImageViewUtil;
import com.hackensack.umc.util.Base64Converter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private static final int PIC_CROP = 11;
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private int rotation;
    private int cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private Context mContext;
    private OnGetCroppedImageCompleteListener onGetCroppedImageCompleteListener;
    private byte[] mBuffer;

    public CameraPreview(Context context, Camera camera) {
        super(context, null);
        mCamera = camera;
        mContext = context;
        onGetCroppedImageCompleteListener = (OnGetCroppedImageCompleteListener) context;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mCamera.setPreviewCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                Toast toast = Toast
                        .makeText(mContext, "onPreview", Toast.LENGTH_LONG);
                toast.show();
                try {
                    Camera.Parameters parameters = camera.getParameters();
                    Camera.Size size = parameters.getPreviewSize();
                    YuvImage image = new YuvImage(data, parameters.getPreviewFormat(),
                            size.width, size.height, null);
                    File file = new File(Environment.getExternalStorageDirectory()
                            .getPath() + "/out.jpg");
                    FileOutputStream filecon = new FileOutputStream(file);
                    image.compressToJpeg(
                            new Rect(0, 0, image.getWidth(), image.getHeight()), 90,
                            filecon);
                } catch (FileNotFoundException e) {
                    Toast toast1 = Toast
                            .makeText(mContext, e.getMessage(), Toast.LENGTH_LONG);
                    toast1.show();
                }
            }
        });

        mCamera.setFaceDetectionListener(new Camera.FaceDetectionListener() {
            @Override
            public void onFaceDetection(Camera.Face[] faces, Camera camera) {

            }
        });
    }


    public CameraPreview(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mCamera = getCameraInstance();
        mContext = context;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public CameraPreview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mCamera = getCameraInstance();
        mContext = context;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // real work here
    }

    public Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();// attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            e.printStackTrace();
        }
        return c; // returns null if camera is unavailable
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        // try {
        openCamera(cameraId);
        updateBufferSize();
        /*} catch (IOException e) {
            Log.d("CameraView", "Error setting camera preview: " + e.getMessage());
        }*/
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            updateBufferSize();
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            openCamera(cameraId);

        } catch (Exception e) {
            Log.d("CameraView", "Error starting camera preview: " + e.getMessage());
        }
    }

    public void onPause() {
        mCamera.release();
        mCamera = null;
    }

    private void setUpCamera(Camera c) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        rotation = ((Activity) mContext).getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 0;
                break;
            case Surface.ROTATION_90:
                degree = 90;
                break;
            case Surface.ROTATION_180:
                degree = 180;
                break;
            case Surface.ROTATION_270:
                degree = 270;
                break;
            default:
                break;
        }

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            // frontFacing
            rotation = (info.orientation + degree) % 330;
            rotation = (360 - rotation) % 360;
        } else {
            // Back-facing
            rotation = (info.orientation - degree + 360) % 360;
        }
        c.setDisplayOrientation(rotation);
        Camera.Parameters params = c.getParameters();


        List<String> focusModes = params.getSupportedFlashModes();
        if (focusModes != null) {
            if (focusModes
                    .contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                params.setFlashMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }
        }

        params.setRotation(rotation);
    }

    private boolean openCamera(int id) {
        boolean result = false;
        cameraId = id;
        releaseCamera();
        try {
            mCamera = Camera.open(cameraId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mCamera != null) {
            setUpCamera(mCamera);
            mCamera.setErrorCallback(new Camera.ErrorCallback() {

                @Override
                public void onError(int error, Camera camera) {

                }
            });
            try {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();
                // mCamera.startFaceDetection();
            } catch (IOException e) {
                e.printStackTrace();
            }

            result = true;

        }
        return result;
    }

    private void releaseCamera() {
        try {
            if (mCamera != null) {
                mCamera.setPreviewCallback(null);
                mCamera.setErrorCallback(null);
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error", e.toString());
            mCamera = null;
        }
    }

    public void takeImage(final DrawView dv) {
        mCamera.takePicture(null, null, new Camera.PictureCallback() {


            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                try {
                    new CreateBitMap(data,dv).execute();
                 /*   // convert byte array into bitmap
                    Bitmap loadedImage = null;
                    Bitmap rotatedBitmap = null;
                    loadedImage = BitmapFactory.decodeByteArray(data, 0,
                            data.length);

                    // rotate Image
                    Matrix rotateMatrix = new Matrix();
                    rotateMatrix.postRotate(rotation);
                    rotatedBitmap = Bitmap.createBitmap(loadedImage, 0, 0,
                            loadedImage.getWidth(), loadedImage.getHeight(),
                            rotateMatrix, false);

                    Matrix matrix = new Matrix();
                    matrix.postScale(0.5f, 0.5f);

                  *//* Bitmap croppedBitmap = Bitmap.createBitmap(rotatedBitmap,dv.l,dv.t,200,200);
                    saveCropedImage(croppedBitmap);*//*
                    // saveCropedImage(getPic(dv.l,dv.t,200,200));

                    BitmapCroppingWorkerTask bitmapCroppingWorkerTask = new BitmapCroppingWorkerTask(CameraPreview.this, rotatedBitmap, getActualCropRect(rotatedBitmap, dv), CropImageView.CropShape.RECTANGLE);
                    bitmapCroppingWorkerTask.execute();*/
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private class CreateBitMap extends AsyncTask<Void, Void, Bitmap> {
        byte[] data;
        DrawView dv;

        CreateBitMap(byte[] data,DrawView dv) {
            this.data = data;
            this.dv=dv;

        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap loadedImage = null;
            Bitmap rotatedBitmap = null;
            loadedImage = BitmapFactory.decodeByteArray(data, 0,
                    data.length);

            // rotate Image
            Matrix rotateMatrix = new Matrix();
            rotateMatrix.postRotate(rotation);
            rotatedBitmap = Bitmap.createBitmap(loadedImage, 0, 0,
                    loadedImage.getWidth(), loadedImage.getHeight(),
                    rotateMatrix, false);

            Matrix matrix = new Matrix();
            matrix.postScale(0.5f, 0.5f);

            return loadedImage;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            BitmapCroppingWorkerTask bitmapCroppingWorkerTask = new BitmapCroppingWorkerTask(CameraPreview.this, bitmap, getActualCropRect(bitmap, dv), CropImageView.CropShape.RECTANGLE);
            bitmapCroppingWorkerTask.execute();
        }
    }

    public void onGetImageCroppingAsyncComplete(BitmapCroppingWorkerTask.Result result) {
        Bitmap bitmap = result.bitmap;
        onGetCroppedImageCompleteListener.onGetCroppedImageComplete(bitmap);


    }

    public String saveCropedImage(Bitmap rotatedBitmap) {
        File imageFile = null;

       // String base64= Base64Converter.createBase64StringFromBitmap(rotatedBitmap, mContext);
        String state = Environment.getExternalStorageState();
        File folder = null;
        if (state.contains(Environment.MEDIA_MOUNTED)) {
            folder = new File(Environment
                    .getExternalStorageDirectory() + "/Demo");
        } else {
            folder = new File(Environment
                    .getExternalStorageDirectory() + "/Demo");
        }

        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        try {
            if (success) {
                java.util.Date date = new java.util.Date();
                imageFile = new File(folder.getAbsolutePath()
                        + File.separator
                        + new Timestamp(date.getTime()).toString()
                        + "Image.png");


                imageFile.createNewFile();

            } else {
                Toast.makeText(mContext, "Image Not saved",
                        Toast.LENGTH_SHORT).show();
                return null;
            }

            ByteArrayOutputStream ostream = new ByteArrayOutputStream();

            // save image into gallery
            rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);

            FileOutputStream fout = new FileOutputStream(imageFile);
            fout.write(ostream.toByteArray());
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageFile.getAbsolutePath();
    }

    public interface OnGetCroppedImageCompleteListener {

        /**
         * Called when a crop image view has completed loading image for cropping.<br>
         * If loading failed error parameter will contain the error.
         *
         * @param bitmap the cropped image bitmap (null if failed)
         */
        void onGetCroppedImageComplete(Bitmap bitmap);
    }

    public Rect getActualCropRect(Bitmap mBitmap, DrawView dv) {
        if (mBitmap != null) {
            final Rect displayedImageRect = ImageViewUtil.getBitmapRect(mBitmap, ((Activity) mContext).getWindow().getDecorView(), ImageView.ScaleType.CENTER_CROP);

            // Get the scale factor between the actual Bitmap dimensions and the displayed dimensions for width.
            final float actualImageWidth = mBitmap.getWidth();
            final float displayedImageWidth = displayedImageRect.width();
            final float scaleFactorWidth = actualImageWidth / displayedImageWidth;

            // Get the scale factor between the actual Bitmap dimensions and the displayed dimensions for height.
            final float actualImageHeight = mBitmap.getHeight();
            final float displayedImageHeight = displayedImageRect.height();
            final float scaleFactorHeight = actualImageHeight / displayedImageHeight;

            // Get crop window position relative to the displayed image.
            final float displayedCropLeft = dv.l - displayedImageRect.left;
            final float displayedCropTop = dv.t - displayedImageRect.top / 3;
            final float displayedCropWidth = dv.rect.width();
            final float displayedCropHeight = dv.rect.height();

            // Scale the crop window position to the actual size of the Bitmap.
            float actualCropLeft = displayedCropLeft * scaleFactorWidth;
            float actualCropTop = displayedCropTop * scaleFactorHeight;
            float actualCropRight = actualCropLeft + displayedCropWidth * scaleFactorWidth;
            float actualCropBottom = (actualCropTop - displayedImageRect.top) + displayedCropHeight * scaleFactorHeight;

            // Correct for floating point errors. Crop rect boundaries should not exceed the source Bitmap bounds.
            actualCropLeft = Math.max(0f, actualCropLeft);
            actualCropTop = Math.max(0f, actualCropTop);
            actualCropRight = Math.min(mBitmap.getWidth(), actualCropRight);
            actualCropBottom = Math.min(mBitmap.getHeight(), actualCropBottom);

            return new Rect((int) actualCropLeft, (int) actualCropTop, (int) actualCropRight, (int) actualCropBottom);
        } else {
            return null;
        }
    }

    /**
     * Gets the crop window's position relative to the source Bitmap (not the image
     * displayed in the CropImageView) and the original rotation.
     *
     * @return a RectF instance containing cropped area boundaries of the source Bitmap
     */
    // @SuppressWarnings("SuspiciousNameCombination")
  /*  public Rect getActualCropRectNoRotation(Bitmap mBitmap) {
        if (mBitmap != null) {
            Rect rect = getActualCropRect(mBitmap);
            int rotateSide = mDegreesRotated / 90;
            if (rotateSide == 1) {
                rect.set(rect.top, mBitmap.getWidth() - rect.right, rect.bottom, mBitmap.getWidth() - rect.left);
            } else if (rotateSide == 2) {
                rect.set(mBitmap.getWidth() - rect.right, mBitmap.getHeight() - rect.bottom, mBitmap.getWidth() - rect.left, mBitmap.getHeight() - rect.top);
            } else if (rotateSide == 3) {
                rect.set(mBitmap.getHeight() - rect.bottom, rect.left, mBitmap.getHeight() - rect.top, rect.right);
            }
            rect.set(rect.left * mLoadedSampleSize, rect.top * mLoadedSampleSize, rect.right * mLoadedSampleSize, rect.bottom * mLoadedSampleSize);
            return rect;
        } else {
            return null;
        }
    }*/
    public Bitmap getPic(int x, int y, int width, int height) {
        System.gc();
        Bitmap b = null;
        Camera.Parameters mParameters = mCamera.getParameters();
        Camera.Size s = mParameters.getPreviewSize();

        YuvImage yuvimage = new YuvImage(mBuffer, ImageFormat.NV21, this.getWidth(), this.getHeight(), null);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        yuvimage.compressToJpeg(new Rect(x, y, width, height), 100, outStream); // make JPG
        b = BitmapFactory.decodeByteArray(outStream.toByteArray(), 0, outStream.size());
        if (b != null) {
            //Log.i(TAG, "getPic() WxH:" + b.getWidth() + "x" + b.getHeight());
        } else {
            //Log.i(TAG, "getPic(): Bitmap is null..");
        }
        yuvimage = null;
        outStream = null;
        System.gc();
        return b;
    }

    private void updateBufferSize() {
        mBuffer = null;
        System.gc();
        // prepare a buffer for copying preview data to
        int h = mCamera.getParameters().getPreviewSize().height;
        int w = mCamera.getParameters().getPreviewSize().width;
        int bitsPerPixel =
                ImageFormat.getBitsPerPixel(mCamera.getParameters().getPreviewFormat());
        mBuffer = new byte[w * h * bitsPerPixel / 8];
        //Log.i("surfaceCreated", "buffer length is " + mBuffer.length + " bytes");
    }
}


