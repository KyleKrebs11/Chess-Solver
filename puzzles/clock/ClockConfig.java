package puzzles.clock;

import puzzles.common.solver.Configuration;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

public class ClockConfig implements Configuration {
    private int hours;
    private int start;
    private int end;

    public ClockConfig(int hours, int start, int end){
        this.hours = hours;
        this.start = start;
        this.end = end;
    }


    @Override
    public boolean isSolution() {
        if (start == end){
            return true;
        }
        return false;
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        ArrayList<Configuration> neighbors = new ArrayList<>();
        if (start + 1 > hours){
            neighbors.add(new ClockConfig(hours, 1, end));
        }else{
            neighbors.add(new ClockConfig(hours, start + 1, end));
        }

        if (start - 1 == 0){
            neighbors.add(new ClockConfig(hours, hours, end));
        }else{
            neighbors.add(new ClockConfig(hours, start - 1, end));
        }

        return neighbors;
    }

    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof ClockConfig){
            result = this.end == ((ClockConfig) other).end &&
                    this.start == ((ClockConfig) other).start &&
                    this.hours == ((ClockConfig) other).hours;
        }
        return result;
    }

    @Override
    public int hashCode() {
        return this.start;
    }

    @Override
    public String toString() {
        return String.valueOf(this.start);
    }
}
