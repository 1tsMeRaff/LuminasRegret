package ai;

import main.GamePanel;
import java.util.ArrayList;

public class PathFinder {

    GamePanel gp;
    Node[][] nodes;

    ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();

    Node startNode, goalNode, currentNode;

    // TAMBAHKAN VARIABLE UNTUK MENGATUR INFLATE
    private int inflateRadius = 0; // UBAH INI: 0 = no inflate, 1 = minimal inflate

    public PathFinder(GamePanel gp) {
        this.gp = gp;
        initNodes();
    }

    // --------------------------------------------------
    // INIT NODE GRID
    // --------------------------------------------------
    private void initNodes() {
        nodes = new Node[gp.maxWorldCol][gp.maxWorldRow];
        for (int col = 0; col < gp.maxWorldCol; col++) {
            for (int row = 0; row < gp.maxWorldRow; row++) {
                nodes[col][row] = new Node(col, row);
            }
        }
    }

    // --------------------------------------------------
    // RESET STATE
    // --------------------------------------------------
    private void reset() {
        openList.clear();
        pathList.clear();

        for (int col = 0; col < gp.maxWorldCol; col++) {
            for (int row = 0; row < gp.maxWorldRow; row++) {
                Node n = nodes[col][row];
                n.open = false;
                n.checked = false;
                n.parent = null;
                n.solid = false;
            }
        }
    }

    // --------------------------------------------------
    // SET SOLID NODE - VERSI MINIMAL INFLATE
    // --------------------------------------------------
    private void setSolidNodes() {
        System.out.println("=== SETTING SOLID NODES ===");
        System.out.println("Inflate radius: " + inflateRadius);
        
        int solidCount = 0;

        for (int col = 0; col < gp.maxWorldCol; col++) {
            for (int row = 0; row < gp.maxWorldRow; row++) {

                int tileNum = gp.tileM.mapTileNum[gp.currentMap][col][row];

                if (gp.tileM.tile[gp.currentMap][tileNum].collision) {
                    // HANYA tile utama yang solid
                    nodes[col][row].solid = true;
                    solidCount++;

                    // INFLATE OPTIONAL - sesuaikan dengan kebutuhan
                    if (inflateRadius > 0) {
                        // Inflate hanya ke kanan dan bawah (untuk menghindari NPC terjebak)
                        if (col + 1 < gp.maxWorldCol) {
                            nodes[col + 1][row].solid = true;
                        }
                        if (row + 1 < gp.maxWorldRow) {
                            nodes[col][row + 1].solid = true;
                        }
                    }
                }
            }
        }

        System.out.println("Total solid tiles: " + solidCount);
    }

    // --------------------------------------------------
    // MAIN SEARCH - DIPERBAIKI
    // --------------------------------------------------
    public boolean search(int startCol, int startRow, int goalCol, int goalRow) {
        System.out.println("\n🔍 PATHFINDER: (" + startCol + "," + startRow + ") -> (" + goalCol + "," + goalRow + ")");

        reset();
        setSolidNodes();

        // Validasi koordinat
        if (!isValidCoordinate(startCol, startRow) || !isValidCoordinate(goalCol, goalRow)) {
            System.out.println("❌ Invalid coordinates");
            return false;
        }

        startNode = nodes[startCol][startRow];
        goalNode = nodes[goalCol][goalRow];

        // Cek jika goal solid
        if (goalNode.solid) {
            System.out.println("⚠️ Goal tile is solid! Finding alternative...");
            
            // Cari tile non-solid terdekat
            Node alternative = findNearestNonSolid(goalCol, goalRow, 3);
            if (alternative != null) {
                System.out.println("✅ Alternative found: (" + alternative.col + "," + alternative.row + ")");
                goalCol = alternative.col;
                goalRow = alternative.row;
                goalNode = nodes[goalCol][goalRow];
            } else {
                System.out.println("❌ No alternative found");
                return false;
            }
        }

        // Jika start dan goal sama
        if (startCol == goalCol && startRow == goalRow) {
            System.out.println("✅ Already at goal");
            return true;
        }

        // Setup A* algorithm
        startNode.gCost = 0;
        startNode.hCost = manhattanDistance(startNode, goalNode);
        startNode.calculateFCost();

        openList.add(startNode);
        startNode.open = true;

        int maxIterations = gp.maxWorldCol * gp.maxWorldRow;
        int iterations = 0;

        while (!openList.isEmpty() && iterations < maxIterations) {
            iterations++;

            currentNode = getBestNode();
            if (currentNode == null) break;

            currentNode.checked = true;
            openList.remove(currentNode);

            // Cek jika mencapai goal
            if (currentNode == goalNode) {
                buildPath();
                System.out.println("✅ PATH FOUND! Length: " + pathList.size());
                return true;
            }

            // Explore neighbors
            exploreNeighbor(currentNode.col, currentNode.row - 1); // up
            exploreNeighbor(currentNode.col - 1, currentNode.row); // left
            exploreNeighbor(currentNode.col, currentNode.row + 1); // down
            exploreNeighbor(currentNode.col + 1, currentNode.row); // right
        }

        System.out.println("❌ NO PATH FOUND after " + iterations + " iterations");
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