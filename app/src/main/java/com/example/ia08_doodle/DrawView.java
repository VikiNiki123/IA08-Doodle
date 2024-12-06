package com.example.ia08_doodle;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DrawView extends View {

    private Paint paint;
    private Path path;
    private float brushSize = 20f;
    private int brushColor = Color.BLACK;
    private int brushOpacity = 255;
    private Bitmap canvasBitmap;
    private Canvas drawCanvas;

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupBrush();
    }

    private void setupBrush() {
        paint = new Paint();
        paint.setColor(brushColor);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(brushSize);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAlpha(brushOpacity);
        path = new Path();
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(canvasBitmap, 0, 0, null);
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(path, paint);
                path.reset();
                break;
        }
        invalidate();
        return true;
    }
    public void clearCanvas() {
        //path.reset();
        if (canvasBitmap != null) {
            canvasBitmap.eraseColor(Color.TRANSPARENT);
        }
        //invalidate();
    }

    public void changeBrushColor(int color) {
        brushColor = color;
        paint.setColor(brushColor);
    }

    public void changeBrushSize(float size) {
        brushSize = size;
        paint.setStrokeWidth(brushSize);
    }

    public void changeBrushOpacity(int opacity) {
        brushOpacity = opacity;
        paint.setAlpha(brushOpacity);
    }


    public float getBrushSize() {
        return brushSize;
    }

    public int getBrushOpacity() {
        return brushOpacity;
    }
}
