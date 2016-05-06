package com.hackensack.umc.fragment;

import android.app.Activity;

import android.content.Intent;
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
import com.hackensack.umc.activity.RegistrationDetailsActivity;
import com.hackensack.umc.camera.CameraTestActivity;
import com.hackensack.umc.util.CameraFunctionality;
import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.Util;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileInsuranceInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileInsuranceInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class ProfileIdInfoFragment extends Fragment implements View.OnClickListener, ProfileInsuranceInfoFragment.OnFragmentInteractionListener {
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
    private int selectedImageView;
    private boolean idInsuranceFargmentAddeed;
    private Uri pathDlFront;
    private Uri pathDlBack;
    private Button btnManul;
    private TextView txtIdFrontMessage;
    private TextView txtIdBackMessage;
    private Uri imageUri;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileInsuranceInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileInsuranceInfoFragment newInstance(String param1, String param2) {
        ProfileInsuranceInfoFragment fragment = new ProfileInsuranceInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfileIdInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

        btnManul.setOnClickListener(this);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onImagesSelected(Uri uri, int selectedImageView) {
        if (mListener != null) {
            mListener.onIdFragmentInteraction(uri, selectedImageView);
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
        mListener = null;
    }

    @Override
    public void onInsuranceFragmentInteraction(Uri uri, int selectedImageView) {

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
        public void onIdFragmentInteraction(Uri uri, int selectedImageView);

        public void onProceedButtonClicked();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.proceed_btn_id:
                // if(!idInsuranceFargmentAddeed) {
                if ((pathDlFront != null && pathDlBack == null) || (pathDlBack != null && pathDlFront == null)) {
                    Util.showAlert(getActivity(), "Please attach Id proof for front and back.", "Alert");
                } else {
                    mListener.onProceedButtonClicked();
                }

                //}
                break;

            case R.id.dl_front:
                selectedImageView = Constant.ID_PROOF_FRONT;
                imageUri=dispatchTakePictureIntent(0);

                //  dispatchGalleryPictureIntent();

                break;
            case R.id.dl_back:
                selectedImageView = Constant.ID_PROOF_BACK;
                imageUri=dispatchTakePictureIntent(0);
             /*  pathIcBack = dispatchTakePictureIntent(0);
                Log.v(TAG, pathIcBack);*/
                //dispatchGalleryPictureIntent();

                break;
            case R.id.manual_btn:
                Intent intent = new Intent(getActivity(), RegistrationDetailsActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constant.REGISTRATION_MODE, Constant.MANUAL);
                intent.putExtra(Constant.BUNDLE, b);
                startActivity(intent);
                break;
        }
    }

 /*   @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       // Log.v("data", data.toString());
        switch (requestCode) {
            case Constant.FRAGMENT_CONST_REQUEST_IMAGE_CAPTURE:
                if (resultCode == getActivity().RESULT_OK) {
                    try {
                        if (data != null) {
                            //imageUri = data.getData();
                            if (imageUri == null) {
                                *//*Bitmap bimapImage= (Bitmap) data.getExtras().get("data");
                                imageUri=getImageUri(getActivity(),bimapImage);*//*

                            }
                            Log.v("onActivityResult", imageUri.toString());
                           *//* if (selectedImageView == Constant.ID_PROOF_FRONT) {
                               *//**//* onImagesSelected(imageUri, selectedImageView);
                                pathDlFront = imageUri;
                                CameraFunctionality.setPhotoToImageView(imageUri.toString(), imgIcFront, getActivity());
                                txtIdFrontMessage.setText(R.string.id_front_image_set_message);*//**//*
                                *//**//*Intent intent=new Intent(getActivity(), ActivityCropImage.class);
                                intent.setData(imageUri);
                                startActivityForResult(intent, Constant.CROP_IMAGE_ACTIVITY);*//**//*


                            } else if (selectedImageView == Constant.ID_PROOF_BACK) {
                               *//**//* CameraFunctionality.setPhotoToImageView(imageUri.toString(), imgIcBack, getActivity());
                                onImagesSelected(imageUri, selectedImageView);
                                pathDlBack = imageUri;
                                txtIdBackMessage.setText(R.string.id_back_image_set_message);*//**//*
                            }*//*

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(getActivity(), ActivityCropImage.class);
                    intent.setData(imageUri);
                    startActivityForResult(intent, Constant.CROP_IMAGE_ACTIVITY);
                }
                break;
            case Constant.CROP_IMAGE_ACTIVITY:
                if (resultCode == getActivity().RESULT_OK) {
                    try {
                        if (data != null) {
                            Uri imageUri = data.getData();
                            Log.v("onActivityResult", imageUri.toString());
                            if (selectedImageView == Constant.ID_PROOF_FRONT) {
                                onImagesSelected(imageUri, selectedImageView);
                                pathDlFront = imageUri;
                                CameraFunctionality.setPhotoToImageView(imageUri.toString(), imgIcFront, getActivity());
                                txtIdFrontMessage.setText(R.string.id_front_image_set_message);
                            } else if (selectedImageView == Constant.ID_PROOF_BACK) {
                                CameraFunctionality.setPhotoToImageView(imageUri.toString(), imgIcBack, getActivity());
                                onImagesSelected(imageUri, selectedImageView);
                                pathDlBack = imageUri;
                                txtIdBackMessage.setText(R.string.id_back_image_set_message);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constant.FRAGMENT_CONST_REQUEST_IMAGE_CAPTURE:
                if (resultCode == getActivity().RESULT_OK) {
                    try {
                        if (data != null) {
                            Uri imageUri = data.getData();
                            Log.v("onActivityResult",imageUri.toString());
                            if (selectedImageView == Constant.ID_PROOF_FRONT) {
                                onImagesSelected(imageUri, selectedImageView);
                                pathDlFront = imageUri;
                                CameraFunctionality.setPhotoToImageView(imageUri.toString(), imgIcFront, getActivity());
                                txtIdFrontMessage.setText(R.string.id_front_image_set_message);
                            } else if (selectedImageView == Constant.ID_PROOF_BACK) {
                                CameraFunctionality.setPhotoToImageView(imageUri.toString(), imgIcBack, getActivity());
                                onImagesSelected(imageUri, selectedImageView);
                                pathDlBack = imageUri;
                                txtIdBackMessage.setText(R.string.id_back_image_set_message);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

        }
    }
  /*  public void dispatchTakePictureIntent(int cameraFacing) {
       *//* Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraFacing == 1) {
            intent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_FRONT);
        } else {
            intent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_BACK);
        }

       startActivityForResult(intent, Constant.FRAGMENT_CONST_REQUEST_IMAGE_CAPTURE);*//*
        Intent intent=new Intent(getActivity(), CameraTestActivity.class);
       startActivityForResult(intent, Constant.FRAGMENT_CONST_REQUEST_IMAGE_CAPTURE);

    }*/


    public Uri dispatchTakePictureIntent(int cameraFacing) {
        Uri imageUri = null;

        Intent intent=new Intent(getActivity(), CameraTestActivity.class);

        File photo;
        try {
            // place where to store camera taken picture
            photo = CameraFunctionality.createTemporaryFile("picture_" + ProfileSelfieActivity.selectedImageView, ".png", getActivity());
            //photo.delete();
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


    /*public Uri dispatchTakePictureIntent(int cameraFacing) {
        Uri imageUri = null;

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo;
        try {
            // place where to store camera taken picture
            photo = CameraFunctionality.createTemporaryFile("picture", ".jpg");
            photo.delete();
        } catch (Exception e) {
            Log.v(TAG, "Can't create file to take picture!");
            //Toast.makeText(getActivity(), "Please check SD card! Image shot is impossible!", 10000);
            return imageUri;
        }
        imageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        //start camera intent
        startActivityForResult(intent, Constant.FRAGMENT_CONST_REQUEST_IMAGE_CAPTURE);
        return imageUri;
    }*/


}
