package com.hackensack.umc.util;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.AvoidXfermode;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.hackensack.umc.R;
import com.hackensack.umc.activity.BaseActivity;
import com.hackensack.umc.activity.HackensackUMCActivity;
import com.hackensack.umc.activity.ProxyAccessNavActivity;
import com.hackensack.umc.http.HttpDownloader;
import com.hackensack.umc.http.ServerConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


/**
 * Created by gaurav_salunkhe on 9/15/2015.
 */
public class Util {

    public static boolean isActivityFinished = false;
    public static boolean isRegistrationFlowFinished = false;
    private static Typeface arrowFont, fontSegoe;

    public static Bitmap getRoundedUserImage(Context context, int height, int width, /*Bitmap scaleBitmapImage, */ String decodedString) {

//        int targetWidth = (int) convertPixelsToDp(width, context);
//        int targetHeight = (int) convertPixelsToDp(height, context);

        int targetWidth = width;
        int targetHeight = height;

        byte[] imageAsBytes = Base64.decode(decodedString.getBytes(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);

        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);

        Bitmap sourceBitmap = bitmap;//scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);

        return targetBitmap;

    }

    public static Bitmap getCircularBitmapWithWhiteBorder( /*Bitmap bitmap,*/ int borderWidth, String decodedString) {

//        if (bitmap == null || bitmap.isRecycled()) {
//            return null;
//        }

        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString.getBytes(), 0, decodedString.length());

        final int width = bitmap.getWidth() + borderWidth;
        final int height = bitmap.getHeight() + borderWidth;

        Bitmap canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);

        Canvas canvas = new Canvas(canvasBitmap);
        float radius = (width > height ? ((float) height) / 2f : ((float) width) / 2f);
        canvas.drawCircle(width / 2, height / 2, radius, paint);
        paint.setShader(null);
        paint.setStyle(Paint.Style.STROKE);
//        paint.setColor(Color.BLUE);
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(borderWidth);
        canvas.drawCircle(width / 2, height / 2, radius - borderWidth / 2, paint);

        return canvasBitmap;
    }

    public static String getHASHCode(String message) {

        String hash = null;
        String hash_url = null;
        String hash_wrap = null;
        String hash_CRLF = null;
        String hash_close = null;
        String hash_padding = null;


        try {
//                String secret = "secret"; //PRIVATE_KEY
//                String message = "Message"; //PUBLIC_KEY + "|" + ADULT_URL + "|" + CURRENT_TIME_SECOND

//            HAK|json/1.0/en/topictable/adult|12345

            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(Constant.SYMPTOM_PRIVATE_KEY.getBytes("UTF-8"), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            byte[] byteCode = sha256_HMAC.doFinal(message.getBytes("ASCII"));
//            hash = URLEncoder.encode(new String(Base64.encode(doFinal, Base64.NO_WRAP)));

            hash = Base64.encodeToString(byteCode, Base64.DEFAULT);
            hash_url = Base64.encodeToString(byteCode, Base64.URL_SAFE);
            hash_wrap = Base64.encodeToString(byteCode, Base64.NO_WRAP);
            hash_CRLF = Base64.encodeToString(byteCode, Base64.CRLF);
            hash_close = Base64.encodeToString(byteCode, Base64.NO_CLOSE);
            hash_padding = Base64.encodeToString(byteCode, Base64.NO_PADDING);

            Log.e("HASH", "DEFAULT : " + hash + " URL_SAFE : " + hash_url + " NO_WRAP : " + hash_wrap + " CRLF : " + hash_CRLF + " NO_CLOSE : " + hash_close + " NO_PADDING : " + hash_padding);
        } catch (Exception e) {
            Log.e("Error in HASH", "" + e.toString());
        }

//        return hash;
        return Uri.encode(hash);
    }

//    public static Bitmap getCircularBitmapWithBorder(Bitmap bitmap,
//                                                     int borderWidth, int bordercolor) {
//        if (bitmap == null || bitmap.isRecycled()) {
//            return null;
//        }
//
//        final int width = bitmap.getWidth() + borderWidth;
//        final int height = bitmap.getHeight() + borderWidth;
//
//        Bitmap canvasBitmap = Bitmap.createBitmap(width, height,
//                Bitmap.Config.ARGB_8888);
//        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP,
//                Shader.TileMode.CLAMP);
//        Paint paint = new Paint();
//        paint.setAntiAlias(true);
//        paint.setShader(shader);
//
//        Canvas canvas = new Canvas(canvasBitmap);
//        float radius = width > height ? ((float) height) / 2f
//                : ((float) width) / 2f;
//        canvas.drawCircle(width / 2, height / 2, radius, paint);
//        paint.setShader(null);
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setColor(bordercolor);
//        paint.setStrokeWidth(borderWidth);
//        canvas.drawCircle(width / 2, height / 2, radius - borderWidth / 2,
//                paint);
//
//        return canvasBitmap;
//    }

//    public static void showDialog(Activity activity, String title, String message) {
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//            builder.setTitle(title);
//            builder.setMessage("Server connectivity problem");
//            builder.setPositiveButton("OK", null);
//            builder.setNegativeButton("Cancel", null);
//            builder.show();
//
//    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    public static Bitmap getRoundedCornerBitmap(Context context, Bitmap bitmap, float upperLeft,
                                                float upperRight, float lowerRight, float lowerLeft, int endWidth,
                                                int endHeight) {
        float densityMultiplier = context.getResources().getDisplayMetrics().density;

        // scale incoming bitmap to appropriate px size given arguments and display dpi
        bitmap = Bitmap.createScaledBitmap(bitmap,
                Math.round(endWidth * densityMultiplier),
                Math.round(endHeight * densityMultiplier), true);

        // create empty bitmap for drawing
        Bitmap output = Bitmap.createBitmap(
                Math.round(endWidth * densityMultiplier),
                Math.round(endHeight * densityMultiplier), Bitmap.Config.ARGB_8888);

        // get canvas for empty bitmap
        Canvas canvas = new Canvas(output);
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        // scale the rounded corners appropriately given dpi
        upperLeft *= densityMultiplier;
        upperRight *= densityMultiplier;
        lowerRight *= densityMultiplier;
        lowerLeft *= densityMultiplier;

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);

        // fill the canvas with transparency
        canvas.drawARGB(0, 0, 0, 0);

        // draw the rounded corners around the image rect. clockwise, starting in upper left.
        canvas.drawCircle(upperLeft, upperLeft, upperLeft, paint);
        canvas.drawCircle(width - upperRight, upperRight, upperRight, paint);
        canvas.drawCircle(width - lowerRight, height - lowerRight, lowerRight, paint);
        canvas.drawCircle(lowerLeft, height - lowerLeft, lowerLeft, paint);

        // fill in all the gaps between circles. clockwise, starting at top.
        RectF rectT = new RectF(upperLeft, 0, width - upperRight, height / 2);
        RectF rectR = new RectF(width / 2, upperRight, width, height - lowerRight);
        RectF rectB = new RectF(lowerLeft, height / 2, width - lowerRight, height);
        RectF rectL = new RectF(0, upperLeft, width / 2, height - lowerLeft);

        canvas.drawRect(rectT, paint);
        canvas.drawRect(rectR, paint);
        canvas.drawRect(rectB, paint);
        canvas.drawRect(rectL, paint);

        // set up the rect for the image
        Rect imageRect = new Rect(0, 0, width, height);

        // set up paint object such that it only paints on Color.WHITE
        paint.setXfermode(new AvoidXfermode(Color.WHITE, 255, AvoidXfermode.Mode.TARGET));

        // draw resized bitmap onto imageRect in canvas, using paint as configured above
        canvas.drawBitmap(bitmap, imageRect, imageRect, paint);

        return output;
    }


    public static boolean ifNetworkAvailableShowProgressDialog(Context context, String progressMessage, boolean showProgressDialogIfNetAvailable) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            if (showProgressDialogIfNetAvailable) {
                ((BaseActivity) context).startProgress(context, progressMessage);
            }
            return true;
        } else {
            showNetworkNotAvailableError(context);
            if (showProgressDialogIfNetAvailable) {
                ((BaseActivity) context).stopProgress();
            }
            return false;
        }


    }

    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        /**
         * if no network is available networkInfo will be null
         * otherwise check if we are connected
         *
         */
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {


            return false;
        }


    }

    static AlertDialog alert;

    public static void showNetworkOfflineDialog(Activity activity, String title, String message) {

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
                }
            });

            alert = builder.show();
        }

    }
    public static boolean hasImageCaptureBug() {

        // list of known devices that have the bug
        ArrayList<String> devices = new ArrayList<String>();
        devices.add("android-devphone1/dream_devphone/dream");
        devices.add("generic/sdk/generic");
        devices.add("vodafone/vfpioneer/sapphire");
        devices.add("tmobile/kila/dream");
        devices.add("verizon/voles/sholes");
        devices.add("google_ion/google_ion/sapphire");

        return devices.contains(android.os.Build.BRAND + "/" + android.os.Build.PRODUCT + "/"
                + android.os.Build.DEVICE);

    }

    public static void showProxyFormDialogue(final Activity activity) {

        if (!activity.isFinishing()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);

            LayoutInflater inflater = activity.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_network_offline, null);
            builder.setView(dialogView);

            ((TextView) dialogView.findViewById(R.id.dialog_title)).setText("Dependant Account not linked");

            ((TextView) dialogView.findViewById(R.id.text_message)).setText("You cannot see dependent data until you link your dependent account with the Proxy Access Form");

            Button btnGetForm = (Button) dialogView.findViewById(R.id.button_dialog_ok);
            btnGetForm.setText("Get Form");
            btnGetForm.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {
                    activity.startActivity(new Intent(activity, ProxyAccessNavActivity.class));
                    alert.dismiss();
                }
            });
            alert = builder.show();
            Button btnCancel = (Button) dialogView.findViewById(R.id.button_dialog_cancel);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });
        }
    }

    public static boolean isJsonDataAvailable(JSONObject obj, String key) {

        boolean dataAvailable = false;

        if (obj.has(key) && !obj.isNull(key)) {
            dataAvailable = true;
        }

        return dataAvailable;
    }

    public static String getJsonData(JSONObject obj, String key) {

        String dataAvailable = "";

        try {
            if (obj.has(key) && !obj.isNull(key)) {
                dataAvailable = obj.getString(key);
            }
        } catch (Exception e) {

        }

        return dataAvailable;
    }

    public static JSONArray getJsonArray(JSONObject obj, String key) {

        JSONArray dataAvailable = new JSONArray();

        try {
            if (obj.has(key) && !obj.isNull(key)) {
                dataAvailable = obj.getJSONArray(key);
            }
        } catch (Exception e) {

        }

        return dataAvailable;
    }

    public static JSONObject getJsonObject(JSONObject obj, String key) {

        JSONObject dataAvailable = new JSONObject();

        try {
            if (obj.has(key) && !obj.isNull(key)) {
                dataAvailable = obj.getJSONObject(key);
            }
        } catch (Exception e) {

        }

        return dataAvailable;
    }

    public static int getDeviceHeight(Context context) {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
//        int width = metrics.widthPixels;
        return metrics.heightPixels;

    }

    public static int getDeviceWidth(Context context) {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.widthPixels;

    }

    public static LatLng getLocationFromAddress(String strAddress, Context mContext) {

        Geocoder coder = new Geocoder(mContext, Locale.ENGLISH);
        List<Address> address = null;

        try {
            strAddress = strAddress.replaceAll("[\n\r]", "");
            String[] strarr = strAddress.split(",");
            strAddress = "";
            if (strarr.length >= 2) {
                for (int i = 0; i < strarr.length; i++) {
                    if (i == 0) {

                    } else {
                        strAddress = strAddress + strarr[i];
                    }
                }
            }
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
        } catch (IOException e) {
            Log.e("Util", "Location Service is not available", e);
        }

        Address addr = (address != null && address.size() > 0) ? address.get(0) : null;
        LatLng location = new LatLng(addr != null ? addr.getLatitude() : 0, addr != null ? addr.getLongitude() : 0);

        return location;

    }

    public static String validateTextInput(String inputStr) {
        String errorStr = "";
        if (TextUtils.isEmpty(inputStr)) {
            errorStr = "This field is Required";
        } else {
            if (!inputStr.matches("[a-zA-Z]+")) {
                errorStr = "invalid input";
            }
        }
        return errorStr;
    }

    public static Boolean validateTextInput1(String inputStr) {
        //[a-zA-Z]+
        if (!inputStr.matches("^[\\p{L} .'-]+$")) {
            return false;
        }

        return true;
    }

    public static void showAlert(Context context, String message, String title) {
//        new AlertDialog.Builder(context)
//                .setTitle(title)
//                .setMessage(message).setCancelable(false)
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                }).show();
        showNetworkOfflineDialog((Activity) context, title, message);
    }

    public static void showNetworkNotAvailableError(Context mContext) {
//        Util.showAlert((Activity) mContext,  mContext.getString(R.string.please_check_network),mContext.getString(R.string.network_connect_title));
        showNetworkOfflineDialog((Activity) mContext, "Network Error", "The internet connection appears to be offline.");
    }

    public static void setToken(Context context, String token, String refreshToken, int expireTime) {

        SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.TOKEN_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedpreferences.edit();
        edit.putString(Constant.TOKEN_SHARED_PREFERENCE_TOKEN_STRING, token);
        edit.putString(Constant.TOKEN_SHARED_PREFERENCE_REFRESH_TOKEN_STRING, refreshToken);
        edit.putLong(Constant.TOKEN_CURRENT_TIME_STRING, System.currentTimeMillis());
        edit.putInt(Constant.TOKEN_CURRENT_EXPIRE_TIME_STRING, expireTime);
//        edit.putInt(Constant.TOKEN_CURRENT_EXPIRE_TIME_STRING, 1200);
        edit.commit();

    }

    public static void setToken(Context context, String token, String refreshToken, int expireTime, int type) {

        SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.TOKEN_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedpreferences.edit();
        edit.putString(Constant.TOKEN_SHARED_PREFERENCE_TOKEN_STRING, token);
        edit.putString(Constant.TOKEN_SHARED_PREFERENCE_REFRESH_TOKEN_STRING, refreshToken);
        edit.putLong(Constant.TOKEN_CURRENT_TIME_STRING, System.currentTimeMillis());
        edit.putInt(Constant.TOKEN_CURRENT_EXPIRE_TIME_STRING, expireTime);
        edit.putInt(Constant.LOGIN_TYPE_STRING, type);
        edit.commit();

    }

    public static int getLoggedInType(Context context) {

        SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.TOKEN_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sharedpreferences.getInt(Constant.LOGIN_TYPE_STRING, Constant.UNKNOWN_LOGIN);

    }

    public static String getRefreshToken(Context context) {

        SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.TOKEN_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sharedpreferences.getString(Constant.TOKEN_SHARED_PREFERENCE_REFRESH_TOKEN_STRING, "");

    }

    public static long getRemainingTokenTime(Context context) {

        long remainingTime = 0;

        if (isUserLogin(context)) {

            SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.TOKEN_SHARED_PREFERENCE, Context.MODE_PRIVATE);

            try {

                long tokenTime = sharedpreferences.getLong(Constant.TOKEN_CURRENT_TIME_STRING, 0);
                remainingTime = ((tokenTime + 3300000)) - System.currentTimeMillis();
//                remainingTime = ((tokenTime + 900000)) - System.currentTimeMillis();


                Log.e("Login Session", "getRemainingTokenTime : tokenTime -> " + tokenTime + ", System.currentTimeMillis() -> " + System.currentTimeMillis() + ", remainingTime -> " + remainingTime);

                int timeInSecond = (int) remainingTime / 1000;
                Log.e("Login Session", "getRemainingTokenTime : timeInSecond -> " + timeInSecond);
                if (timeInSecond < sharedpreferences.getInt(Constant.TOKEN_CURRENT_EXPIRE_TIME_STRING, 0) && timeInSecond > 0) {
                    Log.e("Login Session", "getRemainingTokenTime : 'if' Condition sharedpreferences.getInt(Constant.TOKEN_CURRENT_EXPIRE_TIME_STRING, 0) -> " + sharedpreferences.getInt(Constant.TOKEN_CURRENT_EXPIRE_TIME_STRING, 0));
                    return remainingTime;
                }

            } catch (Exception e) {
                Log.e("Util", "getRemainingTokenTime -> " + e.toString());
            }
        }

        return 0;
    }

    public static String getToken(Context context) {

        String token = "";

        try {

            SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.TOKEN_SHARED_PREFERENCE, Context.MODE_PRIVATE);

            long tokenTime = sharedpreferences.getLong(Constant.TOKEN_CURRENT_TIME_STRING, 0);
            tokenTime = System.currentTimeMillis() - tokenTime;

            Log.e("Login Session", "getToken : Difference in time tokenTime -> " + tokenTime);

            if (System.currentTimeMillis() >= tokenTime) {

                int timeInSecond = (int) tokenTime / 1000;
                Log.e("Login Session", "getToken : timeInSecond -> " + timeInSecond + ", -> sharedpreferences.getInt(Constant.TOKEN_CURRENT_EXPIRE_TIME_STRING, 0) -> " + sharedpreferences.getInt(Constant.TOKEN_CURRENT_EXPIRE_TIME_STRING, 0));
                if (timeInSecond < sharedpreferences.getInt(Constant.TOKEN_CURRENT_EXPIRE_TIME_STRING, 0)) {
                    token = sharedpreferences.getString(Constant.TOKEN_SHARED_PREFERENCE_TOKEN_STRING, "");
                    Log.e("Login Session", "getToken : 'if' Condition -> Give Token : " + token);
                }
//                else {
//                    Log.e("Login Session", "getToken : 'Else' Condition -> Reset Token to Zero");
//                    setToken(context, "", "", 0);
//                    setPatientMRNID(context, "");
//                    setPatientJSON(context, "");
//                }

            }

        } catch (Exception e) {
            Log.e("Util", "getToken -> " + e.toString());
        }

        return token;
    }

//    public static void setUserLogin(Context context, boolean flag) {
//
//        SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.TOKEN_SHARED_PREFERENCE, Context.MODE_PRIVATE);
//        SharedPreferences.Editor edit = sharedpreferences.edit();
//        edit.putBoolean(Constant.KEY_USER_LOGIN, flag);
//        edit.commit();
//    }

    public static boolean isUserLogin(Context context) {

        boolean isLogin = false;

        String token = getToken(context);

        if (token != null && token.length() > 0 && getLoggedInType(context) >= Constant.GUEST_LOGIN) {
            isLogin = true;
        } else {
            Log.e("Login Session", "isUserLogin : User Logout :( ");
            Log.e("Login Session", "isUserLogin : User Logout :( " + getLoggedInType(context));
            if (getLoggedInType(context) >= Constant.GUEST_LOGIN) {
                BaseActivity.showSessionTimeOutPopUp = true;
            }
            setToken(context, "", "", 0);
            setToken(context, "", "", 0, Constant.UNKNOWN_LOGIN);
            setPatientMRNID(context, "");
            setPatientJSON(context, "");
            isLogin = false;
        }

        return isLogin;

    }

    public static boolean isPatientIdValid(Context context) {

        boolean valid = false;

        String id = getPatientMRNID(context);

        if (id != null && id.length() > 2) {

            valid = true;

        }

        return valid;
    }

    public static void setPatientMRNID(Context context, String token) {

        SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.PATIENT_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedpreferences.edit();
        edit.putString(Constant.PATIENT_MRN_ID_SHARED_PREFERENCE_STRING, token);
        edit.commit();

    }

    public static String getPatientMRNID(Context context) {

        String token = "";

        try {

            SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.PATIENT_SHARED_PREFERENCE, Context.MODE_PRIVATE);
            token = sharedpreferences.getString(Constant.PATIENT_MRN_ID_SHARED_PREFERENCE_STRING, "");

        } catch (Exception e) {
            Log.e("Util", "setPatientMRNID -> " + e.toString());
        }

        return token;
    }

    public static void setPatientJSON(Context context, String json) {

        SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.PATIENT_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedpreferences.edit();
        edit.putString(Constant.PATIENT_JSON_SHARED_PREFERENCE_STRING, json);
        edit.commit();

    }

    public static String getPatientJSON(Context context) {

        String data = "";

        if (isUserLogin(context) && Util.isPatientIdValid(context)) {

            try {

                SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.PATIENT_SHARED_PREFERENCE, Context.MODE_PRIVATE);
                data = sharedpreferences.getString(Constant.PATIENT_JSON_SHARED_PREFERENCE_STRING, "");

            } catch (Exception e) {
                Log.e("Util", "getPatientJSON -> " + e.toString());
            }

        }

        return data;
    }

    static ScheduledExecutorService mScheduledThreadPool;

    public static boolean startUserSession(final Context context) {

        boolean flag = false;

        long scheduletime = Util.getRemainingTokenTime(context);

        if (scheduletime > 0) {

            if (mScheduledThreadPool == null || mScheduledThreadPool.isShutdown()) {

                mScheduledThreadPool = Executors.newScheduledThreadPool(1);
                mScheduledThreadPool.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {

                        if (Util.isUserLogin(context)) {

                            if (BaseActivity.isDeviceSleep || BaseActivity.isAppWentToBg || Util.getLoggedInType(context) == Constant.GUEST_LOGIN) {

                                if (context instanceof HackensackUMCActivity) {
                                    ((HackensackUMCActivity) context).updateLogoutUI();
                                }

                                BaseActivity.isUserLogout = true;
                                stopUserSession(context);
                                if (getLoggedInType(context) >= Constant.GUEST_LOGIN) {
                                    BaseActivity.showSessionTimeOutPopUp = true;
                                }
                                setToken(context, "", "", 0);
                                setToken(context, "", "", 0, Constant.UNKNOWN_LOGIN);
                                setPatientMRNID(context, "");
                                setPatientJSON(context, "");

                                if (BaseActivity.isAppWentToBg || BaseActivity.isDeviceSleep) {
                                    //Code for Session Management
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                }

                            } else if (Util.getLoggedInType(context) == Constant.USER_LOGIN) {

                                Log.e("Login Session", "startUserSession - scheduleAtFixedRate : 'Else Condition' Refresh Token ");
                                HttpDownloader http = new HttpDownloader(context, ServerConstants.REFRESH_LOGIN_URL, Constant.LOGIN_REFRESH, null);
                                http.startDownloading();

                            }

                        } else if (!Util.isUserLogin(context)) {

                            if (context instanceof HackensackUMCActivity) {
                                ((HackensackUMCActivity) context).updateLogoutUI();
                            }

                            BaseActivity.isUserLogout = true;
                            stopUserSession(context);
                            if (getLoggedInType(context) >= Constant.GUEST_LOGIN) {
                                BaseActivity.showSessionTimeOutPopUp = true;
                            }
                            setToken(context, "", "", 0);
                            setToken(context, "", "", 0, Constant.UNKNOWN_LOGIN);
                            setPatientMRNID(context, "");
                            setPatientJSON(context, "");

                            if (BaseActivity.isAppWentToBg || BaseActivity.isDeviceSleep || Util.getLoggedInType(context) == Constant.GUEST_LOGIN) {
                                //Code for Session Management
                                android.os.Process.killProcess(android.os.Process.myPid());
                            }

                        }

                    }
                }, scheduletime, 3300000, TimeUnit.MILLISECONDS); //3300000
            }
            flag = true;
        }

        return flag;
    }


    public static void stopUserSession(Context context) {

        if (mScheduledThreadPool != null) {
            Log.e("Login Session", "User Session Stop");
            mScheduledThreadPool.shutdown();
            mScheduledThreadPool = null;
        }

    }

    public static Location getLocation(Context context) {
        boolean isGPSEnabled, isNetworkEnabled;
        Location location = null;
        LocationManager locationManager;
        try {
            locationManager = (LocationManager) context
                    .getSystemService(context.LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // location service disabled
            } else {


                // if GPS Enabled get lat/long using GPS Services

                if (isGPSEnabled) {
                    Log.d("GPS Enabled", "GPS Enabled");

                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                }

                // First get location from Network Provider
                if (isNetworkEnabled) {
                    if (location == null) {
                        Log.d("Network", "Network");

                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        }
                    }

                }
            }
        } catch (Exception e) {
            // e.printStackTrace();
            Log.e("test",
                    "Unable to connect to LocationManager", e);
        }

        return location;
    }

    public static double getDistanceInMiles(double startLatitude, double startLongitude, double endLatitude, double endLongitude) {
        double dist = 0.0;
        float[] results = new float[5];
        Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, results);
        //1 Mile = 1609.344 Meters
        if (results != null && results.length > 0)
            dist = results[0] / 1609.344;
        return dist;
    }

    //Method to show alert if the Location service is not ON
    public static void createLocationServiceError(final Context context) {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);

        builder.setMessage(
                "You need to activate location service to use this feature. Please turn on network or GPS mode in location settings")
                .setTitle("Alert")
                .setCancelable(false)
                .setPositiveButton("Settings",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                context.startActivity(intent);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
        builder.create().show();
    }


    public static void hide_keyboard_from(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void saveLocationIdinSharePreferances(Context context, String id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.SHAREPREF_TAG, context.MODE_PRIVATE);
        sharedPreferences.edit().putString(Constant.LOCATION_ID, id).commit();
    }

    public static void saveAddressinSharePreferances(Context context, String prefName, String address) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.SHAREPREF_TAG, context.MODE_PRIVATE);
        sharedPreferences.edit().putString(prefName, address).commit();
    }

    public static String getAddressinSharePreferances(Context context, String prefName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.SHAREPREF_TAG, context.MODE_PRIVATE);
        return sharedPreferences.getString(prefName, "");
    }

    public static String getLoactionIdFromSharePref(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.SHAREPREF_TAG, context.MODE_PRIVATE);
        return sharedPreferences.getString(Constant.LOCATION_ID, "");
    }

    public static void storehelpboolean(Context context, String prefName, boolean prefVal) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.SHAREPREF_TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefedit = sharedpreferences.edit();
        prefedit.putBoolean(prefName, prefVal);
        prefedit.commit();
    }

    public static boolean gethelpboolean(Context context, String prefName) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.SHAREPREF_TAG, Context.MODE_PRIVATE);
        return sharedpreferences.getBoolean(prefName, true);
    }

    public static Typeface getArrowFont(Context context) {
        if (arrowFont == null) {
            arrowFont = Typeface.createFromAsset(context.getAssets(), "font/ti-glyph.ttf");
        }
        return arrowFont;
    }

    public static Typeface getFontSegoe(Context context) {
        if (fontSegoe == null) {
            fontSegoe = Typeface.createFromAsset(context.getAssets(), "font/segoepr.ttf");
        }
        return fontSegoe;
    }

    public static void setAllHelpTrue(Context context) {
        storehelpboolean(context, Constant.HELP_PREF_HACKENSACK, true);
        storehelpboolean(context, Constant.HELP_PREF_APPIONTMENT_SUMMARY, true);
        storehelpboolean(context, Constant.HELP_PREF_DIRECTION, true);
        storehelpboolean(context, Constant.HELP_PREF_DOCTOR_RESULT, true);
        storehelpboolean(context, Constant.HELP_PREF_FAVORITE_DOCTOR, true);
        storehelpboolean(context, Constant.HELP_PREF_PROFILE, true);
        storehelpboolean(context, Constant.HELP_PREF_SYMPTOM_CHECKER, true);
        storehelpboolean(context, Constant.HELP_PREF_MAP_DIRECTION, true);
    }

    public static boolean removeHelpViewFromParent(Context context, int helpLayoutId) {
        boolean isHelpViewRemoved = false;
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) ((Activity) context).findViewById(android.R.id.content)).getChildAt(0);
        View v = viewGroup.getChildAt(1);
        if (v != null && v.getId() == helpLayoutId) {
            viewGroup.removeView(v);
            isHelpViewRemoved = true;
        }
        return isHelpViewRemoved;
    }

    public static boolean isApiVersionLesstHanLolipop() {
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isApiVersionEqualOrGreaterKitkat() {
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.KITKAT) {
            return true;
        } else {
            return false;
        }
    }

    public static void setTransactionId(String idResult, Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.SHAREPREF_TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefedit = sharedpreferences.edit();
        prefedit.putString(Constant.TRANSACTION_ID, idResult);
        prefedit.commit();
    }

    public static String getTransactionIdFromSharePref(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.SHAREPREF_TAG, context.MODE_PRIVATE);
        return sharedPreferences.getString(Constant.TRANSACTION_ID, "");
    }

    public static void setRegistrationIsFor(int regFor, Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.SHAREPREF_TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefedit = sharedpreferences.edit();
        prefedit.putInt(Constant.REGISTRATION_FOR, regFor);
        prefedit.commit();
    }

    public static int getRegistrationIsForFromSharePref(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.SHAREPREF_TAG, context.MODE_PRIVATE);
        return sharedPreferences.getInt(Constant.REGISTRATION_FOR, Constant.REGISTRATION_FOR_PARENTS);
    }

    public static Dialog crateProgressDialog(Context context, String message) {
        Dialog mProgressDialog;
        mProgressDialog = new Dialog(context, R.style.AppTheme_PopupOverlay);
        mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.semi_tranparent_white)));
        View mDialogView = ((Activity) context).getLayoutInflater().inflate(R.layout.humc_loading, null);
        ((TextView) mDialogView.findViewById(R.id.message_text)).setText(message);
        mProgressDialog.setContentView(mDialogView);
        return mProgressDialog;
    }

    public static void startMychartTokenSync(final Context context, long interval) {
        Log.i("bhagya", "--- Mychart refresh token interval " + interval);

        final AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        final Intent intent = new Intent(context, TokenExpiryReceiver.class);

        final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, 0);
        long pollingInterval;
        if (interval > 0) {
            pollingInterval = interval / 2;
        } else {
            pollingInterval = 900000;
        }
        mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                pollingInterval,
                pendingIntent);
    }

    public static void stopMychartTokenSync(final Context context) {

        final AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        final Intent intent = new Intent(context, TokenExpiryReceiver.class);

        final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, 0);

        mgr.cancel(pendingIntent);

    }

    public static void startDeviceSync(final Context context, long interval) {
        Log.i("bhagya", "--- Mychart Device token interval " + interval);

        final AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        final Intent intent = new Intent(context, DeviceTimedOutReceiver.class);

        final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, 0);

        final long pollingInterval = interval / 2;
        mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                pollingInterval,
                pendingIntent);
    }

    public static void stopDeviceSync(final Context context) {

        final AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        final Intent intent = new Intent(context, DeviceTimedOutReceiver.class);

        final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, 0);

        mgr.cancel(pendingIntent);


    }
    /** Returns the consumer friendly device name */
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }
    public static boolean isCropSupported(Context context,Intent intent){
        PackageManager packageManager=context.getPackageManager();
       if(intent.resolveActivity(packageManager)!=null){
          return  true;
          }else{
          return false;
          }
    }
}
