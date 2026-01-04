package ai;

public class Node {
    public int col, row;

    public int gCost; // cost dari start
    public int hCost; // heuristic ke goal
    public int fCost;

    public boolean solid;
    public boolean open;
    public boolean checked;

    public Node parent;

    public Node(int col, int row) {
        this.col = col;
        this.row = row;
    }

    public void calculateFCost() {
        fCost = gCost + hCost;
    }
}
