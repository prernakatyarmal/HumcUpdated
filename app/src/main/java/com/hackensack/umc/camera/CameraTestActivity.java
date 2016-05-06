package com.hackensack.umc.camera;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hackensack.umc.R;
import com.hackensack.umc.util.Constant;

public class CameraTestActivity extends Activity implements View.OnClickListener, CameraPreview.OnGetCroppedImageCompleteListener {
    // Our variables
    private CameraPreview cv;
    private DrawView dv;
    private FrameLayout alParent;
    private int rotation;
    private Button captureImage;
    private ProgressBar progressBarCamera;
    private String base64path;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Set the screen orientation to landscape, because 
         * the camera preview will be in landscape, and if we 
         * don't do this, then we will get a streached image.*/
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // requesting to turn the title OFF
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // making it full screen
        getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN,
                LayoutParams.FLAG_FULLSCREEN);

    }

    public void Load() {
        // Try to get the camera
        Camera c = getCameraInstance();

        // If the camera was received, create the app
        if (c != null) {
            /* Create our layout in order to layer the
        	 * draw view on top of the camera preview. 
        	 */
            alParent = (FrameLayout) getLayoutInflater().inflate(R.layout.camera_view, null);
            // alParent = new FrameLayout(this);
         /*  alParent.setLayoutParams(new FrameLayout.LayoutParams(
            		LayoutParams.FILL_PARENT,
            		LayoutParams.FILL_PARENT));*/

            RelativeLayout rl = (RelativeLayout) alParent.findViewById(R.id.camerapreview);
            progressBarCamera=(ProgressBar)alParent.findViewById(R.id.progressBarCamera);

            // Create a new camera view and add it to the layout
            cv = new CameraPreview(this, c);
            rl.addView(cv);


            // Create a new draw view and add it to the layout
            dv = new DrawView(this);
            rl.addView(dv);

            // Set the layout as the apps content view 
            setContentView(alParent);
            captureImage = (Button) alParent.findViewById(R.id.takepicture);
            captureImage.setOnClickListener(this);

         
        }
        // If the camera was not received, close the app
        else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Unable to find camera. Closing.", Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }
    }
    
    /* This method is strait for the Android API */

    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance() {
        Camera c = null;

        try {
            c = Camera.open();// attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            e.printStackTrace();
        }
        return c; // returns null if camera is unavailable
    }

    /* Override the onPause method so that we 
     * can release the camera when the app is closing.
     */
    @Override
    protected void onPause() {
        super.onPause();

        if (cv != null) {
            cv.onPause();
            cv = null;
        }
    }

    /* We call Load in our Resume method, because 
     * the app will close if we call it in onCreate
     */
    @Override
    protected void onResume() {
        super.onResume();

        Load();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.takepicture:
                progressBarCamera.setVisibility(View.VISIBLE);
                cv.takeImage(dv);
                break;

            default:
                break;
        }
    }

    @Override
    public void onGetCroppedImageComplete(Bitmap bitmap) {
        base64path=cv.saveCropedImage(bitmap);
        progressBarCamera.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri imageUri = data.getData();
            Intent intent = getIntent();
            intent.setData(imageUri);
            intent.putExtra(Constant.IMAGE_FILE_PATH,intent.getStringExtra(Constant.IMAGE_FILE_PATH));
            setResult(RESULT_OK, intent);
            finish();

        }
    }
}