import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Simulation extends JFrame {
    static int sleep = 5; // 8
    static double pix = 1;// 0.2
    int anzFz = 50;
    int anzZiele = 0;
    int anzToDestroy = 3;

    boolean pause = false;

    boolean enableSight;

    boolean enableMeasureTime;

    int measureTimeSleep;

    JButton stopStimulationButton;

    Logger log = Logger.getLogger("SimLogger");
    ArrayList<Vehicle> allVehicles = new ArrayList<Vehicle>();
    ArrayList<Target> allTargets = new ArrayList<Target>();

    Canvas canvas;

    static int width;
    static int height;

    private EditorFrame editorframe;

    Simulation(Double[][] winkel, ArrayList<Integer[]> swarmPositions, ArrayList<Integer[]> targetPositions, EditorFrame editorFrame) throws IOException {
        this.anzFz = editorFrame.getNrOfVehicles();
        this.anzToDestroy = editorFrame.getNrToDestroy();
        anzZiele = targetPositions.size();
        enableSight = editorFrame.getSightCheckbox().isSelected();
        enableMeasureTime = editorFrame.getMeasureTimeCheckbox().isSelected();
        measureTimeSleep = Integer.parseInt(editorFrame.getSleepTimeField().getText());

        this.editorframe = editorFrame;

        /*
        FileWriter out = new FileWriter("array");

        for (int y = 0; y < winkel.length; y++) {
            for (int x = 0; x < winkel[y].length; x++) {
                out.write(String.valueOf(winkel[y][x]));
                out.write(" ");
            }
            out.write("\n");
        }

         */
        setSize(1500, 1000);
        width = getWidth();
        height = getHeight();

        setLocationRelativeTo(null);

        setTitle("Swarm Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        for (int k = 0; k < anzFz; k++) {
            Vehicle car = new Vehicle(winkel, swarmPositions);
            if (k == 0)
                car.type = 1;
            allVehicles.add(car);
        }

        for (int i = 0; i < anzZiele; i++) {
            Target target = new Target(winkel, targetPositions, anzToDestroy);
            allTargets.add(target);
        }


        log.info("WINKEL SIZE " + winkel.length);

        canvas = new Canvas(allVehicles, allTargets, pix, winkel);

        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new FlowLayout());
        controlsPanel.setBackground(Color.black);

        JSlider speedSlider = new JSlider(JSlider.HORIZONTAL, 100);
        speedSlider.setValue(speedSlider.getMaximum() - sleep);
        speedSlider.setForeground(Color.ORANGE);

        JLabel speedLabel = new JLabel("Speed");
        speedLabel.setForeground(Color.WHITE);

        JLabel currSpeedLabel = new JLabel(String.valueOf(speedSlider.getMaximum() - speedSlider.getValue()));
        currSpeedLabel.setText(String.valueOf(speedSlider.getValue()));
        currSpeedLabel.setForeground(Color.WHITE);

        JButton pauseButton = new JButton("Pause");
        pauseButton.addActionListener((e) -> {
            pause = !pause;
            System.out.println("Pause");
        });

        stopStimulationButton = new JButton("stop sim");

        controlsPanel.add(pauseButton);
        controlsPanel.add(speedLabel);
        controlsPanel.add(speedSlider);
        controlsPanel.add(currSpeedLabel);
        controlsPanel.add(stopStimulationButton);

        add(controlsPanel, BorderLayout.NORTH);
        add(canvas, BorderLayout.CENTER);
        validate();
        setVisible(true);

        if(enableMeasureTime){
            sleep = measureTimeSleep;
            speedSlider.setEnabled(false);
            pauseButton.setEnabled(false);
        }


        stopStimulationButton.addActionListener(e -> {
            dispose();
            editorFrame.setVisible(true);
            sleep = 5;
            Target.id = 0;
        });
        speedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                sleep = speedSlider.getMaximum() - speedSlider.getValue();
                currSpeedLabel.setText(String.valueOf(speedSlider.getValue()));
            }
        });

    }

    public void run() {


        Thread thread = new Thread(() -> {
            Vehicle v;

            long start = System.nanoTime();
            while (true) {

                if (!pause) {
                    //log.info("sim running...");

                    //Move all Vehicles on update
                    for (int i = 0; i < allVehicles.size(); i++) {
                        v = allVehicles.get(i);
                        v.steuern(allVehicles, allTargets);
                        collisionTarget(v);
                        //System.out.println(v.pos[0]);
                        //System.out.println(v.pos[0]);
                    }
                    updateLists();

                    // Update the graphics on the canvas and redraw it
                    canvas.repaint();

                    // Update the graphics on the window and redraw it
                    repaint();

                    if(enableMeasureTime) {
                        if (allTargets.size() == 0) {
                            long end = System.nanoTime();
                            long time = end - start;
                            time = TimeUnit.MILLISECONDS.convert(time, TimeUnit.NANOSECONDS);
                            int option = JOptionPane.showConfirmDialog(this, "All Targets eliminated in " + time + " ms");

                            dispose();
                            editorframe.setVisible(true);
                            Target.id = 0;
                            break;
                        }
                    }
                }
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                }

            }
        });

        thread.start();

    }

    private void collisionTarget(Vehicle vehicle) {

        if(enableSight){
            vehicle.getTargetInSight(allVehicles, allTargets);
        }

        // radius target = 15px
        for (Target target : allTargets) {
            boolean inCircle = Math.pow(vehicle.pos[0] - target.pos[0], 2) +
                                Math.pow(vehicle.pos[1] - target.pos[1], 2)
                                <= 15*15;
            if (inCircle) {
                target.reduceLife();
                vehicle.reduceLife();
                vehicle.directOtherVehicles(allVehicles);
            }
        }
    }

    private void updateLists() {
       // allVehicles.forEach(v -> System.out.println(Arrays.toString(v.tmpTargetPos)));
        allTargets.removeIf(t -> t.getLife() <= 0);
        allVehicles.removeIf(v -> v.getLife() <= 0);
    }
}