package com.example.LecturaLatte.models;

import com.example.LecturaLatte.models.estados.EstadoCliente;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public class ClienteMonitor {
    private Deque<Cliente> queue_wait;
    private LecturaLatte lecturaLatte;
    private Cliente enter;
    private int total;
    private int id;

    public ClienteMonitor(int total, LecturaLatte lecturaLatte){
        this.queue_wait= new LinkedList<Cliente>();
        this.lecturaLatte = lecturaLatte;
        this.enter=null;
        this.total=total;
        this.id=20;
    }

    public synchronized void generateDinersWait(){
        while (total == queue_wait.size()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Cliente cliente = new Cliente(id);
        this.id++;
        queue_wait.add(cliente);
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(2000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.notifyAll();
    }

    public synchronized void extractDinersWait(){
        while (queue_wait.size() == 0 || lecturaLatte.isFull()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        this.enter=this.queue_wait.getFirst();
        this.enter.setState(EstadoCliente.SENTARSE);
        this.lecturaLatte.setData(this.getEnter());
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(2000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.queue_wait.removeFirst();
        this.notifyAll();
    }

    public Cliente getEnter() { return this.enter; }

    public Deque<Cliente> getQueue_wait() {return queue_wait;}

}
