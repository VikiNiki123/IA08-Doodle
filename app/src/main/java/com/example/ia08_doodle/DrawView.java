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
    private final List<Path> undoPathStack = new ArrayList<>();
    private final List<Paint> undoPaintStack = new ArrayList<>();

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

    private Paint getCurrentPaint() {
        Paint newPaint = new Paint(currPaint);
        return newPaint;
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
        for (int i = 0; i < pathList.size(); i++) {
            canvas.drawPath(pathList.get(i), paintList.get(i));
        }
        canvas.drawPath(currPath, currPaint); //Draw the current path while moving
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
                paintList.add(getCurrentPaint());
                currPath = new Path();
                break;
        }

        invalidate();
        return true;
    }

    public void clearCanvas() {
        pathList.clear();
        paintList.clear();
        undoPathStack.clear();
        undoPaintStack.clear();
        canvasBitmap.eraseColor(Color.TRANSPARENT);
        invalidate();
    }

    public void undoPath() {
        if (!pathList.isEmpty()) {
            undoPathStack.add(pathList.remove(pathList.size() - 1));
            undoPaintStack.add(paintList.remove(paintList.size() - 1));
            redrawCanvas();
        }
    }

    private void redrawCanvas() {
        canvasBitmap.eraseColor(Color.TRANSPARENT);
        for (int i = 0; i < pathList.size(); i++) {
            drawCanvas.drawPath(pathList.get(i), paintList.get(i));
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
