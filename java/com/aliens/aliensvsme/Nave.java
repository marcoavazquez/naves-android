package com.aliens.aliensvsme;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

public class Nave {

    public Bitmap nave;
    public GameView gameview;
    public int ancho;
    public int alto;
    public int posAlto = 100;
    public int posAncho = 30;
    public int bmpH;
    public int bmpW;
    public Controles controles;
    public boolean disparado = false;
    public int vida = 200;
    public int puntuacion;
    public Paint score;

    public Nave(GameView gameView){
        this.gameview = gameView;


        this.alto  = gameView.getHeight();
        this.ancho = gameView.getWidth();



    }

    public void onDraw(Canvas canvas , Bitmap bitmap){
        this.nave     = bitmap;
        this.bmpH = bitmap.getHeight();
        this.bmpW = bitmap.getWidth();
        moverNave(canvas);
    }

    public void moverNave(Canvas canvas) {
        score = new Paint();
        score.setColor(Color.GRAY);
        score.setTextSize(gameview.getHeight() / 20);
        score.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Puntos: " + get_puntuacion(), gameview.getWidth() / 2, 50, score );



        if(gameview.touched && gameview.x < gameview.getWidth() / 2 &&
                gameview.x > posAncho && gameview.x < posAncho + bmpW &&
                gameview.y > posAlto  && gameview.y < posAlto  + bmpH){

            posAlto  =  gameview.y - bmpH / 2 ;
            //posAncho =  gameview.x - bmpW / 2;

            canvas.drawBitmap(nave, posAncho, posAlto, null);
            disparado = true;

        }else{
            canvas.drawBitmap(nave, posAncho, posAlto, null);
            disparado = false;
        }

    }

    public void indicadorVida(Canvas canvas) {
        Paint indicador = new Paint();
        indicador.setColor(Color.GREEN);
        indicador.setStyle(Paint.Style.FILL);
        canvas.drawRect(gameview.getLeft() + 10, 50, vida, 40, indicador);

    }


    public boolean disparado() {
        if (disparado) {
            return true;
        } else {
            return false;
        }
    }

    public boolean meHanDado(int navex, int navey) {
        return navex >= posAncho && navex <= posAncho + nave.getWidth() &&
               navey >= posAlto  && navey <= posAlto  + nave.getHeight();
    }

    public void set_vida(int i) {
        this.vida -= i;
    }

    public int get_vida() {
        return this.vida;
    }

    public void set_puntuacion(int i){
        this.puntuacion += i;
    }

    public int get_puntuacion() {
        return this.puntuacion;
    }
}
