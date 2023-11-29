package com.example.LecturaLatte.observadores;

import com.example.LecturaLatte.models.ClienteMonitor;
import java.util.Observable;

public class ProducirColaEnEspera extends Observable implements Runnable{

    private final ClienteMonitor clienteMonitor;
    public ProducirColaEnEspera(ClienteMonitor clienteMonitor){
        this.clienteMonitor = clienteMonitor;
    }
    @Override
    public void run() {
        while (true){
            this.clienteMonitor.generateDinersWait();
            setChanged();
            notifyObservers("1");
        }
    }
}
