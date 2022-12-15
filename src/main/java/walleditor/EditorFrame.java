package walleditor;

import javax.swing.*;

public class EditorFrame extends JFrame {
    public EditorFrame(){
        setTitle("Swarm");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        //requestFocus(false);
        setSize(1000, 800);
        Canvas canvas = new Canvas(this.getWidth(),this.getHeight());

        add(canvas);

        validate();

        setVisible(true);
        canvas.repaint();
    }

    public static void main(String[] args) {
        EditorFrame e= new EditorFrame();
        e.repaint();
    }
}
