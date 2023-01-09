import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Simulation extends JFrame {
	static int sleep = 5; // 8
	static double pix = 1;// 0.2
	int anzFz = 50;
	int anzZiele = 2;
	int anzToDestroy = 3;

	boolean pause=false;

	Logger log = Logger.getLogger("SimLogger");
	ArrayList<Vehicle> allVehicles = new ArrayList<Vehicle>();
	ArrayList<Target> allTargets = new ArrayList<Target>();

	Canvas canvas;

	static int width;
	static int height;

	Simulation(Double[][] winkel, ArrayList<Integer[]> swarmPositions, int anzFz, int anzToDestroy) throws IOException {
		this.anzFz = anzFz;
		this.anzToDestroy = anzToDestroy;
		FileWriter out = new FileWriter("array");

		for (int y = 0; y < winkel.length; y++) {
			for (int x = 0; x < winkel[y].length; x++) {
				out.write(String.valueOf(winkel[y][x]));
				out.write(" ");
			}
			out.write("\n");
		}
		setSize(1000, 800);
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

		for(int i = 0; i<anzZiele; i++){
			Target target = new Target(winkel);
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
		pauseButton.addActionListener((e)-> pause= !pause);

		controlsPanel.add(pauseButton);
		controlsPanel.add(speedLabel);
		controlsPanel.add(speedSlider);
		controlsPanel.add(currSpeedLabel);

		add(controlsPanel, BorderLayout.NORTH);
		add(canvas, BorderLayout.CENTER);
		validate();
		setVisible(true);

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

			while (true) {

				if(!pause) {
					//log.info("sim running...");

					//Move all Vehicles on update
					for (int i = 0; i < allVehicles.size(); i++) {
						v = allVehicles.get(i);
						v.steuern(allVehicles);
						//System.out.println(v.pos[0]);
						//System.out.println(v.pos[0]);
					}

					//Move all Targets on update
					for (Target t : allTargets) {
						t.move();
					}

					// Update the graphics on the canvas and redraw it
					canvas.repaint();

					// Update the graphics on the window and redraw it
					repaint();

					try {
						Thread.sleep(sleep);
					} catch (InterruptedException e) {
					}
				}
			}
		});

		thread.start();

	}
}