package com.hackensack.umc.fraggment_temp;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hackensack.umc.R;
import com.hackensack.umc.activity.ProfileSelfieActivity;
import com.hackensack.umc.util.Base64Converter;
import com.hackensack.umc.util.CameraFunctionality;
import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.Util;

import java.io.File;


public class ProfileIdInfoFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "ProfileIdFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Button btnProceed;
    private ImageView imgIcFront;
    private ImageView imgIcBack;
    private boolean idInsuranceFargmentAddeed;
    private Uri pathDlFront;
    private Uri pathDlBack;
    private Button btnManul;
    private TextView txtIdFrontMessage;
    private TextView txtIdBackMessage;
    private Uri imageUri;
    private TextView txtFront;
    private TextView txtBack;


    public ProfileIdInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageUri = getArguments().getParcelable(Constant.CAPTURE_IMAGE_URI);
        }
    }
    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constant.CAPTURE_IMAGE_URI, imageUri);
        Log.v(TAG, "InonSaveInstanceState" + outState.toString());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_id_info, container, false);
        inflateView(view);
        return view;
    }

    private void inflateView(View view) {
        btnProceed = (Button) view.findViewById(R.id.proceed_btn_id);
        btnProceed.setOnClickListener(this);
        imgIcFront = (ImageView) view.findViewById(R.id.dl_front);
        imgIcBack = (ImageView) view.findViewById(R.id.dl_back);
        imgIcFront.setOnClickListener(this);
        imgIcBack.setOnClickListener(this);
        btnManul = ((Button) view.findViewById(R.id.manual_btn));
        txtIdFrontMessage = (TextView) view.findViewById(R.id.txtIdFrontMessage);
        txtIdBackMessage = (TextView) view.findViewById(R.id.txtIdBackMessage);
        txtFront=(TextView)view.findViewById(R.id.txtFront_dl);
        txtBack=(TextView)view.findViewById(R.id.txtBack_dl);
        btnManul.setOnClickListener(this);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onImagesSelected(Uri uri, int selectedImageView, Bitmap croppedImage, String base64FilePath) {
        if (mListener != null) {
            mListener.onIdFragmentInteraction(uri, selectedImageView, croppedImage, base64FilePath);
        }
    }
    public void setCroppedImageBitMap(Bitmap bitMap,int selectedImageView) {
        if (mListener != null) {
            mListener.getCroppedImageBitmapForProfile(bitMap, selectedImageView);
        }
    }
    public void onProceedButtonClicked() {
        mListener.onProceedButtonClicked();
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

//        if(pathDlFront!=null){
//            CameraFunctionality.deleteImageAfterUploading(pathDlFront.toString(), getActivity());
//        }
//        if(pathDlBack!=null){
//            CameraFunctionality.deleteImageAfterUploading(pathDlBack.toString(), getActivity());
//        }


        mListener = null;
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
         void onIdFragmentInteraction(Uri uri, int selectedImageView, Bitmap croppedImage, String base64FilePath);

         void onProceedButtonClicked();

         void getCroppedImageBitmapForProfile(Bitmap bitmap, int selectedImageView);

        void onManualRegistrationButtonClicked();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.proceed_btn_id:
                // if(!idInsuranceFargmentAddeed) {
                if ((pathDlFront ==null|| pathDlBack == null)) {
                    Util.showAlert(getActivity(), "Please attach Id proof for front and back.", "Alert");
                } else {
                    mListener.onProceedButtonClicked();
                }

                //}
                break;

            case R.id.dl_front:
                ProfileSelfieActivity.selectedImageView = Constant.ID_PROOF_FRONT;
                imageUri=dispatchTakePictureIntent(0);
                onImagesSelected(imageUri,   ProfileSelfieActivity.selectedImageView, null, null);
                //mListener.setSelectedimageview(selectedImageView);

                //  dispatchGalleryPictureIntent();

                break;
            case R.id.dl_back:
                ProfileSelfieActivity.selectedImageView = Constant.ID_PROOF_BACK;
                imageUri=dispatchTakePictureIntent(0);
                onImagesSelected(imageUri,   ProfileSelfieActivity.selectedImageView, null, null);
               // mListener.setSelectedimageview(selectedImageView);
             /*  pathIcBack = dispatchTakePictureIntent(0);
                Log.v(TAG, pathIcBack);*/
                //dispatchGalleryPictureIntent();

                break;
            case R.id.manual_btn:
                /*Intent intent = new Intent(getActivity(), RegistrationDetailsActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constant.REGISTRATION_MODE, Constant.MANUAL);
                intent.putExtra(Constant.BUNDLE, b);
                startActivity(intent);*/
                mListener.onManualRegistrationButtonClicked();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       // Log.v("data", data.toString());
        super.onActivityResult(requestCode, resultCode, data);
        /*if(data!=null){
            selectedImageView=data.getIntExtra(Constant.SELECTED_IMAGE_VIEW,1);
        }*/

        switch (requestCode) {
            case Constant.FRAGMENT_CONST_REQUEST_IMAGE_CAPTURE:
                if (resultCode == getActivity().RESULT_OK) {
                    try {
                        if (data != null) {
                            //imageUri = data.getData();
                            if (imageUri == null) {
                                /*Bitmap bimapImage= (Bitmap) data.getExtras().get("data");
                                imageUri=getImageUri(getActivity(),bimapImage);*/

                            }
                            Log.v("onActivityResult", imageUri.toString());
                           /* if (selectedImageView == Constant.ID_PROOF_FRONT) {
                               *//* onImagesSelected(imageUri, selectedImageView);
                                pathDlFront = imageUri;
                                CameraFunctionality.setPhotoToImageView(imageUri.toString(), imgIcFront, getActivity());
                                txtIdFrontMessage.setText(R.string.id_front_image_set_message);*//*
                                *//*Intent intent=new Intent(getActivity(), ActivityCropImage.class);
                                intent.setData(imageUri);
                                startActivityForResult(intent, Constant.CROP_IMAGE_ACTIVITY);*//*


                            } else if (selectedImageView == Constant.ID_PROOF_BACK) {
                               *//* CameraFunctionality.setPhotoToImageView(imageUri.toString(), imgIcBack, getActivity());
                                onImagesSelected(imageUri, selectedImageView);
                                pathDlBack = imageUri;
                                txtIdBackMessage.setText(R.string.id_back_image_set_message);*//*
                            }*/

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                   /* Intent intent = new Intent(getActivity(), ActivityCropImage.class);
                    intent.setData(imageUri);
                    intent.putExtra(Constant.SELECTED_IMAGE_VIEW, selectedImageView);
                    startActivityForResult(intent, Constant.CROP_IMAGE_ACTIVITY);*/
                   /* if (Util.isApiVersionEqualOrGreaterKitkat()) {
                        if(Util.getDeviceName().startsWith("HTC")||Util.getDeviceName().startsWith("MI")){
                            setImageToImageViewAfterCrop(imageUri);
                        }else{
                            cropPictureIntent(imageUri);
                        }

                    } else {
                        setImageToImageViewAfterCrop(imageUri);
                    }
*/
                    Intent cropIntent = CameraFunctionality.createCameCropInatent(imageUri);
                    if (Util.isCropSupported(getActivity(),cropIntent)) {
                        Log.v(TAG,"isCropSupported::");
                        cropPictureIntent(imageUri);
                    } else {
                        Log.v(TAG,"isCropSupportedNot::");
                        setImageToImageViewAfterCrop(imageUri);
                    }

                }
                break;
            case Constant.CROP_IMAGE_ACTIVITY:
                if(  ProfileSelfieActivity.selectedImageView == Constant.ID_PROOF_FRONT||  ProfileSelfieActivity.selectedImageView == Constant.ID_PROOF_BACK) {
                    if (resultCode == getActivity().RESULT_OK) {
                        if (data != null) {
                            imageUri = getImageUriFromIntent(data);
                            setImageToImageViewAfterCrop(imageUri);
                        } else {
                            setImageToImageViewAfterCrop(imageUri);
                        }
                    }
                }
                break;
            /*default:
                if (resultCode == getActivity().RESULT_OK) {
                    if (imageUri != null) {
                        setImageToImageViewAfterCrop(imageUri);
                    }
                }*/
        }
    }
    /**
     * alternate method to take photo from camera
     * @return Uri of pic to be saved at
     */
    protected Uri getPhotoFromCamera() {
        ContentValues values = new ContentValues();
        values.put(
                MediaStore.Images.Media.TITLE,
                "image_"+ProfileSelfieActivity.selectedImageView
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
                Log.v(TAG,"hasImageCaptureBug");
                photo = CameraFunctionality.createTemporaryFile("picture_" + ProfileSelfieActivity.selectedImageView, ".png", getActivity());
                imageUri = Uri.fromFile(photo);

                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
                //photo.delete();
            } catch (Exception e) {
                return imageUri;
            }

        } else {
            Log.v(TAG,"NohasImageCaptureBug");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        }
        intent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_BACK);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, Constant.FRAGMENT_CONST_REQUEST_IMAGE_CAPTURE);
        return imageUri;
    }


    private Uri getImageUriFromIntent(Intent data) {
        Uri imageUriFromIntent=null;
        try {
            if (data != null) {
                imageUriFromIntent = data.getData();
                // String base64FilePath=data.getStringExtra(Constant.BASE64_FILE_PATH);*/
                if (imageUriFromIntent == null) {
                    imageUriFromIntent = data.getExtras().getParcelable("data");
                }
                if(imageUriFromIntent==null){
                    imageUriFromIntent=imageUri;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return imageUri;
        }

        return imageUriFromIntent;
    }

    /**
     * set cropped imageTo imageview and form base64 of the image.
     * @param imageUri
     */
    private void setImageToImageViewAfterCrop(Uri imageUri) {
        try {

            if (imageUri.toString().startsWith("content://")) {

                imageUri = Uri.parse(CameraFunctionality.getRealPathFromURI(imageUri, ProfileIdInfoFragment.this.getActivity()));
            }
            Bitmap bitmapImage;
            bitmapImage = CameraFunctionality.getBitmapFromFile(imageUri.toString(), getActivity());
            String base64 = Base64Converter.createBase64StringFromBitmap(bitmapImage, getActivity());
            if (  ProfileSelfieActivity.selectedImageView == Constant.ID_PROOF_FRONT) {
                CameraFunctionality.setBitMapToImageView(imageUri.toString(), bitmapImage, imgIcFront, getActivity());
                onImagesSelected(imageUri,   ProfileSelfieActivity.selectedImageView, null, base64);
                pathDlFront = imageUri;
                txtIdFrontMessage.setText(R.string.id_front_image_set_message);
                txtFront.setVisibility(View.GONE);

                //   setCroppedImageBitMap(bitmapImage,selectedImageView);
            } else if (  ProfileSelfieActivity.selectedImageView == Constant.ID_PROOF_BACK) {
                //CameraFunctionality.setPhotoToImageView(imageUri.toString(), imgIcBack, getActivity());

                CameraFunctionality.setBitMapToImageView(imageUri.toString(), bitmapImage, imgIcBack, getActivity());
                onImagesSelected(imageUri,   ProfileSelfieActivity.selectedImageView, null, base64);
                pathDlBack = imageUri;
                txtIdBackMessage.setText(R.string.id_back_image_set_message);
                setCroppedImageBitMap(bitmapImage,   ProfileSelfieActivity.selectedImageView);
                txtBack.setVisibility(View.GONE);
            }
        }catch (Exception e){

        }
    }

    public Uri dispatchTakePictureIntent(int cameraFacing) {
        Uri imageUri = null;

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo;
        try {
            // place where to store camera taken picture
            photo = CameraFunctionality.createTemporaryFile("picture_"+  ProfileSelfieActivity.selectedImageView, ".png",getActivity());
            //photo.delete();
        } catch (Exception e) {
            Log.v(TAG, "Can't create file to take picture!");
            //Toast.makeText(getActivity(), "Please check SD card! Image shot is impossible!", 10000);
            return imageUri;
        }
        imageUri = Uri.fromFile(photo);
        intent.putExtra("android.hardware.camera.back", Camera.CameraInfo.CAMERA_FACING_BACK);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        //start camera intent
        startActivityForResult(intent, Constant.FRAGMENT_CONST_REQUEST_IMAGE_CAPTURE);
        return imageUri;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void cropPictureIntent(Uri picUri) {
        try {

            Intent cropIntent = CameraFunctionality.createCameCropInatent(picUri);
            cropIntent.putExtra(Constant.SELECTED_IMAGE_VIEW,   ProfileSelfieActivity.selectedImageView);
            Fragment fragment = this;
            fragment.startActivityForResult(cropIntent, Constant.CROP_IMAGE_ACTIVITY);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message

           // Log.v(TAG,"imageUri in crop exeption"+imageUri);
            if(imageUri!=null) {
                setImageToImageViewAfterCrop(imageUri);
            }
            /*String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();*/



        }
    }

}
