package Graph;

import java.io.IOException;
import java.util.*;

public class GraphGenerator {

    private int countEdges;
    private int countVertexes;
    private int minWeight;
    private int maxWeight;
    ArrayList<Integer> vertexes;
    ArrayList<Edge> edges;
    private ArrayList<Component> components;
    private ArrayList<Integer> weights;
    Graph graph;

    public GraphGenerator(int countEdges, int countVertexes, int minWeight, int maxWeight) throws IOException {

        if (!checkCorrect(countEdges, countVertexes, minWeight, maxWeight)) {
            throw new IOException("wrong limits for graph");
        }
        this.countEdges = countEdges;
        this.countVertexes = countVertexes;
        this.minWeight = minWeight;
        this.maxWeight = maxWeight;


        vertexes = new ArrayList<>();
        edges = new ArrayList<>();

        weights = generateWeights();
        addVertexes();
        components = new ArrayList<>();
        for (int i = 0; i < countVertexes; i++) {
            components.add(new Component(vertexes.get(i)));
        }
    }

    private boolean checkCorrect(int countEdges, int countVertexes, int minWeight, int maxWeight) {
        return !(countVertexes < 2 || countEdges < countVertexes - 1 || countEdges > countVertexes * (countVertexes - 1) / 2 || minWeight < 0 || maxWeight < minWeight);
    }

    public Graph generateGraph() {
        generate();
        return new Graph(edges, vertexes);
    }

    private void generate() {
        if (components.size() == 1) {
            generateRestEdges();
            return;
        }

        Collections.shuffle(components);
        ArrayList<Component> newComponents = new ArrayList<>();
        Component c1 = null;
        Component c2 = null;

        Random r = new Random();

        ListIterator<Component> iterator = components.listIterator();
        int i = 0;
        while (iterator.hasNext()) {
            if (i % 2 == 0) {
                c1 = iterator.next();
                if (!iterator.hasNext()) {
                    int randomIndex = r.nextInt(newComponents.size());
                    Component link = newComponents.get(randomIndex);
                    newComponents.remove(randomIndex);
                    newComponents.add(linkComponents(c1, link));
                }
            } else {
                c2 = iterator.next();
                newComponents.add(linkComponents(c1, c2));
            }
            i++;
        }
        components = newComponents;
        generate();
    }

    private void generateRestEdges() {
        Random r = new Random();
        for (Integer weight : weights) {
            boolean correctIndexes = false;
            int index1 = 0;
            int index2 = 0;
            while (!correctIndexes) {
                index1 = r.nextInt(vertexes.size());
                index2 = r.nextInt(vertexes.size());
                if (index1 == index2) {
                    continue;
                }
                boolean hasEdge = false;
                for (Edge e : edges) {
                    if (e.getVertex1() == index1 && e.getVertex2() == index2 || e.getVertex1() == index2 && e.getVertex2() == index1) {
                        hasEdge = true;
                        break;
                    }
                }
                correctIndexes = !hasEdge;
            }
            edges.add(new Edge(index1, index2, weight));
        }
    }

    private Component linkComponents(Component c1, Component c2) {
        Random r = new Random();

        int randomIndex1 = r.nextInt(c1.vertexes.length);
        int randomIndex2 = r.nextInt(c2.vertexes.length);
        int randomIndex3 = r.nextInt(weights.size());
        edges.add(new Edge(c1.vertexes[randomIndex1], c2.vertexes[randomIndex2], weights.get(randomIndex3)));
        weights.remove(randomIndex3);

        return new Component(c1, c2);
    }

    private ArrayList<Integer> generateWeights() {
        Random random = new Random();
        ArrayList<Integer> weights = new ArrayList<>();
        for (int i = 0; i < countEdges; i++) {
            weights.add(minWeight + random.nextInt(maxWeight - minWeight + 1));
        }
        return weights;
    }

    private void addVertexes() {
        for (int i = 0; i < countVertexes; i++) {
            vertexes.add(i);
        }
    }


}

class Component {
    public int[] vertexes;

    Component(int vertex) {
        vertexes = new int[1];
        vertexes[0] = vertex;
    }

    Component(Component component1, Component component2) {
        int size = component1.vertexes.length + component2.vertexes.length;
        vertexes = new int[size];

        int j = 0;
        for (int i = 0; i < component1.vertexes.length; i++) {
            vertexes[j++] = component1.vertexes[i];
        }
        for (int i = 0; i < component2.vertexes.length; i++) {
            vertexes[j++] = component2.vertexes[i];
        }
    }
}
