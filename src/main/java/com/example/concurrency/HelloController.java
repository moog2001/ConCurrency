package com.example.concurrency;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class HelloController {

    @FXML
    private HBox conTimer;

    @FXML
    private ProgressBar pbM0;

    @FXML
    private ProgressBar pbM1;

    @FXML
    private ProgressBar pbM10;

    @FXML
    private ProgressBar pbM11;

    @FXML
    private ProgressBar pbM12;

    @FXML
    private ProgressBar pbM13;

    @FXML
    private ProgressBar pbM14;

    @FXML
    private ProgressBar pbM15;

    @FXML
    private ProgressBar pbM16;

    @FXML
    private ProgressBar pbM17;

    @FXML
    private ProgressBar pbM18;

    @FXML
    private ProgressBar pbM19;

    @FXML
    private ProgressBar pbM2;

    @FXML
    private ProgressBar pbM20;

    @FXML
    private ProgressBar pbM21;

    @FXML
    private ProgressBar pbM22;

    @FXML
    private ProgressBar pbM3;

    @FXML
    private ProgressBar pbM4;

    @FXML
    private ProgressBar pbM5;

    @FXML
    private ProgressBar pbM6;

    @FXML
    private ProgressBar pbM7;

    @FXML
    private ProgressBar pbM8;

    @FXML
    private ProgressBar pbM9;

    @FXML
    private HBox conM0;

    ArrayList<HBox> arrayListM = new ArrayList();
    ArrayList<ProgressBar> arrayListBars = new ArrayList();
    ArrayList<Task> tasks = new ArrayList();


    @FXML
    private VBox root;
    private ProgressBar bar;
    private int limit = 50000;
    int total = 0;

    private HelloController self;

    public void init() {
        self = this;
        for (Node node : root.getChildrenUnmodifiable()) {
            if (node instanceof HBox) {
                HBox hbox = (HBox) node;

                if (hbox.getId() == null) {
                    arrayListM.add((HBox) node);
                }
            }
        }


        Random r = new Random();

        arrayListM.forEach((node) -> {
            Task task = new Task<Integer>() {
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
                        updateProgress(i, max);

                        try {
                            Thread.sleep(r.nextInt(1));
                        } catch (InterruptedException interrupted) {
                            if (isCancelled()) {
                                updateMessage("Cancelled");
                            }
                        }
                    }
                    return null;
                }
            };

            tasks.add(task);


            for (Node children : node.getChildrenUnmodifiable()) {
                if (children instanceof ProgressBar) {
                    arrayListBars.add((ProgressBar) children);
                    ProgressBar bar = (ProgressBar) children;
                    bar.progressProperty().bind(task.progressProperty());
                }
                new Thread(task).start();
            }


        });

        Task taskM0 = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                double progress = 0;
                double progressValue = 0;
                while (progressValue < 1 && !isCancelled()) {
                    for (ProgressBar bar : arrayListBars) {
                        if (bar.getProgress() == -1) {
                            break;
                        }
                        progress = progress + bar.getProgress();
                    }
                    progressValue = progress / arrayListBars.size();                    updateProgress(progressValue, total);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException interrupted) {
                        if (isCancelled()) {
                            updateMessage("Cancelled");
                        }
                    }
                }
                return null;
            }
        };
        pbM0.progressProperty().bind(taskM0.progressProperty());
        new Thread(taskM0).start();


    }


}
