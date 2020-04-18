package com.avatarduel.model;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * AlertBox class that is used to make a new window and display an alert message to the user
 */
public class AlertBox {
    /**
     * Static function to display the window
     * @param x coordinate for the window
     * @param y coordinate for the window
     * @param title title of the window
     * @param message the message displayed
     */
    public static void display(double x, double y, String title, String message) {
        Stage window = new Stage();
        window.setResizable(false);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setX(x);
        window.setY(y);

        Label label = new Label(message);
        label.setAlignment(Pos.CENTER_RIGHT);
        label.setStyle("-fx-font-weight: bold");

        Button ok_btn = new Button("Ok");

        ok_btn.setMinWidth(150);
        ok_btn.setOnAction(e -> {
            window.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, ok_btn);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 200, 100);
        window.setScene(scene);
        window.showAndWait();
    }
}
