package com.aliens.aliensvsme;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.Random;


public class BadNave {

    private GameView gameView;
    private Bitmap bmp;

    public int posX = 0;
    public int posY = 0;
    private int speedX = 12;
    private int speedY = 17;

    public int vida = 100;
    public int nivel;
    public int nvl_indicador = nivel;
    public int diferencia;

    Random rnd;

    public BadNave(GameView gameView){

        this.gameView = gameView;
    }

    public void setBitmap(Bitmap bmp) {
        this.bmp = bmp;
    }


    public void onDraw(Canvas canvas){
        rnd = new Random();

        this.diferencia = (gameView.getRight() / 3) - (gameView.getRight() / 15);
        this.nivel =  diferencia / 100;


        if ( posX >= gameView.getWidth()  - bmp.getWidth()  / 2 || posX + speedX <= 0) {

            speedX = -(speedX + rnd.nextInt(4));
        }

        if ( posY >= gameView.getHeight() - bmp.getHeight() / 2 || posY + speedY <= 0) {

            speedY = -(speedY + rnd.nextInt(2));

        }

        this.posX += speedX + speedY;
        this.posY += speedY;

        canvas.drawBitmap(bmp, posX, posY, null);
    }

    public boolean leHasDado(int posBalaX, int posBalaY){

        return posBalaX >= posX && posBalaX <= posX + bmp.getWidth() &&
                posBalaY >= posY && posBalaY <= posY + bmp.getHeight();
    }

    public void indicadorVida(Canvas canvas) {

        Log.e("vida ", diferencia + ", " + vida);

        Paint indicador = new Paint();
        indicador.setColor(Color.RED);
        indicador.setStyle(Paint.Style.FILL);
        canvas.drawRect(gameView.getRight() - gameView.getRight() / 3 + (nivel * 5) + nvl_indicador, 50, (gameView.getRight() - (gameView.getRight() / 15)), 40, indicador);

    }

    public void set_vida(int i){
        this.vida -= i;
    }

    public int get_vida(){
        return this.vida;
    }

    public void set_nvl_indicador(int nivel) {
        this.nvl_indicador += nivel;
    }

    public int get_nivel() {
        return this.nivel;
    }

    public int get_posX() {
        return this.posX;
    }

    public int get_posY() {
        return this.posY;
    }

}
