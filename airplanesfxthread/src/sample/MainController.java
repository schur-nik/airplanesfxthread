package sample;

import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
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
/*                    if (Math.round(plane1.getLayoutX()) > temp) {
                        sr.getData().remove(1);
                        sr.getData().add(new XYChart.Data(plane1.getLayoutX(), 25));
                        temp += 5;
                    }*/
                    plane1.setLayoutX(posPlane1/50);
                    plane2.setLayoutX(posPlane2/50);
                    Thread.currentThread().sleep(20);
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
        new Thread(taskUpdate).start();
        new Thread(taskOneSec).start();
    }

    @FXML
    void initialize() {
        posPlane1 = 0.0;
        posPlane2 = 0.0;
        chart = new LineChart<Number, Number>(new NumberAxis(0,770, 25), new NumberAxis(0, 30, 0));
        chart.setAnimated(false);
        chart.setMaxSize(800, 210);
        chart.setPrefSize(770, 210);
        chart.setLegendVisible(false);
        sr.getData().add(new XYChart.Data( 0, 25));
        sr.getData().add(new XYChart.Data( 0, 25));
        //sr.getData().add(new XYChart.Data( 0, 75));
        chart.getData().add(sr);
        paneAirGraph.getChildren().add(0, chart);
    }

}
