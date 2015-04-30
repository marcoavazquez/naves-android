package com.aliens.aliensvsme;

import android.graphics.Bitmap;
import android.graphics.Canvas;


public class BadNave {

    private GameView gameView;
    private Bitmap bmp;

    public int posX = 0;
    public int posY = 0;
    private int speedX = 12;
    private int speedY = 21;

    public BadNave(GameView gameView, Bitmap bitmap){

        this.gameView = gameView;
        this.bmp      = bitmap;
    }

    public void onDraw(Canvas canvas){

        if ( posX >= gameView.getWidth()  - bmp.getWidth()  / 2 || posX + speedX <= 0) {

            speedX = -speedX;
        }

        if ( posY >= gameView.getHeight() - bmp.getHeight() / 2 || posY + speedY <= 0) {

            speedY = - speedY;

        }

        posX += speedX + speedY;
        posY += speedY;

        canvas.drawBitmap(bmp, posX, posY, null);
    }
}
