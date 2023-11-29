package com.example.LecturaLatte.observadores;

import com.example.LecturaLatte.models.Cliente;
import com.example.LecturaLatte.models.ExitMonitor;
import com.example.LecturaLatte.models.estados.EstadoCliente;

public class TiempoDeComer implements Runnable {
    private ExitMonitor exitMonitor;
    private Cliente cliente;

    public TiempoDeComer(ExitMonitor exitMonitor, Cliente cliente) {
        this.exitMonitor = exitMonitor;
        this.cliente = cliente;
    }

    @Override
    public void run() {
        while (cliente.getTime()>0){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            cliente.decrementTime();
        }
        cliente.setState(EstadoCliente.TERMINAR_COMER);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        exitMonitor.passToExitQueue();
    }
}
