import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class ToggleSwitch extends JComponent {

    private static final Color OFF_COLOR = new Color(180, 180, 180);
    private static final Color ON_COLOR  = new Color(76, 175, 80);

    private boolean on;
    private float knobPos;
    private final Timer animation;
    private Consumer<Boolean> listener = b -> {};

    public ToggleSwitch(boolean initial) {
        on = initial;
        knobPos = on ? 1f : 0f;
        setPreferredSize(new Dimension(60, 30));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        animation = new Timer(10, e -> {
            float target = on ? 1f : 0f;
            knobPos += (target - knobPos) * 0.3f;
            if (Math.abs(target - knobPos) < 0.01f) {
                knobPos = target;
                ((Timer) e.getSource()).stop();
            }
            repaint();
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                setOn(!on);
            }
        });
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean value) {
        if (on == value) return;
        on = value;
        animation.start();
        listener.accept(on);
    }

    public void onToggle(Consumer<Boolean> listener) {
        this.listener = listener;
    }

    @Override
    protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth(), h = getHeight();

        g.setColor(mix(OFF_COLOR, ON_COLOR, knobPos));
        g.fillRoundRect(0, 0, w, h, h, h);

        int pad = 3;
        int d = h - 2 * pad;
        int x = pad + Math.round(knobPos * (w - d - 2 * pad));
        g.setColor(Color.WHITE);
        g.fillOval(x, pad, d, d);

        g.dispose();
    }

    private static Color mix(Color a, Color b, float t) {
        return new Color(
                Math.round(a.getRed()   + (b.getRed()   - a.getRed())   * t),
                Math.round(a.getGreen() + (b.getGreen() - a.getGreen()) * t),
                Math.round(a.getBlue()  + (b.getBlue()  - a.getBlue())  * t));
    }
}
