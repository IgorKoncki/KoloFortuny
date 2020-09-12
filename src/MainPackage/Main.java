package MainPackage;

import com.sun.javafx.cursor.StandardCursorFrame;
import com.sun.javafx.fxml.builder.TriangleMeshBuilder;
import javafx.application.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.text.*;
import javafx.stage.*;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;

public class Main extends Application {
    public int chancesWasted = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Base of Main Scene
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(800);
        primaryStage.setHeight(800);
        primaryStage.setWidth(1000);
        BorderPane mainBorderPane = new BorderPane();
        mainBorderPane.setBackground(new Background(new BackgroundFill(new Color(0.9, 0.86, 0.8, 0.8), new CornerRadii(25), Insets.EMPTY)));
        Scene mainScene = new Scene(mainBorderPane);
        VBox mainTop = new VBox();
        mainTop.setAlignment(Pos.TOP_RIGHT);
        mainBorderPane.setTop(mainTop);

        Pane mainBottom = new Pane();
        mainBorderPane.setBottom(mainBottom);
        mainBottom.prefHeightProperty().bind(mainBorderPane.heightProperty().divide(5).multiply(2));
        mainBottom.prefWidthProperty().bind(mainBorderPane.widthProperty());

        //Base of Options Scene
        BorderPane optionsBorderPane = new BorderPane();
        Scene optionsScene = new Scene(optionsBorderPane);

        HBox optionsTop = new HBox();
        optionsTop.setAlignment(Pos.TOP_RIGHT);
        optionsBorderPane.setTop(optionsTop);

        //Connect the Scenes
        //connect size

        //Go to options button
        Button optionsButton = new Button("Options");
        optionsButton.setOnAction(
                (e) -> {
                    primaryStage.setScene(optionsScene);
                }
        );
        optionsButton.fontProperty().setValue(new Font("Century", 14));
        optionsButton.setCursor(Cursor.HAND);
        mainTop.getChildren().add(optionsButton);

        //Go back button
        Button goBackButton = new Button("Go Back");
        goBackButton.setOnAction(
                (e) -> {
                    primaryStage.setScene(mainScene);
                }
        );
        goBackButton.fontProperty().setValue(new Font("Century", 14));
        goBackButton.setCursor(Cursor.HAND);
        optionsTop.getChildren().add(goBackButton);


        //Setting up wheel
        Circle wheelCircle = new Circle();
        wheelCircle.setRadius(300);
        wheelCircle.setFill(new Color(0.4, 0.025, 0, 0.8)); //Beige
        mainBottom.getChildren().add(wheelCircle);
        wheelCircle.centerXProperty().bind(mainBottom.widthProperty().divide(2));
        wheelCircle.centerYProperty().bind(mainBottom.heightProperty().subtract(mainBottom.heightProperty().multiply(0.1)));
        Circle smallCircle = new Circle();
        smallCircle.setRadius(150);
        smallCircle.setFill(new Color(0.9, 0.75, 0.6, 1));
        mainBottom.getChildren().add(smallCircle);
        smallCircle.centerXProperty().bind(mainBottom.widthProperty().divide(2));
        smallCircle.centerYProperty().bind(mainBottom.heightProperty().subtract(mainBottom.heightProperty().multiply(0.1)));

        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(new Double[]{
                0.0, 0.0,
                20.0, 10.0,
                10.0, 20.0});
        mainBottom.getChildren().add(triangle);
        triangle.setRotate(225);
        triangle.translateXProperty().bind(mainBottom.widthProperty().divide(2).subtract(10));
        triangle.translateYProperty().bind(mainBottom.heightProperty().subtract(mainBottom.heightProperty().multiply(0.1)).subtract(310));

        Wheel wheel = new Wheel(mainBottom);

        //Spin Button
        Button spin = new Button("SPIN!");
        spin.setCursor(Cursor.HAND);
        spin.fontProperty().setValue(new Font("Century", 20));
        mainBottom.getChildren().add(spin);
        spin.setTranslateY(mainBottom.getHeight() - mainBottom.getHeight() * 0.1);
        spin.translateXProperty().bind(mainBottom.widthProperty().divide(2).subtract(spin.widthProperty().divide(2)));
        spin.translateYProperty().bind(mainBottom.heightProperty().subtract(mainBottom.heightProperty().multiply(0.1)).subtract(20));

        //Point Counter
        TextArea counter = new TextArea();
        counter.setEditable(false);
        counter.setText("0");
        counter.setFont(new Font("Century", 20));
        counter.setPrefSize(50, 50);
        mainBottom.getChildren().add(counter);
        counter.translateXProperty().bind(mainBottom.widthProperty().subtract(counter.widthProperty()));
        counter.setTranslateY(0);

        //ITN text and chance counter
        Text itn = new Text();
        itn.setOpacity(0);
        itn.setText("ITN");
        itn.setFill(Color.RED);
        itn.setFont(new Font("Century", 100));
        mainBottom.getChildren().add(itn);
        itn.setTranslateX(0);
        itn.setTranslateY(60);

        //Setting up options change panels
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        optionsBorderPane.setCenter(gp);
        gp.setPadding(new Insets(5));
        gp.setHgap(5);
        gp.setVgap(5);
        ObservableList<String> opcja1 = FXCollections.observableArrayList();
        ListView<String> o1LV = new ListView<>(opcja1);
        gp.add(o1LV, 0, 0);

        ObservableList<String> opcja2 = FXCollections.observableArrayList();
        ListView<String> o2LV = new ListView<>(opcja2);
        gp.add(o2LV, 2, 0);

        Button rButton = new Button(">");
        rButton.setOnAction(
                e -> {
                    String tmp = o1LV.getSelectionModel().getSelectedItem();
                    if (tmp != null) {
                        o1LV.getSelectionModel().clearSelection();
                        opcja1.remove(tmp);
                        opcja2.add(tmp);
                    }
                }
        );
        rButton.setCursor(Cursor.HAND);
        gp.add(rButton, 1, 1);

        Button lButton = new Button("<");
        lButton.setOnAction(
                e -> {
                    String tmp = o2LV.getSelectionModel().getSelectedItem();
                    //o2LV.getSelectionModel().getSelectedItems();

                    if (tmp != null) {
                        if (opcja2.size() != 1) {
                            o2LV.getSelectionModel().clearSelection();
                            opcja2.remove(tmp);
                            opcja1.add(tmp);
                        } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Uwaga!");
                            alert.setHeaderText("Nie można wykonac operacji");
                            alert.setContentText("W prawym oknie musi byc co najmniej jedno haslo!");
                            alert.showAndWait();
                        }
                    }
                }
        );
        lButton.setCursor(Cursor.HAND);
        gp.add(lButton, 1, 2);

        //Setting up memory
        File optionsMemory = new File("MemoryFolder\\Memory.txt");
        if (!optionsMemory.exists()) {
            try {
                FileWriter fw = new FileWriter(optionsMemory);
                fw.write("");
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FileInputStream fis = new FileInputStream(optionsMemory);
                if (fis.available() > 0) {
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    Object obj;
                    int i = 0;
                    while (fis.available() > 0 && i < 10) {
                        obj = ois.readObject();
                        if (((Riddle) obj).getInUse()) {
                            opcja2.add(((Riddle) obj).getText());
                        } else {
                            opcja1.add(((Riddle) obj).getText());
                        }
                        i++;
                    }
                    ois.close();
                }
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        }
        if (opcja2.isEmpty()) {
            opcja2.add("Ala ma kota");
        }

        //Seting up text area for new riddles
        TextArea riddleProvider = new TextArea();
        riddleProvider.setPrefColumnCount(1);
        riddleProvider.setWrapText(false);
        riddleProvider.setMaxHeight(25);
        riddleProvider.setFont(new Font("Papyrus", 20));
        gp.add(riddleProvider, 0, 3);

        Button addRiddleButton = new Button("^");
        addRiddleButton.setOnAction(
                (e) -> {
                    String text = riddleProvider.getText();
                    if (text != null) {
                        boolean badSignsPresent = false;
                        for (int i = 0; i < text.length(); i++) {
                            if (!((text.charAt(i) >= 'a' && text.charAt(i) <= 'z') || (text.charAt(i) >= 'A' && text.charAt(i) <= 'Z') || text.charAt(i) == ' ')) {
                                badSignsPresent = true;
                            }
                        }
                        if (!badSignsPresent) {
                            opcja1.add(text);
                            riddleProvider.clear();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Uwaga!");
                            alert.setHeaderText("Tekst zawiera niedozowlone znaki");
                            alert.setContentText("Usun wszystkie znaki poza literami i spacjami.");
                            alert.showAndWait();
                        }
                    }
                }
        );
        addRiddleButton.setCursor(Cursor.HAND);
        gp.add(addRiddleButton, 0, 2);

        //MainStage riddle and mechanisms around it
        FlowPane mainCenter = new FlowPane();
        mainCenter.setAlignment(Pos.CENTER);
        ObservableList<HBox> wordList = FXCollections.observableArrayList();
        ;
        ObservableList<HiddenLetterLabel> letterList = FXCollections.observableArrayList();
        ;
        resetRiddle(mainCenter, letterList, wordList, opcja2);
        mainBorderPane.setCenter(mainCenter);


        //On exit save to memory
        primaryStage.setOnCloseRequest(
                (e) -> {
                    //Save to memory
                    ArrayList<Riddle> riddleList = new ArrayList<>();
                    for (String o1 : opcja1) {
                        riddleList.add(new Riddle(o1));
                    }
                    for (String o2 : opcja2) {
                        riddleList.add(new Riddle(o2, true));
                    }
                    try {
                        FileWriter writer = new FileWriter(optionsMemory);
                        writer.write("");
                        writer.close();
                        FileOutputStream fos = new FileOutputStream(optionsMemory);
                        ObjectOutputStream oos = new ObjectOutputStream(fos);
                        for (Riddle riddle : riddleList) {
                            oos.writeObject(riddle);
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    //EXIT
                    try {
                        stop();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
        );


        //Setting up Task and Service
            /*Task<Void> task = new Task(){
                public Void call(){
                    mainScene.setCursor(Cursor.WAIT);
                    int rand = (int)(Math.random()*50+50);
                    for(int i = 0; i<rand; i++){
                        wheel.spin();
                        try {
                            Thread.sleep(100);
                        }catch(InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                    for(int i = 2; i<10; i++){
                        wheel.spin();
                        try {
                            Thread.sleep(100*(i/2));
                        }catch(InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                    mainScene.setCursor(Cursor.DEFAULT);
                    spin.setDisable(false);
                    return null;
                }
            };*/
        Service<Void> spinning = new Service<Void>() {

            protected Task createTask() {
                Task task = new Task() {
                    public Void call() {
                        mainScene.setCursor(Cursor.WAIT);
                        int rand = (int) (Math.random() * 50 + 20);
                        for (int i = 0; i < rand; i++) {
                            wheel.spin();
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        for (int i = 0; i < 10; i++) {
                            wheel.spin();
                            try {
                                Thread.sleep(100 + i * 50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        mainScene.setCursor(Cursor.DEFAULT);
                        return null;
                    }
                };
                task.setOnSucceeded(
                        (e) -> {
                            TextInputDialog giveLetterDialog = new TextInputDialog(); // WYKONAJ WLASNE OKNO
                            giveLetterDialog.setTitle("Pora na zgadywanie!");
                            giveLetterDialog.setHeaderText("Dostaniesz " + wheel.head.points + " jezeli zgadniesz poprawnie.");
                            giveLetterDialog.setContentText("Podaj jedna litere:");
                            giveLetterDialog.setWidth(100);
                            giveLetterDialog.setHeight(100);
                            Optional<String> guess = giveLetterDialog.showAndWait();
                            if (guess.isPresent()) {
                                boolean gotLetter = false;
                                for (HiddenLetterLabel letterLabel : letterList) {
                                    if ((("" + letterLabel.getLetter()).equals(guess.get()) || ("" + (char) (letterLabel.getLetter() + ('A' - 'a'))).equals(guess.get()) || ("" + (char) (letterLabel.getLetter() + ('a' - 'A'))).equals(guess.get()))
                                            && !letterLabel.isLetterShown()) {
                                        letterLabel.showLetter();
                                        gotLetter = true;
                                    }
                                }
                                if (!gotLetter) {
                                    chancesWasted++;
                                    if (chancesWasted >= 3) {
                                        itn.setOpacity(1);
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setTitle("Wynik");
                                        alert.setHeaderText(null);
                                        alert.setContentText("Przegrales. Powodzenia za rok!");
                                        alert.showAndWait();
                                        resetGame(mainCenter, letterList, wordList, opcja2, itn, wheel, counter, optionsButton);
                                    } else {
                                        itn.setOpacity(chancesWasted * 0.34);
                                    }
                                } else {
                                    boolean isEnd = true;
                                    for (HiddenLetterLabel letterLabel : letterList) {
                                        if (!letterLabel.isLetterShown()) {
                                            isEnd = false;
                                        }
                                    }
                                    if (!isEnd) {
                                        counter.setText("" + (Integer.parseInt(counter.getText()) + wheel.head.points));
                                    } else {
                                        counter.setText("" + (Integer.parseInt(counter.getText()) + wheel.head.points));
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setTitle("Wynik");
                                        alert.setHeaderText(null);
                                        alert.setContentText("Wygrales majac "+ counter.getText() + " punktow! Gratulacje!");
                                        alert.showAndWait();
                                        resetGame(mainCenter, letterList, wordList, opcja2, itn, wheel, counter, optionsButton);
                                    }
                                }
                            } else {
                                chancesWasted++;
                                if (chancesWasted >= 3) {
                                    itn.setOpacity(1);
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("Wynik");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Przegrales. Powodzenia za rok!");
                                    alert.showAndWait();
                                    resetGame(mainCenter, letterList, wordList, opcja2, itn, wheel, counter, optionsButton);
                                } else {
                                    itn.setOpacity(chancesWasted * 0.34);
                                }
                            }

                            spin.setDisable(false);
                        }

                );
                return task;
            }
        };

        //Spin Button Listener
        spin.setOnAction(
                (e) -> {
                    spin.setDisable(true);
                    optionsButton.setDisable(true);
                    spinning.restart();
                }
        );

        //Reset and apply buttton
        Button raaButton = new Button("Zastosuj");
        raaButton.setOnAction(
                e -> {
                    resetRiddle(mainCenter, letterList, wordList, opcja2);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Gotowe!");
                    alert.setHeaderText("Gotowe!");
                    alert.setContentText("Gra zostala uruchomiona ponownie na obecnych opcjach");

                    alert.showAndWait();
                }
        );
        raaButton.setCursor(Cursor.HAND);
        gp.add(raaButton, 3, 2);

        //Reset options button
        Button robButton = new Button("Resetuj Hasla");
        robButton.setOnAction(
                (e) -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Uwaga!");
                    alert.setHeaderText("Ta akcja usunie wszystkie hasla dodane przez uzytkownika.");
                    alert.setContentText("Czy napewno chcesz kontynuowac?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        opcja1.clear();
                        opcja2.clear();
                        opcja2.add("Ala ma kota");
                    }
                }
        );
        robButton.setCursor(Cursor.HAND);
        gp.add(robButton,3,0);


        //Setting up the Primary Stage
        primaryStage.setTitle("Kolo Fortuny");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    //Reset Game function
    void resetGame(FlowPane mainCenter, ObservableList<HiddenLetterLabel> letterList, ObservableList<HBox> wordList, ObservableList<String> opcja2, Text itn, Wheel wheel, TextArea counter, Button optionsButton) {
        //mainBottom
        itn.setOpacity(0);
        chancesWasted = 0;
        while (wheel.head.points != 500)
            wheel.spin();
        counter.setText("0");


        //mainCenter
        resetRiddle(mainCenter, letterList, wordList, opcja2);
        optionsButton.setDisable(false);
    }

    void resetRiddle(FlowPane mainCenter, ObservableList<HiddenLetterLabel> letterList, ObservableList<HBox> wordList, ObservableList<String> opcja2) {
        mainCenter.getChildren().clear();

        String mainRiddle = opcja2.get((int) (Math.random() * opcja2.size()));
        boolean lastWasSpace = false;
        wordList.clear();
        letterList.clear();
        HBox helpHBox = new HBox();
        HiddenLetterLabel helpLabel = null;
        for (int i = 0; i < mainRiddle.length(); i++) {
            if (mainRiddle.charAt(i) == ' ' && !lastWasSpace) {
                if (!(helpHBox.getChildren().size() == 0)) {
                    wordList.add(helpHBox);
                    helpHBox = new HBox();
                }
                helpLabel = new HiddenLetterLabel();
                helpLabel.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
                helpLabel.setPrefSize(70, 91);
                helpLabel.setStyle("-fx-padding: 0;" +
                        "-fx-border-style: solid;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-insets: 0;" +
                        "-fx-border-color: black;");
                helpHBox.getChildren().add(helpLabel);
                lastWasSpace = true;
            } else {
                if (!(mainRiddle.charAt(i) == ' ')) {
                    if (lastWasSpace) {
                        wordList.add(helpHBox);
                        helpHBox = new HBox();
                    }
                    helpLabel = new HiddenLetterLabel(mainRiddle.charAt(i));
                    letterList.add(helpLabel);
                    helpLabel.setAlignment(Pos.CENTER);
                    helpLabel.setPrefSize(70, 65);
                    helpLabel.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
                    helpLabel.setStyle("-fx-padding: 0;" +
                            "-fx-border-style: solid;" +
                            "-fx-border-width: 2;" +
                            "-fx-border-insets: 0;" +
                            "-fx-border-color: black;");
                    helpLabel.setFont(new Font("Century", 70));
                    helpHBox.getChildren().add(helpLabel);
                    lastWasSpace = false;
                }
            }
        }
        wordList.add(helpHBox);

        for (HBox tmp : wordList) {
            mainCenter.getChildren().add(tmp);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
