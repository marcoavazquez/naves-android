package com.aliens.aliensvsme;


import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class BadNavecita {

    public GameView gameView;
    public Bitmap bmp;
    public int posX;
    public int posY;
    public int speedX = 10;
    public int speedY = 14;

    public Random rnd;

    public BadNavecita(GameView gameView, Bitmap bitmap, int x, int y) {
        this.gameView = gameView;
        this.bmp      = bitmap;

        this.posX = x;
        this.posY = y;
    }


    public void onDraw(Canvas canvas){
        rnd = new Random();

        if ( posX >= gameView.getWidth()  - bmp.getWidth() || posX + speedX <= 150) {

            speedX = -speedX;
        }

        if ( posY >= gameView.getHeight() + bmp.getHeight() || posY + speedY <= 0) {

            speedY = - speedY;

        }

        posX += speedX;
        posY += speedY;

        canvas.drawBitmap(bmp, posX, posY, null);
    }

}
