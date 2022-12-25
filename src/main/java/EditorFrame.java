import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class EditorFrame extends JFrame {


    private static CanvasEditor canvasEditor;
    public EditorFrame(){
        setTitle("Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        //requestFocus(false);
        setSize(1000, 800);
        canvasEditor = new CanvasEditor(this.getWidth(),this.getHeight());

        add(canvasEditor);
        validate();
        setVisible(true);
        canvasEditor.repaint();
    }

    public static void main(String[] args) {
        EditorFrame e= new EditorFrame();

        e.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent event) {
                if (event.getKeyChar() == 's') {
                    System.out.println("The 's' key was typed!");
                    e.dispose();
                    Simulation xx = new Simulation(canvasEditor.getWinkel());
                    canvasEditor.setVisible(false);
                    e.add(xx.canvas);
                    xx.run();

                }
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        e.repaint();
    }
}
