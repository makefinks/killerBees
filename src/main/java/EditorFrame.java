import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class EditorFrame extends JFrame {


    private CanvasEditor canvasEditor;
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

                    if(e.canvasEditor.getSwarmPositions().size() > 0){
                        e.dispose();
                        Simulation xx = null;
                        try {
                            xx = new Simulation(e.canvasEditor.getWinkel(), e.canvasEditor.getSwarmPositions());
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        xx.run();

                    }
                    else{
                        JOptionPane.showMessageDialog(e, "Please select atleast one entry point for the swarm!");
                    }

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


    public  CanvasEditor getCanvasEditor() {
        return canvasEditor;
    }

    public void setCanvasEditor(CanvasEditor canvasEditor) {
        this.canvasEditor = canvasEditor;
    }
}
