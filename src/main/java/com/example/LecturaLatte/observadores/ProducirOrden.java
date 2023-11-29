package com.example.LecturaLatte.observadores;

import com.example.LecturaLatte.models.Barista;
import java.util.concurrent.ThreadLocalRandom;

public class ProducirOrden implements Runnable{
    private Barista barista;

    public ProducirOrden(Barista barista) {
        this.barista = barista;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(3000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            this.barista.generatedCommand();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
