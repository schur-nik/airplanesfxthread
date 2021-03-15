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
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;

import java.util.function.UnaryOperator;

public class MainController {
    private LineChart<Number, Number> chart;
    private Boolean startActionBoolean = false;
    private XYChart.Series sr = new XYChart.Series();
    private XYChart.Series sr2 = new XYChart.Series();
    private Double speedOne;
    private Double speedTwo;
    private Double timer = 0.00;
    private Thread thread;
    private int tempV = 1;

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
    private Label timerLabel;


    @FXML
    void btnResetAction(ActionEvent event) {
        btnStart.setText("START");
        startActionBoolean = false;
        thread.stop();
        sr.getData().remove(1);
        sr2.getData().remove(1);
        timer = 0.00;
        timerLabel.setText(String.format("%.2f", timer));
        plane1.setLayoutX(0);
        plane2.setLayoutX(0);
        tempV = 1;
    }

    private void showAlertWithoutHeaderText(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void btnStartAction(ActionEvent event) {
        if (fieldAirOne.getText().equals("") || fieldAirTwo.getText().equals("")) {
            showAlertWithoutHeaderText("ERROR", "Enter right speed for Airplane 1 and Airplane 2.");
        }
        else {
            if (startActionBoolean) {
                btnStart.setText("START");
                startActionBoolean = false;
                showAlertWithoutHeaderText("End of flight", "Flight distance Airplane 1 = " +
                        String.format("%.2f", plane1.getLayoutX()) + "\nFlight distance Airplane 2 = " + String.format("%.2f", plane2.getLayoutX()));
            } else {
                btnStart.setText("STOP");
                startActionBoolean = true;
                speedOne = Double.valueOf(fieldAirOne.getText());
                speedTwo = Double.valueOf(fieldAirTwo.getText());
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Runnable updater = new Runnable() {
                            @Override
                            public void run() {
                                if (plane1.getLayoutX() < 700 && plane2.getLayoutX() < 700) {
                                    timer += 0.02;
                                    timerLabel.setText(String.format("%.2f", timer));
                                    plane1.setLayoutX(plane1.getLayoutX() + speedOne / 50);
                                    plane2.setLayoutX(plane2.getLayoutX() + speedTwo / 50);
                                    if (sr.getData().size() == 2 && sr2.getData().size() == 2) {
                                        sr.getData().remove(1);
                                        sr2.getData().remove(1);
                                    }
                                    sr.getData().add(new XYChart.Data(plane1.getLayoutX(), 25));
                                    sr2.getData().add(new XYChart.Data(plane2.getLayoutX(), 15));
                                } else {
                                    startActionBoolean = false;
                                    if (tempV == 1) {
                                        showAlertWithoutHeaderText("End of flight(end distance)", "Flight distance Airplane 1 = "
                                                + String.format("%.2f", plane1.getLayoutX()) + "\nFlight distance Airplane 2 = "
                                                + String.format("%.2f", plane2.getLayoutX()));
                                        tempV++;
                                    }
                                }
                            }
                        };
                        while (startActionBoolean) {
                            try {
                                Thread.sleep(20);
                            } catch (InterruptedException ex) {
                            }
                            Platform.runLater(updater);
                        }
                    }
                });
                thread.setDaemon(true);
                thread.start();
            }
        }
    }

    UnaryOperator<TextFormatter.Change> integerFilter = change -> {
        String input = change.getText();
        if (input.matches("[0-9]*")) {
            return change;
        }
        return null;
    };

    @FXML
    void initialize() {
        fieldAirOne.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
        fieldAirTwo.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
        timerLabel.setText("0.0");
        chart = new LineChart<Number, Number>(new NumberAxis(0,700, 25), new NumberAxis(0, 30, 0));
        chart.setAnimated(false);
        chart.setMaxSize(700, 210);
        chart.setPrefSize(700, 210);
        chart.setLegendVisible(false);
        sr.getData().add(new XYChart.Data( 0, 25));
        chart.getData().add(sr);
        sr2.getData().add(new XYChart.Data( 0, 15));
        chart.getData().add(sr2);
        paneAirGraph.getChildren().add(0, chart);
    }

}
