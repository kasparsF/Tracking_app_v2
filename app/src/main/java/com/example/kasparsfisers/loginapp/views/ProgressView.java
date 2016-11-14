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



public class ProgressView extends View {

    private static final int STROKE_WIDTH = 20;

    private String mTitle = "";
    private float mProgress = 0;

    private int mStrokeWidth = STROKE_WIDTH;

    private final RectF mCircleBounds = new RectF();

    private final Paint mProgressColorPaint = new Paint();
    private final Paint mBackgroundColorPaint = new Paint();
    private final Paint mTitlePaint = new Paint();
    private int mShadowColor = Color.BLACK;


    public ProgressView(Context context) {
        super(context);
        init(null, 0);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int style)
    {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);


        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.ProgressView, style, 0);

        String color;
        int size;
        try {

            color = a.getString(R.styleable.ProgressView_ColorOfProgress);
            if(color==null)
                mProgressColorPaint.setColor(Color.BLUE);
            else
                mProgressColorPaint.setColor(Color.parseColor(color));

            color = a.getString(R.styleable.ProgressView_ColorOfCircle);
            if(color==null)
                mBackgroundColorPaint.setColor(Color.BLUE);
            else
                mBackgroundColorPaint.setColor(Color.parseColor(color));

            color = a.getString(R.styleable.ProgressView_MiddleTextColor);
            if(color==null)
                mTitlePaint.setColor(Color.BLUE);
            else
                mTitlePaint.setColor(Color.parseColor(color));

            size = a.getInt(R.styleable.ProgressView_MiddleTextSize, 60);
            mTitlePaint.setTextSize(size);



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

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawArc(mCircleBounds, 0, 360 , false, mBackgroundColorPaint);


       mProgressColorPaint.setShadowLayer(	3, 0, 1, mShadowColor);
        canvas.drawArc(mCircleBounds, 0, mProgress , false, mProgressColorPaint);


        if(!TextUtils.isEmpty(mTitle)){
            int xPos =  (int)(getMeasuredHeight()/2 - mTitlePaint.measureText(mTitle) / 2);
            int yPos = (int) (getMeasuredHeight()/2);


            canvas.drawText(mTitle, xPos, yPos, mTitlePaint);


        }
        canvas.drawText("Hello", getMeasuredWidth() - mTitlePaint.measureText("Hello"),100 , mTitlePaint);
    }


    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int min = Math.min(width, height);
        setMeasuredDimension(min+2*STROKE_WIDTH+(int)mTitlePaint.measureText("Hello"), min+2*STROKE_WIDTH);

        mCircleBounds.set(STROKE_WIDTH, STROKE_WIDTH, min+STROKE_WIDTH, min+STROKE_WIDTH);
    }


    public void setProgress(float progress){
        mProgress = progress * 3.6F;
        invalidate();
    }

    public synchronized void setTitleColor(int color){
        mTitlePaint.setColor(color);
        invalidate();
    }

    public synchronized void setTitle(String title){
        this.mTitle = title+"%";
        invalidate();
    }
}
