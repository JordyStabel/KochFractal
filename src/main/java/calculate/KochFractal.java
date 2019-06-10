/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import fun3kochfractalfx.EdgeDrawer;
import fun3kochfractalfx.EdgeType;
import javafx.scene.paint.Color;

/**
 * @author Peter Boots
 * Modified for FUN3 by Gertjan Schouten
 * Modified for FUN3 by Jordy Stabèl
 */
public class KochFractal implements Runnable {

    private int level = 1;      // The current level of the fractal
    private int nrOfEdges = 3;  // The number of edges in the current level of the fractal
    private float hue;          // Hue value of color for next edge
    private boolean cancelled;  // Flag to indicate that calculation has been cancelled
    private EdgeType edgeType;  // Type of edge this fractal is

    /**
     * Staring positions
     */
    private final double start_ax;
    private final double start_ay;
    private final double start_bx;
    private final double start_by;

    public KochFractal(float hue, double ax, double ay, double bx, double by, EdgeType edgeType) {
        this.hue = hue;
        this.start_ax = ax;
        this.start_ay = ay;
        this.start_bx = bx;
        this.start_by = by;
        this.edgeType = edgeType;
    }

    private void drawKochEdge(double ax, double ay, double bx, double by, int n) {
        if (!cancelled) {
            if (n == 1) {
                hue = hue + 1.0f / nrOfEdges;
                Edge edge = new Edge(ax, ay, bx, by, Color.hsb(hue * 360.0, 1.0, 1.0));
                edgeDrawer.addEdge(edge, edgeType);
            } else {
                double angle = Math.PI / 3.0 + Math.atan2(by - ay, bx - ax);
                double distabdiv3 = Math.sqrt((bx - ax) * (bx - ax) + (by - ay) * (by - ay)) / 3;
                double cx = Math.cos(angle) * distabdiv3 + (bx - ax) / 3 + ax;
                double cy = Math.sin(angle) * distabdiv3 + (by - ay) / 3 + ay;
                final double midabx = (bx - ax) / 3 + ax;
                final double midaby = (by - ay) / 3 + ay;
                drawKochEdge(ax, ay, midabx, midaby, n - 1);
                drawKochEdge(midabx, midaby, cx, cy, n - 1);
                drawKochEdge(cx, cy, (midabx + bx) / 2, (midaby + by) / 2, n - 1);
                drawKochEdge((midabx + bx) / 2, (midaby + by) / 2, bx, by, n - 1);
            }
        }
    }

    public void setLevel(int level) {
        this.level = level;
        nrOfEdges = (int) (3 * Math.pow(4, level - 1));
    }

    private EdgeDrawer edgeDrawer;

    public void setEdgeDrawer(EdgeDrawer edgeDrawer) {
        this.edgeDrawer = edgeDrawer;
    }

    @Override
    public void run() {
        drawKochEdge(start_ax, start_ay, start_bx, start_by, level);
    }
}
