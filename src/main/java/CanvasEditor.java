import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class CanvasEditor extends JPanel implements MouseListener,KeyListener{


    ArrayList<Integer[]> swarmPositions = new ArrayList<>();
    ArrayList<Point> line = new ArrayList<>();
    ArrayList<Integer[]> targetPositions = new ArrayList<>();

    private boolean newLine=true;

    Timer t;
    Double[][] winkel;

    public CanvasEditor(int high, int with) {

        this.setSize(high, with);
        this.setRequestFocusEnabled(true);
        this.setVisible(true);
        this.addMouseListener(this);

        winkel = new Double[this.getWidth()][this.getHeight()];

        repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //Info display
        g2d.setColor(Color.white);
        g2d.drawString("draw obstacles:  [hold left-mouse-button]", 0, 10);
        g2d.setColor(Color.RED);
        g2d.drawString("swarm location: [hold shift] + [left-mouse-button]", 0, 20);
        g2d.setColor(Color.YELLOW);
        g2d.drawString("Target location:  [Double-mouse-click]", 0, 30);

        //draw Target positions
        for (Integer[] tp : targetPositions) {
            g2d.setColor(Color.YELLOW);
            g2d.fillOval(tp[0]-10,tp[1]-10, 20, 20);

        }

        //draw Swarm positions

        for (Integer[] pos : swarmPositions) {
            g2d.setColor(Color.RED);
            g2d.drawLine(pos[0] - 10, pos[1] - 10, pos[0] + 10, pos[1] + 10);
            g2d.drawLine(pos[0] - 10, pos[1] + 10, pos[0] + 10, pos[1] - 10);

        }

        g2d.setColor(Color.white);
        for (int i = 0; i < winkel.length; i++) {
            for (int j = 0; j < winkel[i].length; j++) {
                if (winkel[i][j] != null) {
                    g2d.drawLine(i, j, i, j);
                } else if (winkel[i][j] == null) {

                } else if (winkel[i][j] == 100) {
                    g2d.drawLine(i, j, i, j);
                }

            }
        }
        g2d.setColor(Color.RED);
        if (line.size() == 0) {
            return;
        }
        Point befor = line.get(0);
        for (int i = 0; i < line.size(); i++) {
            g2d.drawLine(befor.x, befor.y, line.get(i).x, line.get(i).y);
            befor = line.get(i);

        }
        //g2d.drawLine(befor.x, befor.y, getMousePosition().x, getMousePosition().y);


    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (e.isShiftDown()) {
            swarmPositions.add(new Integer[]{e.getX(), e.getY()});
        }
        if (e.getClickCount() == 2 && !e.isConsumed()) {
            e.consume();
            targetPositions.add(new Integer[]{e.getX(), e.getY()});
        }


    }

    @Override
    public void mousePressed(MouseEvent e) {

        if(e.getButton()==MouseEvent.BUTTON1){
            line = new ArrayList<>();
            t = new Timer(10, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent a) {
                    Point p = getMousePosition();
                    if (p == null) {
                        return;
                    }
                    if (!line.contains(p) && getBounds().contains(p)) {
                        line.add(p);
                    }
                    repaint();
                }
            });
            t.start();
        }




    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getButton()==MouseEvent.BUTTON1){
            t.stop();
        }
        if(e.getButton()==MouseEvent.BUTTON3){
            if(this.newLine) {
                newLine=false;
                line = new ArrayList<>();
                Point p = getMousePosition();
                if (p == null) {
                    return;
                }
                if (!line.contains(p) && getBounds().contains(p)) {
                    line.add(p);
                }
            }else {
                line.add(e.getPoint());
                newLine=true;
            }
        }



        Point befor = line.get(0);
        int lastI = 0;
        for (int i = 1; i < line.size(); i++) {
            //winkel berechnug
            double angle = Math.atan2(line.get(i).y - befor.y, line.get(i).x - befor.x);

            zeichneLinie(line.get(i), befor, angle);

            befor = line.get(i);

        }
        repaint();
    }

    private void zeichneLinie(Point a, Point b, Double angle) {


        int dx = b.x - a.x;
        int dy = b.y - a.y;
        double x = 0, y = 0;
        double bertrag = Math.sqrt(dx * dx + dy * dy);
        double rx = dx / bertrag;
        double ry = dy / bertrag;
        while (Math.abs(x) < Math.abs(dx) || Math.abs(y) < Math.abs(dy)) {
            winkel[(int) (x + a.x)][(int) (y + a.y)] = angle%Math.PI;
            winkel[(int) (x + a.x+1)][(int) (y + a.y)] = angle%Math.PI;
            winkel[(int) (x + a.x)][(int) (y + a.y+1)] = angle%Math.PI;
            winkel[(int) (x + a.x+1)][(int) (y + a.y+1)] = angle%Math.PI;
            x += rx;
            y += ry;
        }
    /*int x1=b.x,x0=a.x,y1=b.y,y0=a.y;
        int dx = x1 - x0;
        int dy = y1 - y0;

        // Set the initial value of the coordinates
        int x = x0;
        int y = y0;


       double fehler = dx/2;
        // Increment the coordinates until they reach the end of the line
        while (x <= x1 && y <= y1) {
            // Set the color of the current pixel
            winkel[x][y]=angle;
            fehler = fehler-dy;
            // Calculate the new value of p
            if (fehler<0){
                fehler = fehler + dx;
                y++;
            }
            x++;
        }*/


    }


    public void setWinkel(Double[][] winkel) {
        this.winkel = winkel;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public Double[][] getWinkel() {
        return winkel;
    }

    public ArrayList<Integer[]> getSwarmPositions() {
        return swarmPositions;
    }

    public ArrayList<Integer[]> getTargetPositions() {
        return targetPositions;
    }
}
