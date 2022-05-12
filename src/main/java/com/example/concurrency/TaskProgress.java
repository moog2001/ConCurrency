package com.example.concurrency;

import javafx.concurrent.Task;

import java.util.Random;

public class TaskProgress extends Task {

    Random r;
    int total;
    TaskProgress(Random rInput, int totalInput){
        r = rInput;
    }

    Integer integer;
    @Override
    public Integer call() {
        final int max = r.nextInt(50000);
        boolean done = false;
        if (!done) {
            total = total + max;
            done = true;
        }
        int i;
        for (i = 1; i <= max; i++) {
            if (isCancelled()) {
                break;
            }
            integer = Integer.valueOf(i);
            updateProgress(i, max);
            try {
                Thread.sleep(r.nextInt(2));
            } catch (InterruptedException interrupted) {
                if (isCancelled()) {
                    updateMessage("Cancelled");
                }
            }


        }
        return Integer.valueOf(i);
    }

    public Integer returnValue(){
        return Integer.valueOf(integer);
    }
}
