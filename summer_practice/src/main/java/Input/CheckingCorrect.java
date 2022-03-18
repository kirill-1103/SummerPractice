package Input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;

import Graph.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CheckingCorrect {

    public static final int ERROR_COUNT_VERTEXES = -1;
    public static final int ERROR_COUNT_EDGES = -2;
    public static final int ERROR_MIN_WEIGHT = -3;
    public static final int ERROR_MAX_WEIGHT = -4;
    public static final int ERROR_NOT_FILLED = -5;
    public static final int ERROR_DEFAULT = -6;
    private static int[] errors;

    private static boolean isEmptyMatrix(String matrix) {
        return matrix.isEmpty();
    }

    private static boolean checkSymbolsAtStringMatrix(String matrix, String[] stringMatrix) {
        for (int i = 0; i < matrix.length(); i++) {
            char c = matrix.charAt(i);
            if (!(c >= '0' && c <= '9' || c == ' ' || c == '\n' || c == '-')) {
                return false;
            }
        }

        for (String s : stringMatrix) {
            String[] line = s.split(" ");
            for (String value : line) {
                try {
                    if (!value.equals("-")) {
                        Integer.parseInt(value);
                    }
                } catch (NumberFormatException e) {
                    Logger logger = LogManager.getLogger();
                    logger.info("in CheckingCorrect: " + e.getMessage());
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean checkCorrectSizeMatrix(String[] stringMatrix) {

        int size = stringMatrix.length;
        for (String matrix : stringMatrix) {
            String[] line = matrix.split(" ");
            if (line.length != size) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkDiagonal(String[] stringMatrix) {
        for (int i = 0; i < stringMatrix.length; i++) {
            String[] line = stringMatrix[i].split(" ");
            if (!line[i].equals("-")) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkCorrectStringMatrix(String matrix) {
        matrix = matrix.trim();
        String[] stringMatrix = matrix.split("\n");
        return !isEmptyMatrix(matrix) && checkSymbolsAtStringMatrix(matrix, stringMatrix) && checkCorrectSizeMatrix(stringMatrix) && checkDiagonal(stringMatrix);
    }

    public static boolean checkingCorrectStringEdge(String edge) {
        String[] edgeSplit = edge.split(" ");
        if (edgeSplit.length != 3) {
            return false;
        }
        for (int i = 0; i < 3; i++) {
            try {
                Integer.parseInt(edgeSplit[i]);
            } catch (NumberFormatException e) {
                Logger logger = LogManager.getLogger();
                logger.info("in CheckingCorrct: " + e.getMessage());
                return false;
            }
        }
        return true;
    }

    public static boolean checkCorrectMatrix(ArrayList<ArrayList<Integer>> matrix) {
        if (matrix == null) {
            return false;
        }

        int size = matrix.size();
        for (ArrayList<Integer> list : matrix) {//проверка на квадратную матрицу
            if (list.size() != size) {
                return false;
            }
        }

        for (int i = 0; i < matrix.size(); i++)//проверка на симметричность матрицы
        {
            for (int j = i + 1; j < matrix.size(); j++) {
                if (matrix.get(i)
                        .get(j) != matrix.get(j)
                        .get(i)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean checkCorrectStringLimits(String limits) {
        String[] splitLimits = limits.split(" ");
        errors = new int[6];
        Arrays.fill(errors, 0);
        int errorIndex = 0;

        if (splitLimits.length < 4) {
            errors[errorIndex] = ERROR_NOT_FILLED;
            return false;
        }
        if (splitLimits.length > 4) {
            errors[errorIndex] = ERROR_DEFAULT;
            return false;
        }

        for (int i = 0; i < 4; i++) {
            try {
                Integer.parseInt(splitLimits[i]);
            } catch (NumberFormatException ex) {
                switch (i) {
                    case 0:
                        errors[errorIndex++] = ERROR_COUNT_EDGES;
                        break;
                    case 1:
                        errors[errorIndex++] = ERROR_COUNT_VERTEXES;
                        break;
                    case 2:
                        errors[errorIndex++] = ERROR_MIN_WEIGHT;
                        break;
                    case 3:
                        errors[errorIndex++] = ERROR_MAX_WEIGHT;
                        break;
                }
                Logger logger = LogManager.getLogger();
                logger.info("in CheckingCorrect: " + ex.getMessage());
            }
        }
        return errorIndex == 0;
    }

    public static boolean checkCorrectGraphForBoruvka(Graph graph) {//проверка на то что граф связный
        if (graph.getVertexes()
                .size() < 2) {
            return false;
        }

        boolean[] added = new boolean[graph.getVertexes()
                .size()];
        Arrays.fill(added, false);
        LinkedList<Integer> vertexQueue = new LinkedList<Integer>();

        Integer vertex = graph.getEdges()
                .get(0)
                .getVertex1();
        added[graph.getVertexes()
                .indexOf(vertex)] = true;
        vertexQueue.addLast(vertex);
        do {
            vertex = vertexQueue.pollFirst();
            for (Edge edge : graph.getEdges()) {
                if (edge.getVertex1() == vertex || edge.getVertex2() == vertex) {
                    int v = (edge.getVertex1() == vertex) ? edge.getVertex2() : edge.getVertex1();
                    int index = graph.getVertexes()
                            .indexOf(v);
                    if (!added[index]) {
                        added[index] = true;
                        vertexQueue.addLast(v);
                    }
                }
            }
        } while (!vertexQueue.isEmpty());

        for (int i = 0; i < added.length; i++) {
            if (!added[i]) {
                return false;
            }
        }
        return true;
    }

    public static Optional<int[]> getErrors() {
        if (errors == null) {
            return Optional.empty();
        }
        Optional<int[]> errorsOpt = Optional.of(errors);
        return errorsOpt;
    }
}
