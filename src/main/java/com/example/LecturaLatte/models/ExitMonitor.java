package com.example.LecturaLatte.models;

import com.example.LecturaLatte.models.estados.EstadoCliente;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public class ExitMonitor {
    private Deque<Cliente> exitQueue;
    private LecturaLatte lecturaLatte;
    private Cliente exit;

    public ExitMonitor( LecturaLatte lecturaLatte) {
        this.exitQueue = new LinkedList<Cliente>();
        this.lecturaLatte = lecturaLatte;
    }
    public synchronized void extractDinersExit(){
        while (exitQueue.size() == 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        this.exit=this.exitQueue.getFirst();
        this.exit.setState(EstadoCliente.SALIR);
        lecturaLatte.removeDinnerByTableId(exit.getTableId());
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(2000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.notifyAll();
    }

    public synchronized void passToExitQueue(){
        while (exitQueue.size()==20) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
            Cliente clienteOrder = lecturaLatte.getDinnerByState(EstadoCliente.TERMINAR_COMER);
            if(clienteOrder !=null){
                clienteOrder.setState(EstadoCliente.ESPERAR_ORDEN);
                this.exitQueue.add(clienteOrder);
            }
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(2000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.notifyAll();
    }

    public Cliente removeFromExitQueue(){
        return this.exitQueue.remove();
    }
}