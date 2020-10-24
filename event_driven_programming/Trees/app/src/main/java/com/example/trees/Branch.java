package com.example.trees;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Branch {
    protected float x, y, x2, y2, width;

    public Branch(float x, float y, float x2, float y2, float width) {
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
        this.width = width;
    }

    public void drawBranch(Canvas canvas, Paint paint, Resources resources) {
        paint.setStrokeWidth(width);
        canvas.drawLine(x, y, x2, y2, paint);
    }
}
