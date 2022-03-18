package Graph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ListIterator;
import java.util.Optional;

import Input.*;

public class Graph {
    private ArrayList<Edge> edges;
    private ArrayList<Integer> vertexes;
    private int countVertexes;
    private int countEdges;
    public final static Integer EMPTY_EDGE = -1;

    public Graph() {
        vertexes = new ArrayList<>();
        edges = new ArrayList<>();
        countEdges = 0;
        countVertexes = 0;
    }

    public Graph(ArrayList<ArrayList<Integer>> matrix) throws IOException {
        if (CheckingCorrect.checkCorrectMatrix(matrix)) {
            edges = new ArrayList<>();
            vertexes = new ArrayList<>();
            fillEdges(edges, matrix);
            fillVertexes(vertexes, matrix);
            countEdges = edges.size();
            countVertexes = vertexes.size();
        } else {
            throw new IOException("incorrect matrix");
        }
    }

    public Graph(ArrayList<Edge> edges, ArrayList<Integer> vertexes) {
        this.edges = cloneEdges(edges);
        this.countEdges = edges.size();
        this.vertexes = cloneVertexes(vertexes);
        this.countVertexes = vertexes.size();
    }

    public Graph(int vertexesCount) {
        vertexes = new ArrayList<>();
        for (int i = 0; i < vertexesCount; i++) {
            vertexes.add(i);
        }
    }

    public ArrayList<Edge> cloneEdges(ArrayList<Edge> edges) {
        ArrayList<Edge> newEdges = new ArrayList<Edge>();
        for (Edge e : edges) {
            newEdges.add(new Edge(e.getVertex1(), e.getVertex2(), e.getWeight()));
        }
        return newEdges;
    }

    public ArrayList<Integer> cloneVertexes(ArrayList<Integer> vertexes) {
        ArrayList<Integer> newVertexes = new ArrayList<Integer>(vertexes);
        return newVertexes;
    }

    public int getCountVertexes() {
        return countVertexes;
    }

    public int getCountEdges() {
        return countEdges;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public ArrayList<Integer> getVertexes() {
        return vertexes;
    }

    public Integer addVertex() {
        ListIterator<Integer> iterator = vertexes.listIterator();
        int index = 0;
        while (iterator.hasNext()) {//вставляем в первое "пустое" место
            Integer v = iterator.next();
            if (v != index) {
                iterator.previous();
                iterator.add(index);
                countVertexes++;
                return index;
            }
            index++;
        }
        vertexes.add(countVertexes);
        countVertexes++;
        return countVertexes - 1;
    }

    public void deleteVertex(int vertex) throws IndexOutOfBoundsException {
        if (vertexes.contains(vertex)) {
            ListIterator<Integer> iterator = vertexes.listIterator();
            while (iterator.hasNext()) {
                Integer v = iterator.next();
                if (v == vertex) {
                    iterator.remove();
                    break;
                }
            }
            deleteIncidentEdges(vertex);
            countVertexes--;
        } else {
            throw new IndexOutOfBoundsException("Vertex with number " + vertex + "does not exist");
        }
    }

    public void addEdge(Edge edge) throws IOException {
        if (!(vertexes.contains(edge.getVertex1()) && vertexes.contains(edge.getVertex2()) && edge.getVertex1() != edge.getVertex2())) {
            throw new IOException("Wrong edge");
        }

        if (containsEdge(edge)) {//если ребро инцидентное данным вершинам уже есть, то заменяем его
            Optional<ListIterator<Edge>> iteratorOpt = getIteratorEdge(edge);
            if (iteratorOpt.isPresent()) {
                ListIterator<Edge> iterator = iteratorOpt.get();
                iterator.set(edge);
            }
        } else {
            edges.add(edge);
            countEdges++;
        }
    }

    public void deleteEdge(int vertex1, int vertex2) throws IndexOutOfBoundsException {
        if (vertex1 < countVertexes && vertex1 >= 0 && vertex2 < countVertexes && vertex2 >= 0) {
            Edge edge = new Edge(vertex1, vertex2);
            if (containsEdge(edge))//если такое ребро есть
            {
                Optional<ListIterator<Edge>> iteratorOpt = getIteratorEdge(edge);
                if (iteratorOpt.isPresent()) {
                    ListIterator<Edge> iterator = iteratorOpt.get();
                    iterator.remove();
                    countEdges--;
                }
            } else {
                throw new IndexOutOfBoundsException("Edge with vertex numbers" + vertex1 + " and " + vertex2 + " does not exist");
            }
        }
    }

    public void setEdges(ArrayList<Edge> edges) {
        if (edges != null) {
            this.edges = edges;
        }
    }

    private void fillEdges(ArrayList<Edge> edges, ArrayList<ArrayList<Integer>> matrix) {
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = i; j < matrix.size(); j++) {
                Integer weight = matrix.get(i)
                        .get(j);
                if (!weight.equals(EMPTY_EDGE)) {
                    edges.add(new Edge(i, j, weight));
                    countEdges++;
                }
            }
        }
    }

    private void fillVertexes(ArrayList<Integer> vertexes, ArrayList<ArrayList<Integer>> matrix) {
        for (int i = 0; i < matrix.size(); i++) {
            vertexes.add(i);
        }
    }

    public boolean containsEdge(Edge edge) {
        ListIterator<Edge> iterator = edges.listIterator();
        while (iterator.hasNext()) {
            Edge tempEdge = iterator.next();
            if (edge.comparisonIncidentVertexes(tempEdge)) {
                return true;
            }
        }
        return false;
    }

    private Optional<ListIterator<Edge>> getIteratorEdge(Edge edge) {
        ListIterator<Edge> iterator = edges.listIterator();
        while (iterator.hasNext()) {
            Edge tempEdge = iterator.next();
            if (edge.comparisonIncidentVertexes(tempEdge)) {
                break;
            }
        }
        if (iterator.hasPrevious()) {
            iterator.previous();
        }
        return Optional.ofNullable(iterator);
    }

    public Graph copyGraph() {
        return new Graph(this.edges, this.vertexes);
    }

    private void deleteIncidentEdges(int vertex) {
        edges.removeIf(edge -> edge.getVertex1() == vertex || edge.getVertex2() == vertex);
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null || object.getClass() != this.getClass()) {
            return false;
        }
        Graph graph = (Graph) object;
        ArrayList<Edge> edges1 = new ArrayList<Edge>(this.edges);
        ArrayList<Edge> edges2 = new ArrayList<Edge>(graph.edges);
        edges1.sort(Comparator.comparingInt(Edge::getWeight));
        edges2.sort(Comparator.comparingInt(Edge::getWeight));

        return edges1.equals(edges2) && this.countEdges == graph.countEdges &&
                this.countVertexes == graph.countVertexes && this.vertexes.equals(graph.vertexes);

    }

}
