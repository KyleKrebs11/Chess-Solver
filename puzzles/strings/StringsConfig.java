package puzzles.strings;

import puzzles.clock.ClockConfig;
import puzzles.common.solver.Configuration;

import java.util.ArrayList;
import java.util.Collection;

public class StringsConfig implements Configuration {
    private String start;
    private String end;

    public StringsConfig(String start, String end){
        this.start = start;
        this.end = end;
    }

    @Override
    public boolean isSolution() {
        if (start.equals(end)){
            return true;
        }
        return false;
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        ArrayList<Configuration> neighbors = new ArrayList<>();

        for (int i = 0; i < end.length(); i++) {

            char ch = start.charAt(i);
            StringBuilder sbUp = new StringBuilder(start);
            StringBuilder sbDown = new StringBuilder(start);

            if (ch == 'Z') {
                sbUp.setCharAt(i, 'A');
            } else {
                sbUp.setCharAt(i, ++ch);
                --ch;
            }
            if (ch == 'A') {
                sbDown.setCharAt(i, 'Z');
            } else {
                sbDown.setCharAt(i, --ch);
            }


            neighbors.add(new StringsConfig(sbUp.toString(), end));
            neighbors.add(new StringsConfig(sbDown.toString(), end));
        }

        return neighbors;
    }

    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof StringsConfig otherStrConfig){
            result = this.end.equals(otherStrConfig.end) &&
                    this.start.equals(otherStrConfig.start);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return this.start.hashCode();
    }

    @Override
    public String toString() {
        return this.start;
    }
}