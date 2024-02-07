package puzzles.clock;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.clock.ClockConfig;
import java.util.Collection;

public class Clock {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(("Usage: java Clock start stop"));
        } else {
            int hours = Integer.parseInt(args[0]);
            int start = Integer.parseInt(args[1]);
            int end = Integer.parseInt(args[2]);

            ClockConfig clockConfigStart = new ClockConfig(hours, start, end);
            ClockConfig clockConfigEnd = new ClockConfig(hours, end, end);
            Solver solver = new Solver(clockConfigStart);
            Collection<Configuration> path = solver.solve();

            System.out.println("Hours: " + hours +
                    ", Start: " + start +
                    ", End: " + end);
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