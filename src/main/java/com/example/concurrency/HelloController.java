package com.example.concurrency;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

public class HelloController {

    @FXML
    private HBox conTimer;

    @FXML
    private Label lblTimer;

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

    ArrayList<HBox> arrayListM = new ArrayList<>();
    ArrayList<ProgressBar> arrayListBars = new ArrayList<>();
    ArrayList<Task<Integer>> tasks = new ArrayList<Task<Integer>>();
    ArrayList<Integer> totals = new ArrayList();
    ArrayList<Integer> results = new ArrayList();

    @FXML
    private VBox root;
    private ProgressBar bar;
    private int limit = 50000;
    int total = 0;

    private HelloController self;
    Timer timer = new Timer();
    Integer timeSeconds = 3;

    public void init(Stage parent) {
        self = this;
        for (Node node : root.getChildrenUnmodifiable()) {
            if (node instanceof HBox) {
                HBox hbox = (HBox) node;

                if (hbox.getId() == null) {
                    arrayListM.add((HBox) node);
                }
            }
        }


        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        // KeyFrame event handler
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(1),
                        (EventHandler<ActionEvent>) event -> {
                            timeSeconds--;
                            // update timerLabel
                            lblTimer.setText(
                                    timeSeconds.toString());
                            if (timeSeconds <= 0) {
                                int index = 0;
                                tasks.forEach(n -> {
                                    n.cancel();
                                });
                                for (HBox hBox : arrayListM) {
                                    Label test = new Label(String.valueOf(results.get(index)) + "/" + String.valueOf(totals.get(index)));
                                    hBox.getChildren().add(test);
                                    index++;
                                }
                                root.setMinWidth(300);
                                parent.setMinWidth(300);
                                timeline.stop();

                                int total = 0;
                                for(int a :totals){
                                    total= a+total;
                                }
                                int result = 0;
                                for(int a :results){
                                    result= a+result;
                                }

                                Label lblM0 = new Label(String.valueOf(result) + "/" + String.valueOf(total));
                                conM0.getChildren().add(lblM0);
                            }
                        }));


        timeline.playFromStart();


        Random r = new Random();


        int index = 0;
        for (int i = 0; i < 23; i++) {
            results.add(0);
        }
        for (HBox node : arrayListM) {
            Task task = new Task<Integer>() {
                Integer result;
                Integer indexTask;

                private Task init(int indexInput) {
                    indexTask = indexInput;
                    return this;
                }

                @Override
                protected void cancelled() {
                    super.cancelled();
                    results.set(indexTask, result);
                }


                @Override
                public Integer call() {
                    final int max = r.nextInt(50000);
                    totals.add(max);
                    boolean done = false;
                    if (!done) {
                        total = total + max;
                        done = true;
                    }
                    for (int i = 1; i <= max; i++) {
                        if (isCancelled()) {
                            break;
                        }
                        updateProgress(i, max);
                        result = i;
                        try {
                            Thread.sleep(r.nextInt(4));
                        } catch (InterruptedException interrupted) {
                            if (isCancelled()) {
                                updateMessage("Cancelled");
                            }
                        }
                    }
                    return result;
                }
            }.init(index);
            tasks.add(task);
            for (Node children : node.getChildrenUnmodifiable()) {
                if (children instanceof ProgressBar) {
                    arrayListBars.add((ProgressBar) children);
                    ProgressBar bar = (ProgressBar) children;
                    bar.progressProperty().bind(task.progressProperty());
                }
                new Thread(task).start();
            }
            index++;
        }

        Task<Void> taskM0 = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                double progress = 0;
                double progressValue = 0;
                while (progressValue < 1 && !isCancelled()) {
                    progress = 0;
                    for (ProgressBar bar : arrayListBars) {
                        if (bar.getProgress() == -1) {
                            break;
                        }
                        progress = progress + bar.getProgress();
                    }
                    progressValue = progress / arrayListBars.size();
                    updateProgress(progressValue, 1);
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
