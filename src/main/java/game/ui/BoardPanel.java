package game.ui;

import java.awt.*;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import javax.swing.*;

public class BoardPanel extends JPanel {

    private Image boardImage;
    private List<Integer> playerPositions;

    public BoardPanel() {
        this.playerPositions = Collections.emptyList();
        setOpaque(false); // Important for the background to show through if needed
        
        // --- LOAD THE IMAGE ---
        try {
            // Try loading from root (standard for src/main/resources)
            URL imgUrl = ClassLoader.getSystemResource("BoardPanel1.png");
            
            if (imgUrl == null) {
                // Fallback: Try loading relative to class
                imgUrl = getClass().getResource("/BoardPanel1.png");
            }
            
            if (imgUrl != null) {
                this.boardImage = new ImageIcon(imgUrl).getImage();
            } else {
                System.err.println("Error: BoardPanel1.png not found! Make sure it is in your source folder.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePositions(List<Integer> positions) {
        this.playerPositions = positions;
        repaint(); // Trigger a redraw
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = getWidth();
        int h = getHeight();

        // 1. Draw Board Image
        if (boardImage != null) {
            g.drawImage(boardImage, 0, 0, w, h, this);
        } else {
            // Fallback if image fails
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, w, h);
            g.setColor(Color.RED);
            g.drawString("Image Not Found: BoardPanel1.png", 10, 20);
        }

        // 2. Draw Players
        if (playerPositions.isEmpty()) return;
        
        int cellW = w / 10;
        int cellH = h / 10;
        int tokenSize = Math.min(cellW, cellH) / 2;

        // Player Colors
        Color[] pColors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};

        for (int i = 0; i < playerPositions.size(); i++) {
            int tileId = playerPositions.get(i);
            
            // Convert 1-100 tile ID to X,Y coordinates (Zig-Zag pattern)
            // Row 0 is bottom (indices 0-9), Row 9 is top
            int rowFromBottom = (tileId - 1) / 10;
            int col = (tileId - 1) % 10;
            
            // Even rows go Right (1-10), Odd rows go Left (20-11)
            if (rowFromBottom % 2 == 1) {
                col = 9 - col;
            }
            
            int drawRow = 9 - rowFromBottom; // Swing Y starts at top
            
            int xCenter = (col * cellW) + (cellW - tokenSize) / 2;
            int yCenter = (drawRow * cellH) + (cellH - tokenSize) / 2;

            // Small offsets so players don't overlap perfectly
            if (i == 1) { xCenter += 10; yCenter += 10; }
            if (i == 2) { xCenter -= 10; yCenter += 10; }
            if (i == 3) { xCenter += 10; yCenter -= 10; }

            g.setColor(pColors[i % pColors.length]);
            g.fillOval(xCenter, yCenter, tokenSize, tokenSize);
            
            g.setColor(Color.BLACK);
            g.drawOval(xCenter, yCenter, tokenSize, tokenSize);
        }
    }
}