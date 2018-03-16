package tw.com.mygis.fireapp_taoyuan;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


public class WritePadDialog extends Dialog {

    Context context;
    LayoutParams p ;
    DialogListener dialogListener;
    LinearLayout ll_button;
    FrameLayout tablet_view;

    public int screenWidth,screenHeight,tabletWidth,tabletHeight;

    public WritePadDialog(Context context, int screenWidth, int screenHeight, DialogListener dialogListener) {
        super(context);
        this.context = context;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.dialogListener = dialogListener;
    }

    public interface DialogListener {

        public void refreshActivity(Object object);

    }

    static final int BACKGROUND_COLOR = Color.WHITE;

    static final int BRUSH_COLOR = Color.BLACK;

    PaintView mView;

    /** The index of the current color to use. */
    int mColorIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.write_pad);

        setCanceledOnTouchOutside(false);//區域外不消失

        p = getWindow().getAttributes();
        p.height = screenHeight / 2;
        p.width = screenWidth / 2;
        getWindow().setAttributes(p);



        tablet_view = (FrameLayout) findViewById(R.id.tablet_view);
        ViewTreeObserver vto = tablet_view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tablet_view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                tabletWidth = tablet_view.getWidth();
                tabletHeight = tablet_view.getHeight();
                mView = new PaintView(context);
                tablet_view.addView(mView);
                mView.requestFocus();
            }
        });

        ll_button = (LinearLayout) findViewById(R.id.ll_button);

        Button btnClear = (Button) findViewById(R.id.tablet_clear);
        btnClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mView.clear();
            }
        });

        Button btnOk = (Button) findViewById(R.id.tablet_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialogListener.refreshActivity(mView.getCachebBitmap());
                    WritePadDialog.this.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button btnCancel = (Button)findViewById(R.id.tablet_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }
    //private Bitmap comp(Bitmap image) {
    //    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    //    image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    //    ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
    //    BitmapFactory.Options newOpts = new BitmapFactory.Options();
    //    //开始读入图片，此时把options.inJustDecodeBounds 设回true了
    //    newOpts.inJustDecodeBounds = true;
    //    Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
    //    newOpts.inJustDecodeBounds = false;
    //    newOpts.inSampleSize = 3;//设置缩放比例
    //    //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
    //    isBm = new ByteArrayInputStream(baos.toByteArray());
    //    bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
    //    return bitmap;//压缩好比例大小后再进行质量压缩
    //}
    /**
     * This view implements the drawing canvas.
     *
     * It handles all of the input events and drawing functions.
     */
    class PaintView extends View {
        private Paint paint;
        private Canvas cacheCanvas;
        private Bitmap cachebBitmap;
        private Path path;

        public Bitmap getCachebBitmap() {
            return cachebBitmap;
        }

        public PaintView(Context context) {
            super(context);
            init();
        }

        private void init(){
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(15);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            path = new Path();
            cachebBitmap = Bitmap.createBitmap(tabletWidth, tabletHeight, Config.ARGB_8888);
            cacheCanvas = new Canvas(cachebBitmap);
            cacheCanvas.drawColor(Color.WHITE);
        }

        public void clear() {
            if (cacheCanvas != null) {

                paint.setColor(BACKGROUND_COLOR);
                cacheCanvas.drawPaint(paint);
                paint.setColor(Color.BLACK);
                cacheCanvas.drawColor(Color.WHITE);
                invalidate();
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            // canvas.drawColor(BRUSH_COLOR);
            canvas.drawBitmap(cachebBitmap, 0, 0, null);
            canvas.drawPath(path, paint);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {

            int curW = cachebBitmap != null ? cachebBitmap.getWidth() : 0;
            int curH = cachebBitmap != null ? cachebBitmap.getHeight() : 0;
            if (curW >= w && curH >= h) {
                return;
            }

            if (curW < w)
                curW = w;
            if (curH < h)
                curH = h;

            Bitmap newBitmap = Bitmap.createBitmap(curW, curH, Config.ARGB_8888);
            Canvas newCanvas = new Canvas();
            newCanvas.setBitmap(newBitmap);
            if (cachebBitmap != null) {
                newCanvas.drawBitmap(cachebBitmap, 0, 0, null);
            }
            cachebBitmap = newBitmap;
            cacheCanvas = newCanvas;
        }

        private float cur_x, cur_y;

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    cur_x = x;
                    cur_y = y;
                    path.moveTo(cur_x, cur_y);
                    break;
                }

                case MotionEvent.ACTION_MOVE: {
                    path.quadTo(cur_x, cur_y, x, y);
                    cur_x = x;
                    cur_y = y;
                    break;
                }

                case MotionEvent.ACTION_UP: {
                    cacheCanvas.drawPath(path, paint);
                    path.reset();
                    break;
                }
            }

            invalidate();

            return true;
        }
    }

}
