package puzzles.hoppers.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.hoppers.model.HoppersConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class Hoppers {
    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
            System.out.println("Usage: java Hoppers filename");
        } else {
            // Create new scanner for args

            // Read filename

            HoppersConfig hoppersConfig = new HoppersConfig(args[0]);
            Solver hoppSolver = new Solver(hoppersConfig);
            ArrayList<Configuration> configurations = hoppSolver.solve();

            // print file name
            System.out.println(args[0]);


           HoppersConfig firstcon = (HoppersConfig) configurations.get(configurations.size()-1);
           firstcon.printConfig();


            System.out.println("Total Configs: " + hoppSolver.getTotalConfigs());
            System.out.println("Unique Configs: " + hoppSolver.getUniqueConfigs());


            int step = 0;

            for (Configuration c : configurations) {
                System.out.println();
                System.out.println("Step: " + step);
                if (c instanceof HoppersConfig) {
                    HoppersConfig c1 = (HoppersConfig) c;
                    c1.printConfig();
                }
                step++;

            }






        }

    }
}
