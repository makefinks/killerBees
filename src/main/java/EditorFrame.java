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

    private JCheckBox measureTimeCheckbox;
    private JTextField sleepTimeField;

    private JLabel measureSleepLabel;

    private JLabel randFactorLabel;
    private JTextField randFactorField;

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
        lblNrVehicles.setForeground(Color.white);
        JLabel lblNrToDestroy = new JLabel("Number of hits to destroy:");
        lblNrToDestroy.setForeground(Color.white);
        inputNrOfVehicles = new JTextField("100");
        inputNrOfVehicles.setColumns(3);
        inputNrToDestroy = new JTextField("5");
        inputNrToDestroy.setColumns(3);
        inputNrToDestroy.setBackground(Color.GRAY);
        inputNrOfVehicles.setBackground(Color.GRAY);

        randFactorLabel = new JLabel("rand factor");
        randFactorLabel.setForeground(Color.white);
        randFactorField = new JTextField("0.1");

        inputPanel.add(lblNrVehicles);
        inputPanel.add(inputNrOfVehicles);
        inputPanel.add(lblNrToDestroy);
        inputPanel.add(inputNrToDestroy);

        inputPanel.add(randFactorLabel);
        inputPanel.add(randFactorField);
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

        measureTimeCheckbox = new JCheckBox("measure time");
        sleepTimeField = new JTextField("1");
        measureSleepLabel = new JLabel("sleep");
        measureSleepLabel.setForeground(Color.white);
        inputPanel.add(measureTimeCheckbox);
        inputPanel.add(sleepTimeField);
        inputPanel.add(measureSleepLabel);

        this.setResizable(false);


        measureTimeCheckbox.addActionListener(e -> {

            if(measureTimeCheckbox.isSelected()){
                sleepTimeField.setEnabled(true);
            }else{
                sleepTimeField.setEnabled(false);
            }
        });


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
                this.setVisible(false);
                Simulation xx = null;
                try {
                    System.out.println(canvasEditor.targetPositions.size());
                    xx = new Simulation(this.canvasEditor.getWinkel(), this.canvasEditor.getSwarmPositions(),
                            this.canvasEditor.getTargetPositions(),this);
                    Vehicle.setRandom(randomCheckbox.isSelected());
                    Vehicle.setRandFactor(Double.parseDouble(randFactorField.getText()));
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

    public JCheckBox getRandomCheckbox() {
        return randomCheckbox;
    }

    public JCheckBox getMeasureTimeCheckbox() {
        return measureTimeCheckbox;
    }

    public JTextField getSleepTimeField() {
        return sleepTimeField;
    }
}
