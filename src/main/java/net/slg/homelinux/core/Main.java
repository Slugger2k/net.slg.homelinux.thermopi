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
package net.slg.homelinux.core;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.CacheHint;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import net.slg.homelinux.ui.ChartingController;

public class Main extends Application {

    private static String[] args;

    @Override
    public void start(Stage stage) throws Exception {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> Platform.runLater(() -> showErrorDialog(t, e)));
        Thread.currentThread().setUncaughtExceptionHandler(this::showErrorDialog);

        FXMLLoader loader = new FXMLLoader(ChartingController.class.getResource("Charting.fxml"));
        
        Region contentRootRegion = (Region) loader.load();

        StackPane root = new StackPane(contentRootRegion);
        if (args != null & args.length > 0) {
            String[] size = args[0].split("x");

            try {
                int x = Integer.parseInt(size[0]);
                int y = Integer.parseInt(size[1]);
                root.setPrefSize(x, y);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            root.setPrefSize(800, 600);
        }
        root.setCache(true);
        root.setCacheShape(true);
        root.setCacheHint(CacheHint.SPEED);

        try {
            Scene scene = new Scene(root, root.getPrefWidth(), root.getPrefHeight());
            scene.getStylesheets().add(ChartingController.class.getResource("basic.css").toExternalForm());

            stage.setScene(scene);
            stage.setTitle("Temperatur Monitor (Grad in Celsius)");
            stage.show();

        } catch (Throwable t) {
            showErrorDialog(Thread.currentThread(), t);
        }
    }

    public static void main(String[] args) {
        Main.args = args;
        launch(args);
    }

    private void showErrorDialog(Thread t, Throwable e) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error in " + t.getName());
        alert.setHeaderText(e.getMessage());
        alert.setContentText("" + e);

        alert.showAndWait();
    }
}
