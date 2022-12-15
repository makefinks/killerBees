import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;

public class Simulation extends JFrame {
	static int sleep = 8; // 8
	static double pix = 0.2;// 0.2
	int anzFz = 160;


	ArrayList<Vehicle> allVehicles = new ArrayList<Vehicle>();
	ArrayList<Target> allTargets = new ArrayList<Target>();
	JPanel canvas;

	Simulation(Double[][] winkel) {

		canvas = new Canvas(allVehicles, allTargets, pix, winkel);
		setTitle("Swarm Simulation");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);

		for (int k = 0; k < anzFz; k++) {
			Vehicle car = new Vehicle();
			if (k == 0)
				car.type = 1;
			allVehicles.add(car);
		}

		//Add Targets to

		add(canvas);
		canvas.repaint();
		setSize(1000, 800);
		setVisible(true);

	}

	/*
	public static void main(String args[]) {
		Simulation xx = new Simulation();
		xx.run();
	}
	 */

	public void run() {
		Vehicle v;

		while (true) {

			//Move all Vehicles on update
			for (int i = 0; i < allVehicles.size(); i++) {
				v = allVehicles.get(i);
				v.steuern(allVehicles);
				System.out.println(v.pos[0]);
			}

			//Move all Targets on update
			for(Target t : allTargets){
				t.move();
			}


			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
			}
			repaint();
		}
	}
}
