package com.example.LecturaLatte.models;

import com.example.LecturaLatte.models.estados.EstadoCliente;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

public class Barista {
    private Queue<Cliente> commands;

    private Deque<Cliente> orders;

    private LecturaLatte lecturaLatte;

    private int TOTAL;

    public Barista(LecturaLatte lecturaLatte){
        this.commands=new LinkedList<Cliente>();
        this.orders=new LinkedList<Cliente>();
        this.lecturaLatte = lecturaLatte;
        this.TOTAL=20;

    }
    public synchronized void makeTheOrder() {
        while (commands.isEmpty()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Cliente cliente =commands.remove();
        cliente.setState(EstadoCliente.COMER);
        cliente.setTime(getRandomEatTime());
        orders.add(cliente);
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(5000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.notifyAll();

    }

    public int getRandomEatTime() {
        return  (int)(Math.random() * (10 - 5)) + 5;
    }

    public synchronized void generatedCommand(){
        while (commands.size()==TOTAL) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Cliente clienteOrder = lecturaLatte.getDinnerByState(EstadoCliente.SENTARSE);
        if(clienteOrder !=null){
            clienteOrder.setState(EstadoCliente.ESPERAR_ORDEN);
            commands.add(clienteOrder);
        }
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(2000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.notifyAll();
    }

    public Deque<Cliente> getOrders() {
        return orders;
    }
}
