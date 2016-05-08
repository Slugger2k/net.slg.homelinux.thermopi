package org.gillius.jfxutils.chart;

import java.util.List;

import javafx.event.EventHandler;
import javafx.scene.chart.Axis;
import javafx.scene.input.TouchEvent;
import javafx.scene.input.TouchPoint;

public class TouchEventZoomHandler implements EventHandler<TouchEvent> {

    public TouchEventZoomHandler(Axis<?> xAxis, Axis<?> yAxis) {
    }

    @Override
    public void handle(TouchEvent event) {
        List<TouchPoint> touchPoints = event.getTouchPoints();

        TouchPoint touchPoint = touchPoints.get(0);

        System.out.println(touchPoint);

        // // Prevent a silly zoom... I'm still undecided about && vs ||
        // if (selectRect.getWidth() == 0.0 || selectRect.getHeight() == 0.0) {
        // selecting.set(false);
        // return;
        // }
        //
        // Rectangle2D zoomWindow =
        // chartInfo.getDataCoordinates(selectRect.getTranslateX(),
        // selectRect.getTranslateY(), rectX.get(), rectY.get());
        //
        // xAxis.setAutoRanging(false);
        // yAxis.setAutoRanging(false);
        // if (zoomAnimated.get()) {
        // zoomAnimation.stop();
        // zoomAnimation.getKeyFrames().setAll(
        // new KeyFrame(Duration.ZERO, new KeyValue(xAxis.lowerBoundProperty(),
        // xAxis.getLowerBound(), Interpolator.EASE_BOTH), new
        // KeyValue(xAxis.upperBoundProperty(), xAxis.getUpperBound(),
        // Interpolator.EASE_BOTH),
        // new KeyValue(yAxis.lowerBoundProperty(), yAxis.getLowerBound(),
        // Interpolator.EASE_BOTH), new KeyValue(yAxis.upperBoundProperty(),
        // yAxis.getUpperBound(), Interpolator.EASE_BOTH)),
        // new KeyFrame(Duration.millis(zoomDurationMillis.get()), new
        // KeyValue(xAxis.lowerBoundProperty(), zoomWindow.getMinX(),
        // Interpolator.EASE_BOTH), new KeyValue(
        // xAxis.upperBoundProperty(), zoomWindow.getMaxX(),
        // Interpolator.EASE_BOTH), new KeyValue(yAxis.lowerBoundProperty(),
        // zoomWindow.getMinY(), Interpolator.EASE_BOTH), new KeyValue(yAxis
        // .upperBoundProperty(), zoomWindow.getMaxY(),
        // Interpolator.EASE_BOTH)));
        // zoomAnimation.play();
        // } else {
        // zoomAnimation.stop();
        // xAxis.setLowerBound(zoomWindow.getMinX());
        // xAxis.setUpperBound(zoomWindow.getMaxX());
        // yAxis.setLowerBound(zoomWindow.getMinY());
        // yAxis.setUpperBound(zoomWindow.getMaxY());
        // }
        //
        // selecting.set(false);

    }

}
