package puzzles.hoppers.ptui;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.hoppers.model.HoppersConfig;
import puzzles.hoppers.model.HoppersModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class HoppersPTUI implements Observer<HoppersModel, String> {
    private HoppersModel model;
    private HoppersConfig firstConfig;


    public void init(String filename) throws IOException {

        this.model = new HoppersModel(filename);
        this.model.addObserver(this);
        this.firstConfig = this.model.getCurrentConfig();
        System.out.println("Loaded: " + filename);

        update(this.model, "");
        displayHelp();
    }

    @Override
    public void update(HoppersModel model, String data) {

        this.model.getCurrentConfig().printConfig();


        System.out.println(data);

    }

    private void displayHelp() {
        System.out.println( "h(int)              -- hint next move" );
        System.out.println( "l(oad) filename     -- load new puzzle file" );
        System.out.println( "s(elect) r c        -- select cell at r, c" );
        System.out.println( "q(uit)              -- quit the game" );
        System.out.println( "r(eset)             -- reset the current game" );
    }

    private void printPond(HoppersConfig config) {

        for (int r = 0; r < config.getRows(); r++) {
            System.out.print(r+ " | "+ config.getPond()[r][config.getCols()]);
        }
    }

    public void run() {
        Scanner in = new Scanner( System.in );
        for ( ; ; ) {
            System.out.print( "> " );
            String line = in.nextLine();
            String[] words = line.split( "\\s+" );
            if (words.length > 0) {


                if (words[0].startsWith( "h" )) {
                    // hint, show the next move, if possible
                    hint();
                }

                else if (words[0].startsWith( "s" )) {
                    // select cell at r, c

                    int r = Integer.parseInt(words[1]);
                    int c = Integer.parseInt(words[2]);

                    select(r,c,model.getCurrentConfig().getPond()[r][c]);

                    //

                }

                else if (words[0].startsWith( "r" )) {
                    this.model.setCurrentConfig(firstConfig);
                    update(model,"Game was reset!");

                } else if (words[0].startsWith( "l" )) {
                    try {
                        load();
                    }catch (IOException e) {

                    }


                }


                if (words[0].startsWith( "q" )) {
                    break;
                }

                else {
                   // displayHelp();
                }
            }
        }
    }

    private void load() throws IOException {
        // loads in new puzzle file and updates the model

        Scanner in = new Scanner( System.in );
        System.out.print( "what is the name of the file you would like to load? " );
        String line = in.nextLine();
        HoppersConfig newConfig = new HoppersConfig(line);
        this.model.setCurrentConfig(newConfig);
        update(this.model,"Loaded: " + line);


    }

    private void hint() {
        // solves the puzzle and sets model to the next step
        Solver hoppSolver = new Solver(model.getCurrentConfig());
        ArrayList<Configuration> solutionPath = hoppSolver.solve();
        model.setCurrentConfig((HoppersConfig) solutionPath.get(1));

        update(model,"Next Step ->");
        if (model.getCurrentConfig().isSolution()) {
            System.out.println("GAME IS SOLVED!");
        }
    }

    private void select(int currentR, int currentC, String s) {
        Scanner in = new Scanner( System.in );
        System.out.print( " which cell would you like to hop to? " );
        String line = in.nextLine();
        String[] words = line.split(" ");
        int r = Integer.parseInt(words[0]);
        int c = Integer.parseInt(words[1]);

        if (r > model.getCurrentConfig().getRows() || c > model.getCurrentConfig().getCols() || r < 0 || c < 0) {
            System.out.println("Invalid cell");
            return;
        } else if (model.getCurrentConfig().getPond()[r][c].equals("*")) {
            System.out.println("Invalid cell");
            return;
        }

        HoppersConfig copyConfig = new HoppersConfig(model.getCurrentConfig());
        copyConfig.changeCell(r,c,model.getCurrentConfig().getPond()[currentR][currentC]);
        copyConfig.changeCell(currentR,currentC,".");




        // NW
        if (currentR > r && currentC > c){

            if (!model.getCurrentConfig().getPond()[currentR - 1][currentC - 1].equals("G")) {
                System.out.println("INVALID MOVE");
                return;
            } else {
                copyConfig.changeCell(currentR - 1, currentC - 1, ".");
            }

        }//N
        else if (currentR > r && currentC == c){

            if (!model.getCurrentConfig().getPond()[currentR - 2][currentC ].equals("G")) {
                System.out.println("INVALID MOVE");
                return;
            } else {

                copyConfig.changeCell(currentR - 2, currentC, ".");
            }
        // NE
        } else if (currentR > r && c > currentC){

            if (!model.getCurrentConfig().getPond()[currentR - 1][currentC + 1].equals("G")) {
                System.out.println("INVALID MOVE");
                return;
            } else {

                copyConfig.changeCell(currentR - 1, currentC + 1, ".");
            }
        } // E
        else if (currentR == r && c > currentC){

            if (!model.getCurrentConfig().getPond()[currentR ][currentC +2].equals("G")) {
                System.out.println("INVALID MOVE");
                return;
            } else {

                copyConfig.changeCell(currentR, currentC + 2, ".");
            }
        }
        //S
        else if (currentR < r && c == currentC){

            if (!model.getCurrentConfig().getPond()[currentR +2][currentC].equals("G")) {
                System.out.println("INVALID MOVE");
                return;
            } else {

                copyConfig.changeCell(currentR + 2, currentC, ".");
            }
        } // SE
        else if (r > currentR && c > currentC){

            if (!model.getCurrentConfig().getPond()[currentR + 1][currentC + 1].equals("G")) {
                System.out.println("INVALID MOVE");
                return;
            } else {
                copyConfig.changeCell(currentR + 1, currentC + 1, ".");
            }
        } //SW
        else if (r > currentR && c < currentC){
            if (!model.getCurrentConfig().getPond()[currentR + 1][currentC - 1].equals("G")) {
                System.out.println("INVALID MOVE");
                return;
            } else {
                copyConfig.changeCell(currentR + 1, currentC - 1, ".");
            }


        }


        this.model.setCurrentConfig(copyConfig);

        //SW


        String data = "Jumped from (" + currentR + "," + currentC + ") to (" + r + "," + c +")";
        update(model,data);

        if (model.getCurrentConfig().isSolution()) {
            System.out.println("GAME IS SOLVED!");
        }


    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            try {
                HoppersPTUI ptui = new HoppersPTUI();
                ptui.init(args[0]);
                ptui.run();
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }
}
