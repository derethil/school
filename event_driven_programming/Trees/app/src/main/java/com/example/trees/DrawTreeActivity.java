package com.example.trees;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatActivity;
import com.example.trees.views.DrawTree;

public class DrawTreeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        float minAngle = Float.parseFloat(intent.getStringExtra("minAngle"));
        float maxAngle = Float.parseFloat(intent.getStringExtra("maxAngle"));
        float minLength = Float.parseFloat(intent.getStringExtra("minLength"));
        float maxLength = Float.parseFloat(intent.getStringExtra("maxLength"));
        float minTrunkWidth = Float.parseFloat(intent.getStringExtra("minTrunkWidth"));
        float maxTrunkWidth = Float.parseFloat(intent.getStringExtra("maxTrunkWidth"));


        Tree tree = new Tree(minAngle, maxAngle, minLength, maxLength, minTrunkWidth, maxTrunkWidth);
        tree.generateTree(10);

        DrawTree drawTree = new DrawTree(this, tree);
        drawTree.setBackgroundColor(Color.CYAN);
        setContentView(drawTree);
    }
}
