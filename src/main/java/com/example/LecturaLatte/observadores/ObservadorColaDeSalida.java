package com.example.LecturaLatte.observadores;

import com.example.LecturaLatte.models.ExitMonitor;

import java.util.Observable;

public class ObservadorColaDeSalida extends Observable implements Runnable{
    // Referencia al monitor de salida que maneja la lógica relacionada con la salida de clientes.
    private ExitMonitor exitMonitor;
    // Constructor que recibe un objeto ExitMonitor.
    public ObservadorColaDeSalida(ExitMonitor exitMonitor) {
        this.exitMonitor = exitMonitor;
    }

    // Método que se ejecuta en un hilo separado.
    @Override
    public void run() {
        while (true){
            // Extraer clientes de la cola de salida
            exitMonitor.extractDinersExit();
            // Indica que el estado a cambiado
            setChanged();
            //Notificar al observador
            notifyObservers("4");
        }

    }
}
