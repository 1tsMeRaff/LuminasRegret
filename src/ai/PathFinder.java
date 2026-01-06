package ai;

import main.GamePanel;
import java.util.ArrayList;

public class PathFinder {

    GamePanel gp;
    Node[][] nodes;
    ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();
    Node startNode, goalNode, currentNode;
    
    private int inflateRadius = 0;

    public PathFinder(GamePanel gp) {
        this.gp = gp;
        initNodes();
    }

    private void initNodes() {
        nodes = new Node[gp.maxWorldCol][gp.maxWorldRow];
        for (int col = 0; col < gp.maxWorldCol; col++) {
            for (int row = 0; row < gp.maxWorldRow; row++) {
                nodes[col][row] = new Node(col, row);
            }
        }
    }

    // PENTING: Jalankan ini hanya saat ganti Map atau awal game, bukan setiap frame!
    public void setNodes() {
        for (int col = 0; col < gp.maxWorldCol; col++) {
            for (int row = 0; row < gp.maxWorldRow; row++) {
                
                // Reset state node
                nodes[col][row].open = false;
                nodes[col][row].checked = false;
                nodes[col][row].solid = false;
                nodes[col][row].parent = null;

                // Tentukan solid berdasarkan tile map
                int tileNum = gp.tileM.mapTileNum[gp.currentMap][col][row];
                if (gp.tileM.tile[gp.currentMap][tileNum].collision == true) {
                    nodes[col][row].solid = true;
                    
                    // Inflate Logic (Membuat area di sekitar tembok jadi solid agar monster tidak nyangkut)
                    if (inflateRadius > 0) {
                        inflateSolid(col, row);
                    }
                }
            }
        }
    }
    
    private void inflateSolid(int col, int row) {
        for(int i = 1; i <= inflateRadius; i++) {
            if(col+i < gp.maxWorldCol) nodes[col+i][row].solid = true;
            if(row+i < gp.maxWorldRow) nodes[col][row+i].solid = true;
            if(col-i >= 0) nodes[col-i][row].solid = true;
            if(row-i >= 0) nodes[col][row-i].solid = true;
        }
    }

    private void resetNodes() {
        // Hanya reset status pencarian, bukan status 'solid'
        for (int col = 0; col < gp.maxWorldCol; col++) {
            for (int row = 0; row < gp.maxWorldRow; row++) {
                nodes[col][row].open = false;
                nodes[col][row].checked = false;
                nodes[col][row].parent = null;
            }
        }
        openList.clear();
        pathList.clear();
    }

    public boolean search(int startCol, int startRow, int goalCol, int goalRow) {
        resetNodes();

        if (!isValidCoordinate(startCol, startRow) || !isValidCoordinate(goalCol, goalRow)) return false;

        startNode = nodes[startCol][startRow];
        goalNode = nodes[goalCol][goalRow];

        // Jika Goal solid (misal player berdiri tepat di depan tembok), cari tile terdekat yang kosong
        if (goalNode.solid) {
            Node alt = findNearestNonSolid(goalCol, goalRow, 2);
            if (alt != null) goalNode = alt;
            else return false;
        }

        openList.add(startNode);

        int iterations = 0;
        int maxIterations = 1000; // Batasi agar tidak infinite loop

        while (!openList.isEmpty() && iterations < maxIterations) {
            iterations++;

            currentNode = getBestNode();
            currentNode.checked = true;
            openList.remove(currentNode);

            if (currentNode == goalNode) {
                buildPath();
                return true;
            }

            // Neighbors
            exploreNeighbor(currentNode.col, currentNode.row - 1); // up
            exploreNeighbor(currentNode.col, currentNode.row + 1); // down
            exploreNeighbor(currentNode.col - 1, currentNode.row); // left
            exploreNeighbor(currentNode.col + 1, currentNode.row); // right
        }
        return false;
    }
    
    // --------------------------------------------------
    // HELPER METHODS
    // --------------------------------------------------
    private boolean isValidCoordinate(int col, int row) {
        return col >= 0 && col < gp.maxWorldCol && row >= 0 && row < gp.maxWorldRow;
    }

    private Node findNearestNonSolid(int centerCol, int centerRow, int maxRadius) {
        for (int radius = 1; radius <= maxRadius; radius++) {
            for (int c = centerCol - radius; c <= centerCol + radius; c++) {
                for (int r = centerRow - radius; r <= centerRow + radius; r++) {
                    if (isValidCoordinate(c, r) && !nodes[c][r].solid) {
                        return nodes[c][r];
                    }
                }
            }
        }
        return null;
    }

    private void exploreNeighbor(int col, int row) {
        if (!isValidCoordinate(col, row)) return;

        Node neighbor = nodes[col][row];
        if (neighbor.checked || neighbor.solid) return;

        int tentativeG = currentNode.gCost + 1;

        if (!neighbor.open || tentativeG < neighbor.gCost) {
            neighbor.parent = currentNode;
            neighbor.gCost = tentativeG;
            neighbor.hCost = manhattanDistance(neighbor, goalNode);
            neighbor.calculateFCost();

            if (!neighbor.open) {
                neighbor.open = true;
                openList.add(neighbor);
            }
        }
    }

    private Node getBestNode() {
        if (openList.isEmpty()) return null;

        Node best = openList.get(0);
        for (Node n : openList) {
            if (n.fCost < best.fCost || 
               (n.fCost == best.fCost && n.hCost < best.hCost)) {
                best = n;
            }
        }
        return best;
    }

    private int manhattanDistance(Node a, Node b) {
        return Math.abs(a.col - b.col) + Math.abs(a.row - b.row);
    }

    private void buildPath() {
        Node current = goalNode;
        pathList.clear();

        while (current != null && current != startNode) {
            pathList.add(0, current);
            current = current.parent;
        }
    }

    // --------------------------------------------------
    // PUBLIC METHODS
    // --------------------------------------------------
    public void setInflateRadius(int radius) {
        this.inflateRadius = Math.max(0, Math.min(radius, 2)); // Max 2 untuk safety
        System.out.println("Inflate radius set to: " + inflateRadius);
    }

    public void debugTile(int col, int row) {
        if (!isValidCoordinate(col, row)) {
            System.out.println("Invalid tile: (" + col + "," + row + ")");
            return;
        }
        
        Node n = nodes[col][row];
        int tileNum = gp.tileM.mapTileNum[gp.currentMap][col][row];
        boolean collision = gp.tileM.tile[gp.currentMap][tileNum].collision;
        
        System.out.println("Tile (" + col + "," + row + "):");
        System.out.println("  Tile number: " + tileNum);
        System.out.println("  Has collision: " + collision);
        System.out.println("  Node solid: " + n.solid);
        System.out.println("  Is walkable: " + !n.solid);
    }

    public void printArea(int centerCol, int centerRow, int size) {
        System.out.println("\nMap area around (" + centerCol + "," + centerRow + "):");
        
        for (int r = centerRow - size; r <= centerRow + size; r++) {
            StringBuilder line = new StringBuilder();
            for (int c = centerCol - size; c <= centerCol + size; c++) {
                if (isValidCoordinate(c, r)) {
                    Node n = nodes[c][r];
                    if (c == centerCol && r == centerRow) {
                        line.append(" S "); // Center
                    } else if (n.solid) {
                        line.append(" X "); // Solid
                    } else {
                        line.append(" . "); // Walkable
                    }
                } else {
                    line.append(" # "); // Out of bounds
                }
            }
            System.out.println(line.toString());
        }
    }
    
    
}