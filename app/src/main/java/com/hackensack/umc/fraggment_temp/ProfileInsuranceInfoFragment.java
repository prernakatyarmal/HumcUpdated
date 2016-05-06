package com.hackensack.umc.fraggment_temp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hackensack.umc.R;
import com.hackensack.umc.activity.ProfileSelfieActivity;
import com.hackensack.umc.camera.CameraTestActivity;
import com.hackensack.umc.custom_camera.Custom_CameraActivity;
import com.hackensack.umc.util.Base64Converter;
import com.hackensack.umc.util.CameraFunctionality;
import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.Util;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileInsuranceInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileInsuranceInfoFragment extends Fragment implements View.OnClickListener, ProfileIdInfoFragment.OnFragmentInteractionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "ProfileInsuranceInfoFragment";
    private static boolean isLandScapeDialogShown = false;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Button btnProceed;
    private ImageView imgIcFront;
    private ImageView imgIcBack;
    //public static int selectedImageView;
    private boolean idInsuranceFargmentAddeed;
    private Uri pathIcFront;
    private Uri pathIcBack;
    private Button btnManul;
    private TextView txtFontInsuCardMessage;
    private TextView txtbackInsuCardMessage;
    private Uri imageUri;
    private Intent cameraIntent;
    private TextView txtFront;
    private TextView txtBack;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileInsuranceInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public ProfileInsuranceInfoFragment newInstance(String param1, String param2) {
        ProfileInsuranceInfoFragment fragment = new ProfileInsuranceInfoFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constant.CAPTURE_IMAGE_URI, imageUri);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

       /* outState.putParcelable(Constant.CAPTURE_IMAGE_URI, imageUri);
        outState.putInt(Constant.SELECTED_IMAGE_VIEW, ProfileSelfieActivity.selectedImageView);

        Log.v(TAG, "InonSaveInstanceState" + outState.toString());*/
    }

    public ProfileInsuranceInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if(!isLandScapeDialogShown) {
        showLandScapeAlert(getActivity(), getString(R.string.suggestion), getString(R.string.landscape_note));
//        }

    }

    static AlertDialog alert;

    public static void showLandScapeAlert(Activity activity, String title, String message) {

        if (!activity.isFinishing()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);

            LayoutInflater inflater = activity.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_network_offline, null);
            builder.setView(dialogView);

            if (title.length() > 0)
                ((TextView) dialogView.findViewById(R.id.dialog_title)).setText(title);
            else
                (dialogView.findViewById(R.id.dialog_title)).setVisibility(View.GONE);

            ((TextView) dialogView.findViewById(R.id.text_message)).setText(message);

            Button btnDismiss = (Button) dialogView.findViewById(R.id.button_dialog_ok);
            btnDismiss.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {
                    alert.dismiss();
                    isLandScapeDialogShown = true;
                }
            });

            alert = builder.show();
        }

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {

          /*  Log.v(TAG, "In onViewStateRestored:");
            imageUri = savedInstanceState.getParcelable(Constant.CAPTURE_IMAGE_URI);
            ProfileSelfieActivity.selectedImageView=savedInstanceState.getInt(Constant.SELECTED_IMAGE_VIEW);
            Log.v(TAG, "onViewStateRestored:" + imageUri);*/
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_insurance_info, container, false);
        inflateView(view);
        return view;
    }

    private void inflateView(View view) {
        btnProceed = (Button) view.findViewById(R.id.proceed_btn_insu);
        btnProceed.setOnClickListener(this);
        imgIcFront = (ImageView) view.findViewById(R.id.ic_front);
        imgIcBack = (ImageView) view.findViewById(R.id.ic_back);
        imgIcFront.setOnClickListener(this);
        imgIcBack.setOnClickListener(this);
        btnManul = ((Button) view.findViewById(R.id.manual_btn));
        txtFontInsuCardMessage = (TextView) view.findViewById(R.id.txtInsFrontMessage);
        txtbackInsuCardMessage = (TextView) view.findViewById(R.id.txtInsBackMessage);
        txtFront = (TextView) view.findViewById(R.id.txtFront_ins);
        txtBack = (TextView) view.findViewById(R.id.txtBack_ins);

        btnManul.setOnClickListener(this);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onImagesSelected(Uri uri, int selectedImageView, Bitmap bitmapImage, String base64Path) {
        if (mListener != null) {
            mListener.onInsuranceFragmentInteraction(uri, selectedImageView, bitmapImage, base64Path);
        }
    }

    public void setCroppedImageBitMap(Bitmap bitMap, int selectedImageView) {
        if (mListener != null) {
            mListener.getCroppedImageBitmapForIsurance(bitMap, selectedImageView);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

//        if(pathIcFront!=null){
//            CameraFunctionality.deleteImage(pathIcFront.toString(), getActivity()) ;
//        }
//        if(pathIcBack!=null){
//            CameraFunctionality.deleteImage(pathIcBack.toString(),getActivity());
//        }

        mListener = null;
    }

    @Override
    public void onIdFragmentInteraction(Uri uri, int selectedImageView, Bitmap croppedImage, String base64FilePath) {

    }

    @Override
    public void onProceedButtonClicked() {

    }

    @Override
    public void getCroppedImageBitmapForProfile(Bitmap bitmap, int selectedImageView) {

    }

    @Override
    public void onManualRegistrationButtonClicked() {

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onInsuranceFragmentInteraction(Uri uri, int selectedImageView, Bitmap croppedBitmapImage, String base64Path);

        void getCroppedImageBitmapForIsurance(Bitmap bitmap, int selectedImageView);

        void getSelectedImageView(int selectedImageView);

        void onManualRegistrationClickedFromInsuranceFragment();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.proceed_btn_insu:
                if ((pathIcFront != null && pathIcBack == null) || (pathIcBack != null && pathIcFront == null)) {
                    Util.showAlert(getActivity(), "Please attach Insurance proof for front and back.", "Alert");
                } else {
                    ProfileIdInfoFragment profileIdInfoFragment = new ProfileIdInfoFragment();
                    profileIdInfoFragment.setTargetFragment(this, Constant.FRAGMENT_CODE);
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentIdContainer, profileIdInfoFragment, "IdFragment");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                break;

            case R.id.ic_front:
               /* StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
                long bytesAvailable = (long) stat.getBlockSize() * (long) stat.getAvailableBlocks();
                long megAvailable = bytesAvailable / (1024 * 1024);
                Log.e(TAG, "Available MB : " + megAvailable);*/
                ProfileSelfieActivity.selectedImageView = Constant.INSU_PROOF_FRONT;
                int selectedImageView = ProfileSelfieActivity.selectedImageView;
                imageUri = dispatchTakePictureIntent(1);
                onImagesSelected(imageUri, selectedImageView, null, null);

                break;
            case R.id.ic_back:
                ProfileSelfieActivity.selectedImageView = Constant.INSU_PROOF_BACK;
                int selectedImageView1 = ProfileSelfieActivity.selectedImageView;
                imageUri = startCustomCam();
                onImagesSelected(imageUri, selectedImageView1, null, null);

                break;
            case R.id.manual_btn:
               /* Intent intent = new Intent(getActivity(), RegistrationDetailsActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constant.REGISTRATION_MODE, Constant.MANUAL);
                intent.putExtra(Constant.BUNDLE, b);
                startActivity(intent);*/
                mListener.onManualRegistrationClickedFromInsuranceFragment();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if(data!=null){
            selectedImageView=data.getIntExtra(Constant.SELECTED_IMAGE_VIEW,1);
        }
*/
        if (ProfileSelfieActivity.selectedImageView == Constant.ID_PROOF_FRONT || ProfileSelfieActivity.selectedImageView == Constant.ID_PROOF_BACK) {
            Fragment fragment = getChildFragmentManager().findFragmentByTag("IdFragment");
            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data);
                return;
            }
        } else {
            switch (requestCode) {
                case Constant.FRAGMENT_CONST_REQUEST_IMAGE_CAPTURE:
                    if (resultCode == getActivity().RESULT_OK) {
                    /*Intent intent = new Intent(getActivity(), ActivityCropImage.class);
                    intent.setData(imageUri);


                   // intent.putExtra(Constant.Cropp_InterFace,  saveCroppedImage);
                    intent.putExtra(Constant.SELECTED_IMAGE_VIEW, selectedImageView);
                    startActivityForResult(intent, Constant.CROP_IMAGE_ACTIVITY);*/
                        // if(data==null){

                        //  }
                        // else{
                 /*   if (imageUri == null) {
                        imageUri=getImageFromCamera();
                    }*/
                        //imageUri=Util.getSelectedImageUriPath(getActivity());
                        //doCrop(imageUri);
                        Intent cropIntent = CameraFunctionality.createCameCropInatent(imageUri);
                        if (Util.isCropSupported(getActivity(), cropIntent)) {
                            Log.v(TAG, "isCropSupported::");
                            cropPictureIntent(imageUri);
                        } else {
                            Log.v(TAG, "isCropSupportedNot::");
                            setImageToImageViewAfterCrop(imageUri);
                        }
                        //setImageToImageViewAfterCrop(imageUri);


                        // }
                    }
                    break;
                case Constant.CROP_IMAGE_ACTIVITY:
                    //imageUri=Util.getSelectedImageUriPath(getActivity());
                    //selectedImageView=Util.getSelectedImageView(getActivity());
                    if (ProfileSelfieActivity.selectedImageView == Constant.INSU_PROOF_FRONT || ProfileSelfieActivity.selectedImageView == Constant.INSU_PROOF_BACK) {
                        if (resultCode == getActivity().RESULT_OK) {
                            if (data != null) {
                                imageUri = getImageUriFromIntent(data);

                                setImageToImageViewAfterCrop(imageUri);
                            } else {
                                setImageToImageViewAfterCrop(imageUri);
                            }
                        }
                        break;
                    }
                case Constant.FRAGMENT_CONST_REQUEST_IMAGE_CAPTURE_CUSTOM:
                    imageUri = Uri.parse(data.getExtras().getString(Constant.CAPTURE_IMAGE_URI));
                    setImageToImageViewAfterCrop(imageUri);
                    break;

                /*default:
                    if (resultCode == getActivity().RESULT_OK)
                    if(imageUri!=null) {
                        setImageToImageViewAfterCrop(imageUri);
                    }

                    break;*/

            }
        }

    }
    public Uri dispatchTakePictureIntent(int cameraFacing) {
        Uri imageUri = null;

        Intent intent=new Intent(getActivity(), CameraTestActivity.class);

        File photo;
        try {
            // place where to store camera taken picture
            photo = CameraFunctionality.createTemporaryFile("picture_" + ProfileSelfieActivity.selectedImageView, ".png", getActivity());
            // photo.delete();
        } catch (Exception e) {
            //Toast.makeText(getActivity(), "Please check SD card! Image shot is impossible!", 10000);
            return imageUri;
        }
        imageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        //start camera intent
        startActivityForResult(intent, Constant.FRAGMENT_CONST_REQUEST_IMAGE_CAPTURE);
        return imageUri;
    }
    private Uri startCustomCam() {
        Intent intent = new Intent(getActivity(), Custom_CameraActivity.class);
        File photo;
        try {
            photo = CameraFunctionality.createTemporaryFile("picture_" + ProfileSelfieActivity.selectedImageView, ".png", getActivity());
            //photo.delete();
        } catch (Exception e) {
            return imageUri;
        }
        imageUri = Uri.fromFile(photo);
        intent.putExtra(Constant.CAPTURE_IMAGE_URI, imageUri.toString());
        startActivity(intent);
        return imageUri;

    }

    private Uri getImageUriFromIntent(Intent data) {
        Uri imageUriFromIntent = null;
        try {
            if (data != null) {
                imageUriFromIntent = data.getData();
                // String base64FilePath=data.getStringExtra(Constant.BASE64_FILE_PATH);*/
                if (imageUriFromIntent == null) {
                    imageUriFromIntent = data.getExtras().getParcelable("data");
                }

                if (imageUriFromIntent == null) {
                    imageUriFromIntent = imageUri;
                }
                /*else{
                    CameraFunctionality.deleteImage(imageUri.getPath(),ProfileInsuranceInfoFragment.this.getActivity() );
                }*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return imageUriFromIntent;
    }

    /**
     * set cropped imageTo imageview and form base64 of the image.
     *
     * @param imageUri
     */
    private void setImageToImageViewAfterCrop(Uri imageUri) {
        if (imageUri.toString().startsWith("content://")) {

            imageUri = Uri.parse(CameraFunctionality.getRealPathFromURI(imageUri, ProfileInsuranceInfoFragment.this.getActivity()));
        }
        try {
            Bitmap bitmapImage = CameraFunctionality.getBitmapFromFile(imageUri.toString(), getActivity());
            String base64 = Base64Converter.createBase64StringFromBitmap(bitmapImage, getActivity());
            if (ProfileSelfieActivity.selectedImageView == Constant.INSU_PROOF_FRONT) {
                onImagesSelected(imageUri, ProfileSelfieActivity.selectedImageView, bitmapImage, base64);
                CameraFunctionality.setBitMapToImageView(imageUri.toString(), bitmapImage, imgIcFront, getActivity());
                pathIcFront = imageUri;
                txtFontInsuCardMessage.setText(R.string.ic_front_image_set_message);
                txtFront.setVisibility(View.GONE);
            } else if (ProfileSelfieActivity.selectedImageView == Constant.INSU_PROOF_BACK) {//selectedImageView=4
                CameraFunctionality.setBitMapToImageView(imageUri.toString(), bitmapImage, imgIcBack, getActivity());
                onImagesSelected(imageUri, ProfileSelfieActivity.selectedImageView, null, base64);
                pathIcBack = imageUri;
                txtbackInsuCardMessage.setText(R.string.ic_back_image_set_message);
                txtBack.setVisibility(View.GONE);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Uri dispatchTakePictureIntent() {
        Uri imageUri = null;
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo;
        try {
            photo = CameraFunctionality.createTemporaryFile("picture_" + ProfileSelfieActivity.selectedImageView, ".png", getActivity());
            //photo.delete();
        } catch (Exception e) {
            return imageUri;
        }
        imageUri = Uri.fromFile(photo);
        // Log.v(TAG, "dispatchTakePictureIntent:" + imageUri.toString());
        intent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_BACK);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        //start camera intent
        startActivityForResult(intent, Constant.FRAGMENT_CONST_REQUEST_IMAGE_CAPTURE);
        return imageUri;
        //  return getPhotoFromCamera();
    }


    protected Uri getPhotoFromCamera() {
        ContentValues values = new ContentValues();
        values.put(
                MediaStore.Images.Media.TITLE,
                "image_" + ProfileSelfieActivity.selectedImageView
                        + ".png");
        try {

            imageUri = getActivity().getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } catch (Exception e) {
            try {
                imageUri = getActivity().getContentResolver().insert(
                        MediaStore.Images.Media.INTERNAL_CONTENT_URI, values);
            } catch (Exception e2) {
                Util.showAlert(getActivity(), getString(R.string.app_name),
                        "no memory card");
            }
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (Util.hasImageCaptureBug()) {
            File photo;
            try {
                Log.v(TAG, "hasImageCaptureBug");
                photo = CameraFunctionality.createTemporaryFile("picture_" + ProfileSelfieActivity.selectedImageView, ".png", getActivity());
                imageUri = Uri.fromFile(photo);

                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);// File://
                //photo.delete();
            } catch (Exception e) {
                return imageUri;
            }

        } else {
            Log.v(TAG, "NohasImageCaptureBug");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);// Content://
        }
        intent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_BACK);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, Constant.FRAGMENT_CONST_REQUEST_IMAGE_CAPTURE);
        return imageUri;
    }

    private void cropPictureIntent(Uri picUri) {
        try {

            Intent cropIntent = CameraFunctionality.createCameCropInatent(picUri);
           // cropIntent.putExtra(Constant.SELECTED_IMAGE_VIEW, ProfileSelfieActivity.selectedImageView);
            Fragment fragment = this;
            fragment.startActivityForResult(cropIntent, Constant.CROP_IMAGE_ACTIVITY);
        }
        // respond to users whose devices do not support the crop action
        catch (Exception anfe) {
            // display an error message
            if (imageUri != null) {
                setImageToImageViewAfterCrop(imageUri);
            }
          /*  String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();*/


        }
    }


}
