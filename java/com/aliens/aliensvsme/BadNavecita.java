package com.aliens.aliensvsme;


import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class BadNavecita {

    public GameView gameView;
    public Bitmap bmp;
    public int posX;
    public int posY;
    public int speedX = 11;
    public int speedY = 13;

    public int vida = 10;

    public Random rnd;

    public BadNavecita(GameView gameView, int x, int y) {
        this.gameView = gameView;

        this.posX = x;
        this.posY = y;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bmp = bitmap;
    }

    public void onDraw(Canvas canvas){
        rnd = new Random();

        if ( posX >= gameView.getWidth()  - 50 || posX + speedX <= 100) {

            speedX = - (speedX + rnd.nextInt(3));
        }

        if ( posY >= gameView.getHeight() + 50 || posY + speedY <= -50) {

            speedY = - (speedY + rnd.nextInt(5));

        }

        posX += speedX;
        posY += speedY;

        canvas.drawBitmap(bmp, posX, posY, null);
    }

    public boolean leHasDado(int posBalaX, int posBalaY){

        return posBalaX >= posX && posBalaX <= posX + bmp.getWidth() &&
               posBalaY >= posY && posBalaY <= posY + bmp.getHeight();
    }

    public int get_vida(){
        return this.vida;
    }

    public void set_vida(int i) {
        this.vida -= i;
    }

    public int get_posX() {
        return this.posX;
    }

    public int get_posY() {
        return this.posY;
    }

}
