package com.example.root.arkanoid;
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

        rect = new RectF(column * width + 2,
                row * height + 2,
                column * width + width - 2,
                row * height + height - 2);
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
