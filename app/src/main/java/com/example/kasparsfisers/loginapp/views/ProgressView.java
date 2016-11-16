package com.example.kasparsfisers.loginapp.views;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.example.kasparsfisers.loginapp.R;
import com.example.kasparsfisers.loginapp.utils.SharedPreferencesUtils;


public class ProgressView extends View {

    private static final int STROKE_WIDTH = 30;

    private String mTitle = "";
    private String maxTitle = "Max: ";
    private float mProgress = 0;
    private static float ONE_PERCENT = 3.6F;
    private int mStrokeWidth = STROKE_WIDTH;
    SharedPreferencesUtils preferences;
    private final RectF mCircleBounds = new RectF();

    private final Paint mProgressColorPaint = new Paint();
    private final Paint mBackgroundColorPaint = new Paint();
    private final Paint mTitlePaint = new Paint();
    private final Paint mMaxPaint = new Paint();
    private int mShadowColor = Color.BLACK;




    public ProgressView(Context context) {
        super(context);
        init(null, 0, context);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0, context);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int style, Context context) {
        preferences = SharedPreferencesUtils.getInstance(context);
        maxTitle+= preferences.getMaxLoc();

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);


        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.ProgressView, style, 0);

        String color;
        int size;
        try {

            color = a.getString(R.styleable.ProgressView_ColorOfProgress);
            if (color == null)
                mProgressColorPaint.setColor(Color.BLUE);
            else
                setColorOfProgres(preferences.getColorOfProgress());

            color = a.getString(R.styleable.ProgressView_ColorOfCircle);
            if (color == null)
                mBackgroundColorPaint.setColor(Color.BLUE);
            else

              setColorOfCircle(preferences.getColorOfCircle());

            color = a.getString(R.styleable.ProgressView_MiddleTextColor);
            if (color == null)
                mTitlePaint.setColor(Color.BLUE);
            else
               setTitleColor(preferences.getTextColor());

            size = a.getInt(R.styleable.ProgressView_MiddleTextSize, preferences.getTitleSize());
            setTitleSize(size);

            size = a.getInt(R.styleable.ProgressView_MiddleTextSize, preferences.getMaxTxtSize());
            setMaxTxtSize(size);


        } finally {
            a.recycle();
        }


        mProgressColorPaint.setAntiAlias(true);
        mProgressColorPaint.setStyle(Paint.Style.STROKE);
        mProgressColorPaint.setStrokeWidth(mStrokeWidth);

        mBackgroundColorPaint.setAntiAlias(true);
        mBackgroundColorPaint.setStyle(Paint.Style.STROKE);
        mBackgroundColorPaint.setStrokeWidth(mStrokeWidth);


        mTitlePaint.setStyle(Paint.Style.FILL);
        mTitlePaint.setAntiAlias(true);
        mTitlePaint.setTypeface(Typeface.create("Roboto-Thin", Typeface.NORMAL));
        mTitlePaint.setShadowLayer(0.1f, 0, 1, Color.GRAY);

        mMaxPaint.setStyle(Paint.Style.FILL);
        mMaxPaint.setAntiAlias(true);
        mMaxPaint.setTypeface(Typeface.create("Roboto-Thin", Typeface.NORMAL));
        mMaxPaint.setShadowLayer(0.1f, 0, 1, Color.GRAY);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawArc(mCircleBounds, 0, 360, false, mBackgroundColorPaint);


        mProgressColorPaint.setShadowLayer(3, 0, 1, mShadowColor);
        canvas.drawArc(mCircleBounds, 0, mProgress, false, mProgressColorPaint);


        if (!TextUtils.isEmpty(mTitle)) {
            int xPos = (int) (getMeasuredHeight() / 2 - mTitlePaint.measureText(mTitle) / 2);
            int yPos = (int) (getMeasuredHeight() / 2);


            canvas.drawText(mTitle, xPos, yPos, mTitlePaint);


        }
        canvas.drawText(maxTitle, getMeasuredWidth() - mMaxPaint.measureText(maxTitle), 100, mMaxPaint);
    }


    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int min = Math.min(width, height);
        setMeasuredDimension(min + 2 * STROKE_WIDTH + (int) mMaxPaint.measureText(maxTitle), min + 2 * STROKE_WIDTH);

        mCircleBounds.set(STROKE_WIDTH, STROKE_WIDTH, min + STROKE_WIDTH, min + STROKE_WIDTH);
    }


    public void setProgress(float progress) {
        mProgress = progress * ONE_PERCENT;
        invalidate();
    }

    //For custom attributes
    public void setRadius(int radius) {
        mTitlePaint.setColor(radius);
        invalidate();
    }

    public void setTitleSize(int size) {
        mTitlePaint.setTextSize(size);
        invalidate();
    }

    public  void setTitleColor(String color) {
        mTitlePaint.setColor(Color.parseColor(color));
        mMaxPaint.setColor(Color.parseColor(color));
        invalidate();
    }

    public void setMaxTxtSize(int size) {
        mMaxPaint.setTextSize(size);
        invalidate();
    }

    public void setColorOfCircle(String color) {
        mBackgroundColorPaint.setColor(Color.parseColor(color));
        invalidate();
    }

    public void setColorOfProgres(String color) {
        mProgressColorPaint.setColor(Color.parseColor(color));
        invalidate();
    }

    public synchronized void setTitle(String title) {
        this.mTitle = title + "%";
        invalidate();
    }

    public void refresh() {
        invalidate();
    }
}
