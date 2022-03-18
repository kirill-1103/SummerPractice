package Graph;

public class Edge {
    private int vertex1;
    private int vertex2;
    private int weight;
    private boolean markLastAdded = false;

    public Edge(int v1, int v2, int weight) {
        this.vertex1 = v1;
        this.vertex2 = v2;
        this.weight = weight;
    }

    public Edge(int v1, int v2) {
        this.vertex1 = v1;
        this.vertex2 = v2;
        this.weight = 0;
    }

    public int getVertex1() {
        return vertex1;
    }

    public int getVertex2() {
        return vertex2;
    }

    public int getWeight() {
        return weight;
    }

    public boolean comparisonIncidentVertexes(Edge edge) {
        return this.vertex1 == edge.getVertex1() && this.vertex2 == edge.getVertex2() || this.vertex2 == edge.getVertex1() && this.vertex1 == edge.getVertex2();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null || object.getClass() != this.getClass()) {
            return false;
        }
        Edge edge = (Edge) object;
        return this.comparisonIncidentVertexes(edge) && edge.weight == this.weight;
    }

    public void setMarkLastAdded(boolean mark) {
        markLastAdded = mark;
    }

    public boolean getMarkLastAdded() {
        return markLastAdded;
    }

    @Override
    public String toString() {
        return "v1:" + vertex1 + " v2:" + vertex2 + " weight" + weight + "\n";
    }
}
