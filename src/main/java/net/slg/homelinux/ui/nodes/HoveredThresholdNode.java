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
package net.slg.homelinux.ui.nodes;

import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/** a node which displays a value on hover, but is otherwise empty */
public class HoveredThresholdNode extends StackPane {

    public HoveredThresholdNode(Date priorValue, double value) {
        setPrefSize(6, 6);

        final Label label = createDataThresholdLabel(priorValue, value);

        setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                getChildren().setAll(label);
                setCursor(Cursor.NONE);
                toFront();
            }
        });
        setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                getChildren().clear();
                setCursor(Cursor.CROSSHAIR);
            }
        });
    }

    private Label createDataThresholdLabel(Date priorValue, double value) {
        Date d = priorValue;
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM HH:mm");
        final Label label = new Label(sdf.format(d) + "\n" + value + " °C");
        label.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");

        if (priorValue == null) {
            label.setTextFill(Color.RED);
        } else {
            label.setTextFill(Color.DIMGRAY);
        }

        label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
        return label;
    }
}
