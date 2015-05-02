package com.aliens.aliensvsme;



public class Escenario {
    public GameView gameView;
    public Boolean constelacionElegida = false, naveElegida = false;


    public int naveSeleccionada;


    public Escenario(GameView gameView) {
        this.gameView = gameView;

    }


    public void setNaveSeleccionada(int i) {
        this.naveSeleccionada = i;
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

}
