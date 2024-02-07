package puzzles.hoppers.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HoppersModel {
    /** the collection of observers of this model */
    private final List<Observer<HoppersModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private HoppersConfig currentConfig;


    private  static int numMoves = 0;
    private final ArrayList<Configuration> solutionPath;


    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<HoppersModel, String> observer) {
        this.observers.add(observer);

    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String msg) {

        for (var observer : observers) {
            try {
                observer.update(this, msg);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

    }

    public HoppersModel(String filename) throws IOException {
        this.currentConfig = new HoppersConfig(filename);

        // create a new solver to solve puzzle and hold the solution path
        Solver hoppSolver = new Solver(currentConfig);
        this.solutionPath = hoppSolver.solve();

    }

    public List<Observer<HoppersModel, String>> getObservers() {
        return observers;
    }

    public HoppersConfig getCurrentConfig() {
        return currentConfig;
    }

    public void setCurrentConfig(HoppersConfig currentConfig) {
        this.currentConfig = currentConfig;
    }



}
