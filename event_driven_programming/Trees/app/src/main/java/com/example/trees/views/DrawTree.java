package com.example.trees.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.View;

import com.example.trees.Branch;
import com.example.trees.Tree;

import java.util.ArrayList;

public class DrawTree extends View {
    private Paint paint = new Paint();
    private ArrayList<Branch> branches;

    public DrawTree(Context context, Tree tree) {
        super(context);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        branches = tree.toBranches(displayMetrics.widthPixels, displayMetrics.heightPixels);
        System.out.println();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        branches.forEach(branch -> {
            branch.drawBranch(canvas, paint, getResources());
        });
    }
}
