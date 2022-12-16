package walleditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class Canvas extends JPanel implements MouseListener, KeyListener {


    ArrayList<Point>line = new ArrayList<>();

    Timer t;
    Double[][] winkel;

    public Canvas(int high,int with){
        this.setSize(high,with);
        this.setRequestFocusEnabled(true);
        this.setVisible(true);
        this.addMouseListener(this);

        winkel =new Double[this.getWidth()][this.getHeight()];

        repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);


        g.setColor(Color.black);
        for(int i=0;i< winkel.length;i++){
            for(int j=0;j< winkel[i].length;j++){
                if(winkel[i][j]!=null){
                    g.drawRect(i,j,1,1);
                }else if(winkel[i][j]==null){

                }
                else if(winkel[i][j]==100){
                    g.drawOval(i,j,3,3);
                }

            }
        }
        g.setColor(Color.RED);
        if(line.size()==0){
            return;
        }
        Point befor=line.get(0);
        for(int i=0;i<line.size();i++){
            g.drawLine(befor.x,befor.y,line.get(i).x,line.get(i).y);
            befor=line.get(i);
        }


    }

    @Override
    public void mouseClicked(MouseEvent e) {



    }

    @Override
    public void mousePressed(MouseEvent e) {


        line = new ArrayList<>();
        t=new Timer(10,new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                Point p=getMousePosition();
                if(p==null){
                    return;
                }
                if(!line.contains(p)&&getBounds().contains(p)){
                    line.add(p);
                }
                repaint();
            }
        });
        t.start();



    }

    @Override
    public void mouseReleased(MouseEvent e) {
        t.stop();

        Point befor=line.get(0);
        int lastI=0;
        for(int i=1;i<line.size();i++){
            //winkel berechnug
            double angle=Math.atan2(line.get(i).x-befor.x,line.get(i).y-befor.y);

                zeichneLinie(line.get(i),befor,angle);


            befor=line.get(i);

        }


        repaint();


    }
    private void zeichneLinie(Point a,Point b,Double angle) {
        winkel[a.x][a.y]=100.0;
        winkel[b.x][b.y]=100.0;

        int dx=b.x-a.x;
        int dy=b.y-a.y;
        double x=0,y=0;
        double bertrag=Math.sqrt(dx*dx+dy*dy);
        double rx=dx/bertrag;
        double ry=dy/bertrag;
        while(Math.abs(x)<Math.abs(dx)||Math.abs(y)<Math.abs(dy)){
            winkel[(int)(x+a.x)][(int)(y+a.y)]=angle;
            x+=rx;
            y+=ry;
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



    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_S){
            JFileChooser fc = new JFileChooser();
            File f=fc.getSelectedFile();
            FileWriter fileWriter;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
