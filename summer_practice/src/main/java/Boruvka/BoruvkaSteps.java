package Boruvka;

import java.util.ArrayList;
import java.util.Optional;

import Graph.*;

public class BoruvkaSteps {
    private ArrayList<ArrayList<Edge>> steps;
    private int index;

    BoruvkaSteps() {
        steps = new ArrayList<>();
        steps.add(new ArrayList<Edge>());
        index = 0;
    }

    public void addStep(ArrayList<Edge> step) {
        ArrayList<Edge> newArray = new ArrayList<>();
        for (Edge edge : step) {
            Edge newEdge = new Edge(edge.getVertex1(), edge.getVertex2(), edge.getWeight());
            newEdge.setMarkLastAdded(edge.getMarkLastAdded());
            newArray.add(newEdge);
        }
        steps.add(newArray);
    }

    public Optional<ArrayList<Edge>> nextStep() {
        if (index != steps.size() - 1) {
            return Optional.of(steps.get(++index));
        }
        return Optional.empty();
    }

    public Optional<ArrayList<Edge>> prevStep() {
        if (index != 0 && steps.size() != 0) {
            return Optional.of(steps.get(--index));
        }
        return Optional.empty();
    }

    public Optional<ArrayList<Edge>> lastStep() {
        if (steps.size() != 0) {
            index = steps.size() - 1;
            return Optional.of(steps.get(index));
        }
        return Optional.empty();
    }

}
