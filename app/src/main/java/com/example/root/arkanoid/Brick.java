package com.example.root.arkanoid;
import android.graphics.Color;
import android.graphics.RectF;
public class Brick {

    private RectF rect;

    private boolean isVisible;

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    private int color;

    public Brick(int row, int column, int width, int height, int color){

        isVisible = true;

        this.color = color;

        int padding = 1;

        rect = new RectF(column * width + padding,
                row * height + padding,
                column * width + width - padding,
                row * height + height - padding);
    }

    public RectF getRect(){
        return this.rect;
    }

    public void setInvisible(){
        isVisible = false;
    }

    public boolean getVisibility(){
        return isVisible;
    }
}
