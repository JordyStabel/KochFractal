/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.ArrayList;

import fun3kochfractalfx.EdgeDrawer;
import fun3kochfractalfx.EdgeType;
import fun3kochfractalfx.FUN3KochFractalFX;
import timeutil.TimeStamp;
import util.Observable;

/**
 * @author Nico Kuijpers
 * Modified for FUN3 by Gertjan Schouten
 * Modified for FUN3 by Jordy Stabèl
 */
public class KochManager extends Observable implements Runnable {

    private FUN3KochFractalFX application;
    private TimeStamp tsCalc;
    private TimeStamp tsDraw;
    //public int EdgeCount;
    private final int nextLevel;
    private final EdgeDrawer edgeDrawer;

    public KochManager(FUN3KochFractalFX application, int nextLevel, EdgeDrawer edgeDrawer) {
        this.application = application;
        this.edgeDrawer = edgeDrawer;
        this.nextLevel = nextLevel;
        this.tsCalc = new TimeStamp();
        this.tsDraw = new TimeStamp();
    }

    private void changeLevel(int nxt) {
        KochFractal left = new KochFractal(0, 0.5, 0.0, (1 - Math.sqrt(3.0) / 2.0) / 2, 0.75, EdgeType.LEFT);
        left.setLevel(nxt);
        left.setEdgeDrawer(edgeDrawer);

        KochFractal bottom = new KochFractal(1f / 3f, (1 - Math.sqrt(3.0) / 2.0) / 2, 0.75, (1 + Math.sqrt(3.0) / 2.0) / 2, 0.75, EdgeType.BOTTOM);
        bottom.setLevel(nxt);
        bottom.setEdgeDrawer(edgeDrawer);

        KochFractal right = new KochFractal(2f / 3f, (1 + Math.sqrt(3.0) / 2.0) / 2, 0.75, 0.5, 0.0, EdgeType.RIGHT);
        right.setLevel(nxt);
        right.setEdgeDrawer(edgeDrawer);

        tsCalc.init();
        tsCalc.setBegin("Begin calculating");

        Thread tLeft = new Thread(left);
        Thread tBottom = new Thread(bottom);
        Thread tRight = new Thread(right);

        tLeft.start();
        tBottom.start();
        tRight.start();

        try {
            tLeft.join();
            tBottom.join();
            tRight.join();
            tsCalc.setEnd("End calculating");
        } catch (InterruptedException ex) {
            System.out.println(String.format("Something went wrong: %s", ex.getMessage()));
        }

        application.setTextNrEdges("" + (int) (3 * Math.pow(4, nxt - 1)));
        application.setTextCalc(tsCalc.toString());

        notifyObservers();
    }

    @Override
    public void run() {
        edgeDrawer.clearEdges();
        edgeDrawer.startDrawThread();

        changeLevel(nextLevel);
        edgeDrawer.stopDrawThread();
    }
}
