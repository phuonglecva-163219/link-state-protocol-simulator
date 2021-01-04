package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;

import java.awt.*;

public class SampleController {

    public Label helloWorld;

    public void sayHelloWorld(ActionEvent actionEvent) {
        helloWorld.setText("Hello World!");
    }
}
