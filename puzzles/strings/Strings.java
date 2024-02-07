package puzzles.strings;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.strings.StringsConfig;
import java.util.Collection;

public class Strings {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(("Usage: java Strings start finish"));
        } else {
            String start = args[0];
            String end = args[1];

            StringsConfig stringsConfigStart = new StringsConfig(start, end);
            Solver solver = new Solver(stringsConfigStart);
            Collection<Configuration> path = solver.solve();

            System.out.println("Start: " + start + ", End: " + end);
            System.out.println("Total Configs: " + solver.getTotalConfigs());
            System.out.println("Unique Configs: " + solver.getUniqueConfigs());
            int counter = 0;
            if (path.isEmpty()){
                System.out.print("No solution!");
            }else {
                for (Configuration config : path) {
                    System.out.println("Step " + counter + ": " + config);
                    counter++;
                }
            }
        }
    }
}