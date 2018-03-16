package tw.com.mygis.fireapp_taoyuan;

/**
 * Created by ken on 2018/2/11.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class GraphView extends View {

    private Bitmap mBitmap;
    private Paint mPaint = new Paint();
    private Paint mPaint2 = new Paint();
    private Canvas mCanvas = new Canvas();


    private float mSpeed = 0.5f;  //更改顯示速度(寬窄)，數字越小顯示越密;最小設1.0f。
    private float mLastX;
    private float mScale;
    private float mLastValue,mLastValue2,mLastValue3,mLastValue4,mLastValue5,mLastValue6;

    private float mYOffset;
    private int mColor,mColor2;
    private float mWidth;
    private float maxValue = 1024f;

    public GraphView(Context context) {
        super(context);
        init();
    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mColor = Color.argb(192, 128, 128, 128); //定義顏色ARGB
        mColor2 = Color.argb(80, 128, 0, 0); //定義顏色ARGB
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaint2.setFlags(Paint.ANTI_ALIAS_FLAG);
    }

    public void addDataPoint(float value,float value2,float value3,float value4,float value5,float value6) {
        final Paint paint = mPaint;
        final Paint paint2 = mPaint2;
        float newX = mLastX + mSpeed;
        final float v1 = mYOffset + value * mScale;
        final float v2 = mYOffset + value2 * mScale;
        final float v3 = mYOffset + value3 * mScale;
        final float v4 = mYOffset + value4 * mScale;
        final float v5 = mYOffset + value5 * mScale;
        final float v6 = mYOffset + value6 * mScale;


        paint.setColor(mColor);
        paint2.setColor(mColor2);

        mCanvas.drawLine(mLastX, mLastValue, newX, v1, paint);
        mCanvas.drawLine(mLastX, mLastValue2, newX, v2, paint);
        mCanvas.drawLine(mLastX, mLastValue3, newX, v3, paint);
        mCanvas.drawLine(mLastX, mLastValue4, newX, v4, paint);
        mCanvas.drawLine(mLastX, mLastValue5, newX, v5, paint);
        mCanvas.drawLine(mLastX, mLastValue6, newX, v6, paint);


        mCanvas.drawLine(mLastX, 200, newX, 200, paint2);
        mLastValue = v1;
        mLastValue2=v2;
        mLastValue3=v3;
        mLastValue4=v4;
        mLastValue5=v5;
        mLastValue6=v6;

        mLastX += mSpeed;

        invalidate();
    }

    public void setMaxValue(int max) {
        maxValue = max;
        mScale = -(mYOffset * (1.0f / maxValue));
    }

    public void setSpeed(float speed) {
        mSpeed = speed;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mBitmap = Bitmap.createBitmap(w*5, h, Bitmap.Config.RGB_565);
        mCanvas.setBitmap(mBitmap);

        mCanvas.drawColor(0xFFFFFFFF);

        mYOffset = h;
        mScale = -(mYOffset * (1.0f / maxValue));
        mWidth = w*5;
        mLastX = mWidth;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        synchronized (this) {
            if (mBitmap != null) {

                if (mLastX >= mWidth) {
                    mLastX = 0;
                    final Canvas cavas = mCanvas;
                    cavas.drawColor(0xFFFFFFFF);
                    mPaint.setColor(0xFF777777);
                    cavas.drawLine(0, mYOffset, mWidth, mYOffset, mPaint);
                }
                canvas.drawBitmap(mBitmap, 0, 0, null);

            }


        }
    }

    public Bitmap getmBitmap(){
        Bitmap bitmap=mBitmap;
        return bitmap;
    }


}