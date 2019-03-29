/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import javafx.scene.paint.Color;

/**
 * @author Peter Boots
 * Modified for FUN3 by Gertjan Schouten
 * Modified for FUN3 by Jordy Stabèl
 */
public class KochFractal {

    private int level = 1;      // The current level of the fractal
    private int nrOfEdges = 3;  // The number of edges in the current level of the fractal
    private float hue;          // Hue value of color for next edge
    private boolean cancelled;  // Flag to indicate that calculation has been cancelled
    private KochManager manager;

    public KochFractal(KochManager manager) {
        this.manager = manager;
    }

    public void generateLeftEdge() {
        hue = 0f;
        cancelled = false;
        Runnable left = new Task(0.5, 0.0, (1 - Math.sqrt(3.0) / 2.0) / 2, 0.75, level, level, nrOfEdges, manager);
        new Thread(left).start();
    }

    public void generateBottomEdge() {
        hue = 1f / 3f;
        cancelled = false;
        Runnable bottom = new Task((1 - Math.sqrt(3.0) / 2.0) / 2, 0.75, (1 + Math.sqrt(3.0) / 2.0) / 2, 0.75, level, level, nrOfEdges, manager);
        new Thread(bottom).start();
    }

    public void generateRightEdge() {
        hue = 2f / 3f;
        cancelled = false;
        Runnable right = new Task((1 + Math.sqrt(3.0) / 2.0) / 2, 0.75, 0.5, 0.0, level, level, nrOfEdges, manager);
        new Thread(right).start();
    }
    
    public void cancel() {
        cancelled = true;
    }

    public void setLevel(int lvl) {
        level = lvl;
        nrOfEdges = (int) (3 * Math.pow(4, level - 1));
    }

    public int getLevel() {
        return level;
    }

    public int getNrOfEdges() {
        return nrOfEdges;
    }
}
