package ai;

import java.util.ArrayList;
import main.GamePanel;

public class PathFinder {

    GamePanel gp;
    Node[][] nodes;
    ArrayList<Node> openList = new ArrayList<>();
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

        for (int col = 0; col < gp.maxWorldCol; col++) {
            for (int row = 0; row < gp.maxWorldRow; row++) {
                
                // 1. Reset Node State
                Node node = nodes[col][row];
                node.open = false;
                node.checked = false;
                node.solid = false;
                node.parent = null;

                // 2. CEK TILE COLLISION (Tembok, Air, Jurang)
                int tileNum = gp.tileM.mapTileNum[gp.currentMap][col][row];
                // Sesuaikan baris ini dengan struktur TileManager kamu (apakah array 1D atau 2D)
                if (gp.tileM.tile[gp.currentMap][tileNum].collision == true) { 
                    node.solid = true;
                }

                // 3. CEK INTERACTIVE TILES (Pohon potong, Tembok hancur)
                // Loop semua interactive tile di map ini
                for (int i = 0; i < gp.iTile[1].length; i++) {
                     if (gp.iTile[gp.currentMap][i] != null && gp.iTile[gp.currentMap][i].destructible) {
                         // Cek apakah iTile ini ada di koordinat [col][row] dan punya collision
                         int itCol = gp.iTile[gp.currentMap][i].worldX / gp.tileSize;
                         int itRow = gp.iTile[gp.currentMap][i].worldY / gp.tileSize;
                         
                         if (col == itCol && row == itRow && gp.iTile[gp.currentMap][i].collision) {
                             node.solid = true;
                         }
                     }
                }

                // 4. CEK OBJECTS (Pintu, Peti, NPC Lain)
                // Ini penting agar monster tidak mencoba menembus pintu yang terkunci
                for (int i = 0; i < gp.obj[1].length; i++) {
                     if (gp.obj[gp.currentMap][i] != null && gp.obj[gp.currentMap][i].collision) {
                         int objCol = gp.obj[gp.currentMap][i].worldX / gp.tileSize;
                         int objRow = gp.obj[gp.currentMap][i].worldY / gp.tileSize;

                         if (col == objCol && row == objRow) {
                             node.solid = true;
                         }
                     }
                }

                // 5. Apply Inflate Radius (Padding tembok)
                // Hanya lakukan ini jika node sudah confirm solid dari langkah 2, 3, atau 4
                if (node.solid && inflateRadius > 0) {
                    inflateSolid(col, row);
                }
            }
        }
    }
    
    private void inflateSolid(int col, int row) {
        // Tandai area sekitar tembok sebagai solid juga (safety buffer)
        for(int i = 1; i <= inflateRadius; i++) {
            if(col+i < gp.maxWorldCol) nodes[col+i][row].solid = true;
            if(row+i < gp.maxWorldRow) nodes[col][row+i].solid = true;
            if(col-i >= 0) nodes[col-i][row].solid = true;
            if(row-i >= 0) nodes[col][row-i].solid = true;
        }
    }

    private void resetNodes() {
        // Reset hanya variabel tracking pathfinding
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

            currentNode = getBestNode();
            currentNode.checked = true;
            openList.remove(currentNode);

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
        
        // INI KUNCINYA: Jangan masukkan neighbor yang SOLID ke dalam kalkulasi
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
            if (n.fCost < best.fCost || (n.fCost == best.fCost && n.hCost < best.hCost)) {
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
}