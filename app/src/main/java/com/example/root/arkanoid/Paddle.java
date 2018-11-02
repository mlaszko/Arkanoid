package com.example.root.arkanoid;
import android.graphics.RectF;

public class Paddle {
    private RectF rect;

    private float length;
    private float height;

    private int screenX;

    public Paddle(int screenX, int screenY){

        this.screenX = screenX;

        length = screenX/5;
        height = screenY/10;

        float x = (screenX / 2) - (length / 2) ;
        float y = screenY - height -5;

        rect = new RectF(x, y, x + length, y + height);
    }

    public RectF getRect(){
        return rect;
    }

    public void updateX(float x){
        rect.left = x - length/2;
        if(rect.left < 0)
            rect.left =0;
        if(rect.left > screenX - length)
            rect.left = screenX - length;
        rect.right = rect.left + length;
    }
}
