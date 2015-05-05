package com.aliens.aliensvsme;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.Random;


public class BadNave {

    private GameView gameView;  // Instancia de la vista de la pantalla para obtener tamaños de esta

    private Bitmap bmp; // Variable que almacena la imagen de la nave

    public int posX = 0;    //posicion de la nave en X
    public int posY = 0;        //posicion de la nave en Y
    private int speedX = 12;    // Velocidad inicial en la que se mueve la nave en X
    private int speedY = 17;    // Velocidad inicial en la que se mueve la nave en Y

    public int vida = 100;      // Nivel de salud de la nave
    public int nivel;           // Cantidad de nivel de salud a diminuir por cada golpe de acuerdo al indicador
    public int nvl_indicador = nivel;
    public int diferencia;     // valor de vida, para el indicador en pixeles

    Random rnd;

    public BadNave(GameView gameView){

        this.gameView = gameView;
    }

    public void setBitmap(Bitmap bmp) {
        this.bmp = bmp;
    }


    public void onDraw(Canvas canvas){ // Dibuja la NAVE y su movimiento
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

    public boolean leHasDado(int posBalaX, int posBalaY){ // Si has golpeado la nave

        return posBalaX >= posX && posBalaX <= posX + bmp.getWidth() &&
                posBalaY >= posY && posBalaY <= posY + bmp.getHeight();
    }

    public void indicadorVida(Canvas canvas) {

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
