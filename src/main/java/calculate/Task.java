package calculate;

import javafx.scene.paint.Color;
import util.Observable;

public class Task extends Observable implements Runnable {

    private int level;      // The current level of the fractal
    private int nrOfEdges;  // The number of edges in the current level of the fractal
    private float hue;          // Hue value of color for next edge
    private boolean cancelled;  // Flag to indicate that calculation has been cancelled
    private double ax;
    private double ay;
    private double bx;
    private double by;
    private int n;
    private KochManager kochManager;

    public Task(
            double ax,
            double ay,
            double bx,
            double by,
            int n,
            int nrOfEdges,
            int level,
            KochManager kochManager) {
        this.ax = ax;
        this.ay = ay;
        this.bx = bx;
        this.by = by;
        this.n = n;
        this.nrOfEdges = nrOfEdges;
        this.level = level;
        this.kochManager = kochManager;
    }

    @Override
    public void run() {
        //nrOfEdges = kochManager.edges.size();
        drawKochEdge(ax, ay, bx, by, n);
    }

    private void drawKochEdge(double ax, double ay, double bx, double by, int n) {
        if (!cancelled) {
            if (n == 1) {
                hue = hue + 1.0f / nrOfEdges;
                Edge e = new Edge(ax, ay, bx, by, Color.hsb(hue * 360.0, 1.0, 1.0));
                kochManager.addEdge(e);
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


}
