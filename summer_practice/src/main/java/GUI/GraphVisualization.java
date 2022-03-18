package GUI;

import Controller.*;
import org.apache.logging.log4j.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class GraphVisualization extends JPanel implements MouseListener, MouseMotionListener {
    ArrayList<VertexVisualization> vertexes;
    ArrayList<EdgeVisualization> edges;
    private final int HEIGHT = 600;
    private final int WIDTH = 930;
    VertexVisualization movableVertex;
    VertexHandler vHandler;
    EdgeHandler eHandler;
    private int button;
    AddVertexAction addVertexAction;
    private static boolean waitClick = false;

    public static final int RADIUS = 20;

    public GraphVisualization(ArrayList<VertexVisualization> vertexes, ArrayList<EdgeVisualization> edges) {
        this.vertexes = vertexes;
        this.edges = edges;
        vertexes.forEach((v) -> v.setRadius(RADIUS));
        setCoordinates();
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void setVertexes(ArrayList<VertexVisualization> v) {
        this.vertexes = v;
        vertexes.forEach((vertex) -> vertex.setRadius(RADIUS));
    }

    public void setEdges(ArrayList<EdgeVisualization> e) {
        this.edges = e;
    }

    public ArrayList<VertexVisualization> getVertexes() {
        return vertexes;
    }

    public ArrayList<EdgeVisualization> getEdges() {
        return edges;
    }

    public void setCoordinates() {
        int count = vertexes.size();
        float pi = (float) Math.PI;
        float delta = 2 * pi / count;
        int i = 0;
        for (VertexVisualization v : vertexes) {
            double angle = pi - i * delta;
            Dimension d = getSize();
            float panel_radius = Float.min(WIDTH, HEIGHT) / 2;
            float x = panel_radius * (float) Math.cos(angle) + panel_radius;
            float y = panel_radius * (float) Math.sin(angle) + panel_radius;
            x = x >= RADIUS ? x : RADIUS;
            y = y >= RADIUS ? y : RADIUS;
            x = Math.min(x, panel_radius * 2 - RADIUS);
            y = Math.min(y, panel_radius * 2 - RADIUS);
            v.setCoords(x + 110, y);
            i++;
        }
    }

    public void setVertexHandler(ElementHandler vertexHandler) {
        this.vHandler = (VertexHandler) vertexHandler;
    }

    public void setEdgeHandler(ElementHandler edgeHandler) {
        this.eHandler = (EdgeHandler) edgeHandler;
    }

    public void setAddVertexAction(AddVertexAction action) {
        this.addVertexAction = action;
    }

    public static void setWaitClick(boolean wait) {
        waitClick = wait;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics = (Graphics2D) g;
        try {
            for (EdgeVisualization e : edges) {
                e.draw(graphics);
            }

            for (VertexVisualization v : vertexes) {
                v.setColor(Color.GREEN);
                v.draw(graphics);
            }
        } catch (NumberFormatException e) {
            Logger logger = LogManager.getLogger();
            logger.info("in GraphVisualization: " + e.getMessage());
        }
        setSize(WIDTH, HEIGHT);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (waitClick) {
            int numVertex = -1;
            for (VertexVisualization v : vertexes) {
                float x = v.getCoordX();
                float y = v.getCoordY();
                float radius = v.getRadius();
                if (e.getX() <= x + radius && e.getY() <= y + radius && e.getX() >= x - radius && e.getY() >= y - radius) {
                    numVertex = v.getVertexNum();
                    break;
                }
            }
            vHandler.setVertexNumForLink(numVertex);
            vHandler.setNumButton(e.getButton());
            vHandler.addLinkProcessing();
            return;
        }
        if (e.getButton() == MouseEvent.BUTTON3 || e.getButton() == MouseEvent.BUTTON1) {
            float x = e.getX();
            float y = e.getY();
            for (VertexVisualization vertex : vertexes) {
                float xVertex = vertex.getCoordX();
                float yVertex = vertex.getCoordY();
                float radius = vertex.getRadius();

                if (xVertex <= x + radius && xVertex >= x - radius && yVertex <= y + radius && yVertex >= y - radius) {
                    vHandler.setVertex(vertex);
                    vHandler.setNumButton(e.getButton());
                    vHandler.processing();
                    return;
                }
            }
            for (EdgeVisualization edge : edges) {
                VertexVisualization v1 = edge.getVertex1();
                VertexVisualization v2 = edge.getVertex2();
                float x0 = v1.getCoordX();
                float x1 = v2.getCoordX();
                float y0 = v1.getCoordY();
                float y1 = v2.getCoordY();

                if (lineContainsPoint(x, y, x0, y0, x1, y1)) {
                    eHandler.setEdge(edge);
                    eHandler.setNumButton(e.getButton());
                    eHandler.setCoords((int) x, (int) y);
                    eHandler.processing();
                    return;
                }
            }
            if (e.getButton() == MouseEvent.BUTTON3 && !Controller.algoStart) {
                JPopupMenu menu = new JPopupMenu();
                JMenuItem itemAddVertex = new JMenuItem("Добавить сюда вершину");

                itemAddVertex.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        addVertexAction.setCoords(e.getX(), e.getY());
                        addVertexAction.addVertex();
                    }
                });

                menu.add(itemAddVertex);
                menu.show(this, e.getX(), e.getY());
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        button = e.getButton();
        if (button == MouseEvent.BUTTON1 && !waitClick) {
            Collections.reverse(vertexes);
            for (VertexVisualization v : vertexes) {
                float x = v.getCoordX();
                float y = v.getCoordY();
                float radius = v.getRadius();

                if (e.getX() <= x + radius && e.getX() >= x - radius && e.getY() <= y + radius && e.getY() >= y - radius) {
                    movableVertex = v;
                    break;
                }
            }
            Collections.reverse(vertexes);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        button = -1;
        movableVertex = null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private boolean lineContainsPoint(float x, float y, float x0, float y0, float x1, float y1) {
        float diff = 15;
        if (Math.abs(x0 - x1) < diff) {//для вертикальной прямой
            diff = 15.0f;
            return Math.abs(x - x0) < diff && y < Math.max(y0, y1) && y > Math.min(y0, y1);
        }
        diff = 15;
        if (Math.abs(y0 - y1) < diff) {//для горизонтальной
            diff = 15.0f;
            return Math.abs(y - y0) < diff && x < Math.max(x0, x1) && x > Math.min(x0, x1);
        }
        diff = 0.03f;


        return Math.abs((x - x0) / (x1 - x0) - (y - y0) / (y1 - y0)) < diff && x < Math.max(x0, x1) && x > Math.min(x0, x1) && y < Math.max(y0, y1) && y > Math.min(y0, y1);
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        if (button != MouseEvent.BUTTON1 || movableVertex == null || waitClick) {
            return;
        }
        int x = mouseEvent.getX();
        int y = mouseEvent.getY();
        if (x < movableVertex.getRadius() || y < movableVertex.getRadius() || x > getWidth() - movableVertex.getRadius() || y > getHeight() - movableVertex.getRadius()) {
            return;
        }
        movableVertex.setCoords(x, y);
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
    }
}
