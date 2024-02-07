package puzzles.chess.solver;

import puzzles.chess.model.ChessConfig;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import java.io.IOException;
import java.util.Collection;

public class Chess {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java Chess filename");
        } else{

            String filename = args[0];
            ChessConfig chessConfig = new ChessConfig(filename);

            Solver solver = new Solver(chessConfig);
            Collection<Configuration> path = solver.solve();

            System.out.println("Total Configs: " + solver.getTotalConfigs());
            System.out.println("Unique Configs: " + solver.getUniqueConfigs());
            int counter = 0;
            if (path.isEmpty()){
                System.out.print("No solution!");
            }else {
                for (Configuration config : path) {
                    System.out.println("Step " + counter + ":\n" + config.toString());
                    counter++;
                }
            }


        }
    }
}
