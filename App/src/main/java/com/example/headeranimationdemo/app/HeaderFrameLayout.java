package com.example.headeranimationdemo.app;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

public class HeaderFrameLayout extends FrameLayout {
    private static final String TAG = HeaderFrameLayout.class.getSimpleName();
    private int bottomOld = -1;

    public HeaderFrameLayout(Context context) {
        super(context);
    }

    public HeaderFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private String getMeasureSpecTypeString(int measureSpec) {
        switch (MeasureSpec.getMode(measureSpec)) {
            case MeasureSpec.AT_MOST:
                return "AT_MOST";
            case MeasureSpec.EXACTLY:
                return "EXACTLY";
            case MeasureSpec.UNSPECIFIED:
                return "UNSPECIFIED";
            default:
                return "DEFAULT???";
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //Log.i(TAG, "onMeasure() widthType=" + getMeasureSpecTypeString(widthMeasureSpec) + " width=" + MeasureSpec.getSize(widthMeasureSpec));
        //Log.i(TAG, "onMeasure() heightType=" + getMeasureSpecTypeString(heightMeasureSpec) + " height=" + MeasureSpec.getSize(heightMeasureSpec));

        // Fix for flicker, but will break WRAP_CONTENT for this layout
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            //Log.i(TAG, "OVERIDE!");
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (bottomOld == -1) {
            bottomOld = bottom;
        } else if (bottomOld < bottom) {
            Log.i(TAG, "onLayout() changed: " + changed + " left: " + left + " top: " + top + " right: " + right + " bottom: " + bottom);
        }

        bottomOld = bottom;
        super.onLayout(changed, left, top, right, bottom);
    }
}
