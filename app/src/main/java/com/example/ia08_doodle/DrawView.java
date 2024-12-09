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
import java.util.ArrayList;
import java.util.List;

public class DrawView extends View {

    private Paint currpaint;
    private Path currpath;
    private float brushSize = 20f;
    private int brushColor = Color.BLACK;
    private int brushOpacity = 255;
    private Bitmap canvasBitmap;
    private Canvas drawCanvas;

    //New Pathways for Undo Feature
    private final List<Path> paths = new ArrayList<>();
    private final List<Paint> paints = new ArrayList<>();

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupBrush();
    }

    private void setupBrush() {
        currpaint = new Paint();
        currpaint.setColor(brushColor);
        currpaint.setAntiAlias(true);
        currpaint.setStrokeWidth(brushSize);
        currpaint.setStyle(Paint.Style.STROKE);
        currpaint.setStrokeJoin(Paint.Join.ROUND);
        currpaint.setStrokeCap(Paint.Cap.ROUND);
        currpaint.setAlpha(brushOpacity);
        currpath = new Path();
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);

        if (canvasBitmap == null) {
            canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            drawCanvas = new Canvas(canvasBitmap);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(canvasBitmap, 0, 0, null);
        canvas.drawPath(currpath, currpaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currpath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                currpath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(currpath, currpaint);
                currpath.reset();
                break;
        }
        invalidate();
        return true;
    }
    public void clearCanvas() {
        currpath.reset();
        if (canvasBitmap != null) {
            canvasBitmap.eraseColor(Color.TRANSPARENT);
        }
        invalidate();
    }

    public void changeBrushColor(int color) {
        brushColor = color;
        currpaint.setColor(brushColor);
    }

    public void changeBrushSize(float size) {
        brushSize = size;
        currpaint.setStrokeWidth(brushSize);
    }

    public void changeBrushOpacity(int opacity) {
        brushOpacity = opacity;
        currpaint.setAlpha(brushOpacity);
    }


    public float getBrushSize() {
        return brushSize;
    }

    public int getBrushOpacity() {
        return brushOpacity;
    }
}
