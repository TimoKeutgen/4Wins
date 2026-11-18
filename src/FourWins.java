import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.Arrays;

/**
 * Class to play 4Wins
 */
public class FourWins extends JPanel {

    public static final Image ICON_IMAGE = Toolkit.getDefaultToolkit().getImage("4wins.ico");
    private static final int ROWS = 6;
    private static final int COLS = 7;
    private static final int CELL = 90;
    private static final int PAD = 10;

    /* Settings */
    public static boolean tetrisMode = false;

    private final int[][] board = new int[ROWS][COLS];
    private int player = 1;
    private int hoverCol = -1;
    private int winner = 0;

    public FourWins() {
        setPreferredSize(new Dimension(COLS*CELL, (ROWS + 1) * CELL));
        setBackground(Color.GRAY);

        MouseAdapter mouse = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                hoverCol = e.getX() / CELL;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hoverCol = -1;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                drop(e.getX() / CELL);
            }
        };
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
    }

    /**
     * Drop in a column
     * @param col clicked column
     */
    public void drop(int col) {
        if(this.winner != 0) {
            this.reset();
            return;
        }
        if(col < 0 || col >= COLS) return;
        for(int r = ROWS -1; r >= 0; r--) {
            if(board[r][col] == 0) {
                board[r][col] = this.player;
                if(this.wins(r, col)) this.winner = this.player;
                else if(tetrisMode && this.isFirstLineFilled()) this.removeFirstLine();
                else if(this.filled()) this.winner = this.player;
                this.player = 3 - this.player;
                this.repaint();
                return;
            }
        }
    }

    private boolean filled() {
        for(int[] arr : this.board) {
            for(int x : arr) {
                if(x == 0) return false;
            }
        }
        return true;
    }

    private boolean isFirstLineFilled() {
        for(int x : this.board[ROWS-1]) {
            if(x == 0) return false;
        }
        return true;
    }

    private void removeFirstLine() {
        for(int r = ROWS - 1; r > 0; r--) {
            this.board[r] = this.board[r - 1];
        }
        this.board[0] = new int[COLS];
    }

    /**
     * Check if one player has won the game
     * @param c dropped column
     * @param r dropped row
     */
    public boolean wins(int r, int c) {
        int[][] dirs = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};
        for(int[] d : dirs) {
            if(1 + count(r, c, d[0], d[1]) + count(r, c, -d[0], -d[1]) >= 4) return true;
        }
        return false;
    }

    private int count(int r, int c, int dr, int dc) {
        int n = 0;
        int p = this.board[r][c];
        for(r += dr, c += dc; r >= 0 && r < ROWS && c >= 0 && c < COLS && this.board[r][c] == p; r += dr, c += dc) {
            n++;
        }
        return n;
    }

    /**
     * Reset the game field (fill with zeros)
     */
    public void reset() {
        for(int[] row : board) Arrays.fill(row, 0);
        this.winner = 0;
        this.player = 1;
        this.repaint();
    }


    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if(winner == 0 && hoverCol >= 0 && hoverCol < COLS) {
            g.setColor(color(player));
            g.fillOval(hoverCol * CELL + PAD, PAD, CELL - 2 * PAD, CELL - 2 * PAD);
        }

        g.setColor(Color.BLUE);
        g.fill(new RoundRectangle2D.Double(0, CELL, COLS * CELL, ROWS * CELL, 30, 30));
        for(int r = 0; r < ROWS; r++) {
            for(int c = 0; c < COLS; c++) {
                g.setColor(this.board[r][c] == 0 ? getBackground() : color(this.board[r][c]));
                g.fillOval(c * CELL + PAD, (r + 1) * CELL + PAD, CELL - 2 * PAD, CELL - 2 * PAD);
            }
        }

        if(winner != 0) {
            if(this.winner == -1) {
                String text = "Unentschieden";
                g.setFont(new Font("Arial", Font.ITALIC, 26));
                int w = g.getFontMetrics().stringWidth(text);
                g.setColor(Color.WHITE);
                g.drawString(text, (getWidth() - w) / 2, CELL / 2 + 10);
                return;
            }
            String text = (this.winner == 1 ? "Rot" : "Gelb") + " gewinnt!";
            g.setFont(new Font("Arial", Font.BOLD, 26));
            int w = g.getFontMetrics().stringWidth(text);
            g.setColor(color(this.winner));
            g.drawString(text, (getWidth() - w) / 2, CELL / 2 + 10);
        }
    }

    private Color color(int p) {
        return p == 1 ? Color.RED : Color.YELLOW;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("4 Wins");
            f.setIconImage(ICON_IMAGE);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.add(new FourWins());
            f.setJMenuBar(new MenuBar());
            f.pack();
            f.setResizable(false);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }

}
