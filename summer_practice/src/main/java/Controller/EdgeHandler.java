package Controller;

import GUI.EdgeVisualization;
import GUI.GraphVisualization;
import GUI.Gui;
import GUI.VertexVisualization;
import Graph.*;
import org.apache.logging.log4j.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

public class EdgeHandler implements ElementHandler {
    private EdgeVisualization edgeVisualization;
    private int button = -1;
    int coordX = -1;
    int coordY = -1;
    Controller controller;

    EdgeHandler(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void processing() {
        if (edgeVisualization != null && coordX != -1 && coordY != -1) {
            if (button == MouseEvent.BUTTON3 && !Controller.algoStart) {
                JPopupMenu menu = new JPopupMenu();
                JMenuItem itemDelete = new JMenuItem("Удалить ребро");
                JMenuItem itemChange = new JMenuItem("Изменить вес ребра");

                itemDelete.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        controller.graph.deleteEdge(edgeVisualization.getVertex1()
                                .getVertexNum(), edgeVisualization.getVertex2()
                                .getVertexNum());
                        setChanges();
                    }
                });

                itemChange.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String result = JOptionPane.showInputDialog(controller.graphVisualization, "<html><h3>Введите неотрицательный вес");
                        if (result == null) {
                            return;
                        }
                        try {
                            int weight = Integer.parseInt(result);
                            if (weight < 0) {
                                JOptionPane.showOptionDialog(controller.graphVisualization, "Введен отрицательный вес!", "information",
                                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                                return;
                            }
                            controller.graph.addEdge(new Edge(edgeVisualization.getVertex1()
                                    .getVertexNum(), edgeVisualization.getVertex2()
                                    .getVertexNum(), weight));
                            setChanges();
                        } catch (NumberFormatException | IOException ex) {
                            JOptionPane.showOptionDialog(controller.graphVisualization, "Неверный формат веса ребра.", "information",
                                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                            Logger logger = LogManager.getLogger();
                            logger.info("in EdgeHandler: Неверный формат веса ребра; вес:" + result);
                        }
                    }
                });

                menu.add(itemDelete);
                menu.add(itemChange);
                menu.show(controller.graphVisualization, coordX, coordY);
            } else if (button == MouseEvent.BUTTON1) {
                JOptionPane.showOptionDialog(controller.graphVisualization, "вес: " + edgeVisualization.getWeight(), "information", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
            }
        }
    }

    private void setChanges() {
        ArrayList<VertexVisualization> vertexes = controller.graphVisualization.getVertexes();
        ArrayList<EdgeVisualization> edges = new ArrayList<>();

        for (Edge edge : controller.graph.getEdges()) {
            int vertex1 = edge.getVertex1();
            int vertex2 = edge.getVertex2();
            int indexVertex1 = controller.graph.getVertexes()
                    .indexOf(vertex1);
            int indexVertex2 = controller.graph.getVertexes()
                    .indexOf(vertex2);
            edges.add(new EdgeVisualization(vertexes.get(indexVertex1), vertexes.get(indexVertex2), edge.getWeight()));
        }

        controller.graphVisualization.setEdges(edges);
        controller.graphVisualization.setVertexHandler(new VertexHandler(controller));
        controller.graphVisualization.setEdgeHandler(new EdgeHandler(controller));
        controller.gui.setGraphVisualization(controller.graphVisualization);
    }

    public void setEdge(EdgeVisualization edge) {
        this.edgeVisualization = edge;
    }

    public void setCoords(int x, int y) {
        coordX = x;
        coordY = y;
    }

    public void setNumButton(int button) {
        this.button = button;
    }
}
