import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.swing.*;

public class Simulation extends JFrame {
	static int sleep = 8; // 8
	static double pix = 0.2;// 0.2
	int anzFz = 160;
	int anzZiele = 2;

	Logger log = Logger.getLogger("SimLogger");
	ArrayList<Vehicle> allVehicles = new ArrayList<Vehicle>();
	ArrayList<Target> allTargets = new ArrayList<Target>();

	Canvas canvas;

	Simulation(Double[][] winkel) {

		setTitle("Swarm Simulation");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		for (int k = 0; k < anzFz; k++) {
			Vehicle car = new Vehicle();
			if (k == 0)
				car.type = 1;
			allVehicles.add(car);
		}

		for(int i = 0; i<anzZiele; i++){
			Target target = new Target(winkel);
			allTargets.add(target);
		}

		canvas = new Canvas(allVehicles, allTargets, pix, winkel);


		add(canvas, BorderLayout.CENTER);
		validate();
		setSize(1000, 800);
		setVisible(true);
	}

	public void run() {


		Thread thread = new Thread(() -> {
			Vehicle v;

			while (true) {

				log.info("sim running...");

				//Move all Vehicles on update
				for (int i = 0; i < allVehicles.size(); i++) {
					v = allVehicles.get(i);
					v.steuern(allVehicles);
					//System.out.println(v.pos[0]);
					//System.out.println(v.pos[0]);
				}

				//Move all Targets on update
				for(Target t : allTargets){
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
		});

		thread.start();

	}
}