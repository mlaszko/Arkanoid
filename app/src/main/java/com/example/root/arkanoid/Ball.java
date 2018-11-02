package com.example.root.arkanoid;

import android.util.Log;

public class Ball {

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    private final float initSpeed = 250;

    float x;
    float y;

    float speed;
    float xVelocity;
    float yVelocity;

    Ball(float x, float y){
        this.x = x;
        this.y = y;
        speed = initSpeed;
        xVelocity = -0.3f;
        yVelocity = (float) -Math.sqrt(1 - (xVelocity*xVelocity));
        Log.i("ball X", Float.toString(x));
        Log.i("ball Y", Float.toString(y));
        Log.i("xVelocity", Float.toString(xVelocity));
        Log.i("yVelocity", Float.toString(yVelocity));
    }

    public void update(long fps){
        if(fps == 0)
            return;
        x += xVelocity * speed / fps;
        y += yVelocity * speed / fps;
    }

    public void upVelocity(){
        if(yVelocity > 0){
            reverseYVelocity();
        }
    }

    public void reverseYVelocity(){
        yVelocity = -yVelocity;
    }

    public void reverseXVelocity(){
        xVelocity = - xVelocity;
    }

    public void speedUp(float s){
        speed += s;
    }

    public void reset(float x, float y){
        speed = initSpeed;
        this.x = x;
        this.y = y;
        yVelocity = - Math.abs(yVelocity);
    }
}
