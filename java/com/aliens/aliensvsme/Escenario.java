package com.aliens.aliensvsme;

/* Escenario 1 : elegir nave
*  Escenario 2 : elegir lugar
* */


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Escenario {
    public GameView gameView;
    public Boolean constelacionElegida =false, naveElegida = false;

    public int velocidadEstrella;
    public int colorEstrella;
    public int colorFondo;
    public int naveSeleccionada, constSeleccionada;


    public Escenario(GameView gameView) {
        this.gameView = gameView;

    }

    public Boolean constelacionElegida () {
        return this.constelacionElegida;
    }

    public Boolean naveElegida () {
        return this.naveElegida;
    }

    public void elegirNave(Canvas canvas) {

        Paint texto = new Paint();
        texto.setColor(Color.MAGENTA);
        texto.setTextAlign(Paint.Align.CENTER);
        texto.setTextSize(gameView.getHeight() / 20);
        canvas.drawText("Seleccione una nave", gameView.getWidth() / 2, gameView.getHeight() / 10, texto);


    }


    public void setNaveSeleccionada(int i) {
        this.naveSeleccionada = i;
    }

    public void setConstSeleccionada(int i) {
        this.constSeleccionada = i;
    }

    public void setFueSeleccionada(Boolean b) {
        this.naveElegida = b;
    }

    public Boolean getFueSeleccionada() {
        return this.naveElegida;
    }

    public void setFueConstSeleccionada(Boolean b) {
        this.constelacionElegida = b;
    }

    public Boolean getFueConstSeleccionada() {
        return this.constelacionElegida;
    }



    public int getNaveSeleccionada() {
        return this.naveSeleccionada;
    }

    public void set_VelocidadEstrella(int velocidad) {
        this.velocidadEstrella = velocidad;
    }

    public int getVelocidadEstrella() {
        return this.velocidadEstrella;
    }

    public void setColorFondo(int color) {
        this.colorFondo = color;
    }

    public int getColorFondo(){
        return this.colorFondo;
    }

    public void setColorEstrella( int color) {
        this.colorEstrella = color;
    }

    public int getColorEstrella() {
        return this.colorEstrella;
    }


}
