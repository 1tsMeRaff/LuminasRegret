package ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import main.GamePanel;

public class PathFinder {

    GamePanel gp;
    Node[][] nodes;
    PriorityQueue<Node> openList = new PriorityQueue<>();
    public ArrayList<Node> pathList = new ArrayList<>();
    Node startNode, goalNode, currentNode;
    
    // Default 0. Ubah ke 1 jika NPC sering nyangkut di pojokan tembok
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

    // Panggil ini setiap kali NPC mau mencari jalan baru
    // Agar status pintu terbuka/tertutup atau tembok hancur selalu update
    public void setNodes(int startCol, int startRow, int goalCol, int goalRow) {
        
        resetNodes();

        // 1. Reset Node State & CEK TILE COLLISION (Tembok, Air, Jurang)
        for (int col = 0; col < gp.maxWorldCol; col++) {
            for (int row = 0; row < gp.maxWorldRow; row++) {
                Node node = nodes[col][row];
                node.open = false;
                node.checked = false;
                node.parent = null;

                int tileNum = gp.tileM.mapTileNum[gp.currentMap][col][row];
                node.solid = gp.tileM.tile[gp.currentMap][tileNum] != null && 
                             gp.tileM.tile[gp.currentMap][tileNum].collision;
            }
        }

        // 2. CEK INTERACTIVE TILES (Pohon potong, Tembok hancur) - CUKUP 1 KALI LOOP
        if (gp.iTile != null && gp.currentMap >= 0 && gp.currentMap < gp.iTile.length && gp.iTile[gp.currentMap] != null) {
            for (int i = 0; i < gp.iTile[gp.currentMap].length; i++) {
                if (gp.iTile[gp.currentMap][i] != null && gp.iTile[gp.currentMap][i].destructible) {
                    int itCol = gp.iTile[gp.currentMap][i].worldX / gp.tileSize;
                    int itRow = gp.iTile[gp.currentMap][i].worldY / gp.tileSize;
                    if (isValidCoordinate(itCol, itRow) && gp.iTile[gp.currentMap][i].collision) {
                        nodes[itCol][itRow].solid = true;
                    }
                }
            }
        }

        // 3. CEK OBJECTS (Pintu, Peti, NPC Lain) - CUKUP 1 KALI LOOP
        if (gp.obj != null && gp.currentMap >= 0 && gp.currentMap < gp.obj.length && gp.obj[gp.currentMap] != null) {
            for (int i = 0; i < gp.obj[gp.currentMap].length; i++) {
                if (gp.obj[gp.currentMap][i] != null && gp.obj[gp.currentMap][i].collision) {
                    int objCol = gp.obj[gp.currentMap][i].worldX / gp.tileSize;
                    int objRow = gp.obj[gp.currentMap][i].worldY / gp.tileSize;
                    if (isValidCoordinate(objCol, objRow)) {
                        nodes[objCol][objRow].solid = true;
                    }
                }
            }
        }

        // 4. Apply Inflate Radius (Padding tembok)
        if (inflateRadius > 0) {
            for (int col = 0; col < gp.maxWorldCol; col++) {
                for (int row = 0; row < gp.maxWorldRow; row++) {
                    if (nodes[col][row].solid) {
                        inflateSolid(col, row);
                    }
                }
            }
        }
    }
    
    private void inflateSolid(int col, int row) {
        // Tandai area sekitar tembok sebagai solid juga (safety buffer)
        for (int i = 1; i <= inflateRadius; i++) {
            if (col + i < gp.maxWorldCol) nodes[col + i][row].solid = true;
            if (row + i < gp.maxWorldRow) nodes[col][row + i].solid = true;
            if (col - i >= 0) nodes[col - i][row].solid = true;
            if (row - i >= 0) nodes[col][row - i].solid = true;
        }
    }

    private void resetNodes() {
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
        
        // Setup Node Solid/Walkable TERBARU sebelum mencari jalan
        setNodes(startCol, startRow, goalCol, goalRow);

        if (!isValidCoordinate(startCol, startRow) || !isValidCoordinate(goalCol, goalRow)) return false;

        startNode = nodes[startCol][startRow];
        goalNode = nodes[goalCol][goalRow];

        // Jika Goal solid (misal player masuk ke dalam pintu), cari tile tetangga terdekat yg kosong
        if (goalNode.solid) {
            Node alt = findNearestNonSolid(goalCol, goalRow, 2); // Radius cari 2 tile
            if (alt != null) {
                goalNode = alt;
            } else {
                return false; // Tidak ada jalan ke sana
            }
        }

        openList.add(startNode);

        int iterations = 0;
        int maxIterations = 500; // Safety break biar game gak freeze kalau path terlalu kompleks

        while (!openList.isEmpty() && iterations < maxIterations) {
            iterations++;

            currentNode = openList.poll();
            currentNode.checked = true;

            if (currentNode == goalNode) {
                buildPath();
                return true;
            }

            // Cek 4 Arah (Atas, Bawah, Kiri, Kanan)
            exploreNeighbor(currentNode.col, currentNode.row - 1); 
            exploreNeighbor(currentNode.col, currentNode.row + 1); 
            exploreNeighbor(currentNode.col - 1, currentNode.row); 
            exploreNeighbor(currentNode.col + 1, currentNode.row); 
        }
        
        return false;
    }
    
    private boolean isValidCoordinate(int col, int row) {
        return col >= 0 && col < gp.maxWorldCol && row >= 0 && row < gp.maxWorldRow;
    }

    private Node findNearestNonSolid(int centerCol, int centerRow, int maxRadius) {
        // Cari spiral tile kosong terdekat
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
        
        // Jangan masukkan neighbor yang SOLID ke dalam kalkulasi
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

    private int manhattanDistance(Node a, Node b) {
        return Math.abs(a.col - b.col) + Math.abs(a.row - b.row);
    }

    private void buildPath() {
        Node current = goalNode;
        pathList.clear();
        while (current != null && current != startNode) {
            pathList.add(current); 
            current = current.parent;
        }
        Collections.reverse(pathList);
    }
}