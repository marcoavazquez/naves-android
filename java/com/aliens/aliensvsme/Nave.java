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

    public Nave(GameView gameView, Bitmap bitmap){
        this.gameview = gameView;
        this.nave     = bitmap;

        this.alto  = gameView.getHeight();
        this.ancho = gameView.getWidth();

        this.bmpH = bitmap.getHeight();
        this.bmpW = bitmap.getWidth();

    }

    public void onDraw(Canvas canvas){
        moverNave(canvas);
    }

    public void moverNave(Canvas canvas) {


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

    public boolean disparado() {
        if (disparado) {
            return true;
        } else {
            return false;
        }
    }
}
