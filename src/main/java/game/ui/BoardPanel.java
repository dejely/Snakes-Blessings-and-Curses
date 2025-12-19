package game.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class BoardPanel extends JPanel {

    private Image boardImage;
    private List<Integer> playerPositions;

    // 0=Normal, 1=blessing (Green), 2=curse (Red), 3=Finish (Gold), 4 = snake, 5 = ladder
    private final int[][] tileMap = {
        {3, 0, 1, 5, 0, 0, 1, 2, 0, 0}, 
        {0, 2, 0, 0, 1, 1, 0, 2, 0, 0},
        {1, 0, 2, 0, 0, 0, 1, 4, 0, 1},
        {0, 1, 0, 5, 2, 0, 0, 0, 0, 1},
        {2, 0, 0, 1, 0, 0, 0, 1, 0, 0},
        {1, 0, 2, 4, 0, 0, 5, 0, 0, 1},
        {1, 5, 0, 0, 2, 1, 0, 0, 0, 0},
        {0, 2, 0, 0, 0, 0, 0, 5, 2, 1},
        {0, 1, 0, 0, 1, 0, 2, 0, 0, 0},
        {0, 0, 0, 1, 0, 5, 0, 0, 0, 2}  
    };

    public BoardPanel() {
        this.setSize(new Dimension(800, 800));
        loadBoardImage();
        playerPositions = new ArrayList<>();
    }

    private void loadBoardImage() {
        URL imgUrl = getClass().getResource("/BoardPanel1.png");
        if (imgUrl != null) {
            boardImage = new ImageIcon(imgUrl).getImage();
        }
    }
    
    public void updatePositions(List<Integer> newPositions) {
        this.playerPositions = newPositions;
        this.repaint(); 
    }

    public int getTileType(int tileId) {
        if (tileId < 1 || tileId > 100) return 0;
        int rowFromBottom = (tileId - 1) / 10;
        int arrayRow = 9 - rowFromBottom; 
        int col = (tileId - 1) % 10;
        if (rowFromBottom % 2 == 1) col = 9 - col;
        return tileMap[arrayRow][col];
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = getWidth();
        int h = getHeight();

        // 1. Draw Image
        if (boardImage != null) g.drawImage(boardImage, 0, 0, w, h, this);
        drawPlayers(g, w, h);
    }

    private void drawPlayers(Graphics g, int w, int h) {
        if (playerPositions.isEmpty()) return;
        int cellW = w / 10;
        int cellH = h / 10;
        int tokenSize = Math.min(cellW, cellH) / 2; 

        for (int i = 0; i < playerPositions.size(); i++) {
            int tileId = playerPositions.get(i);
            int rowFromBottom = (tileId - 1) / 10; 
            int col = (tileId - 1) % 10;           
            if (rowFromBottom % 2 == 1) col = 9 - col;
            int drawRow = 9 - rowFromBottom; 
            
            int xCenter = (col * cellW) + (cellW - tokenSize) / 2;
            int yCenter = (drawRow * cellH) + (cellH - tokenSize) / 2;
            
            // Visual Offsets
            if (i == 1) { xCenter += 10; yCenter += 10; }
            if (i == 2) { xCenter -= 10; yCenter += 10; }
            if (i == 3) { xCenter += 10; yCenter -= 10; }

            // Colors
            if (i == 0) g.setColor(Color.RED);
            else if (i == 1) g.setColor(Color.BLUE);
            else if (i == 2) g.setColor(Color.GREEN);
            else g.setColor(Color.YELLOW);

            g.fillOval(xCenter, yCenter, tokenSize, tokenSize);
            g.setColor(Color.BLACK);
            g.drawOval(xCenter, yCenter, tokenSize, tokenSize);
        }
    }
}