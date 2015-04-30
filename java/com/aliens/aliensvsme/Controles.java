package com.aliens.aliensvsme;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;


public class Controles {

    public GameView gameView;
    public Bitmap boton;
    public int ancho;
    public int alto;
    public int posArmaAncho;
    public int posArmaAlto;
    public int bmpH;
    public int bmpW;
    public boolean disparado;

    public int posBalaX = -5;
    public int posBalaY = -5;

    public Controles(GameView gameview, Bitmap bitmap){
        this.gameView = gameview;
        this.boton    = bitmap;

        this.bmpH = bitmap.getHeight();
        this.bmpW = bitmap.getWidth();

        this.ancho    = gameview.getWidth();
        this.alto     = gameview.getHeight();

        posArmaAncho = ancho - (ancho / 8);
        posArmaAlto  = alto - (alto / 4);
    }

    public void onDraw(Canvas canvas){

        //canvas.drawBitmap(boton, posArmaAncho, posArmaAlto , null);

    }

    //public void disparar(int naveX, int naveW, int naveY, int naveH){
    public boolean disparar(){

        if(gameView.touched && (gameView.indice == 0 || gameView.indice == 1) &&
                    gameView.x > posArmaAncho && gameView.x < posArmaAncho + bmpW &&
                    gameView.y > posArmaAlto  && gameView.y < posArmaAlto + bmpH ) {

            //posBalaX = naveX + naveW;
            //posBalaY = naveY + naveH / 2;
            return true;
        }
        return false;
    }



}
