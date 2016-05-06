package com.hackensack.umc.camera;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.SurfaceView;

import com.hackensack.umc.R;
import com.hackensack.umc.cropper.Defaults;
import com.hackensack.umc.cropper.util.PaintUtil;

public class DrawView extends SurfaceView {

    public final float mCornerOffset;
    public final float mCornerExtension;
    private final float mCornerLength;
    private final Paint mCornerPaint;
    private final Paint mBorderPaint;
    private Paint textPaint = new Paint();

    private Context mContext;
    private int b;
    public int l;
    public int t;
    private int r;

    public Rect getRect() {
        return rect;
    }

    public Rect rect;


    public DrawView(Context context) {
        super(context);
        mContext = context;
        mCornerPaint = PaintUtil.newCornerPaint(context);
        // mCornerPaint.setStrokeWidth(10);


        mBorderPaint = PaintUtil.newBorderPaint(mContext);
        mBorderPaint.setStrokeWidth(5);
        mBorderPaint.setColor(mContext.getResources().getColor(R.color.black));

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        // Create out paint to use for drawing
        textPaint.setARGB(255, 200, 0, 0);
        textPaint.setTextSize(30);
        /* This call is necessary, or else the 
         * draw method will not be called. 
         */
        setWillNotDraw(false);

        mCornerOffset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                Defaults.DEFAULT_CORNER_OFFSET_DP,
                displayMetrics);
        mCornerExtension = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                Defaults.DEFAULT_CORNER_EXTENSION_DP,
                displayMetrics);
        mCornerLength = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                Defaults.DEFAULT_CORNER_LENGTH_DP,
                displayMetrics);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // A Simple Text Render to test the display

  /*     drawFocusRect(canvas, this.getLeft() + (this.getRight() - this.getLeft()) / 4,
                this.getTop() + (this.getBottom() - this.getTop()) / 4,
                this.getRight() - (this.getRight() - this.getLeft()) / 3,
                this.getBottom() - (this.getBottom() - this.getTop()) / 3, mBorderPaint.getColor());*/


        b = ((Activity) mContext).getWindowManager().getDefaultDisplay().getHeight()/2;
        l = this.getLeft()+ (int)com.hackensack.umc.util.Util.convertDpToPixel((float)20,mContext);
        t = this.getTop() + (int)com.hackensack.umc.util.Util.convertDpToPixel((float)70,mContext);
        r = this.getRight() - (int)com.hackensack.umc.util.Util.convertDpToPixel((float)20,mContext);

         rect=new Rect(l,t,r,b);
        /*drawFocusRect(canvas, l,
                t,
                r, b
                , mBorderPaint.getColor());*/
       // canvas.drawText(mContext.getString(R.string.tap_to_dismiss),((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth()/2, ((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth(), textPaint);
        /*float w = mBorderPaint.getStrokeWidth();
        float l = Edge.LEFT.getCoordinate() + w / 2;
        float t = Edge.TOP.getCoordinate() + w / 2;
        float r = Edge.RIGHT.getCoordinate() - w / 2;*/
        //  float b = Edge.BOTTOM.getCoordinate() - w / 2;
        // Draw rectangle crop window border.
        //drawFocusRect(canvas, l, t, r, b, mBorderPaint.getColor());
        drawCorners(canvas);
    }

    private void drawCorners(Canvas canvas) {

        /*float w = mBorderPaint.getStrokeWidth() * 1.5f + 1;
        float l = Edge.LEFT.getCoordinate() + w;
        float t = Edge.TOP.getCoordinate() + w;
        float r = Edge.RIGHT.getCoordinate() - w;
        float b = Edge.BOTTOM.getCoordinate() - w;*/
        // mBorderPaint.setStrokeWidth(10);

        // Top left

        canvas.drawLine(l - mCornerOffset, t - mCornerExtension, l - mCornerOffset, t + mCornerLength, mCornerPaint);
        canvas.drawLine(l, t - mCornerOffset, l + mCornerLength, t - mCornerOffset, mCornerPaint);
//for border
        /*canvas.drawLine(l - mCornerOffset, t - mCornerExtension, l - mCornerOffset, t + mCornerLength, mBorderPaint);
        canvas.drawLine(l, t - mCornerOffset, l + mCornerLength, t - mCornerOffset, mBorderPaint);*/

        // Top right
        canvas.drawLine(r + mCornerOffset, t - mCornerExtension, r + mCornerOffset, t + mCornerLength, mCornerPaint);
        canvas.drawLine(r, t - mCornerOffset, r - mCornerLength, t - mCornerOffset, mCornerPaint);

        // Bottom left
        canvas.drawLine(l - mCornerOffset, b + mCornerExtension, l - mCornerOffset, b - mCornerLength, mCornerPaint);
        canvas.drawLine(l, b + mCornerOffset, l + mCornerLength, b + mCornerOffset, mCornerPaint);

        // Bottom left
        canvas.drawLine(r + mCornerOffset, b + mCornerExtension, r + mCornerOffset, b - mCornerLength, mCornerPaint);
        canvas.drawLine(r, b + mCornerOffset, r - mCornerLength, b + mCornerOffset, mCornerPaint);
    }

    private void drawFocusRect(Canvas canvas, float RectLeft, float RectTop, float RectRight, float RectBottom, int color) {

        //  Canvas canvas = surfaceHolder.lockCanvas();
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        //border's properties
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        paint.setStrokeWidth(3);
        canvas.drawRect(RectLeft, RectTop, RectRight, RectBottom, paint);
    }

}
