import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.*;

import java.util.*;

public class MainUI extends Application {

    List<String> listColor = Arrays.asList("red", "blue", "gray", "green", "yellow");
    Label messageLbl = new Label("Press any Button to see the message");
    Label messageFlooding = new Label("Ready to flooding....");
    Label dijkstraLabel = new Label("Dijkstra Map");
    Label dijkstraDetail = new Label("");
    @Override
    public void start(Stage primaryStage){
        // button and pane are created
//        Network net = DemoMain.getNetWork();
        Network net = DemoTest.generateNework(15);

        List<Router> routers = net.getListRouter();
//        System.out.println(routers);
        Map<Router, Button> mapRouterToButton = new HashMap<>();
        Map<Button, List<Line>> listStart = new HashMap<>();
        Map<Button, List<Line>> listEnd = new HashMap<>();
        List<Button> listButton = new ArrayList<>();
        List<Line> listLine = new ArrayList<>();
        for(Router router: routers) {
            Button temp = new Button(router.getName());
            temp.setStyle(
                    "-fx-background-radius: 5em; " +
                            "-fx-min-width: 40px; " +
                            "-fx-min-height: 40px; " +
                            "-fx-max-width: 40px; " +
                            "-fx-max-height: 40px;"
            );
            temp.setLayoutX(100 + Math.random()*(600 - 100));
            temp.setLayoutY(100 + Math.random()*(600 - 100));
            temp.setOnMouseDragged(e->{
                temp.setLayoutX(e.getSceneX() - 20);
                temp.setLayoutY(e.getSceneY() - 20);
            });

            mapRouterToButton.putIfAbsent(router, temp);
            listButton.add(temp);
        }

        for(List<Router> list: net.getListLinkTruncate()) {
            Router r1 = list.get(0);
            Router r2 = list.get(1);
            Button b1 = mapRouterToButton.get(r1);
            Button b2 = mapRouterToButton.get(r2);

            Line line = new Line();
            line.setStartX(b1.getLayoutX() + 20);
            line.setStartY(b1.getLayoutY() + 20);
            if(listStart.containsKey(b1)) {
                List<Line> tempList = listStart.get(b1);
                tempList.add(line);
                listStart.put(b1, tempList);
            }else {
                List<Line> tempList = new ArrayList<>();
                tempList.add(line);
                listStart.put(b1, tempList);
            }


            line.setEndX(b2.getLayoutX() + 20);
            line.setEndY(b2.getLayoutY() + 20);

            if(listEnd.containsKey(b2)) {
                List<Line> tempList = listEnd.get(b2);
                tempList.add(line);
                listEnd.put(b2, tempList);
            }else {
                List<Line> tempList = new ArrayList<>();
                tempList.add(line);
                listEnd.put(b2, tempList);
            }
            listLine.add(line);
        }

        for(Button start: listButton) {
            start.setOnMouseDragged(e->{
                if(listStart.containsKey(start)) {
                    for(Line line: listStart.get(start)) {
                        start.setLayoutX(e.getSceneX());
                        start.setLayoutY(e.getSceneY());
                        line.setStartX(start.getLayoutX() + 20);
                        line.setStartY(start.getLayoutY() + 20);
                    }


                }
                if(listEnd.containsKey(start)) {
                    for(Line line: listEnd.get(start)) {
                        start.setLayoutX(e.getSceneX());
                        start.setLayoutY(e.getSceneY());
                        line.setEndX(start.getLayoutX() + 20);
                        line.setEndY(start.getLayoutY() + 20);

                    }
                }


            });
        }

        Button changeColor = new Button("Start Flooding");
        Button genLsaMessage = new Button("Generate LSA Message");
        messageFlooding.setLayoutY(10);
        changeColor.setLayoutY(35);
        genLsaMessage.setLayoutY(60);
        genLsaMessage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                TextArea area = new TextArea();

                Stage stage  = new Stage();
                String str = "";
                for(Router router: net.getListRouter()) {
                    LMessage lMessage = router.generateLMessage();
                    str += "LSA Message of Router "+ lMessage.getSourceName() +"\n";
                    str += "  - Souce Name: " + lMessage.getSourceName() +"\n";
                    str += "  - Age: " + lMessage.getAge() +"\n";
                    str += "  - Sequence Number: " + lMessage.getSeq()+"\n";
                    str += "  - ListLink: " +"\n";
                    for(LinkCount linkCount: lMessage.getListLink()) {
                        str += "    + LinkCount: " + linkCount +"\n";
                    }
                }
//                System.out.println(result);
                Label label = new Label("");
                label.setText(str);
                area.setText(str);
                area.setPrefHeight(350);
//                label.setLayoutX(10);
//                label.setLayoutY(10);
                VBox box = new VBox();
                box.getChildren().add(area);
                Scene scene = new Scene(box, 500, 350);
                stage.setScene(scene);
                stage.setTitle("List LSA Messages");
                stage.show();
//                messageLbl.setText(result);

            }
        });

        changeColor.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {


                Runnable task = new Runnable() {
                    @Override
                    public void run() {
                        Map<Router, List<Router>> trackingFromRouter = new HashMap<>();
                        for(Router r: net.getListRouter()) {
                            try {
                                trackingFromRouter.put(r, net.distributeLsaMessageFrom(r));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        try {
                            String oldColor = "";
                            for(Router r: trackingFromRouter.keySet()) {
                                List<Router> trackingList = null;

                                try {
                                    trackingList = net.distributeLsaMessageFrom(r);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                List<Button> buttons = new ArrayList<>();
                                for(Router router: trackingList) {
                                    buttons.add(mapRouterToButton.get(router));
                                }
                                String color = "";
                                do{
                                    color = listColor.get(new Random().nextInt(listColor.size()));
                                }while (color == oldColor);
                                oldColor = color;

                                for(int i  = 0; i < buttons.size(); i++) {
                                    Button button = buttons.get(i);
                                    Thread.sleep(40);
                                    String finalColor = color;
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            messageFlooding.setText("Flooling message from router: " + r.getName()+"....");
//                                            line.setStyle(
//                                                    "-fx-background-color:" + color
//                                            );
                                            button.setStyle(
                                                    "-fx-background-radius: 5em; " +
                                                            "-fx-min-width: 40px; " +
                                                            "-fx-min-height: 40px; " +
                                                            "-fx-max-width: 40px; " +
                                                            "-fx-max-height: 40px;" +
                                                            "-fx-background-color:" + finalColor
                                            );
                                        }
                                    });
                            }
                            }
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    messageFlooding.setText("Flooded successfully all LSA Message!");
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                    }
                };
                new Thread(task).start();


            }
        });

        for(Router router: net.getListRouter()) {
            Button button = mapRouterToButton.get(router);
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                        if(mouseEvent.getClickCount() == 2){
                            showLsaDatabase(router, net);
                        }
                    }
                }
            });
        }


        Button showMap = new Button("Dijstra Map");

        showMap.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage stage = new Stage();
                Group group = new Group();
                Label label = new Label();
                label.setLayoutX(10);
                label.setLayoutY(10);
                label.setText(net.getDijkstraMapStr());
                group.getChildren().add(label);
                Scene scene = new Scene(group, 300, 300);

                stage.setScene(scene);
                stage.setTitle("Dijkstra Map");
                stage.show();
//                dijkstraDetail.setText(net.getDijkstraMapStr());


            }
        });

        showMap.setLayoutY(85);

        Group group = new Group();
        group.getChildren().add(showMap);
//        group.getChildren().addAll(btOK, btSave, line, changeColor);
        group.getChildren().add(changeColor);
        group.getChildren().add(messageFlooding);
        group.getChildren().add(genLsaMessage);
        for(Router router: mapRouterToButton.keySet()) {
            System.out.println(router);
            group.getChildren().add(mapRouterToButton.get(router));
        }
        for(Line line: listLine) {
            group.getChildren().add(line);
        }

        dijkstraDetail.setLayoutY(220);
        group.getChildren().add(dijkstraDetail);
//        pane.getChildren().addAll(btOK, btSave);

//        Add Button to send new LMessage


        Scene scene = new Scene(group,800, 800);
        primaryStage.setTitle("A Link State Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();

        Thread taskThread = new Thread(new Runnable() {
            @Override
            public void run() {
                do{
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            for(Router router: net.getListRouter()) {
                                net.increaseAge(router);
//                                System.out.println(1);
                            }
                        }
                    });

                }while(net.getMaxAgeCurr() < 1000);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        for(Router router: net.getListRouter()) {
                            router.resetLMessage();
                        }
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Lsa Database is discared from all routers! You want flooding now ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                        alert.showAndWait();

                        if (alert.getResult() == ButtonType.YES) {
                            Runnable task = new Runnable() {
                                @Override
                                public void run() {
                                    Map<Router, List<Router>> trackingFromRouter = new HashMap<>();
                                    for(Router r: net.getListRouter()) {
                                        try {
                                            trackingFromRouter.put(r, net.distributeLsaMessageFrom(r));
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    try {
                                        for(Router r: trackingFromRouter.keySet()) {
                                            List<Router> trackingList = null;

                                            try {
                                                trackingList = net.distributeLsaMessageFrom(r);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            List<Button> buttons = new ArrayList<>();
                                            for(Router router: trackingList) {
                                                buttons.add(mapRouterToButton.get(router));
                                            }

                                            String color = listColor.get(new Random().nextInt(listColor.size()));
                                            for(int i  = 0; i < buttons.size(); i++) {
                                                Button button = buttons.get(i);
                                                Thread.sleep(40);
                                                Platform.runLater(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        messageFlooding.setText("Flooling message from router: " + r.getName()+"....");
//                                            line.setStyle(
//                                                    "-fx-background-color:" + color
//                                            );
                                                        button.setStyle(
                                                                "-fx-background-radius: 5em; " +
                                                                        "-fx-min-width: 40px; " +
                                                                        "-fx-min-height: 40px; " +
                                                                        "-fx-max-width: 40px; " +
                                                                        "-fx-max-height: 40px;" +
                                                                        "-fx-background-color:" + color
                                                        );
                                                    }
                                                });
                                            }
                                        }
                                        Platform.runLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                messageFlooding.setText("Flooded successfully all LSA Message!");
                                            }
                                        });
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }


                                }
                            };
                            new Thread(task).start();
                        }
                    }
                });
            }
        });

        taskThread.start();

    }

    private void showLsaDatabase(Router router, Network network) {
        Stage stage = new Stage();
        Group group= new Group();

        Label label = new Label("");
        label.setLayoutX(20);
        label.setLayoutY(20);
        TextArea area = new TextArea();
        group.getChildren().add(area);

        String str = "";
        for(LMessage lMessage: router.getListLMessage()) {
            str += "LSA Message of Router "+ lMessage.getSourceName() +"\n";
            str += "  - Souce Name: " + lMessage.getSourceName() +"\n";
            str += "  - Age: " + lMessage.getAge() +"\n";
            str += "  - Sequence Number: " + lMessage.getSeq()+"\n";
            str += "  - ListLink: " +"\n";
            for(LinkCount linkCount: lMessage.getListLink()) {
                str += "    + LinkCount: " + linkCount +"\n";
            }

        }
        area.setText(str);
        area.setPrefHeight(350);

        TextArea area1 = new TextArea();
        area1.setLayoutX(550);
        Map<String, String> routeMap = network.routingTableMap(router);
        str = "\nDestination |\t Cost|\t NextHop\n";
        for(String destination: routeMap.keySet()) {
            str += destination+"\t ";
            str += routeMap.get(destination).split("\t")[0]+"\t ";
            str += routeMap.get(destination).split("\t")[1]+"\n";
        }
        System.out.println(str);

//        Create table
        TableView<Row> table = new TableView<Row>();
        List<Row> listRow = new ArrayList<>();
        for(String destination: routeMap.keySet()) {
            listRow.add(new Row(
                    destination,
                    routeMap.get(destination).split("\t")[0],
                    routeMap.get(destination).split("\t")[1]
            ));
        }
        ObservableList<Row> data = FXCollections.observableArrayList(
            listRow
        );

        TableColumn firstNameCol = new TableColumn("Destination");
        firstNameCol.setMinWidth(100);
        firstNameCol.setCellValueFactory(
                new PropertyValueFactory<Row, String>("destination"));

        TableColumn lastNameCol = new TableColumn("Cost");
        lastNameCol.setMinWidth(100);
        lastNameCol.setCellValueFactory(
                new PropertyValueFactory<Row, String>("cost"));

        TableColumn emailCol = new TableColumn("nextHop");
        emailCol.setMinWidth(200);
        emailCol.setCellValueFactory(
                new PropertyValueFactory<Row, String>("nextHop"));

        table.setItems(data);
        table.getColumns().addAll(firstNameCol, lastNameCol, emailCol);

        table.setLayoutX(550);
        table.setPrefHeight(350);

        Label  changeSeq = new Label(" Change Sequence Number:");
        TextField seqNumber = new TextField();
        changeSeq.setLayoutX(550);
        changeSeq.setLayoutY(400);
        seqNumber.setLayoutX(700);
        seqNumber.setLayoutY(395);
        seqNumber.setPrefColumnCount(5);
        Button sendNewLsaMess = new Button("Send");
        sendNewLsaMess.setLayoutX(800);
        sendNewLsaMess.setLayoutY(395);

        sendNewLsaMess.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                LMessage lMessage = router.generateLMessage();
                int seq = 0;
                if(seqNumber.getText() != null) {
                    seq = Integer.parseInt(seqNumber.getText());
                }
                lMessage.setSeq(seq);
                System.out.println(lMessage);
                try {
                    network.distributeLsaMessageFrom(router, lMessage);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                for(Router r: network.getListRouter()) {
//                    System.out.println(r.getName());
//                    for(LMessage l: r.getListLMessage()) {
//                        System.out.println(l);
//                    }
//                }
                stage.close();
            }
        });


        group.getChildren().addAll(changeSeq, seqNumber, sendNewLsaMess);
        group.getChildren().add(table);
        Scene scene = new Scene(group, 1000, 500);
        stage.setScene(scene);
        stage.setTitle("Link State Database in Router "+ router.getName());
        stage.show();

    }

    public static class Row{
        private final String destination;
        private final String cost;
        private final String nextHop;

        public String getDestination() {
            return destination;
        }

        public String getCost() {
            return cost;
        }

        public String getNextHop() {
            return nextHop;
        }

        public Row(String destination, String cost, String nextHop) {
            this.destination = destination;
            this.cost = cost;
            this.nextHop = nextHop;
        }
    }
    public Line getLineBetweenTwoNode(Button node1, Button node2, Map<Button, List<Line>> startMap, Map<Button, List<Line>> endMap) {
        if(startMap.containsKey(node1)) {
            List<Line> list1 = startMap.get(node1);
            if(endMap.containsKey(node2)) {
                List<Line> list2 = endMap.get(node2);
                for(Line temp: list1) {
                    for(Line temp2: list2) {
                        if(temp.equals(temp2)) {
                            return temp;
                        }
                    }
                }
            }
        }else {
            List<Line> list1 = startMap.get(node2);
            List<Line> list2 = endMap.get(node1);
            for(Line temp: list1) {
                for(Line temp2: list2) {
                    if(temp.equals(temp2)) {
                        return temp;
                    }
                }
            }
        }
        return null;
    }
    public static void main(String[] args) {
        launch(args);
    }
}
