package me.sean.sudoku.view;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import me.sean.sudoku.model.Model;

public class View extends Application implements Observer<Model, String>{

    private Model model;
    private BorderPane borderPane = new BorderPane();
    public void init(Stage stage) {
        Parameters p = this.getParameters();
        this.model = new Model(p.getRaw().get(0), this);
        System.out.println(model);
        borderPane.setCenter(initializeGrid());
        Label label = initializeTop();
        borderPane.setTop(label);
        borderPane.setAlignment(label, Pos.CENTER);
        Scene scene = new Scene(borderPane);
        scene.setOnKeyPressed(event -> {
            int code = event.getCode().getCode();
            switch(code) {
                case 49, 50, 51, 52, 53, 54, 55, 56, 57:
                    this.model.setCell(code-48);
                    break;
                case 8:
                    this.model.setCell(null);
            }

        });
        stage.setScene(scene);
    }
    public Label initializeTop() {
        Label label = new Label(this.model.getGameState());
        label.setStyle("""
                    -fx-font-size: 15;
                    -fx-font-family: Menlo;
""");
        return label;
    }

    public GridPane initializeGrid() {
        GridPane pane = new GridPane();
        int size = this.model.getSize();
        for(int i = 0; i < size*size; i++) {
            for(int j = 0; j < size*size; j++) {
                Label item = new Label(String.valueOf(this.model.get(i, j)));
                item.setStyle( """
                            -fx-border-style: solid inside;
                            -fx-border-width: 1;
                            -fx-border-color: black;
                """);
                item.setFont(new Font("Menlo", 30));
                if (this.model.isCursor(i, j)) {
                    item.setBackground(new Background(new BackgroundFill(Color.LIGHTCYAN, null, null)));
                }
                item.setMinSize(50, 50);
                item.setAlignment(Pos.CENTER);
                int finalI = i;
                int finalJ = j;
                item.setOnMouseClicked(event -> {
                    this.model.setCursor(finalI, finalJ);
                });
                pane.add(item, i, j);
            }
        }
        pane.setAlignment(Pos.CENTER);
        return pane;
    }


    @Override
    public void update(Model model, String message) {
        borderPane.setCenter(initializeGrid());
        Label label = initializeTop();
        borderPane.setTop(label);
        borderPane.setAlignment(label, Pos.CENTER);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.setTitle("Sudoku");
        primaryStage.show();
    }

    public static void main(String[] args) {
        if(args.length > 1) {
            System.out.println("Usage: java View (filename)");
        }
        launch(args);
    }
}
