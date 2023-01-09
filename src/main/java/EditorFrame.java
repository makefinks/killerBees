import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class EditorFrame extends JFrame {


    private CanvasEditor canvasEditor;
     private JTextField inputNrOfVehicles;
    private JTextField inputNrToDestroy;

    private JButton btnRun;
    public EditorFrame(){

        setLayout(new BorderLayout());
        setTitle("Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //requestFocus(false);
        setSize(1000, 800);
        canvasEditor = new CanvasEditor(this.getWidth(),this.getHeight());

        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(Color.darkGray);
        btnRun = new JButton("Run Simulation");
        JLabel lblNrVehicles = new JLabel("Number of Vehicles:");
        JLabel lblNrToDestroy = new JLabel("Number of hits to destroy:");
        inputNrOfVehicles = new JTextField("100");
        inputNrOfVehicles.setColumns(3);
        inputNrToDestroy = new JTextField("5");
        inputNrToDestroy.setColumns(3);
        inputNrToDestroy.setBackground(Color.GRAY);
        inputNrOfVehicles.setBackground(Color.GRAY);
        inputPanel.add(lblNrVehicles);
        inputPanel.add(inputNrOfVehicles);
        inputPanel.add(lblNrToDestroy);
        inputPanel.add(inputNrToDestroy);
        inputPanel.add(btnRun);
        btnRun.addActionListener(e -> {
            if(this.canvasEditor.getSwarmPositions().size() > 0){
                int nrOfVehicles = 0;
                int nrToDestroy = 0;
                try {
                    nrOfVehicles = Integer.parseInt(this.getInputNrOfVehicles().getText());
                    nrToDestroy = Integer.parseInt(this.getInputNrToDestroy().getText());
                } catch (NumberFormatException exception) {
                    showErrorMessage();
                    return;
                }
                if (nrOfVehicles <= 0 || nrToDestroy <= 0) {
                    showErrorMessage();
                    return;
                }
                this.dispose();
                Simulation xx = null;
                try {
                    xx = new Simulation(this.canvasEditor.getWinkel(), this.canvasEditor.getSwarmPositions());
                    xx.setAnzFz(nrOfVehicles);
                    xx.setAnzToDestroy(nrToDestroy);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                xx.run();

            }
            else{
                JOptionPane.showMessageDialog(this, "Please select atleast one entry point for the swarm!");
            }
            this.repaint();
        });
        add(canvasEditor, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.NORTH);
        validate();
        setVisible(true);
        canvasEditor.repaint();
    }


    public static void main(String[] args) {
        EditorFrame e= new EditorFrame();

/*
        e.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent event) {
                if (event.getKeyChar() == 's') {

                    if(e.canvasEditor.getSwarmPositions().size() > 0){
                        int nrOfVehicles = Integer.parseInt(e.getInputNrOfVehicles().getText());
                        e.dispose();
                        Simulation xx = null;
                        try {
                            xx = new Simulation(e.canvasEditor.getWinkel(), e.canvasEditor.getSwarmPositions());
                            xx.setAnzFz(nrOfVehicles);
                            System.out.println("Hello");
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

 */
    }




    public  CanvasEditor getCanvasEditor() {
        return canvasEditor;
    }

    public void setCanvasEditor(CanvasEditor canvasEditor) {
        this.canvasEditor = canvasEditor;
    }

    public JTextField getInputNrOfVehicles() {
        return inputNrOfVehicles;
    }

    public JTextField getInputNrToDestroy() {
        return inputNrToDestroy;
    }

    public void showErrorMessage() {
        JOptionPane.showMessageDialog(this, "Please enter valid Input!");
    }
}
