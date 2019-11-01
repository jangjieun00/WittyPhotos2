package com.example.wittyphotos2.KNN;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class DataPointCanvas extends View {

    private Paint paint;
    private List<DataPoint> listDataPoints = new ArrayList<>();

    public DataPointCanvas(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setupPaint();
    }

    private void setupPaint(){
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(30);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.SQUARE);
    }

    public void setListDataPoints(List<DataPoint> listDataPoints){
        this.listDataPoints = listDataPoints;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(DataPoint dataPoint : listDataPoints){
            if (dataPoint.getCategory() == Category.food1) paint.setColor(Color.CYAN);
            else if (dataPoint.getCategory() == Category.food2) paint.setColor(Color.BLUE);
            else if (dataPoint.getCategory() == Category.food3) paint.setColor(Color.YELLOW);
            else if (dataPoint.getCategory() == Category.food4) paint.setColor(Color.GREEN);
            else if (dataPoint.getCategory() == Category.food5) paint.setColor(Color.GRAY);
            else paint.setColor(Color.RED);
            canvas.drawCircle((float)dataPoint.getX(), (float)dataPoint.getY(), 5, paint);
        }
    }
}