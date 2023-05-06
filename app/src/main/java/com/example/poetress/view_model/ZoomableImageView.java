package com.example.poetress.view_model;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import androidx.appcompat.widget.AppCompatImageView;

public class ZoomableImageView extends AppCompatImageView {
    private static final float MIN_SCALE = 1.0f;
    private static final float MAX_SCALE = 5.0f;

    private Matrix matrix;
    private PointF lastTouchPoint;
    private ScaleGestureDetector scaleGestureDetector;
    private float viewWidth, viewHeight;
    private float imageWidth, imageHeight;
    private float scaleFactor = 1.0f;

    public ZoomableImageView(Context context) {
        super(context);
        init();
    }

    public ZoomableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ZoomableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        matrix = new Matrix();
        lastTouchPoint = new PointF();
        scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleGestureListener());
        setOnTouchListener(new TouchListener());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        updateImageDimensions();
    }

    private void updateImageDimensions() {
        Drawable drawable = getDrawable();
        if (drawable != null) {
            imageWidth = drawable.getIntrinsicWidth();
            imageHeight = drawable.getIntrinsicHeight();
            adjustScale();
            adjustTranslation();
        }
    }

    private void adjustScale() {
        scaleFactor = Math.min(viewWidth / imageWidth, viewHeight / imageHeight);
        matrix.setScale(scaleFactor, scaleFactor);
    }

    private void adjustTranslation() {
        float scaledImageWidth = imageWidth * scaleFactor;
        float scaledImageHeight = imageHeight * scaleFactor;
        float translateX = (viewWidth - scaledImageWidth) / 2f;
        float translateY = (viewHeight - scaledImageHeight) / 2f;
        matrix.postTranslate(translateX, translateY);
    }

    @Override
    protected void onDraw(android.graphics.Canvas canvas) {
        canvas.save();
        canvas.concat(matrix);
        super.onDraw(canvas);
        canvas.restore();
    }

    private class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(MIN_SCALE, Math.min(scaleFactor, MAX_SCALE));
            matrix.setScale(scaleFactor, scaleFactor);
            adjustTranslation();
            invalidate();
            return true;
        }
    }

    private class TouchListener implements View.OnTouchListener {
        private static final int INVALID_POINTER_ID = -1;

        private int activePointerId = INVALID_POINTER_ID;
        private PointF initialTouchPoint = new PointF();
        private Matrix initialMatrix = new Matrix();
        private PointF lastTouchPoint = new PointF();

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            int action = event.getActionMasked();

            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    activePointerId = event.getPointerId(0);
                    initialTouchPoint.set(event.getX(), event.getY());
                    lastTouchPoint.set(initialTouchPoint);
                    initialMatrix.set(matrix);
                    break;

                case MotionEvent.ACTION_POINTER_DOWN:
                    int pointerIndex = event.getActionIndex();
                    int pointerId = event.getPointerId(pointerIndex);

                    if (pointerId != activePointerId) {
                        initialTouchPoint.set(event.getX(pointerIndex), event.getY(pointerIndex));
                        lastTouchPoint.set(initialTouchPoint);
                        initialMatrix.set(matrix);
                    }
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (activePointerId != INVALID_POINTER_ID) {
                        pointerIndex = event.findPointerIndex(activePointerId);
                        float currentTouchX = event.getX(pointerIndex);
                        float currentTouchY = event.getY(pointerIndex);

                        float deltaX = currentTouchX - lastTouchPoint.x;
                        float deltaY = currentTouchY - lastTouchPoint.y;

                        matrix.set(initialMatrix);
                        matrix.postTranslate(deltaX, deltaY);
                        adjustTranslation();
                        invalidate();

                        lastTouchPoint.set(currentTouchX, currentTouchY);
                    }
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    pointerIndex = event.getActionIndex();
                    pointerId = event.getPointerId(pointerIndex);

                    if (pointerId == activePointerId) {
                        activePointerId = INVALID_POINTER_ID;
                        lastTouchPoint.set(initialTouchPoint);
                        initialMatrix.set(matrix);
                    }
                    break;
            }

            scaleGestureDetector.onTouchEvent(event);
            setImageMatrix(matrix);
            return true;
        }
    }
}
