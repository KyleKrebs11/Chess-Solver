package puzzles.chess.gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import puzzles.chess.model.ChessConfig;
import puzzles.common.Observer;
import puzzles.chess.model.ChessModel;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.hoppers.model.HoppersModel;
import javafx.stage.FileChooser;
import java.nio.file.Paths;
import java.io.File;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class ChessGUI extends Application implements Observer<ChessModel, String> {
    private ChessModel model;

    /** The size of all icons, in square dimension */
    private final static int ICON_SIZE = 75;
    /** the font size for labels and buttons */
    private final static int FONT_SIZE = 12;

    private Stage stage;

    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";

    // for demonstration purposes
    private Image bishop = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"bishop.png"));
    private Image king = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"king.png"));
    private Image knight = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"knight.png"));
    private Image rook = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"rook.png"));
    private Image queen = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"queen.png"));
    private Image pawn = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"pawn.png"));

    private ArrayList<Integer> firstButton = new ArrayList<>();

    private ArrayList<Integer> secondButton = new ArrayList<>();

    private String msg;

    private String filename;

    private static final Background[] colors = {
            new Background(new BackgroundFill(Color.LIGHTGREEN, null, null)),
            new Background(new BackgroundFill(Color.BURLYWOOD, null, null)),
            new Background(new BackgroundFill(Color.LIGHTBLUE, null, null)),
            new Background(new BackgroundFill(Color.WHITE, null, null))
    };

    /** a definition of light and dark and for the button backgrounds */
    private static final Background LIGHT =
            new Background( new BackgroundFill(Color.WHITE, null, null));
    private static final Background DARK =
            new Background( new BackgroundFill(Color.MIDNIGHTBLUE, null, null));

    @Override
    public void init() throws IOException {
        // get the file name from the command line
        this.filename = getParameters().getRaw().get(0);
        this.model = new ChessModel(filename);
        this.model.addObserver(this);
        this.msg = "Loaded: " + filename;
    }

    @Override
    public void start(Stage stage) throws Exception {
        ChessConfig curConfig = this.model.getCurrentConfig();
        this.stage = stage;
        BorderPane bp = new BorderPane();

        // Need a Top HBox inside a BoarderPane for title
        // Need a middle GridPane that's in a BoarderPane
        // Need a bottom HBox with buttons for game controls


        HBox topHBox = setTopHBox();
        bp.setTop(topHBox);
        topHBox.setAlignment(Pos.CENTER);

        GridPane middleGridPane = setMiddleGridPane();
        bp.setCenter(middleGridPane);
        middleGridPane.setAlignment(Pos.CENTER);

        HBox bottomHBox = setBottomHBox();
        bottomHBox.setSpacing(15);
        bp.setBottom(bottomHBox);
        bottomHBox.setAlignment(Pos.CENTER);


        bp.setBackground(colors[2]);
        bp.setMinSize(500, 500);
        Scene scene = new Scene(bp);
        stage.setMinHeight(500);
        stage.setMinWidth(500);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Sets the top HBox for messages to be displayed
     * @return HBox
     */
    public HBox setTopHBox(){
        HBox hbox = new HBox();
        Label label = new Label(msg);
        label.setAlignment(Pos.CENTER);
        label.setBackground(colors[1]);
        label.setFont(new Font("Times New Roman", FONT_SIZE*2));
        hbox.getChildren().add(label);
        return hbox;
    }

    /**
     * Sets the Middle GridPane for the chessboard to be displayed
     * @return GridPane
     */
    public GridPane setMiddleGridPane(){
        ChessConfig curConfig = this.model.getCurrentConfig();
        GridPane gridPane = new GridPane();

        for (int i = 0; i < curConfig.getRowNums(); i++){
            for (int k = 0; k < curConfig.getColNums(); k++){
                // Creates new button and makes the chess board the right colors
                Button button = new Button();
                if ((i + k) % 2 == 0){
                    button.setBackground(DARK);
                } else {
                    button.setBackground(LIGHT);
                }

                button.setMinSize(ICON_SIZE, ICON_SIZE);
                button.setMaxSize(ICON_SIZE, ICON_SIZE);

                String piece = this.model.getCurrentConfig().getChessPiece(i, k);
                switch (piece) {
                    case "P":
                        button.setGraphic(new ImageView(pawn));
                        break;
                    case "N":
                        button.setGraphic(new ImageView(knight));
                        break;
                    case "R":
                        button.setGraphic(new ImageView(rook));
                        break;
                    case "Q":
                        button.setGraphic(new ImageView(queen));
                        break;
                    case "K":
                        button.setGraphic(new ImageView(king));
                        break;
                    case "B":
                        button.setGraphic(new ImageView(bishop));
                        break;
                }
                ArrayList<Integer> coors = new ArrayList<>();
                coors.add(i);
                coors.add(k);
                button.setOnAction(event -> this.model.checkNumButton(coors));
                gridPane.add(button, k, i);
            }
        }
        gridPane.setAlignment(Pos.CENTER);
        return gridPane;
    }

    /**
     * Sets the bottom HBox for buttons to be displayed
     * @return HBox
     */
    public HBox setBottomHBox(){
        HBox hBox = new HBox();

        Button b1 = new Button("Hint");
        b1.setFont(new Font("Times New Roman", FONT_SIZE));
        b1.setBackground(colors[1]);
        b1.setPrefSize(60, 30);
        b1.setOnAction(event -> {
            model.hint();
        });

        Button b2 = new Button("Load");
        b2.setBackground(colors[1]);
        b2.setFont(new Font("Times New Roman", FONT_SIZE));
        b2.setPrefSize(60, 30);
        b2.setOnAction(event -> {
            try {
                model.fileReader(this.stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Button b3 = new Button("Reset");
        b3.setFont(new Font("Times New Roman", FONT_SIZE));
        b3.setBackground(colors[1]);
        b3.setPrefSize(60, 30);
        b3.setOnAction(event -> {
            try {
                model.reset();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        hBox.getChildren().addAll(b1, b2, b3);
        return hBox;
    }


    @Override
    public void update(ChessModel chessModel, String msg) {
        this.model = chessModel;
        this.msg = msg;
        try {
            start(this.stage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.stage.sizeToScene();  // when a different sized puzzle is loaded
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
