package Controller;

import Boruvka.Boruvka;
import GUI.*;
import GUI.Gui;
import Graph.*;
import Input.CheckingCorrect;
import Input.Converter;
import Boruvka.*;
import SaveLoad.Loader;
import SaveLoad.Unloader;
import org.apache.logging.log4j.*;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.Style;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


public class Controller {
    Graph graph;
    Boruvka boruvka;
    BoruvkaSteps steps;
    Gui gui;
    GraphVisualization graphVisualization;
    ArrayList<EdgeVisualization> edges;
    ArrayList<VertexVisualization> vertexes;
    AddGraphListener addGraphListener;
    AddVertexListener addVertexListener;
    AddEdgeListener addEdgeListener;
    GenerateGraphListener generateGraphListener;
    StartListener startListener;
    NextListener nextListener;
    PrevListener prevListener;
    StopListener stopListener;
    EndListener endListener;
    SaveListener saveListener;
    LoadListener loadListener;
    String lastPath = null;

    public static boolean algoStart = false;

    public Controller() {
        addGraphListener = new AddGraphListener();
        addVertexListener = new AddVertexListener();
        addEdgeListener = new AddEdgeListener();
        generateGraphListener = new GenerateGraphListener();
        startListener = new StartListener();
        nextListener = new NextListener();
        prevListener = new PrevListener();
        stopListener = new StopListener();
        endListener = new EndListener();
        saveListener = new SaveListener();
        loadListener = new LoadListener();

    }

    public void start() {
        graph = new Graph();
        edges = new ArrayList<>();
        vertexes = new ArrayList<>();

        graphVisualization = new GraphVisualization(vertexes, edges);
        gui = new Gui(graphVisualization);

        graphVisualization.setVertexHandler(new VertexHandler(this));
        graphVisualization.setEdgeHandler(new EdgeHandler(Controller.this));
        graphVisualization.setAddVertexAction(new AddVertexAction(this));
        setGuiButtonsListeners();
        gui.setVisible(true);
    }

    private void setGuiButtonsListeners() {
        gui.setButtonInputGraphListener(addGraphListener);
        gui.setButtonInputAddVertexListener(addVertexListener);
        gui.setButtonInputAddEdgeListener(addEdgeListener);
        gui.setButtonGenerateGraphListener(generateGraphListener);
        gui.setButtonStartListener(startListener);
        gui.setButtonNextListener(nextListener);
        gui.setButtonPrevListener(prevListener);
        gui.setButtonEndListener(endListener);
        gui.setButtonStopListener(stopListener);

        gui.setSaveListener(saveListener);
        gui.setLoadListener(loadListener);
    }

    private void changeStep(Optional<ArrayList<Edge>> edgesOpt) {
        if (edgesOpt.isPresent()) {
            ArrayList<Edge> edgesList = edgesOpt.get();
            boolean color;
            edges = graphVisualization.getEdges();
            for (Edge edge : graph.getEdges()) {
                EdgeVisualization edgeVisualization = null;
                color = false;
                for (EdgeVisualization e : edges) {
                    if (e.getVertex1()
                            .getVertexNum() == edge.getVertex1() && e.getVertex2()
                            .getVertexNum() == edge.getVertex2() ||
                            e.getVertex2()
                                    .getVertexNum() == edge.getVertex1() && e.getVertex1()
                                    .getVertexNum() == edge.getVertex2()) {
                        edgeVisualization = e;
                        break;
                    }
                }
                for (Edge edge2 : edgesList) {
                    if (edge2.equals(edge)) {
                        if (edge2.getMarkLastAdded()) {
                            edgeVisualization.setColor(EdgeVisualization.COLOR_LAST_ADD);
                        } else {
                            edgeVisualization.setColor(EdgeVisualization.COLOR_JUST_ADD);
                        }
                        color = true;
                        break;
                    }
                }
                if (!color) {
                    edgeVisualization.setColor(EdgeVisualization.DEFAULT_COLOR);
                }
            }

        }
        graphVisualization.setEdges(edges);
        gui.setGraphVisualization(graphVisualization);
    }

    class AddGraphListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (algoStart) {
                JOptionPane.showOptionDialog(graphVisualization, "Недоступно, пока работает алгоритм!", "information",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                return;
            }
            try {
                String matrix = Controller.this.gui.getTextAtInputAddGraph();
                if (CheckingCorrect.checkCorrectStringMatrix(matrix)) {
                    vertexes.clear();
                    edges.clear();

                    ArrayList<ArrayList<Integer>> matrixInt = Converter.convertStringMatrix(matrix);
                    graph = new Graph(matrixInt);

                    for (Integer v : graph.getVertexes()) {
                        vertexes.add(new VertexVisualization(v));
                    }

                    for (Edge edge : graph.getEdges()) {
                        edges.add(new EdgeVisualization(vertexes.get(edge.getVertex1()), vertexes.get(edge.getVertex2()), edge.getWeight()));
                    }

                    graphVisualization.setVertexes(vertexes);
                    graphVisualization.setCoordinates();
                    graphVisualization.setEdges(edges);
                    graphVisualization.setVertexHandler(new VertexHandler(Controller.this));
                    graphVisualization.setEdgeHandler(new EdgeHandler(Controller.this));
                    gui.setGraphVisualization(graphVisualization);
                } else {
                    JOptionPane.showOptionDialog(graphVisualization, "Неверная матрица смежности!", "information",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                }
            } catch (IOException ex) {
                Logger logger = LogManager.getLogger();
                logger.error(ex.getMessage());
            }
        }
    }

    class AddVertexListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (algoStart) {
                JOptionPane.showOptionDialog(graphVisualization, "Недоступно, пока работает алгоритм!", "information",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                return;
            }
            AddVertexAction action = new AddVertexAction(Controller.this);
            action.addVertex();
        }
    }


    class AddEdgeListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (algoStart) {
                JOptionPane.showOptionDialog(graphVisualization, "Недоступно, пока работает алгоритм", "information",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                return;
            }
            String stringEdge = gui.getTextAtInputAddEdge();
            if (CheckingCorrect.checkingCorrectStringEdge(stringEdge)) {
                Edge edge = Converter.convertStringEdge(stringEdge);
                try {
                    graph.addEdge(edge);

                    edges.clear();
                    for (Edge edge_ : graph.getEdges()) {
                        try {
                            edges.add(new EdgeVisualization(vertexes.get(edge_.getVertex1()), vertexes.get(edge_.getVertex2()), edge_.getWeight()));
                        } catch (NumberFormatException ex) {
                            JOptionPane.showOptionDialog(graphVisualization, "Ребро введено неверно!", "information",
                                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                            Logger logger = LogManager.getLogger();
                            logger.info("in AddEdgeListener: Неверный ввод ребра! ребро:" + stringEdge);
                        }
                    }

                    graphVisualization.setEdges(edges);
                    graphVisualization.setVertexHandler(new VertexHandler(Controller.this));
                    graphVisualization.setEdgeHandler(new EdgeHandler(Controller.this));
                    gui.setGraphVisualization(graphVisualization);
                } catch (IOException ex) {
                    JOptionPane.showOptionDialog(graphVisualization, "Ребро введено неверно!", "information",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                    Logger logger = LogManager.getLogger();
                    logger.info("in AddEdgeListener: Неверный ввод ребра! ребро: " + stringEdge);
                }
            } else {
                JOptionPane.showOptionDialog(graphVisualization, "Ребро введено неверно!", "information",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
            }
        }
    }

    class GenerateGraphListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (algoStart) {
                JOptionPane.showOptionDialog(graphVisualization, "Недоступно, пока работает алгоритм!", "information",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                return;
            }
            String stringGenerate = gui.getTextAtInputLimits();

            if (CheckingCorrect.checkCorrectStringLimits(stringGenerate)) {
                int[] limits = Converter.convertStringLimits(stringGenerate);
                try {
                    int countEdges = limits[0];
                    int countVertexes = limits[1];
                    int minWeight = limits[2];
                    int maxWeight = limits[3];

                    StringBuilder message = new StringBuilder();
                    if (countVertexes < 2) {
                        message.append("Введено неверное количество вершин! Число должно быть больше 1.\n");
                    }
                    if (countEdges < countVertexes - 1 || countEdges > countVertexes * (countVertexes - 1) / 2) {
                        message.append("Введено неверное количство ребер! Ребер должно хватать, чтобы граф был связным и не больше чем максимальное количество ребер для графа.\n");
                    }
                    if (maxWeight < 0) {
                        message.append("Введен неверный максимальный вес! Должен быть положительным.\n");
                    }
                    if (minWeight > maxWeight) {
                        message.append("Введен неверный минимальный вес! Должен быть меньше максимального, и не меньше 0.\n");
                    }
                    if (message.length() != 0) {
                        JOptionPane.showOptionDialog(graphVisualization, message.toString(), "information",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                        return;
                    }


                    GraphGenerator generator = new GraphGenerator(countEdges, countVertexes, minWeight, maxWeight);
                    graph = generator.generateGraph();

                    vertexes.clear();
                    edges.clear();

                    for (Integer v : graph.getVertexes()) {
                        vertexes.add(new VertexVisualization(v));
                    }

                    for (Edge edge : graph.getEdges()) {
                        edges.add(new EdgeVisualization(vertexes.get(edge.getVertex1()), vertexes.get(edge.getVertex2()), edge.getWeight()));
                    }

                    graphVisualization.setVertexes(vertexes);
                    graphVisualization.setCoordinates();
                    graphVisualization.setEdges(edges);
                    graphVisualization.setVertexHandler(new VertexHandler(Controller.this));
                    graphVisualization.setEdgeHandler(new EdgeHandler(Controller.this));
                    gui.setGraphVisualization(graphVisualization);
                } catch (IOException ex) {
                    JOptionPane.showOptionDialog(graphVisualization, "Неверно введены параметры графа!", "information",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                    Logger logger = LogManager.getLogger();
                    logger.info("in GenerateGraphListener: Неверно введены параметры графа! параметры:" + stringGenerate);
                }
            } else {
                Optional<int[]> errorsOpt = CheckingCorrect.getErrors();
                if (errorsOpt.isPresent()) {
                    StringBuilder message = new StringBuilder();
                    int[] errors = errorsOpt.get();
                    for (int i = 0; i < errors.length && message.length() == 0; i++) {
                        switch (errors[i]) {
                            case CheckingCorrect.ERROR_COUNT_EDGES:
                                message.append("Неверно введено поле для количества ребер!\n");
                                break;
                            case CheckingCorrect.ERROR_COUNT_VERTEXES:
                                message.append("Неверно введено поле для количество вершин!\n");
                                break;
                            case CheckingCorrect.ERROR_MIN_WEIGHT:
                                message.append("Неверно введено поле для минимального веса!\n");
                                break;
                            case CheckingCorrect.ERROR_MAX_WEIGHT:
                                message.append("Неверно введено поле для максимального веса!\n");
                                break;
                            case CheckingCorrect.ERROR_NOT_FILLED:
                                message.append("Не все поля заполнены\n");
                                break;
                            case CheckingCorrect.ERROR_DEFAULT:
                                message.append("Неверно введены параметры графа!\n");
                                break;
                        }
                    }
                    JOptionPane.showOptionDialog(graphVisualization, message.toString(), "information",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                }
            }
        }
    }

    class StartListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!algoStart) {
                if (CheckingCorrect.checkCorrectGraphForBoruvka(graph)) {
                    boruvka = new Boruvka(graph);
                    steps = boruvka.getBoruvkaSteps();
                    algoStart = true;
                    gui.changeMode();
                } else {
                    JOptionPane.showOptionDialog(graphVisualization, "Граф не удовлетворяет условиям работы алгоритма!", "information",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                }
            }
        }
    }

    class NextListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (algoStart) {
                Optional<ArrayList<Edge>> edgesOpt = steps.nextStep();
                Controller.this.changeStep(edgesOpt);
            } else {
                JOptionPane.showOptionDialog(graphVisualization, "Недоступно, пока алгоритм не работает!", "information",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
            }
        }
    }

    class PrevListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (algoStart) {
                Optional<ArrayList<Edge>> edgesOpt = steps.prevStep();
                Controller.this.changeStep(edgesOpt);
            } else {
                JOptionPane.showOptionDialog(graphVisualization, "Недоступно, пока алгоритм не работает!", "information",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
            }
        }
    }

    class EndListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (algoStart) {
                Optional<ArrayList<Edge>> edgesOpt = steps.lastStep();
                Controller.this.changeStep(edgesOpt);
            } else {
                JOptionPane.showOptionDialog(graphVisualization, "Недоступно, пока не работает алгоритм!", "information",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
            }
        }
    }

    class StopListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (algoStart) {
                algoStart = false;
                for (EdgeVisualization edge : edges) {
                    edge.setColor(EdgeVisualization.DEFAULT_COLOR);
                }
                graphVisualization.setEdges(edges);
                gui.setGraphVisualization(graphVisualization);
                gui.changeMode();
            } else {
                JOptionPane.showOptionDialog(graphVisualization, "Недоступно, пока не работает алгоритм", "information",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
            }
        }
    }

    class LoadListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            JFileChooser fileChooser;
            if (lastPath != null) {
                fileChooser = new JFileChooser(lastPath);
            } else {
                fileChooser = new JFileChooser();
            }
            fileChooser.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.getName()
                            .endsWith(".json") || file.isDirectory();
                }

                @Override
                public String getDescription() {
                    return null;
                }
            });
            int option = fileChooser.showOpenDialog(gui);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String fileName = file.getAbsolutePath()
                        .toString();
                int indexSlash = -1;
                for (int i = 0; i < fileName.length(); i++) {
                    if (fileName.charAt(i) == '\\' || fileName.charAt(i) == '/') {
                        indexSlash = i;
                    }
                }
                if (indexSlash != -1) {
                    lastPath = fileName.substring(0, indexSlash);
                }

                Loader loader = new Loader();
                Optional<Graph> graphOpt = loader.load(fileName);
                if (graphOpt.isPresent()) {
                    graph = graphOpt.get();

                    edges.clear();
                    vertexes.clear();
                    for (Integer vertex : graph.getVertexes()) {
                        vertexes.add(new VertexVisualization(vertex));
                    }
                    for (Edge edge : graph.getEdges()) {
                        VertexVisualization vertexVisualization1 = null;
                        VertexVisualization vertexVisualization2 = null;
                        for (VertexVisualization vertex : vertexes) {
                            if (vertex.getVertexNum() == edge.getVertex1()) {
                                vertexVisualization1 = vertex;
                            }
                            if (vertex.getVertexNum() == edge.getVertex2()) {
                                vertexVisualization2 = vertex;
                            }
                        }
                        edges.add(new EdgeVisualization(vertexVisualization1, vertexVisualization2, edge.getWeight()));
                    }
                } else {
                    JOptionPane.showOptionDialog(graphVisualization, "Не удалось загрузить файл!", "information",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                    return;
                }
                graphVisualization.setEdges(edges);
                graphVisualization.setVertexes(vertexes);

                graphVisualization.setVertexHandler(new VertexHandler(Controller.this));
                graphVisualization.setEdgeHandler(new EdgeHandler(Controller.this));
                graphVisualization.setCoordinates();
                gui.setGraphVisualization(graphVisualization);
            }
        }
    }

    class SaveListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            JFileChooser fileChooser;
            if (lastPath != null) {
                fileChooser = new JFileChooser(lastPath);
            } else {
                fileChooser = new JFileChooser();
            }
            fileChooser.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.getName()
                            .endsWith(".json") || file.isDirectory();
                }

                @Override
                public String getDescription() {
                    return null;
                }
            });
            int option = fileChooser.showSaveDialog(gui);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String fileName = file.getAbsolutePath();
                if (fileName.isEmpty()) {
                    JOptionPane.showOptionDialog(graphVisualization, "Введено пустое имя файла!", "information",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                    return;
                }
                int indexSlash = -1;
                for (int i = 0; i < fileName.length(); i++) {
                    if (fileName.charAt(i) == '\\' || fileName.charAt(i) == '/') {
                        indexSlash = i;
                    }
                }
                if (indexSlash != -1) {
                    lastPath = fileName.substring(0, indexSlash);
                }

                Unloader unloader = new Unloader();
                boolean saveok;
                if (fileName.length() > 5 && fileName.substring(fileName.length() - 5)
                        .equals(".json")) {
                    saveok = unloader.save(graph, fileName);
                } else {
                    saveok = unloader.save(graph, fileName + ".json");
                }
                if (!saveok) {
                    JOptionPane.showOptionDialog(graphVisualization, "Не удалось сохранить файл!", "information",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                }
            }
        }
    }

}

