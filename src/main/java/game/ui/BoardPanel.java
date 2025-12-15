package game.ui;

import game.engine.*;
import game.tiles.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class BoardPanel extends JPanel implements GameListener {
	
	private static final Color[] PLAYER_COLORS = {
		    Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA
		};


    private static final int TILE_SIZE = 60;
    private static final int BOARD_SIZE = 10;

    private final Game game;

    public BoardPanel(Game game) {
        this.game = game;
        setPreferredSize(new Dimension(
                TILE_SIZE * BOARD_SIZE,
                TILE_SIZE * BOARD_SIZE
        ));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawTiles(g);
        drawSnakesAndVines(g);
        drawPlayers(g);
    }

    /* ---------------- TILE GRID ---------------- */

    private void drawTiles(Graphics g) {
        for (int i = 1; i <= 100; i++) {
            Point p = toPoint(i);

            Tile tile = game.getTileAt(i);


            if (tile instanceof BoonTile) {
                g.setColor(new Color(180, 255, 180));
            } else if (tile instanceof CurseTile) {
                g.setColor(new Color(255, 180, 180));
            } else {
                g.setColor(Color.LIGHT_GRAY);
            }

            g.fillRect(p.x, p.y, TILE_SIZE, TILE_SIZE);
            g.setColor(Color.BLACK);
            g.drawRect(p.x, p.y, TILE_SIZE, TILE_SIZE);

            g.drawString(String.valueOf(i), p.x + 5, p.y + 15);
        }
    }
    
    private Color getColor(int playerIndex) {
        return switch (playerIndex) {
            case 0 -> Color.RED;
            case 1 -> Color.BLUE;
            case 2 -> Color.GREEN;
            case 3 -> Color.MAGENTA;
            default -> Color.BLACK;
        };
    }


    /* ---------------- SNAKES & VINES ---------------- */

    private void drawSnakesAndVines(Graphics g) {
        Map<Integer, Integer> snakes = game.getBoard().getSnakes();
        Map<Integer, Integer> vines = game.getBoard().getVines();

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(4));

        g2.setColor(Color.RED);
        snakes.forEach((start, end) ->
                drawConnection(g2, start, end));

        g2.setColor(new Color(34, 139, 34));
        vines.forEach((start, end) ->
                drawConnection(g2, start, end));
    }

    private void drawConnection(Graphics2D g, int start, int end) {
        Point a = centerOf(start);
        Point b = centerOf(end);
        g.drawLine(a.x, a.y, b.x, b.y);
    }

    /* ---------------- PLAYERS ---------------- */

    private void drawPlayers(Graphics g) {
        var players = game.getPlayers();

        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);

            int pos = p.getPosition() - 1;
            int row = pos / 10;
            int col = pos % 10;

            int x = col * TILE_SIZE + 20;
            int y = (9 - row) * TILE_SIZE + 30;

            g.setColor(getColor(i));
            g.fillOval(x, y, 16, 16);
        }
    }


    /* ---------------- HELPERS ---------------- */

    private Point toPoint(int position) {
        int index = position - 1;
        int row = 9 - index / 10;
        int col = index % 10;

        return new Point(col * TILE_SIZE, row * TILE_SIZE);
    }

    private Point centerOf(int position) {
        Point p = toPoint(position);
        return new Point(
                p.x + TILE_SIZE / 2,
                p.y + TILE_SIZE / 2
        );
    }

    /* ---------------- LISTENER ---------------- */


    @Override
    public void onPlayerMoved(Player p, int from, int to) {
        SwingUtilities.invokeLater(this::repaint);
    }


    @Override
    public void onGameEnded(game.engine.Player winner) {
        JOptionPane.showMessageDialog(this,
                winner.getName() + " wins!");
    }

	@Override
	public void onTurnStarted(Player p) {
		// TODO Auto-generated method stub
		
	}
}
