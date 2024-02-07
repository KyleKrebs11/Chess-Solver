package puzzles.chess.model;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class ChessModel {
    /** the collection of observers of this model */
    private final List<Observer<ChessModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private ChessConfig currentConfig;

    private String filename;

    private String msg;

    private ArrayList<Integer> firstButton = new ArrayList<>();

    private ArrayList<Integer> secondButton = new ArrayList<>();

    private static final Background[] colors = {
            new Background(new BackgroundFill(Color.LIGHTGREEN, null, null)),
            new Background(new BackgroundFill(Color.BURLYWOOD, null, null)),
            new Background(new BackgroundFill(Color.LIGHTBLUE, null, null)),
            new Background(new BackgroundFill(Color.WHITE, null, null))
    };


    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<ChessModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String data) {
        for (var observer : observers) {
            observer.update(this, data);
        }
    }

    /**
     * Constructor to create currentconfig from file
     * @return
     */
    public ChessModel(String filename) throws IOException {
        this.currentConfig = new ChessConfig(filename);
        this.filename = filename;
    }

    /**
     * Displays a hint for the user
     */
    public void hint(){
        Solver solver = new Solver(currentConfig);
        ArrayList<Configuration> path = solver.solve();
        if (!path.isEmpty()){
            setCurrentConfig((ChessConfig) path.get(1));
            if (path.get(1).isSolution()){
                msg = "Congrats you won!\n" + "Please quit and restart";
            } else {
                msg = "Hint Given";
            }
        } else {
            msg = "There is no solution!\n" + "Please quit and restart";
        }
        alertObservers(msg);
    }

    /**
     * Resets the current game
     */
    public void reset() throws IOException {
        try {
            msg = "The game has been reset!";
            currentConfig = new ChessConfig(filename);
            alertObservers(msg);
        } catch (FileNotFoundException e){}
    }


    /**
     * Keeps track of amount of clicks
     * @return
     */
    public void checkNumButton(ArrayList<Integer> ints){
        if (firstButton.isEmpty()){
            firstButton = ints;
        } else {
            secondButton = ints;
            makeMove(firstButton, secondButton);
        }
    }

    /**
     * Moves chess piece
     * @return
     */
    public void makeMove(ArrayList<Integer> b1, ArrayList<Integer> b2){
        int b1Row = b1.get(0);
        int b1Col = b1.get(1);

        int b2Row = b2.get(0);
        int b2Col = b2.get(1);
        if (!b1.equals(b2)) {
            msg = isValid(b1Row, b1Col, b2Row, b2Col);
        } else {
            msg = "Invalid Capture";
        }
        firstButton = new ArrayList<>();
        secondButton = new ArrayList<>();
        alertObservers(msg);
    }

    /**
     * Checks if move is valid
     * @return String
     */
    public String isValid(int b1Row, int b1Col, int b2Row, int b2Col){
        String piece = currentConfig.getChessPiece(b1Row, b1Col);
        if (!piece.equals(".")) {
            String cappedPiece = currentConfig.getChessPiece(b2Row, b2Col);
            ChessConfig configCopy = new ChessConfig(currentConfig);
            configCopy.setChessPiece(b2Row, b2Col, piece);
            configCopy.setChessPiece(b1Row, b1Col, ".");
            ArrayList<Configuration> chessConfigs = new ArrayList<>();
            if (!cappedPiece.equals(".")) {
                switch (piece) {
                    case "K":
                        chessConfigs.addAll(currentConfig.checkKing(b1Row, b1Col));
                        break;
                    case "N":
                        chessConfigs.addAll(currentConfig.checkKnight(b1Row, b1Col));
                        break;
                    case "Q":
                        chessConfigs.addAll(currentConfig.checkQueen(b1Row, b1Col));
                        break;
                    case "R":
                        chessConfigs.addAll(currentConfig.checkRook(b1Row, b1Col, "R"));
                        break;
                    case "P":
                        chessConfigs.addAll(currentConfig.checkPawn(b1Row, b1Col));
                        break;
                    case "B":
                        chessConfigs.addAll(currentConfig.checkBishop(b1Row, b1Col, "B"));
                        break;
                }
                if (chessConfigs.contains(configCopy)) {
                    setCurrentConfig(configCopy);
                    if (currentConfig.isSolution()) {
                        msg = "Congrats you won!\n" + "Please quit and restart";
                    } else {
                        msg = piece + " captured " + cappedPiece + " at (" + b2Row + ", " + b2Col + ")";
                    }
                } else {msg = "Invalid Move";}

            } else{msg = "Not a Valid Capture";}

        }else{msg = "Invalid Cell Selected";}

        return msg;
    }

    /**
     * Reads new file for load
     * @return
     */
    public void fileReader(Stage stage) throws IOException {
        FileChooser chooser = new FileChooser();
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
        currentPath += File.separator + "data" + File.separator + "chess";  // or "hoppers"
        chooser.setInitialDirectory(new File(currentPath));
        File file = chooser.showOpenDialog(stage);
        this.filename = file.toString();
        if (file != null) {
            setCurrentConfig(new ChessConfig(file.toString()));
            this.msg = "Loaded: data/chess/" + file.getName();
            alertObservers(msg);
        }
    }

    /**
     * @return CurrentConfig
     */
    public ChessConfig getCurrentConfig(){return currentConfig;}

    /**
     * Sets the new current config
     */
    public void setCurrentConfig(ChessConfig config){this.currentConfig = config;}
}
