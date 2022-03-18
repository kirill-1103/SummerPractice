package GUI;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class VertexVisualization {
    private static final float DEFAULT_FLOAT = -1.0f;
    private static final int DEFAULT_INT = -1;
    private static final Color DEFAULT_COLOR = Color.GREEN;

    private Integer vertexNum = DEFAULT_INT;
    private float coordX = DEFAULT_FLOAT;
    private float coordY = DEFAULT_FLOAT;
    private Color color = DEFAULT_COLOR;
    private float radius = DEFAULT_FLOAT;

    public VertexVisualization(Integer vertexNum) throws NumberFormatException {
        if (vertexNum < 0) {
            throw new NumberFormatException("wrong vertex format");
        }
        this.vertexNum = vertexNum;
    }

    public Integer getVertex() {
        return vertexNum;
    }

    public float getCoordX() {
        return coordX;
    }

    public float getCoordY() {
        return coordY;
    }

    public void setCoords(float coordX, float coordY) throws NumberFormatException {
        if (coordX < 0 || coordY < 0) {
            throw new NumberFormatException("wrong vertex format");
        }
        this.coordX = coordX;
        this.coordY = coordY;
    }

    public void setColor(Color color) throws NumberFormatException {
        if (color == null) {
            throw new NumberFormatException("color for vertex is null");
        }
        this.color = color;
    }


    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }

    public void draw(Graphics2D graphics) {
        if (coordX != DEFAULT_FLOAT && coordY != DEFAULT_FLOAT && radius != DEFAULT_FLOAT) {
            Ellipse2D vertexView = new Ellipse2D.Double(coordX - radius, coordY - radius, radius * 2, radius * 2);
            graphics.setColor(color);
            graphics.fill(vertexView);
            graphics.setColor(Color.black);
            graphics.drawString("v" + vertexNum.toString(), coordX - radius / 5, coordY + radius / 5);
        }
    }

    public int getVertexNum() {
        return vertexNum;
    }

    @Override
    public String toString() {
        return Integer.toString(vertexNum);
    }
}
