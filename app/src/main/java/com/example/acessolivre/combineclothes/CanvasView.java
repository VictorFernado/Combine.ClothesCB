package com.example.acessolivre.combineclothes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Projeto on 07/10/2017.
 */

public class CanvasView extends View {

    private Paint mRectPaint;

    private int mStartX = 0;
    private int mStartY = 0;
    private int mEndX = 0;
    private int mEndY = 0;
    private boolean mDrawRect = false;
    private TextPaint mTextPaint = null;
    private static final String DEBUGTAG = "JWP";
    private TelaCamera telaCamera;
    private Bitmap mCurrentPhotoPath;


    private OnUpCallback mCallback = null;

    public interface OnUpCallback {
        void onRectFinished(Rect rect);
    }

    public CanvasView(final Context context) {
        super(context);
        init();
    }

    public CanvasView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CanvasView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * Sets callback for up
     *
     * @param callback {@link OnUpCallback}
     */
    public void setOnUpCallback(OnUpCallback callback) {
        mCallback = callback;
    }

    /**
     * Inits internal data
     */
    private void init() {
        mRectPaint = new Paint();
        mRectPaint.setColor(getContext().getResources().getColor(android.R.color.holo_green_light));
        mRectPaint.setStyle(Paint.Style.STROKE);
        mRectPaint.setStrokeWidth(5); // TODO: should take from resources

        mTextPaint = new TextPaint();
        mTextPaint.setColor(getContext().getResources().getColor(android.R.color.holo_green_light));
        mTextPaint.setTextSize(20);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {

        //final TextView corPred = (TextView) findViewById(R.id.cordenates);


        // TODO: be aware of multi-touches
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDrawRect = false;
                mStartX = (int) event.getX();
                mStartY = (int) event.getY();
                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                final int x = (int) event.getX();
                final int y = (int) event.getY();

                if (!mDrawRect || Math.abs(x - mEndX) > 5 || Math.abs(y - mEndY) > 5) {
                    mEndX = x;
                    mEndY = y;
                    invalidate();
                }

                mDrawRect = true;
                break;

            case MotionEvent.ACTION_UP:
                if (mCallback != null) {
                    mCallback.onRectFinished(new Rect(Math.min(mStartX, mEndX), Math.min(mStartY, mEndY),
                            Math.max(mEndX, mStartX), Math.max(mEndY, mStartX)));


                }
                invalidate();
                break;

            default:
                break;
        }

        return true;
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        if (mDrawRect) {
            canvas.drawRect(Math.min(mStartX, mEndX), Math.min(mStartY, mEndY),
                    Math.max(mEndX, mStartX), Math.max(mEndY, mStartY), mRectPaint);

            canvas.drawText("  (" + Math.abs(mStartX - mEndX) + ", " + Math.abs(mStartY - mEndY) + ")",
                    Math.max(mEndX, mStartX), Math.max(mEndY, mStartY), mTextPaint);
        }
    }

    public static int[] getDominantColor(Bitmap bitmap, int mStartX, int mStartY, int mEndX, int mEndY) {

        if (bitmap == null)
            throw new NullPointerException();

        int firstPixelX = mStartX;
        int firstPixelY = mStartY;
        int pixelToRows = (mEndX -  mStartX);
        int rows = (mEndY - mStartY);
        int size = pixelToRows * rows;
        int pixels[] = new int[size];

        Bitmap bitmap2 = bitmap.copy(Bitmap.Config.ARGB_4444, false);

        bitmap2.getPixels(pixels, 0, pixelToRows, firstPixelX, firstPixelY, pixelToRows, rows);

        final List<HashMap<Integer, Integer>> colorMap = new ArrayList<HashMap<Integer, Integer>>();
        colorMap.add(new HashMap<Integer, Integer>());
        colorMap.add(new HashMap<Integer, Integer>());
        colorMap.add(new HashMap<Integer, Integer>());

        int color = 0;
        int r = 0;
        int g = 0;
        int b = 0;
        Integer rC, gC, bC;
        for (int i = 0; i < pixels.length; i++) {
            color = pixels[i];

            r = Color.red(color);
            g = Color.green(color);
            b = Color.blue(color);

            rC = colorMap.get(0).get(r);
            if (rC == null)
                rC = 0;
            colorMap.get(0).put(r, ++rC);

            gC = colorMap.get(1).get(g);
            if (gC == null)
                gC = 0;
            colorMap.get(1).put(g, ++gC);

            bC = colorMap.get(2).get(b);
            if (bC == null)
                bC = 0;
            colorMap.get(2).put(b, ++bC);
        }

        int[] rgb = new int[3];
        for (int i = 0; i < 3; i++) {
            int max = 0;
            int val = 0;
            for (Map.Entry<Integer, Integer> entry : colorMap.get(i).entrySet()) {
                if (entry.getValue() > max) {
                    max = entry.getValue();
                    val = entry.getKey();
                }
            }
            rgb[i] = val;
        }

        int dominantColor = Color.rgb(rgb[0], rgb[1], rgb[2]);
        //String dominantColorString = String.format("Cor Predom.: {%d, %d, %d}",rgb[0], rgb[],rgb[2]);

        return rgb;
    }
}
