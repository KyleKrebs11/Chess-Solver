package puzzles.chess.ptui;

import puzzles.chess.model.ChessConfig;
import puzzles.common.Observer;
import puzzles.chess.model.ChessModel;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class ChessPTUI implements Observer<ChessModel, String> {
    private ChessModel model;
    private String filename;

    public void init(String filename) throws IOException {
        this.filename = filename;
        this.model = new ChessModel(filename);
        this.model.addObserver(this);
        update(model, "Loaded: " + filename );
        displayHelp();
    }

    @Override
    public void update(ChessModel model, String data) {
        // for demonstration purposes
        System.out.print(data);
        System.out.println(model.getCurrentConfig());
    }

    private void displayHelp() {
        System.out.println( "\nh(int)              -- hint next move" );
        System.out.println( "l(oad) filename     -- load new puzzle file" );
        System.out.println( "s(elect) r c        -- select cell at r, c" );
        System.out.println( "q(uit)              -- quit the game" );
        System.out.println( "r(eset)             -- reset the current game" );
    }

    public void run() throws IOException {
        Scanner in = new Scanner( System.in );
        for ( ; ; ) {
            System.out.print( "> " );
            String line = in.nextLine();
            String[] words = line.split( "\\s+" );
            if (words.length > 0) {
                if (words[0].startsWith( "q" )) {
                    break;
                } else if (words[0].startsWith( "L" )){
                    this.model.setCurrentConfig(load(words[1]));
                } else if (words[0].startsWith( "S" )){
                    String msg = select(Integer.parseInt(words[1]), Integer.parseInt(words[2]));
                    update(model, msg);
                } else if (words[0].startsWith( "H" )){
                    String msg = hint();
                    update(model, msg);
                } else if (words[0].startsWith( "R" )){
                    String msg = reset();
                    update(model, msg);
                } else {
                    displayHelp();
                }
            }
        }
    }

    public String hint(){
        String msg = "";
        Solver solver = new Solver(model.getCurrentConfig());
        ArrayList<Configuration> path = solver.solve();
        if (!path.isEmpty()){
            this.model.setCurrentConfig((ChessConfig) path.get(1));
            msg += "Hint Given";
        } else  if (this.model.getCurrentConfig().isSolution()){
            msg += "Congrats you won!\n" + "Please quit and restart";
        } else {
            msg += "There is no solution!\n" + "Please quit and restart";
        }
        return msg;
    }

    public ChessConfig load(String filename) throws IOException {
        ChessConfig config = new ChessConfig(filename);
        System.out.print("File successfully read!");
        System.out.println(config);
        return config;
    }

    public String select(int row, int col){
        String msg = "";
        String piece = this.model.getCurrentConfig().getChessPiece(row, col);
        if (!piece.equals(".")){
            update(model, "Pick a cell to capture!");
            Scanner in = new Scanner(System.in);
            System.out.print("> ");

            String line = in.nextLine();
            String[] words = line.split( "\\s+" );
            String cappedPiece = this.model.getCurrentConfig().getChessPiece(Integer.parseInt(words[0]),
                                    Integer.parseInt(words[1]));
            ChessConfig configCopy = new ChessConfig(model.getCurrentConfig());
            configCopy.setChessPiece(Integer.parseInt(words[0]), Integer.parseInt(words[1]), piece);
            configCopy.setChessPiece(row, col, ".");

            ArrayList<Configuration> chessConfigs = new ArrayList<>();
            if (!cappedPiece.equals(".")){
                switch (piece){
                    case "K":
                        chessConfigs.addAll(this.model.getCurrentConfig().checkKing(row, col));
                        break;
                    case "N":
                        chessConfigs.addAll(this.model.getCurrentConfig().checkKnight(row, col));
                        break;
                    case "Q":
                        chessConfigs.addAll(this.model.getCurrentConfig().checkQueen(row, col));
                        break;
                    case "R":
                        chessConfigs.addAll(this.model.getCurrentConfig().checkRook(row, col, "R"));
                        break;
                    case "P":
                        chessConfigs.addAll(this.model.getCurrentConfig().checkPawn(row, col));
                        break;
                    case "B":
                        chessConfigs.addAll(this.model.getCurrentConfig().checkBishop(row, col, "B"));
                        break;
                }
                if (chessConfigs.contains(configCopy)){
                    this.model.setCurrentConfig(configCopy);
                    if (this.model.getCurrentConfig().isSolution()){
                        msg += "Congrats you won!\n" + "Please quit and restart";
                    } else {
                        msg += piece + " captured " + cappedPiece + " at (" + words[0] + ", " + words[1] + ")";
                    }
                } else {msg += "Invalid Move";}

            } else{msg += "Not a Valid Capture";}

        }else{msg += "Invalid Cell Selected";}

        return msg;
    }

    public String reset() throws IOException {
        String msg = "The game has been reset!";
        this.model.setCurrentConfig(load(filename));
        return msg;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java ChessPTUI filename");
        } else {
            try {
                ChessPTUI ptui = new ChessPTUI();
                ptui.init(args[0]);
                ptui.run();
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }
}

