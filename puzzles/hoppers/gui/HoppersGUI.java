package puzzles.hoppers.gui;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.hoppers.model.HoppersConfig;
import puzzles.hoppers.model.HoppersModel;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class HoppersGUI extends Application implements Observer<HoppersModel, String> {
    /**
     * The size of all icons, in square dimension
     */
    private final static int ICON_SIZE = 75;
    /**
     * the font size for labels and buttons
     */
    private final static int FONT_SIZE = 12;

    /**
     * The resources directory is located directly underneath the gui package
     */
    private final static String RESOURCES_DIR = "resources/";

    private String message;

    // for demonstration purposes
    private Image redFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR + "red_frog.png"));
    private Image greenFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR + "green_frog.png"));
    private Image lily_pad = new Image(getClass().getResourceAsStream(RESOURCES_DIR + "lily_pad.png"));
    private Image water = new Image(getClass().getResourceAsStream(RESOURCES_DIR + "water.png"));

    private HoppersModel model;

    private String firstClicked;
    private String nextClicked;
    private BorderPane borderPane;
    private String fileName;


    private Stage stage;
    private HoppersConfig firstConfig;

    public void init() throws IOException {
        String filename = getParameters().getRaw().get(0);

        //The view creates the model as part of its startup.
        this.model = new HoppersModel(filename);
        this.model.addObserver(this);
        this.firstConfig = this.model.getCurrentConfig();
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        //The view calls the model's addObserver and add itself to the model.
        // The model stores the view in its list of observers

        // create horizontal grid to hold label
        this.borderPane = new BorderPane();
        GridPane pondGrid = new GridPane();


        // create horizontal box for buttons
        HBox buttonBox = bottomBox();
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);
        borderPane.setBottom(buttonBox);

        HBox labelBox = topBox();
        labelBox.setAlignment(javafx.geometry.Pos.CENTER);
        borderPane.setTop(labelBox);


        //When the user makes an interaction the controller informs the model of it.
        for (int r = 0; r < model.getCurrentConfig().getCols(); r++) {
            for (int c = 0; c < model.getCurrentConfig().getRows(); c++) {
                //  Button button = new Button();
                // add action listeners to the buttons


                if (model.getCurrentConfig().getPond()[r][c].equals("G")) {
                    Button button = new Button("G" + r + "" + c);
                    button.setGraphic(new ImageView(greenFrog));
                    button.setOnAction((ActionEvent event) -> {
                        buttonClicked(event.getSource().toString());
                    });
                    button.setMinSize(ICON_SIZE, ICON_SIZE);
                    button.setMaxSize(ICON_SIZE, ICON_SIZE);
                    pondGrid.add(button, c, r);
                } else if (model.getCurrentConfig().getPond()[r][c].equals("R")) {
                    Button button = new Button("R" + r + "" + c);
                    button.setGraphic(new ImageView(redFrog));
                    button.setOnAction((ActionEvent event) -> {
                        buttonClicked(event.getSource().toString());
                    });
                    button.setMinSize(ICON_SIZE, ICON_SIZE);
                    button.setMaxSize(ICON_SIZE, ICON_SIZE);
                    pondGrid.add(button, c, r);

                } else if (model.getCurrentConfig().getPond()[r][c].equals(".")) {
                    Button button = new Button("." + r + "" + c);
                    button.setGraphic(new ImageView(lily_pad));
                    button.setOnAction((ActionEvent event) -> {
                        buttonClicked(event.getSource().toString());
                    });
                    button.setMinSize(ICON_SIZE, ICON_SIZE);
                    button.setMaxSize(ICON_SIZE, ICON_SIZE);
                    pondGrid.add(button, c, r);
                } else {
                    Button button = new Button("*" + r + "" + c);
                    button.setGraphic(new ImageView(water));
                    button.setOnAction((ActionEvent event) -> {
                        buttonClicked(event.getSource().toString());
                    });
                    button.setMinSize(ICON_SIZE, ICON_SIZE);
                    button.setMaxSize(ICON_SIZE, ICON_SIZE);
                    pondGrid.add(button, c, r);
                }


            }
        }


        borderPane.setCenter(pondGrid);

        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();
    }

    private HBox topBox() {
        // function to create horozontal box that will hold label
        HBox labelBox = new HBox();
        Label boxLabel = new Label(message);
        labelBox.getChildren().add(boxLabel);

        return labelBox;


    }

    private HBox bottomBox() {
        // function to create a horizontal box
        HBox buttonBox = new HBox();

        Button Load = new Button("Load");
        Load.setOnAction((ActionEvent event) -> {

            try {
                load();
            }catch (Exception e){

            }

        });


        buttonBox.getChildren().add(Load);

        Button restart = new Button("Reset");
        restart.setOnAction((ActionEvent event) -> {
            reset(event.getSource().toString());
        });

        buttonBox.getChildren().add(restart);

        Button hint = new Button("Hint");
        hint.setOnAction((ActionEvent event) -> {
            hint((event.getSource().toString()));
        });
        buttonBox.getChildren().add(hint);


        buttonBox.setSpacing(10);
        return buttonBox;
    }

    private void hint(String s) {
        Solver hoppSolver = new Solver(model.getCurrentConfig());
        ArrayList<Configuration> solutionPath = hoppSolver.solve();
        this.model.setCurrentConfig((HoppersConfig) solutionPath.get(1));

        update(this.model, "Next Step!");

        if (model.getCurrentConfig().isSolution()) {
            gameWon();
        }


    }

    private void load() throws IOException {

        FileChooser chooser = new FileChooser();
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
        currentPath += File.separator + "data" + File.separator + "hoppers";
        chooser.setInitialDirectory(new File(currentPath));
        File file = chooser.showOpenDialog(stage);
        this.fileName = file.toString();
        if (file!= null) {
            this.model = new HoppersModel(fileName);
            this.message= "Loaded: data/hoppers/ " + file.getName();
            update(model,message);
        }

    }

    private void reset(String s) {
        this.model.setCurrentConfig(firstConfig);
        update(this.model, "Game Reset!");

    }

    private void buttonClicked(String toString) {
        //
        int start = toString.indexOf("'") + 1;
        int end = toString.lastIndexOf("'");
        String cutButtonClicked = toString.substring(start, end);

        System.out.println(toString);
        if (this.firstClicked == null) {
            this.firstClicked = cutButtonClicked;
        } else if (this.nextClicked == null) {
            this.nextClicked = cutButtonClicked;

            bothClicked(firstClicked, nextClicked);
        }


    }

    private void bothClicked(String firstClicked, String nextClicked) {
        if (firstClicked.contains("G") || firstClicked.contains("R") && nextClicked.contains(".")) {


            int currentR = Integer.parseInt(String.valueOf(firstClicked.charAt(1)));
            int currentC = Integer.parseInt(String.valueOf(firstClicked.charAt(2)));
            String frogType = String.valueOf(firstClicked.charAt(0));

            int r = Integer.parseInt(String.valueOf(nextClicked.charAt(1)));
            int c = Integer.parseInt(String.valueOf(nextClicked.charAt(2)));

            if (r - 1 == currentR || c - 1 == currentC) {
                invalidMove();
                return;
            }


            HoppersConfig copyConfig = new HoppersConfig(model.getCurrentConfig());
            copyConfig.changeCell(r, c, model.getCurrentConfig().getPond()[currentR][currentC]);
            copyConfig.changeCell(currentR, currentC, ".");

            // NW
            if (currentR > r && currentC > c) {
                if (!model.getCurrentConfig().getPond()[currentR - 1][currentC - 1].equals("G")) {
                    invalidMove();
                    return;
                } else {
                    copyConfig.changeCell(currentR - 1, currentC - 1, ".");
                }
                copyConfig.changeCell(currentR - 1, currentC - 1, ".");
            }//N
            else if (currentR > r && currentC == c) {
                if (!model.getCurrentConfig().getPond()[currentR - 2][currentC - 1].equals("G")) {
                    invalidMove();
                    return;
                } else {
                    copyConfig.changeCell(currentR - 2, currentC, ".");
                }
                // NE
            } else if (currentR > r && c > currentC) {

                if (!model.getCurrentConfig().getPond()[currentR - 1][currentC + 1].equals("G")) {
                    invalidMove();
                    return;
                } else {
                    copyConfig.changeCell(currentR - 1, currentC + 1, ".");
                }
            } // E
            else if (currentR == r && c > currentC) {

                if (!model.getCurrentConfig().getPond()[currentR][currentC + 2].equals("G")) {
                    invalidMove();
                    return;
                } else {
                    copyConfig.changeCell(currentR, currentC + 2, ".");
                }

            }
            //S
            else if (currentR < r && c == currentC) {

                if (!model.getCurrentConfig().getPond()[currentR + 2][currentC].equals("G")) {
                    invalidMove();
                    return;
                } else {
                    copyConfig.changeCell(currentR + 2, currentC, ".");
                }


            } // SE
            else if (r > currentR && c > currentC) {

                if (!model.getCurrentConfig().getPond()[currentR + 1][currentC + 1].equals("G")) {
                    invalidMove();
                    return;
                } else {
                    copyConfig.changeCell(currentR + 1, currentC + 1, ".");
                }


            } //SW
            else if (r > currentR && c < currentC) {

                if (!model.getCurrentConfig().getPond()[currentR + 1][currentC - 1].equals("G")) {
                    invalidMove();
                    return;
                } else {
                    copyConfig.changeCell(currentR + 1, currentC - 1, ".");
                }


            }


            this.model.setCurrentConfig(copyConfig);
            update(this.model, "Jumped From (" + currentR + "," + currentC + ") to (" + r + "," + c + ")");

        }
        if (model.getCurrentConfig().isSolution()) {
            gameWon();
        }

        // reset button clicked
        this.firstClicked = null;
        this.nextClicked = null;

    }

    private void invalidMove() {
        // change label to indicate invalid move
        update(this.model, "Invalid Move!");

        // reset button clicked
        this.firstClicked = null;
        this.nextClicked = null;
    }

    private void gameWon() {
        // function that displays message when the game is won
        update(this.model, "Game Won!");

    }

    @Override
    public void update(HoppersModel hoppersModel, String msg) {
        this.model = hoppersModel;
        this.message = msg;

        try {
            start(this.stage);
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
        this.stage.sizeToScene();
    }


    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {

            Application.launch(args);

        }
    }
}
