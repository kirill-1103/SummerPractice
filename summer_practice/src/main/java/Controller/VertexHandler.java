package Controller;

import Graph.*;
import GUI.*;
import org.apache.logging.log4j.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

public class VertexHandler implements ElementHandler {
    private int vertexNum = -1;
    private VertexVisualization vertexVisualization;
    private int secondVertexNum = -1;
    private int button = -1;
    private Controller controller;
    private boolean waitClickOnVertex = false;
    private int weight;

    VertexHandler(Controller controller) {
        this.controller = controller;
    }

    public void setVertexNumForLink(int vertexNum) {
        this.secondVertexNum = vertexNum;
    }

    public void setVertex(VertexVisualization vertex) {
        this.vertexNum = vertex.getVertexNum();
        vertexVisualization = vertex;
    }

    public void setNumButton(int button) {
        this.button = button;
    }

    @Override
    public void processing() {
        if (vertexNum != -1 && !Controller.algoStart) {
            JPopupMenu menu = new JPopupMenu();
            JMenuItem itemDelete = new JMenuItem("Удалить вершину");
            JMenuItem itemAddLink = new JMenuItem("Соедининть с другой вершиной");

            itemDelete.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    controller.graph.deleteVertex(vertexNum);
                    setChanges();
                }
            });

            itemAddLink.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!waitClickOnVertex && button == MouseEvent.BUTTON3 && !Controller.algoStart) {
                        String result = JOptionPane.showInputDialog(controller.graphVisualization, "<html><h3>Введите вес ребра, а затем кликните на нужную вершину");
                        if (result == null) {
                            return;
                        }
                        String[] splitResult = result.split(" ");
                        if (splitResult.length != 1) {
                            JOptionPane.showOptionDialog(controller.graphVisualization, "Неверный формат!", "information",
                                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                            return;
                        }
                        try {
                            weight = Integer.parseInt(splitResult[0]);
                            if (weight < 0) {
                                JOptionPane.showOptionDialog(controller.graphVisualization, "Введен отрицательный вес!", "information",
                                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                                return;
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showOptionDialog(controller.graphVisualization, "Неверный формат!", "information",
                                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                            Logger logger = LogManager.getLogger();
                            logger.info("in VertexHandler: Неверный формат веса! вес: " + result);
                            return;
                        }
                        waitClickOnVertex = true;
                        GraphVisualization.setWaitClick(true);
                        controller.gui.setButtonsEnable(false);
                    }
                }
            });
            if (button == MouseEvent.BUTTON3 && !waitClickOnVertex && !Controller.algoStart) {
                menu.add(itemDelete);
                menu.add(itemAddLink);
                menu.show(controller.graphVisualization, (int) vertexVisualization.getCoordX(), (int) vertexVisualization.getCoordY());
            }
        }
    }

    private void setChanges() {//изменяет визуализацию графа
        ArrayList<EdgeVisualization> edges = new ArrayList<>();
        ArrayList<VertexVisualization> vertexes = controller.graphVisualization.getVertexes();

        vertexes.removeIf(vertex -> !controller.graph.getVertexes()
                .contains(vertex.getVertexNum()));


        for (Edge edge : controller.graph.getEdges()) {
            int vertex1 = edge.getVertex1();
            int vertex2 = edge.getVertex2();
            VertexVisualization v1 = null;
            VertexVisualization v2 = null;
            for (VertexVisualization v : vertexes) {
                if (v.getVertexNum() == vertex1) {
                    v1 = v;
                }
                if (v.getVertexNum() == vertex2) {
                    v2 = v;
                }
            }
            edges.add(new EdgeVisualization(v1, v2, edge.getWeight()));
        }

        controller.graphVisualization.setVertexes(vertexes);
        controller.graphVisualization.setEdges(edges);
        controller.graphVisualization.setVertexHandler(new VertexHandler(controller));
        controller.graphVisualization.setEdgeHandler(new EdgeHandler(controller));
        controller.gui.setGraphVisualization(controller.graphVisualization);
    }

    public void addLinkProcessing() {
        if (waitClickOnVertex && button == MouseEvent.BUTTON1) {
            if (controller.graph.getVertexes()
                    .contains(vertexNum)) {
                try {
                    controller.graph.addEdge(new Edge(vertexVisualization.getVertex(), secondVertexNum, weight));
                    controller.gui.setButtonsEnable(true);
                    GraphVisualization.setWaitClick(false);
                    waitClickOnVertex = false;
                    setChanges();
                } catch (IOException ex) {
                    JOptionPane.showOptionDialog(controller.graphVisualization, "Нужно нажать на другую вершину!", "information",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                    Logger logger = LogManager.getLogger();
                    logger.info("in VertexHandler: Неверно выбрана вершина");
                }
            } else {
                JOptionPane.showOptionDialog(controller.graphVisualization, "Нужно нажать на вершину!", "information",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                Logger logger = LogManager.getLogger();
                logger.info("in VertexHandler: Неверно выбрана вершина");
            }
        }
    }
}
