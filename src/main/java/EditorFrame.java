import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.sql.SQLOutput;

public class EditorFrame extends JFrame {


    private CanvasEditor canvasEditor;
    private JTextField inputNrOfVehicles;
    private JTextField inputNrToDestroy;
    private JComboBox<String> mapList;
    private JButton loadMapButton;

    private JButton saveCurrMapButton;

    private JCheckBox  sightCheckbox;

    private JCheckBox randomCheckbox;

    private JButton btnRun;

    private int nrOfVehicles;
    private int nrToDestroy;
    public EditorFrame(){

        setLayout(new BorderLayout());
        setTitle("Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //requestFocus(false);
        setSize(1500, 1000);
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

        //Map loader
        mapList = new JComboBox<>();
        loadAllMaps(mapList);
        loadMapButton = new JButton("Load");
        inputPanel.add(mapList);
        inputPanel.add(loadMapButton);

        //Map saver
        saveCurrMapButton = new JButton("save map");
        inputPanel.add(saveCurrMapButton);

        sightCheckbox = new JCheckBox("Enable sight");
        sightCheckbox.setSelected(true);
        inputPanel.add(sightCheckbox);

        randomCheckbox = new JCheckBox("Random");
        inputPanel.add(randomCheckbox);


        saveCurrMapButton.addActionListener(e -> {
            String mapName = JOptionPane.showInputDialog("Map name: ");

            if(!mapName.isEmpty()){

                FileOutputStream fout = null;
                try {
                    fout = new FileOutputStream("src/main/java/maps/" + mapName);
                    ObjectOutputStream oos = new ObjectOutputStream(fout);

                    oos.writeObject(canvasEditor.getWinkel());

                    fout.close();
                    oos.close();
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }


            }


        });

        loadMapButton.addActionListener(e ->{
            String mapName = (String) mapList.getSelectedItem();

            try {
                FileInputStream fin = new FileInputStream("src/main/java/maps/"+mapName);
                ObjectInputStream oin = new ObjectInputStream(fin);


                Double[][] loadWinkel = (Double[][]) oin.readObject();

                canvasEditor.setWinkel(loadWinkel);
                canvasEditor.repaint();
                validate();

            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }

        });

        btnRun.addActionListener(e -> {
            if(this.canvasEditor.getSwarmPositions().size() > 0){
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
                    xx = new Simulation(this.canvasEditor.getWinkel(), this.canvasEditor.getSwarmPositions(),
                            this.canvasEditor.getTargetPositions(),this);
                    Vehicle.setRandom(randomCheckbox.isSelected());
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


    private void loadAllMaps(JComboBox<String> mapList) {

        File dir = new File("src/main/java/maps");
        File[] mapFiles = dir.listFiles();

        assert mapFiles != null;
        for(File map : mapFiles){
            mapList.addItem(map.getName());
        }
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

    public JComboBox<String> getMapList() {
        return mapList;
    }

    public JButton getLoadMapButton() {
        return loadMapButton;
    }

    public JButton getSaveCurrMapButton() {
        return saveCurrMapButton;
    }

    public JCheckBox getSightCheckbox() {
        return sightCheckbox;
    }

    public JButton getBtnRun() {
        return btnRun;
    }

    public int getNrOfVehicles() {
        return nrOfVehicles;
    }

    public int getNrToDestroy() {
        return nrToDestroy;
    }
}
