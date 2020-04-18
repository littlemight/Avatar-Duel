package com.avatarduel.model;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * ConfirmBox class used to make a new window and get confirmation of an action
 */
public class ConfirmBox {
    /**
     * The answer confirmed by the user
     */
    static boolean answer;

    /**
     * Static function to display the window
     * @param x coordinate for the window
     * @param y coordinate for the window
     * @param title title of the window
     * @param message the message displayed
     * @return answer chosen
     */
    public static boolean display(double x, double y, String title, String message) {
        Stage window = new Stage();
        window.setResizable(false);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setX(x - 100);
        window.setY(y);

        Label label = new Label(message);
        label.setAlignment(Pos.CENTER_RIGHT);
        label.setStyle("-fx-font-weight: bold");

        Button confirm_btn = new Button("Confirm");
        Button cancel_btn = new Button("Cancel");

        confirm_btn.setMinWidth(150);
        cancel_btn.setMinWidth(150);
        confirm_btn.setOnAction(e -> {
            answer = true;
            window.close();
        });

        cancel_btn.setOnAction(e -> {
            answer = false;
            window.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, confirm_btn, cancel_btn);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 200, 100);
        window.setScene(scene);
        window.showAndWait();
        return answer;
    }
}
