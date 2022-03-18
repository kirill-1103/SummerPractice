package Controller;

import GUI.EdgeVisualization;
import GUI.VertexVisualization;
import Graph.Edge;

import javax.swing.*;
import java.util.ArrayList;

public class AddVertexAction {

    private static final int DEFAULT_COORD = 40;
    private int coordX = DEFAULT_COORD;
    private int coordY = DEFAULT_COORD;

    Controller controller;

    AddVertexAction(Controller controller) {
        this.controller = controller;
    }

    public void setCoords(int x, int y) {
        this.coordX = x;
        this.coordY = y;
    }

    public void addVertex() {
        if (Controller.algoStart) {
            return;
        }
        controller.edges.clear();
        ArrayList<VertexVisualization> newVertexes = new ArrayList<>();
        controller.graph.addVertex();

        for (Integer v : controller.graph.getVertexes()) {
            newVertexes.add(new VertexVisualization(v));
        }

        boolean newVertex;
        for (VertexVisualization v1 : newVertexes) {
            newVertex = false;
            for (VertexVisualization v2 : controller.vertexes) {
                if (v1.getVertexNum() == v2.getVertexNum() && v2.getVertexNum() == v1.getVertexNum()) {
                    v1.setCoords(v2.getCoordX(), v2.getCoordY());
                    newVertex = true;
                    break;
                }
            }
            if (!newVertex) {
                v1.setCoords(coordX, coordY);
            }
        }

        controller.vertexes = newVertexes;

        for (Edge edge : controller.graph.getEdges()) {
            int vertex1 = edge.getVertex1();
            int vertex2 = edge.getVertex2();
            int indexVertex1 = controller.graph.getVertexes()
                    .indexOf(vertex1);
            int indexVertex2 = controller.graph.getVertexes()
                    .indexOf(vertex2);
            controller.edges.add(new EdgeVisualization(controller.vertexes.get(indexVertex1), controller.vertexes.get(indexVertex2), edge.getWeight()));
        }

        controller.graphVisualization.setVertexes(controller.vertexes);
        controller.graphVisualization.setEdges(controller.edges);
        controller.graphVisualization.setVertexHandler(new VertexHandler(controller));
        controller.graphVisualization.setEdgeHandler(new EdgeHandler(controller));
        controller.gui.setGraphVisualization(controller.graphVisualization);
    }
}
