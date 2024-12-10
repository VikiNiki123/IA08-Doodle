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

    private Paint currPaint;
    private Path currPath;
    private float brushSize = 20f;
    private int brushColor = Color.BLACK;
    private int brushOpacity = 255;

    private Bitmap canvasBitmap;
    private Canvas drawCanvas;

    private final List<Path> pathList = new ArrayList<>();
    private final List<Paint> paintList = new ArrayList<>();
    private final List<Path> undoStack = new ArrayList<>();

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupBrush();
    }

    private void setupBrush() {
        currPaint = new Paint();
        currPaint.setColor(brushColor);
        currPaint.setAntiAlias(true);
        currPaint.setStrokeWidth(brushSize);
        currPaint.setStyle(Paint.Style.STROKE);
        currPaint.setStrokeJoin(Paint.Join.ROUND);
        currPaint.setStrokeCap(Paint.Cap.ROUND);
        currPaint.setAlpha(brushOpacity);
        currPath = new Path();
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
        canvas.drawPath(currPath, currPaint); // Draw the current path while moving
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currPath = new Path();
                currPath.moveTo(touchX, touchY);
                break;

            case MotionEvent.ACTION_MOVE:
                currPath.lineTo(touchX, touchY);
                break;

            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(currPath, currPaint);
                pathList.add(currPath);
                paintList.add(currPaint);
                currPath = new Path();
                break;
        }

        invalidate();
        return true;
    }

    public void clearCanvas() {
        pathList.clear();
        undoStack.clear();
        canvasBitmap.eraseColor(Color.TRANSPARENT);
        invalidate();
    }

    public void undoPath() {
        if (!pathList.isEmpty()) {
            Path lastPath = pathList.remove(pathList.size() - 1);
            undoStack.add(lastPath);
            redrawCanvas();
        }
    }

    private void redrawCanvas() {
        canvasBitmap.eraseColor(Color.TRANSPARENT);
        for (Path path : pathList) {
            drawCanvas.drawPath(path, currPaint);
        }
        invalidate();
    }

    public void changeBrushColor(int color) {
        brushColor = color;
        currPaint.setColor(brushColor);
    }

    public void changeBrushSize(float size) {
        brushSize = size;
        currPaint.setStrokeWidth(brushSize);
    }

    public void changeBrushOpacity(int opacity) {
        brushOpacity = opacity;
        currPaint.setAlpha(brushOpacity);
    }

    public float getBrushSize() {
        return brushSize;
    }

    public int getBrushOpacity() {
        return brushOpacity;
    }
}
