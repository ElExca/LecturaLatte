package com.example.LecturaLatte.observadores;

import com.example.LecturaLatte.models.Barista;

import java.util.Observable;

public class ObservadorConsumirOrdenes extends Observable implements Runnable{
    private Barista barista;

    public ObservadorConsumirOrdenes(Barista barista) {
        this.barista = barista;
    }

    @Override
    public void run() {
        while(true){
            this.barista.makeTheOrder();
            setChanged();
            notifyObservers("3");
        }
    }
}
