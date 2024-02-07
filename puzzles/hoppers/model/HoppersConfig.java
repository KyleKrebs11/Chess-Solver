package puzzles.hoppers.model;

import puzzles.common.solver.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

// TODO: implement your HoppersConfig for the common solver

public class HoppersConfig implements Configuration {



    static int rows;
    static int cols;
    private String[][] pond;



    public HoppersConfig(String filename) throws IOException {


        Scanner in = new Scanner(new File(filename));
        String line = in.nextLine();
        String[] lineInfo = line.split(" ");

        rows = Integer.parseInt(lineInfo[0]);
        cols = Integer.parseInt(lineInfo[1]);

        this.pond = new String[rows][cols];

        for (int i = 0; i < rows; i++) {
            line = in.nextLine();
            lineInfo = line.split(" ");

            for (int j = 0; j < cols; j++) {
                this.pond[i][j] = lineInfo[j];
            }
        }

        System.out.println("finished reading file");


    }

    public HoppersConfig(HoppersConfig other) {
        // copy constructor
        String[][] coppiedPond = new String[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                coppiedPond[i][j] = other.pond[i][j];
            }
        }
        this.pond = coppiedPond;
      //  System.out.println("yp");



    }


    private ArrayList<String> CanJumpFrog(int r, int c) {
        // provide direction a frog can be jumped
        // need to make sure that the landed square is valid

        // north
        ArrayList<String> directions = new ArrayList<String>();


        if (r - 4 >= 0 && pond[r - 4][c].equals(".")) {
            if (pond[r - 2][c].equals("G")) {
                directions.add("N") ;
            }
        }
        // south
        if (r + 4 < rows && pond[r + 4][c].equals(".")) {
            if (pond[r + 2][c].equals("G")) {
                directions.add("S") ;

            }
        }


        // East
        if (c + 4 < cols && pond[r][c + 4].equals(".")) {
            if (pond[r][c + 2].equals("G")) {
                directions.add("E") ;

            }
            // West
        }
        if (c - 4 >= 0 && pond[r][c - 4].equals(".")) {
            if (pond[r][c - 2].equals("G")) {
                directions.add("W") ;
            }
            }


        if (  (r + c) % 2 == 0) {

            // NorthWest
            if (r - 2 >= 0 && c - 2 >= 0 && pond[r - 2][c - 2].equals(".")) {
                if (pond[r - 1][c - 1].equals("G")) {
                    directions.add("NW") ;

                }

            } // NorthEast
             if (r - 2 >= 0 && c + 2 < cols && pond[r - 2][c + 2].equals(".")) {
                if (pond[r - 1][c + 1].equals("G")) {
                    directions.add("NE");

                }

            }  // southWest
             if (r + 2 < rows && c - 2 >= 0 && pond[r + 2][c - 2].equals(".")) {
                if (pond[r + 1][c - 1].equals("G")) {
                    directions.add("SW");

                }

                }
             //se
            if (r + 2 < rows && c + 2 < cols && pond[r + 2][c + 2].equals(".")) {
                if (pond[r + 1][c + 1].equals("G")) {
                    directions.add("SE");

                }
            }

            }
        return directions;

    }


    @Override
    public boolean isSolution() {

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (pond[i][j].equals("G")) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Collection<Configuration> getNeighbors() {

        ArrayList<Configuration> neighbors = new ArrayList<>();
        HoppersConfig congfig = new HoppersConfig(this);


        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // find frog
                if (congfig.pond[i][j].equals("G") || congfig.pond[i][j].equals("R")) {


                    // check if frog can jump frog
                    ArrayList<String> jumpedDirections = CanJumpFrog(i, j);
                    boolean redFrogJump = false;
                    if (pond[i][j].equals("R")) {
                        redFrogJump = true;
                    }




                       for (String dir : jumpedDirections) {
                           String[][] updatedPond = new String[rows][cols];


                        switch (dir) {
                            case "N" -> {

                                HoppersConfig newConfig = new HoppersConfig(congfig);
                                updatedPond = newConfig.pond;


                                updatedPond[i][j] = ".";
                                updatedPond[i - 2][j] = ".";

                                if (redFrogJump){
                                    updatedPond[i - 4][j] = "R";
                                }else {
                                    updatedPond[i - 4][j] = "G";
                                }


                                neighbors.add(newConfig);

                                break;
                            }
                            case "S" -> {

                                HoppersConfig newConfig = new HoppersConfig(congfig);
                                updatedPond = newConfig.pond;
                                updatedPond[i][j] = ".";
                                updatedPond[i + 2][j] = ".";


                                if (redFrogJump ){
                                    updatedPond[i + 4][j] = "R";
                                }else {
                                    updatedPond[i + 4][j] = "G";
                                }

                                neighbors.add(newConfig);
                                break;

                            }
                            case "E" -> {

                                HoppersConfig newConfig = new HoppersConfig(congfig);
                                updatedPond = newConfig.pond;
                                updatedPond[i][j] = ".";
                                updatedPond[i][j + 2] = ".";

                                if (redFrogJump ){
                                    updatedPond[i][j+4] = "R";
                                }else {
                                    updatedPond[i][j+4] = "G";
                                }

                                neighbors.add(newConfig);
                                break;
                            }
                            case "W" -> {

                                HoppersConfig newConfig = new HoppersConfig(congfig);
                                updatedPond = newConfig.pond;
                                updatedPond[i][j] = ".";
                                updatedPond[i][j - 2] = ".";


                                if (redFrogJump ){
                                    updatedPond[i][j-4] = "R";
                                }else {
                                    updatedPond[i][j-4] = "G";
                                }

                                neighbors.add(newConfig);

                                break;
                            }
                            case "NW" -> {

                                HoppersConfig newConfig = new HoppersConfig(congfig);
                                updatedPond = newConfig.pond;
                                updatedPond[i][j] = ".";
                                updatedPond[i - 1][j - 1] = ".";


                                if (redFrogJump ){
                                    updatedPond[i -2 ][j-2] = "R";
                                }else {
                                    updatedPond[i -2][j-2] = "G";
                                }

                                neighbors.add(newConfig);
                                break;
                            }
                            case "NE" -> {

                                HoppersConfig newConfig = new HoppersConfig(congfig);
                                updatedPond =  newConfig.pond;


                                updatedPond[i][j] = ".";
                                updatedPond[i - 1][j + 1] = ".";


                                if (redFrogJump) {
                                    updatedPond[i - 2][j + 2] = "R";
                                } else {
                                    updatedPond[i - 2][j + 2] = "G";
                                }


                                neighbors.add(newConfig);
                                break;
                            }

                            case "SW" -> {

                                HoppersConfig newConfig = new HoppersConfig(congfig);
                                updatedPond = newConfig.pond;

                                updatedPond[i][j] = ".";
                                updatedPond[i + 1][j - 1] = ".";

                                if (redFrogJump ){
                                    updatedPond[i +2 ][j-2] = "R";
                                }else {
                                    updatedPond[i + 2][j-2] = "G";
                                }

                                neighbors.add(newConfig);
                                break;
                            }
                            case "SE" -> {

                                HoppersConfig newConfig = new HoppersConfig(congfig);
                                updatedPond = newConfig.pond;
                                updatedPond[i][j] = ".";
                                updatedPond[i + 1][j + 1] = ".";

                                if (redFrogJump ){
                                    updatedPond[i +2 ][j+2] = "R";
                                }else {
                                    updatedPond[i + 2][j+2] = "G";
                                }



                                neighbors.add(newConfig);
                                break;
                            }
                        }



                    }




                }

            }
        }
        return neighbors;
    }

    public void printConfig() {

        String nums = "    ";

        for (int r = 0; r < rows; r++) {
            nums+=r + " ";
        }
        System.out.println(nums);

        for (int r = 0; r < rows; r++) {
            System.out.print(r + " | ");
            for (int c = 0; c < cols; c++) {
                System.out.print(  this.pond[r][c] + " ");
            }
            System.out.println();
        }

    }


    public static int getRows() {
        return rows;
    }

    public static int getCols() {
        return cols;
    }

    public String[][] getPond() {
        return pond;
    }

    public void changeCell(int r, int c, String s) {
        this.pond[r][c] = s;
    }
    public void setPond(String[][] pond) {
        this.pond = pond;
    }


}
