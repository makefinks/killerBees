package winkelTest;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class WinkelDarstellung extends JPanel {

    Zeichenfeld zf = new Zeichenfeld(300, 0, 200, 200);
    private Line spiegelLine;
    private Line resultLine;

    private ArrayList<Line> alleLinien = new ArrayList<>();
    private Line line = new Line(new Point(0, 0), new Point(0, 0));

    public WinkelDarstellung() {

        this.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (zf.inFeld(e.getX(), e.getY())) {
                    zf.klick(e.getX(), e.getY());
                }
                repaint();
            }
        });
        this.setFocusable(true);
        this.requestFocus();
        this.setBounds(0, 0, 4000, 4000);
        this.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_C:
                        line = zf.getLine();

                        break;

                    case KeyEvent.VK_V:
                        spiegelLine = zf.getLine();

                        break;

                    case KeyEvent.VK_P:
                        line.print();

                        break;

                }

                repaint();
            }
        });
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, 200, 200);
        g.drawRect(0, 200, 200, 200);
        g.drawOval(0, 200, 200, 200);
        zf.draw(g2d);

        if (spiegelLine != null && line != null) {
            resultLine = line.getSpiegelLine(spiegelLine);
            g.setColor(Color.BLUE);
            this.spiegelLine.InKreis().draw(g2d);
            g.drawLine(spiegelLine.InKreis().getStartPunkt().x, spiegelLine.InKreis().getStartPunkt().y,
                    spiegelLine.InKreis().getEPRev().x, spiegelLine.InKreis().getEPRev().y);
            g.setColor(Color.GREEN);
            line.draw(g2d);
            line.InKreis().draw(g2d);
            g.setColor(Color.RED);
            resultLine.draw(g2d);
            System.out.println();
            g.setColor(Color.BLACK);
            g.drawString("Line" + line.angleDeg + " Diff=" + (line.angleDeg - spiegelLine.angleDeg) + " Spiegel=" + this.spiegelLine.angleDeg + " Neuangle=" + resultLine.angleDeg, 250, 230);

        }

    }

    public void setLine(Line line) {
        this.line = line;
		repaint();
    }

    public void setMirror(Line line) {
        this.spiegelLine = line;
        repaint();
    }

    public static void main(String[] args) {
        JFrame jf = new JFrame();
        WinkelDarstellung g = new WinkelDarstellung();
        jf.setLayout(null);
        jf.setSize(500 + 18, 400 + 38);
        jf.setFocusable(false);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setLocationRelativeTo(null);
        jf.add(g);
        g.setVisible(true);
        jf.setVisible(true);

    }

}
