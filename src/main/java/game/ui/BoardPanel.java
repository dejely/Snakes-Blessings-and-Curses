package game.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

import javax.swing.JPanel;

import game.engine.Board;
import game.engine.Player;

public class BoardPanel extends JPanel {

    private Board board;
    private List<Player> players;
    private int currentIndex = -1;

    public void setModel(Board board, List<Player> players, int currentIndex) {
        this.board = board;
        this.players = players;
        this.currentIndex = currentIndex;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (board == null) return;

        int total = board.getSize();
        int cols = (int)Math.ceil(Math.sqrt(total));
        int rows = (int)Math.ceil((double)total / cols);

        int pad = 20;
        int w = getWidth() - pad * 2;
        int h = getHeight() - pad * 2;

        int cw = w / cols;
        int ch = h / rows;

        Graphics2D g2 = (Graphics2D)g;

        // draw grid
        for (int i = 0; i < total; i++) {
            int r = i / cols;
            int c = i % cols;

            int x = pad + c * cw;
            int y = pad + r * ch;

            g2.setColor(Color.LIGHT_GRAY);
            g2.fillRect(x, y, cw - 2, ch - 2);

            g2.setColor(Color.DARK_GRAY);
            g2.drawRect(x, y, cw - 2, ch - 2);

            g2.setColor(Color.BLACK);
            g2.drawString(String.valueOf(i), x + 5, y + 15);
        }

        // no players yet?
        if (players == null || players.isEmpty()) return;

        int size = Math.min(cw, ch) / 3;

        // draw tokens
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            int pos = p.getPosition();

            int r = pos / cols;
            int c = pos % cols;

            int px = pad + c * cw + cw/2 - size/2;
            int py = pad + r * ch + ch/2 - size/2;

            Color col = switch (i % 6) {
                case 0 -> Color.RED;
                case 1 -> Color.BLUE;
                case 2 -> Color.GREEN;
                case 3 -> Color.MAGENTA;
                case 4 -> Color.ORANGE;
                default -> Color.CYAN;
            };

            g2.setColor(col);
            g2.fillOval(px, py, size, size);

            g2.setColor(Color.WHITE);
            g2.drawString(String.valueOf(i+1), px + size/4, py + size/2 + 4);

            // highlight current player
            if (i == currentIndex) {
                g2.setColor(Color.YELLOW);
                g2.setStroke(new BasicStroke(4));
                g2.drawOval(px - 3, py - 3, size + 6, size + 6);
                g2.setStroke(new BasicStroke(1));
            }
        }
    }
}
