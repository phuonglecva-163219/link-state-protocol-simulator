package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import models.Network;
import java.util.ArrayList;
import java.util.List;

public class ButtonExample  extends Application {
    @Override
    public void start(Stage primaryStage){
        // button and pane are created

        Button btOK = new Button("1");
        btOK.setLayoutX(100);
        Button btSave = new Button("2");
        btSave.setLayoutY(200);
        Line line = new Line();

        // set staring position
        line.setStartX(btOK.getLayoutX() + 20);
        line.setStartY(btOK.getLayoutY() + 20);

        // set ending position
        line.setEndX(btSave.getLayoutX() + 20);
        line.setEndY(btSave.getLayoutY() + 20);
        Pane pane = new Pane();
        // this code drags the button
        btOK.setOnMouseDragged(e -> {
            btOK.setLayoutX(e.getSceneX());
            btOK.setLayoutY(e.getSceneY());
            line.setStartX(btOK.getLayoutX() + 20);
            line.setStartY(btOK.getLayoutY() + 20);
        });
        btOK.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                btOK.setStyle(
                        "-fx-background-radius: 5em; " +
                                "-fx-min-width: 40px; " +
                                "-fx-min-height: 40px; " +
                                "-fx-max-width: 40px; " +
                                "-fx-max-height: 40px;"+
                                "-fx-background-color: gray;"
                );
            }
        });
        btOK.setStyle(
                "-fx-background-radius: 5em; " +
                        "-fx-min-width: 40px; " +
                        "-fx-min-height: 40px; " +
                        "-fx-max-width: 40px; " +
                        "-fx-max-height: 40px;"
        );
        btSave.setStyle(
                "-fx-background-radius: 5em; " +
                        "-fx-min-width: 40px; " +
                        "-fx-min-height: 40px; " +
                        "-fx-max-width: 40px; " +
                        "-fx-max-height: 40px;"
        );
        // this code drags the button
        btSave.setOnMouseDragged(e -> {
            btSave.setLayoutX(e.getSceneX());
            btSave.setLayoutY(e.getSceneY());
            line.setEndX(btSave.getLayoutX() + 20);
            line.setEndY(btSave.getLayoutY() + 20);
        });

        // create a line
        List<Button> list = new ArrayList<>();
        list.add(btOK);
        list.add(btSave);
        Button changeColor = new Button("Change");
        changeColor.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                list.forEach(button->{
                    button.setStyle(
                            "-fx-background-radius: 5em; " +
                                    "-fx-min-width: 40px; " +
                                    "-fx-min-height: 40px; " +
                                    "-fx-max-width: 40px; " +
                                    "-fx-max-height: 40px;"+
                                    "-fx-background-color: gray;"
                    );
                });
            }
        });


//        pane.getChildren().addAll(btOK, btSave);
        Group group = new Group();
        group.getChildren().addAll(btOK, btSave, line, changeColor);

        Scene scene = new Scene(group,500, 250);
        primaryStage.setTitle("A Draggable button");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
