/**
 * Author: Kyle Krebs
 * Date: 4/12/23
 * BFS Solver class
 */
package puzzles.common.solver;

import puzzles.clock.ClockConfig;

import java.io.ObjectInputFilter;
import java.util.*;

public class Solver {
    private LinkedList<Configuration> queue = new LinkedList<>();
    private HashMap<Configuration, Configuration> predMap = new HashMap<>();
    private Configuration solution;
    private Configuration start;
    private int totalConfigs = 0;
    private int uniqueConfigs = 0;


    public Solver(Configuration start) {
        this.start = start;
    }

    /**
     * Backtracking method that returns the shortest path via BFS
     * @return ArrayList of shortest path
     */
    public ArrayList<Configuration> returnPath() {
        ArrayList<Configuration> path = new ArrayList<>();
        while (predMap.get(this.solution) != null) {
            path.add(this.solution);
            Configuration prevConfig = predMap.get(this.solution);
            this.solution = prevConfig;
        }
        path.add(start);
        Collections.reverse(path);
        return path;
    }

    /**
     *  BFS Method that finds the shortest path to solution
     * @return ArrayList of shortest path
     */
    public ArrayList<Configuration> solve() {
        queue.add(start);
        totalConfigs += 1;
        uniqueConfigs += 1;
        predMap.put(start, null);

        // find solution
        while (true) {
            if (queue.isEmpty()){
                return new ArrayList<>();
            }
            Configuration frontConfig = queue.remove();
            if (frontConfig.isSolution()) {
                this.solution = frontConfig;
                break;
            }
            Collection<Configuration> neighbors = frontConfig.getNeighbors();
            for (Configuration neighbor : neighbors) {
                totalConfigs += 1;
                if (!predMap.containsKey(neighbor)) {
                    uniqueConfigs += 1;
                    queue.add(neighbor);
                    predMap.put(neighbor, frontConfig);
                }
            }
        }
        // return path
        return returnPath();
    }

    /**
     * Gets total configs
     * @return number of total configs
     */
    public int getTotalConfigs(){
        return totalConfigs;
    }

    /**
     * Gets total unique configs
     * @return total unique configs
     */
    public int getUniqueConfigs(){
        return uniqueConfigs;
    }
}