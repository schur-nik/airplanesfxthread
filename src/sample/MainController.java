package sample;

import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class MainController {
    private LineChart<Number, Number> chart;
    private Boolean startActionBoolean = false;
    private XYChart.Series sr = new XYChart.Series();
    private Double posPlane1;
    private Double posPlane2;

    @FXML
    private Button btnStart;

    @FXML
    private AnchorPane paneAirGraph;

    @FXML
    private TextField fieldAirOne;

    @FXML
    private TextField fieldAirTwo;

    @FXML
    private Button btnReset;

    @FXML
    private ImageView plane1;

    @FXML
    private ImageView plane2;

    @FXML
    void btnResetAction(ActionEvent event) {
        startActionBoolean = false;
        System.out.println(plane1.getLayoutX());
        System.out.println(chart);
    }

    @FXML
    void btnStartAction(ActionEvent event) {
        startActionBoolean = true;
        Double speedOne = Double.valueOf(fieldAirOne.getText());
        Double speedTwo = Double.valueOf(fieldAirTwo.getText());
        Task taskUpdate = new Task<Void>() {
            @Override
            public Void call() throws InterruptedException {
                while (startActionBoolean) {
                    while (plane1.getLayoutX() < posPlane1) {
                        System.out.println(plane1.getLayoutX());
                        plane1.setLayoutX(plane1.getLayoutX() + speedOne / 50);
                        plane2.setLayoutX(plane2.getLayoutX() + speedTwo / 50);
                        Thread.currentThread().sleep(20);
                    }
                }
                return null;
            }
        };
        Task taskOneSec = new Task<Void>() {
            @Override
            public Void call() throws InterruptedException {
                while (startActionBoolean) {
                    posPlane1 = posPlane1+speedOne;
                    posPlane2 = posPlane2+speedTwo;
                    Thread.currentThread().sleep(1000);
                }
                return null;
            }
        };
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Runnable updater = new Runnable() {
                    @Override
                    public void run() {
                            sr.getData().remove(1);
                            sr.getData().add(new XYChart.Data(plane1.getLayoutX(), 25));
                            System.out.println(sr.getData());
                    }
                };
                while (startActionBoolean) {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException ex) {}
                    Platform.runLater(updater);
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
        new Thread(taskOneSec).start();
        new Thread(taskUpdate).start();

    }

    @FXML
    void initialize() {
        posPlane1 = 0.0;
        posPlane2 = 0.0;
        chart = new LineChart<Number, Number>(new NumberAxis(0,770, 25), new NumberAxis(0, 30, 0));
        chart.setAnimated(false);
        chart.setMaxSize(770, 210);
        chart.setPrefSize(770, 210);
        chart.setLegendVisible(false);
        sr.getData().add(new XYChart.Data( 0, 25));
        sr.getData().add(new XYChart.Data( 0, 25));
        //sr.getData().add(new XYChart.Data( 0, 75));
        chart.getData().add(sr);
        paneAirGraph.getChildren().add(0, chart);
    }

}
