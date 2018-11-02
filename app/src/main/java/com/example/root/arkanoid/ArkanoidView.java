package com.example.root.arkanoid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.Random;


public class ArkanoidView extends SurfaceView implements Runnable {

    Thread gameThread = null;
    SurfaceHolder surfaceHolder;
    volatile boolean playing;
    Canvas canvas;
    Paint paint;
    long fps;
    private long timeThisFrame;
    boolean paused = false;
    int score = 0;
    int lives = 3;
    int screenX;
    int screenY;

    boolean touchInRect = false;
    boolean bounced = false;

    Paddle paddle;
    float paddleX;
    Ball ball;
    float r = 20;
    Brick[] bricks = new Brick[40];
    int numBricks = 0;
    int visBricks = 0;

    boolean gameover = false;


    public ArkanoidView(Context context, int screenX, int screenY) {
        super(context);
        surfaceHolder = getHolder();
        paint = new Paint();
        playing = true;
        paused = true;
        this.screenX = screenX;
        this.screenY = screenY;
        paddle = new Paddle(screenX, screenY);
        paddleX = paddle.getRect().centerX();
        ball = new Ball(paddle.getRect().centerX(), paddle.getRect().top -r);
        createBricks();
    }

    @Override
    public void run() {
        while (playing) {
            long startFrameTime = System.currentTimeMillis();

            if(!paused){
                update();
            }

            draw();

            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }
        }
    }

    public void update() {
        paddle.updateX(paddleX);
        ball.update(fps);
        RectF ballRect = new RectF(ball.getX() - r, ball.getY() - r, ball.getX() + r, ball.getY() + r);
        if(ball.getY() < screenY/2) {
            for (int i = 0; i < numBricks; i++) {
                if (bricks[i].getVisibility()) {
                    if (RectF.intersects(bricks[i].getRect(), ballRect)) {
                        bricks[i].setInvisible();
                        score += 10;
                        visBricks--;
                        if (visBricks == 0) {
                            gameover = true;
                            paused = true;
                        }
                        ball.speedUp(5);
                        if(bricks[i].getRect().contains(ball.getX() - r, ball.getY()) ||
                                bricks[i].getRect().contains(ball.getX() + r, ball.getY())){
                            ball.reverseXVelocity();
                        }
                        else {
                            ball.reverseYVelocity();
                        }
                        bounced = false;
                        break;
                    }
                }
            }
        }

        if(ball.getX() - r < 0 ||
                ball.getX() + r > screenX){
            ball.reverseXVelocity();
            ball.speedUp(1);
            bounced = false;
        }

        if(ball.getY() - r < 0){
            ball.reverseYVelocity();
            ball.speedUp(1);
            bounced = false;
        }
        if(ball.getY() + r >= paddle.getRect().top) {
            if(!bounced && RectF.intersects(ballRect, paddle.getRect())){
                if(paddle.getRect().contains(ball.getX() + r, ball.getY()) ||
                        paddle.getRect().contains(ball.getX() - r, ball.getY())){
                    ball.reverseXVelocity();
                    ball.speedUp(10);
                }
                else{
                    ball.reverseYVelocity();
                    ball.speedUp(10);
                }
                bounced = true;
            } else if (ball.getY() > screenY) {
                Log.i("Life", "-1");
                lives--;
                paused = true;
                if(lives == 0){
                    gameover = true;
                }
                ball.reset(paddle.getRect().centerX(), paddle.getRect().top - r);
                ball.upVelocity();
            }
        }

    }

    public void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();

            canvas.drawColor(Color.argb(255,  0, 0, 0));
            paint.setColor(Color.argb(255,  255, 255, 255));
            //paddle
            canvas.drawRect(paddle.getRect(), paint);
            //ball
            paint.setColor(Color.argb(255,  255, 255, 255));
            canvas.drawCircle(ball.getX(), ball.getY(), r, paint);
            //bricks
            for (int i = 0; i < numBricks; i++) {
                if (bricks[i].getVisibility()) {
                    paint.setColor(bricks[i].getColor());
                    canvas.drawRect(bricks[i].getRect(), paint);
                }
            }
            //score
            paint.setColor(Color.argb(255,  255, 255, 255));
            paint.setTextSize(40);
            canvas.drawText("Score: " + score + "   Lives: " + lives, 10,50, paint);

            if(gameover) {
                paint.setTextSize(140);
                if(visBricks == 0)
                    canvas.drawText("YOU WON!!!", 0.25f * screenX, screenY / 2, paint);
                else
                    canvas.drawText("GAME OVER", 0.25f * screenX, screenY / 2, paint);
            }
            else if(paused){
                paint.setTextSize(40);
                canvas.drawText("tap to start", 0.4f * screenX , screenY / 2, paint);
            }

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void createBricks() {
        int brickWidth = screenX / 10;
        int brickHeight = screenY / 10;

        numBricks = 0;
        for (int column = 0; column < 10; column++) {
            for (int row = 0; row < 4; row++) {
                Random generator = new Random();
                int r = generator.nextInt(200)+56;
                int g = generator.nextInt(200)+56;
                int b = generator.nextInt(200)+56;
                bricks[numBricks] = new Brick(row, column, brickWidth, brickHeight, Color.rgb(r,g,b));
                numBricks++;
            }
        }
        visBricks = numBricks;
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                if(paused & !gameover)
                    paused = false;
                float x = motionEvent.getX();
                float y = motionEvent.getY();
                if(paddle.getRect().contains(x, y))
                    touchInRect = true;
                break;

            case MotionEvent.ACTION_MOVE:
                if(touchInRect) {
                    paddleX = motionEvent.getX();
                }
                break;

            case MotionEvent.ACTION_UP:
                touchInRect = false;
                break;
        }
        return true;
    }
}
