package GUI;

import java.awt.*;
import java.awt.geom.Line2D;

public class EdgeVisualization {

    private final static int DEFAULT_INT = -1;
    public final static Color DEFAULT_COLOR = Color.gray;
    public static final Color COLOR_JUST_ADD = Color.magenta;
    public static final Color COLOR_LAST_ADD = Color.red;

    private VertexVisualization vertex1;
    private VertexVisualization vertex2;
    private int weight = DEFAULT_INT;
    private Color color = DEFAULT_COLOR;

    public EdgeVisualization(VertexVisualization v1, VertexVisualization v2, int weight) throws NumberFormatException {
        if (weight < 0) {
            throw new NumberFormatException("wrong edge format ");
        }
        this.vertex1 = v1;
        this.vertex2 = v2;
        this.weight = weight;

    }

    public void setColor(Color color) throws NumberFormatException {
        if (color == null) {
            throw new NumberFormatException("wrong edge color format");
        }
        this.color = color;
    }

    public VertexVisualization getVertex1() {
        return vertex1;
    }

    public VertexVisualization getVertex2() {
        return vertex2;
    }

    public int getWeight() {
        return weight;
    }

    public void draw(Graphics2D graphics) {
        if (vertex1 != null && vertex2 != null && weight != DEFAULT_INT) {
            Line2D edge = new Line2D.Float(vertex1.getCoordX(), vertex1.getCoordY(), vertex2.getCoordX(), vertex2.getCoordY());
            graphics.setColor(color);
            graphics.draw(edge);
            graphics.drawString(Integer.toString(weight), (vertex1.getCoordX() + vertex2.getCoordX()) / 2, (vertex1.getCoordY() + vertex2.getCoordY()) / 2);
        }
    }
}
