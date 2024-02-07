package puzzles.chess.model;

import puzzles.common.solver.Configuration;
import puzzles.strings.StringsConfig;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

// TODO: implement your ChessConfig for the common solver

public class ChessConfig implements Configuration {

    private int rowNums;
    private int colNums;
    private String[][] chessBoard;
    private String filename;

    public ChessConfig(String filename) throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(filename))){
            this.filename = filename;
            String line = in.readLine();
            String[] fields = line.split("\\s+");
            this.rowNums = Integer.parseInt(fields[0]);
            this.colNums = Integer.parseInt(fields[1]);

            this.chessBoard = new String[rowNums][colNums];
            for (int i = 0; i < rowNums; i++) {
                line = in.readLine();
                fields = line.split("\\s+");
                for (int k = 0; k < colNums; k++) {
                    chessBoard[i][k] = fields[k];
                }
            }
        } catch (FileNotFoundException e){}
    }

    public ChessConfig(ChessConfig other){
        this.rowNums = other.rowNums;
        this.colNums = other.colNums;
        String[][] copiedArr = new String[rowNums][colNums];
        for(int i = 0; i < rowNums; i++){
            for(int k = 0; k < colNums; k++){
                copiedArr[i][k] = other.chessBoard[i][k];
            }
        }
        this.chessBoard = copiedArr;
    }

    @Override
    public boolean isSolution() {
        // Checks to see if there is only one piece left
        // and if there isn't, is there a valid move?
        // Nested for loop. Keep a counter. If the counter exceeds 1
        // return false
        int counter = 0;
        for (int i = 0; i < rowNums; i++) {
            for (int k = 0; k < colNums; k++) {
                if (!this.chessBoard[i][k].equals(".")){
                    counter++;
                    if (counter > 1){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        // Goes through each piece on the board and
        // finds all spots that are valid captures
        ArrayList<Configuration> list = new ArrayList<>();

        // Nested for loop, check each spot. If there is a piece
        // run the respective check Piece method and add it to Collection
        // of Configs

        for (int i = 0; i < rowNums; i++){
            for (int k = 0; k < colNums; k++){
                String piece = chessBoard[i][k];
                switch (piece) {
                    case "P":
                        ArrayList<Configuration> pawnMoves = checkPawn(i, k);
                        list.addAll(pawnMoves);
                        break;
                    case "N":
                        ArrayList<Configuration> knightMoves = checkKnight(i, k);
                        list.addAll(knightMoves);
                        break;
                    case "R":
                        ArrayList<Configuration> rookMoves = checkRook(i, k, "R");
                        list.addAll(rookMoves);
                        break;
                    case "Q":
                        ArrayList<Configuration> queenMoves = checkQueen(i, k);
                        list.addAll(queenMoves);
                        break;
                    case "K":
                        ArrayList<Configuration> kingMoves = checkKing(i, k);
                        list.addAll(kingMoves);
                        break;
                    case "B":
                        ArrayList<Configuration> bishopMoves = checkBishop(i, k, "B");
                        list.addAll(bishopMoves);
                        break;
                }
            }
        }
        // Don't need to worry about empty collection of neighbors
        // in this method.

        return list;
    }

    // Need methods for each piece that checks its valid moves
    // For each one, find position, make a valid restrictions that
    // is used as a check ( move the piece, check to make sure its
    // within valid restrictions and capturing a piece). If both premises
    // are satisfied, add it to the collection.

    public ArrayList<Configuration> checkBishop(int row, int col, String letter){
        ChessConfig config = new ChessConfig(this);
        String[][] grid = config.chessBoard;
        ArrayList<Configuration> validCaps = new ArrayList<Configuration>();

        // 4 for loops that go in all different directions, at each spot check for a piece
        // and if it has hit the borders. If it hits the border, there are no validCaps in
        // that direction. If it hits a piece, capture that piece and add it to validCaps

        // Goes up and right
        for (int i = row-1, k = col+1; i >= 0 && k < this.colNums; i--, k++){
            if (!grid[i][k].equals(".")){
                ChessConfig newConfig = new ChessConfig(this);
                String[][] copiedGrid = newConfig.chessBoard;
                copiedGrid[row][col] = ".";
                copiedGrid[i][k] = letter;
                validCaps.add(newConfig);
            }
        }
        // Goes up and left
        for (int i = row-1, k = col-1; i >= 0 && k >= 0; i--, k--){
            if (!grid[i][k].equals(".")){
                ChessConfig newConfig = new ChessConfig(this);
                String[][] copiedGrid = newConfig.chessBoard;
                copiedGrid[row][col] = ".";
                copiedGrid[i][k] = letter;
                validCaps.add(newConfig);
            }
        }
        // Goes down and right
        for (int i = row+1, k = col+1; i < this.rowNums && k < this.colNums; i++, k++){
            if (!grid[i][k].equals(".")){
                ChessConfig newConfig = new ChessConfig(this);
                String[][] copiedGrid = newConfig.chessBoard;
                copiedGrid[row][col] = ".";
                copiedGrid[i][k] = letter;
                validCaps.add(newConfig);
            }
        }
        // Goes down and left
        for (int i = row+1, k = col-1; i < this.rowNums && k >= 0; i++, k--){
            if (!grid[i][k].equals(".")){
                ChessConfig newConfig = new ChessConfig(this);
                String[][] copiedGrid = newConfig.chessBoard;
                copiedGrid[row][col] = ".";
                copiedGrid[i][k] = letter;
                validCaps.add(newConfig);
            }
        }


        return validCaps;
    }

    public ArrayList<Configuration> checkRook(int row, int col, String letter){
        ChessConfig config = new ChessConfig(this);
        String[][] grid = config.chessBoard;
        ArrayList<Configuration> validCaps = new ArrayList<Configuration>();

        // 4 for loops that go in all different directions, at each spot check for a piece
        // and if it has hit the borders. If it hits the border, there are no validCaps in
        // that direction. If it hits a piece, capture that piece and add it to validCaps

        // Goes straight up.
        for (int i = row-1; i >= 0; i--){
            if (!grid[i][col].equals(".")){
                ChessConfig newConfig = new ChessConfig(this);
                String[][] copiedGrid = newConfig.chessBoard;
                copiedGrid[row][col] = ".";
                copiedGrid[i][col] = letter;
                validCaps.add(newConfig);
                break;
            }
        }
        // Goes straight down
        for (int i = row+1; i < this.rowNums; i++){
            if (!grid[i][col].equals(".")){
                ChessConfig newConfig = new ChessConfig(this);
                String[][] copiedGrid = newConfig.chessBoard;
                copiedGrid[row][col] = ".";
                copiedGrid[i][col] = letter;
                validCaps.add(newConfig);
                break;
            }
        }
        // Goes right
        for (int i = col+1; i < this.colNums; i++){
            if (!grid[row][i].equals(".")){
                ChessConfig newConfig = new ChessConfig(this);
                String[][] copiedGrid = newConfig.chessBoard;
                copiedGrid[row][col] = ".";
                copiedGrid[row][i] = letter;
                validCaps.add(newConfig);
                break;
            }
        }
        // Goes left
        for (int i = col-1; i >= 0; i--){
            if (!grid[row][i].equals(".")){
                ChessConfig newConfig = new ChessConfig(this);
                String[][] copiedGrid = newConfig.chessBoard;
                copiedGrid[row][col] = ".";
                copiedGrid[row][i] = letter;
                validCaps.add(newConfig);
                break;
            }
        }

        return validCaps;
    }

    public ArrayList<Configuration> checkPawn(int row, int col){
        ChessConfig config = new ChessConfig(this);
        String[][] grid = config.chessBoard;
        ArrayList<Configuration> validCaps = new ArrayList<Configuration>();

        boolean leftCap = true;
        boolean rightCap = true;
        if (row == 0){leftCap = false;  rightCap = false;}
        if (col == 0){leftCap = false;}
        if (col == this.colNums-1){rightCap = false;}

        if (leftCap && !grid[row-1][col-1].equals(".")){
            ChessConfig newConfig = new ChessConfig(this);
            String[][] copiedGrid = newConfig.chessBoard;
            copiedGrid[row][col] = ".";
            copiedGrid[row-1][col-1] = "P";
            validCaps.add(newConfig);
        }
        if (rightCap && !grid[row-1][col+1].equals(".")){
            ChessConfig newConfig = new ChessConfig(this);
            String[][] copiedGrid = newConfig.chessBoard;
            copiedGrid[row][col] = ".";
            copiedGrid[row-1][col+1] = "P";
            validCaps.add(newConfig);
        }
        return validCaps;
    }

    public ArrayList<Configuration> checkQueen(int row, int col){
        ChessConfig config = new ChessConfig(this);
        ArrayList<Configuration> validCaps = new ArrayList<Configuration>();

        ArrayList<Configuration> nsewCaps = checkRook(row, col, "Q");
        validCaps.addAll(nsewCaps);

        ArrayList<Configuration> diagnalCaps = checkBishop(row, col, "Q");
        validCaps.addAll(diagnalCaps);
        return validCaps;
    }

    public ArrayList<Configuration> checkKing(int row, int col){
        ChessConfig config = new ChessConfig(this);
        String[][] grid = config.chessBoard;
        ArrayList<Configuration> validCaps = new ArrayList<Configuration>();

        boolean leftCap = true;
        boolean rightCap = true;
        boolean upCap = true;
        boolean downCap = true;
        boolean leftUpCap = true;
        boolean rightUpCap = true;
        boolean leftDownCap = true;
        boolean rightDownCap = true;

        if (row == 0){upCap = false;  rightUpCap = false;  leftUpCap = false;}
        if (col == 0){leftCap = false;  leftUpCap = false;  leftDownCap = false;}
        if (col == this.colNums-1){rightCap = false;  rightUpCap = false;  rightDownCap = false;}
        if (row == this.rowNums-1){downCap = false;  rightDownCap = false;  leftDownCap = false;}

        if (leftCap && !grid[row][col-1].equals(".")){
            ChessConfig newConfig = new ChessConfig(this);
            String[][] copiedGrid = newConfig.chessBoard;
            copiedGrid[row][col] = ".";
            copiedGrid[row][col-1] = "K";
            validCaps.add(newConfig);
        }
        if (rightCap && !grid[row][col+1].equals(".")){
            ChessConfig newConfig = new ChessConfig(this);
            String[][] copiedGrid = newConfig.chessBoard;
            copiedGrid[row][col] = ".";
            copiedGrid[row][col+1] = "K";
            validCaps.add(newConfig);
        }
        if (upCap && !grid[row-1][col].equals(".")){
            ChessConfig newConfig = new ChessConfig(this);
            String[][] copiedGrid = newConfig.chessBoard;
            copiedGrid[row][col] = ".";
            copiedGrid[row-1][col] = "K";
            validCaps.add(newConfig);
        }
        if (downCap && !grid[row+1][col].equals(".")){
            ChessConfig newConfig = new ChessConfig(this);
            String[][] copiedGrid = newConfig.chessBoard;
            copiedGrid[row][col] = ".";
            copiedGrid[row+1][col] = "K";
            validCaps.add(newConfig);
        }
        if (leftUpCap && !grid[row-1][col-1].equals(".")){
            ChessConfig newConfig = new ChessConfig(this);
            String[][] copiedGrid = newConfig.chessBoard;
            copiedGrid[row][col] = ".";
            copiedGrid[row-1][col-1] = "K";
            validCaps.add(newConfig);
        }
        if (rightUpCap && !grid[row-1][col+1].equals(".")){
            ChessConfig newConfig = new ChessConfig(this);
            String[][] copiedGrid = newConfig.chessBoard;
            copiedGrid[row][col] = ".";
            copiedGrid[row-1][col+1] = "K";
            validCaps.add(newConfig);
        }
        if (leftDownCap && !grid[row+1][col-1].equals(".")){
            ChessConfig newConfig = new ChessConfig(this);
            String[][] copiedGrid = newConfig.chessBoard;
            copiedGrid[row][col] = ".";
            copiedGrid[row+1][col-1] = "K";
            validCaps.add(newConfig);
        }
        if (rightDownCap && !grid[row+1][col+1].equals(".")){
            ChessConfig newConfig = new ChessConfig(this);
            String[][] copiedGrid = newConfig.chessBoard;
            copiedGrid[row][col] = ".";
            copiedGrid[row+1][col+1] = "K";
            validCaps.add(newConfig);
        }
        return validCaps;
    }

    public ArrayList<Configuration> checkKnight(int row, int col){
        ChessConfig config = new ChessConfig(this);
        String[][] grid = config.chessBoard;
        ArrayList<Configuration> validCaps = new ArrayList<Configuration>();

        boolean upLeftCap = true;
        boolean upRightCap = true;
        boolean downLeftCap = true;
        boolean downRightCap = true;
        boolean leftUpCap = true;
        boolean rightUpCap = true;
        boolean leftDownCap = true;
        boolean rightDownCap = true;


        if (col == colNums - 1) {upRightCap=false; downRightCap=false; rightUpCap=false; rightDownCap=false;}
        if (col == 0){upLeftCap=false; downLeftCap=false; leftUpCap=false; leftDownCap=false;}
        if (row == rowNums - 1) {downLeftCap=false; downRightCap=false; leftDownCap=false; rightDownCap=false;}
        if (row == 0){upLeftCap=false; upRightCap=false; leftUpCap=false; rightUpCap=false;}

        if (col < 2){leftUpCap=false; leftDownCap=false;}
        if (col > colNums-3){rightUpCap=false; rightDownCap=false;}
        if (row < 2){upRightCap=false; upLeftCap=false;}
        if (row > rowNums-3){downLeftCap=false; downRightCap=false;}



        if (upRightCap && !grid[row-2][col+1].equals(".")){
            ChessConfig newConfig = new ChessConfig(this);
            String[][] copiedGrid = newConfig.chessBoard;
            copiedGrid[row][col] = ".";
            copiedGrid[row-2][col+1] = "N";
            validCaps.add(newConfig);
        }
        if (upLeftCap && !grid[row-2][col-1].equals(".")){
            ChessConfig newConfig = new ChessConfig(this);
            String[][] copiedGrid = newConfig.chessBoard;
            copiedGrid[row][col] = ".";
            copiedGrid[row-2][col-1] = "N";
            validCaps.add(newConfig);
        }
        if (downRightCap && !grid[row+2][col+1].equals(".")){
            ChessConfig newConfig = new ChessConfig(this);
            String[][] copiedGrid = newConfig.chessBoard;
            copiedGrid[row][col] = ".";
            copiedGrid[row+2][col+1] = "N";
            validCaps.add(newConfig);
        }
        if (downLeftCap && !grid[row+2][col-1].equals(".")){
            ChessConfig newConfig = new ChessConfig(this);
            String[][] copiedGrid = newConfig.chessBoard;
            copiedGrid[row][col] = ".";
            copiedGrid[row+2][col-1] = "N";
            validCaps.add(newConfig);
        }
        if (leftUpCap && !grid[row-1][col-2].equals(".")){
            ChessConfig newConfig = new ChessConfig(this);
            String[][] copiedGrid = newConfig.chessBoard;
            copiedGrid[row][col] = ".";
            copiedGrid[row-1][col-2] = "N";
            validCaps.add(newConfig);
        }
        if (leftDownCap && !grid[row+1][col-2].equals(".")){
            ChessConfig newConfig = new ChessConfig(this);
            String[][] copiedGrid = newConfig.chessBoard;
            copiedGrid[row][col] = ".";
            copiedGrid[row+1][col-2] = "N";
            validCaps.add(newConfig);
        }
        if (rightUpCap && !grid[row-1][col+2].equals(".")){
            ChessConfig newConfig = new ChessConfig(this);
            String[][] copiedGrid = newConfig.chessBoard;
            copiedGrid[row][col] = ".";
            copiedGrid[row-1][col+2] = "N";
            validCaps.add(newConfig);
        }
        if (rightDownCap && !grid[row+1][col+2].equals(".")){
            ChessConfig newConfig = new ChessConfig(this);
            String[][] copiedGrid = newConfig.chessBoard;
            copiedGrid[row][col] = ".";
            copiedGrid[row+1][col+2] = "N";
            validCaps.add(newConfig);
        }
        return validCaps;
    }

    public String getFilename(){return this.filename;}

    public String getChessPiece(int row, int col){return this.chessBoard[row][col];}

    public void setChessPiece(int row, int col, String val){this.chessBoard[row][col] = val;}

    public int getRowNums(){return this.rowNums;}

    public int getColNums(){return this.colNums;}

    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof ChessConfig otherChessConfig){
            result = Arrays.deepEquals(this.chessBoard, otherChessConfig.chessBoard);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(this.chessBoard);
    }

    @Override
    public String toString() {
        String chessBoardStr = "";
        for (int i = 0; i < rowNums+2; i++){
            chessBoardStr += "\n";
            for (int k = 0; k < colNums+2; k++){
                if (i == 0 && k > 1){
                    chessBoardStr += k-2 + " ";
                } else if (k == 0 && i > 1) {
                    chessBoardStr += i - 2 + " ";
                } else if ( k == 1 && i > 1){
                    chessBoardStr += "| ";
                } else if (i == 1 && k > 0){
                    chessBoardStr += "--";
                } else if (i < 2 || k < 2){
                    chessBoardStr += "  ";
                }else {
                    chessBoardStr += this.chessBoard[i - 2][k - 2] + " ";
                }
            }
        }
        return chessBoardStr;
    }
}
