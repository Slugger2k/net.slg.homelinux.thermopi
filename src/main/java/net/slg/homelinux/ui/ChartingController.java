/*
 * Copyright 2016 Christian Mueller (christian.muell3r@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.slg.homelinux.ui;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.gillius.jfxutils.chart.ChartPanManager;
import org.gillius.jfxutils.chart.FixedFormatTickFormatter;
import org.gillius.jfxutils.chart.JFXChartUtil;
import org.gillius.jfxutils.chart.StableTicksAxis;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.CacheHint;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import net.slg.homelinux.core.LoggerUtil;
import net.slg.homelinux.core.Util;
import net.slg.homelinux.core.data.DataService;
import net.slg.homelinux.core.domain.TempData;
import net.slg.homelinux.ui.nodes.HoveredThresholdNode;

public class ChartingController {

    @FXML
    private LineChart<Number, Number> chart;
    @FXML
    private Label outputLabel;
    @FXML
    private ToggleButton reloadButton;
    @FXML
    private ToggleButton restButton;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private TextArea console;
    @FXML
    private Label topLabel;

    private final static int millisToWait = 60000;

    private boolean db = false;

    private DataService ds = new DataService();

    private XYChart.Series<Number, Number> series1;
    private XYChart.Series<Number, Number> series2;
    private XYChart.Series<Number, Number> series3;
    private Timeline addDataTimeline;
    private LoggerUtil loggerUtil;
    private int lastId;
    private int anzRows;
    private int anzRowsPerDay = 1440;
    private double maxTemp1;
    private double maxTemp2;
    private double maxTemp3;

    public ChartingController() {
        loggerUtil = LoggerUtil.getInstance();
    }

    @FXML
    void autoZoom() {
        chart.getXAxis().setAutoRanging(true);
        chart.getYAxis().setAutoRanging(true);
        // There seems to be some bug, even with the default NumberAxis, that
        // simply setting the
        // auto ranging does not recompute the ranges. So we clear all chart
        // data then re-add it.
        // Hopefully I find a more proper way for this, unless it's really bug,
        // in which case I hope
        // it gets fixed.
        ObservableList<XYChart.Series<Number, Number>> data = chart.getData();
        chart.setData(FXCollections.<XYChart.Series<Number, Number>> emptyObservableList());
        chart.setData(data);
    }

    @FXML
    void toggleAdd() {
        switch (addDataTimeline.getStatus()) {
        case PAUSED:
        case STOPPED:
            addDataTimeline.play();
            // chart.getXAxis().setAutoRanging(true);
            // chart.getYAxis().setAutoRanging(true);
            // Animation looks horrible if we're updating a lot
            chart.setAnimated(false);
            chart.getXAxis().setAnimated(false);
            chart.getYAxis().setAnimated(false);
            break;
        case RUNNING:
            addDataTimeline.stop();
            // Return the animation since we're not updating a lot
            chart.setAnimated(true);
            chart.getXAxis().setAnimated(true);
            chart.getYAxis().setAnimated(true);
            break;

        default:
            throw new AssertionError("Unknown status");
        }
    }

    @FXML
    void rest() {
        db = !db;
        initialize();
    }

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {

        List<TempData> data;

        chart.getData().clear();
        chart.setCache(true);
        chart.setCacheShape(true);
        chart.setCacheHint(CacheHint.SPEED);

        anzRows = 0;

        if (db) {
            log("loading Data from MySQL...");
        } else {

            log("loading Data from RestWebService...");
        }

        try {

            data = ds.getLastData(db, anzRowsPerDay);
            log(data.size() + " Points loaded.");
            data = Util.getInstance().reduceData(data, 10);

        } catch (Exception e) {
            data = new ArrayList<TempData>(0);
            data.add(new TempData(new Date(0), new Timestamp(0l), 0, 0, 0));
            log(e);
        }

        lastId = anzRows;

        SimpleDateFormat format = new SimpleDateFormat("dd.MM HH:mm");

        ((StableTicksAxis) chart.getXAxis()).setAxisTickFormatter(new FixedFormatTickFormatter(format));
        ((StableTicksAxis) chart.getXAxis()).setForceZeroInRange(false);

        series1 = new XYChart.Series<Number, Number>();
        series1.setName("Temperatur1");

        series2 = new XYChart.Series<Number, Number>();
        series2.setName("Temperatur2");

        series3 = new XYChart.Series<Number, Number>();
        series3.setName("Temperatur3");

        data.forEach(tempData -> {
            XYChart.Data<Number, Number> cData1 = new XYChart.Data<Number, Number>(tempData.getTs().getTime(), tempData.getTemp1());
            cData1.setNode(new HoveredThresholdNode(tempData.getTs(), tempData.getTemp1()));
            series1.getData().add(cData1);

            if (tempData.getTemp1() > maxTemp1) {
                maxTemp1 = tempData.getTemp1();
            }

            XYChart.Data<Number, Number> cData2 = new XYChart.Data<Number, Number>(tempData.getTs().getTime(), tempData.getTemp2());
            cData2.setNode(new HoveredThresholdNode(tempData.getTs(), tempData.getTemp2()));
            series2.getData().add(cData2);

            if (tempData.getTemp2() > maxTemp2) {
                maxTemp2 = tempData.getTemp2();
            }

            XYChart.Data<Number, Number> cData3 = new XYChart.Data<Number, Number>(tempData.getTs().getTime(), tempData.getTemp3());
            cData3.setNode(new HoveredThresholdNode(tempData.getTs(), tempData.getTemp3()));
            series3.getData().add(cData3);

            if (tempData.getTemp3() > maxTemp3) {
                maxTemp3 = tempData.getTemp3();
            }

            setTopLabel(maxTemp1, maxTemp2, maxTemp3);
        });

        chart.getData().addAll(series1, series2, series3);

        addDataTimeline = new Timeline(new KeyFrame(Duration.millis(millisToWait), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    addNewValue();
                } catch (Exception e) {
                    e.printStackTrace();
                    log(e.getMessage());
                }
            }
        }));
        addDataTimeline.setCycleCount(Animation.INDEFINITE);

        // Panning works via either secondary (right) mouse or primary with ctrl
        // held down
        ChartPanManager panner = new ChartPanManager(chart);
        panner.setMouseFilter(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseButton.SECONDARY || (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.isShortcutDown())) {
                    // let it through
                } else {
                    mouseEvent.consume();
                }
            }
        });
        panner.start();

        // Zooming works only via primary mouse button without ctrl held down
        JFXChartUtil.setupZooming(chart, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() != MouseButton.PRIMARY || mouseEvent.isShortcutDown())
                    mouseEvent.consume();
            }
        });
    }

    private void setTopLabel(double maxTemp1, double maxTemp2, double maxTemp3) {
        String labelText = "      max. Temp1=" + maxTemp1;
        labelText += "           max. Temp2=" + maxTemp2;
        labelText += "           max. Temp3=" + maxTemp3;

        topLabel.setText(labelText);
    }

    @FXML
    void addNewValue() throws Exception {

        List<TempData> data = ds.getLastData(db, 1);

        lastId = lastId + data.size();

        log("added " + data.size() + " new values...");

        data.forEach(tempData -> {
            XYChart.Data<Number, Number> cData1 = new XYChart.Data<Number, Number>(tempData.getTs().getTime(), tempData.getTemp1());
            cData1.setNode(new HoveredThresholdNode(tempData.getTs(), tempData.getTemp1()));
            series1.getData().add(cData1);

            XYChart.Data<Number, Number> cData2 = new XYChart.Data<Number, Number>(tempData.getTs().getTime(), tempData.getTemp2());
            cData2.setNode(new HoveredThresholdNode(tempData.getTs(), tempData.getTemp2()));
            series2.getData().add(cData2);

            XYChart.Data<Number, Number> cData3 = new XYChart.Data<Number, Number>(tempData.getTs().getTime(), tempData.getTemp3());
            cData3.setNode(new HoveredThresholdNode(tempData.getTs(), tempData.getTemp3()));
            series3.getData().add(cData3);
        });

    }

    @FXML
    private void exit() {
        log("Exit....");
        Platform.exit();
    }

    @FXML
    private void setStartTime() {
        LocalDate ld = startDatePicker.getValue();

        if (ld != null) {
            LocalDate now = LocalDate.now();
            int i = now.getDayOfYear() - ld.getDayOfYear();

            // Anz Tage * Anz aktualisierungen
            anzRowsPerDay = i * 1440;

            initialize();
        }

    }

    private void log(Exception e) {
        e.printStackTrace();
        log(e.getMessage());
    }

    private void log(String message) {
        String log = loggerUtil.log(message);
        console.appendText(log + "\n");
    }

}
