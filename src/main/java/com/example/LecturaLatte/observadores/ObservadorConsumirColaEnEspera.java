package com.example.LecturaLatte.observadores;

import com.example.LecturaLatte.models.ClienteMonitor;

import java.util.Observable;

public class ObservadorConsumirColaEnEspera extends Observable implements Runnable{
    private ClienteMonitor clienteMonitor;

    public ObservadorConsumirColaEnEspera(ClienteMonitor clienteMonitor){
        this.clienteMonitor = clienteMonitor;
    }
    @Override
    public void run() {
        while (true){
            this.clienteMonitor.extractDinersWait();
            setChanged();
            notifyObservers("2");
        }
    }
}
