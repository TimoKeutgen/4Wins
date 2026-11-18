import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MenuBar extends JMenuBar {

    public MenuBar() {
        JMenu menu = new JMenu("Einstellungen");
        JMenuItem item = new JMenuItem(new AbstractAction("Challenges") {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    JFrame f = new JFrame("Challenges");
                    f.setIconImage(FourWins.ICON_IMAGE);
                    f.add(new ChallengeWindow());
                    f.pack();
                    f.setResizable(true);
                    f.setLocationRelativeTo(null);
                    f.setVisible(true);
                });
            }
        });
        menu.add(item);
        add(menu);
    }

    static class ChallengeWindow extends JPanel {

        public ChallengeWindow() {
            setBackground(Color.GRAY);
            setPreferredSize(new Dimension(400, 300));
            setLayout(new GridBagLayout());

            JLabel label = new JLabel();
            label.setFont(new Font("Arial", Font.PLAIN, 16));
            label.setForeground(Color.WHITE);

            ToggleSwitch toggle = new ToggleSwitch(FourWins.tetrisMode);
            updateLabel(label, toggle.isOn());

            toggle.onToggle(on -> {
                FourWins.tetrisMode = on;
                updateLabel(label, on);
            });

            JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
            row.setOpaque(false);
            row.add(label);
            row.add(toggle);
            add(row);
        }

        private static void updateLabel(JLabel label, boolean on) {
            label.setText("Tetris Mode: ");
        }

        @Override
        protected void paintComponent(Graphics g0) {
            super.paintComponent(g0);
            Graphics2D g = (Graphics2D) g0;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            String text = "Challenges";
            g.setFont(new Font("Arial", Font.BOLD, 30));
            int w = g.getFontMetrics().stringWidth(text);
            g.setColor(Color.BLUE);
            g.drawString(text, (getWidth() - w)/2, 30);

        }
    }

}
