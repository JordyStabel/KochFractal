package fun3kochfractalfx;

import calculate.Edge;

import java.util.ArrayList;

public class EdgeDrawer {

    private FUN3KochFractalFX application;
    private final ArrayList<Edge> savedEdges;
    private final ArrayList<Edge> allEdges;

    private Thread drawThread;
    private volatile boolean isAlive;

    EdgeDrawer(FUN3KochFractalFX application) {
        this.application = application;
        savedEdges = new ArrayList<>();
        allEdges = new ArrayList<>();
    }

    public void startDrawThread() {
        isAlive = true;
        drawThread = new Thread(() -> {
            try {
                while (isAlive) {
                    synchronized (savedEdges) {
                        if (savedEdges.isEmpty()) {
                            savedEdges.wait();
                        }
                        application.drawEdge(savedEdges);

                        savedEdges.clear();
                    }
                }
            } catch (InterruptedException exception) {
                isAlive = false;
            }
        });
        drawThread.start();
    }

    public void stopDrawThread() {
        drawThread.interrupt();
        try {
            drawThread.join();
        } catch (InterruptedException exception) {
            System.out.println("Error in stopDrawThread (EdgeDrawer)" + exception.getMessage());
        }
    }

    public void drawEdges() {
        application.clearKochPanel();
        application.drawEdge(allEdges);
    }

    public void addEdge(Edge edge, EdgeType edgeType) {
        synchronized (savedEdges) {
            savedEdges.add(edge);
            allEdges.add(edge);
            // Wakes up waiting thread
            savedEdges.notify();
        }
        application.updateProgressBar(edgeType);
    }

    public void clearEdges() {
        savedEdges.clear();
        allEdges.clear();
        application.clearKochPanel();
    }
}
