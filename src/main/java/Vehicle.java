import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.*;

public class Vehicle {
	static int allId = 0;
	int id; // Fahrzeug-ID
	double rad_sep; // Radius f�r Zusammenbleiben
	double rad_zus; // Radius f�r Separieren
	int type; // Fahrzeug-Type (0: Verfolger; 1: Anf�hrer)
	final double FZL; // L�nge
	final double FZB; // Breite
	double[] pos; // Position

	double[] last_pos;
	Queue<Double[]> last_pos_list = new LinkedList<>();
	double[] last_vel;
	int lastCount;
	double[] vel; // Geschwindigkeit
	final double max_acc; // Maximale Beschleunigung
	final double max_vel; // Maximale Geschwindigkeit

	Double[][] winkel;

	ArrayList<Integer[]> swarmPositions = new ArrayList<>();

	Vehicle(Double[][] winkel, ArrayList<Integer[]> swarmPositions) {
		this.winkel = winkel;
		this.swarmPositions = swarmPositions;
		allId++;
		this.id = allId;
		this.FZL = 2; //2
		this.FZB = 1; //1
		this.rad_sep = 7;// 50
		this.rad_zus = 25;// 25
		this.type = 0;
		this.max_acc = 0.05;// 0.1
		this.max_vel = 1;

		pos = new double[2];
		vel = new double[2];
		last_pos = new double[2];
		last_vel = new double[2];
		lastCount = 0;

		//generate random pos based on swarmPositions
		Random rand = new Random();
		int posFromIndex = rand.nextInt(swarmPositions.size());


		pos[0] = swarmPositions.get(posFromIndex)[0] + Simulation.pix * 50 * Math.random();
		pos[1] = swarmPositions.get(posFromIndex)[1] + Simulation.pix * 50 * Math.random();
		vel[0] = max_vel * Math.random();
		vel[1] = max_vel * Math.random();
	}

	ArrayList<Vehicle> nachbarErmitteln(ArrayList<Vehicle> all, double radius1, double radius2) {
		ArrayList<Vehicle> neighbours = new ArrayList<Vehicle>();
		for (int i = 0; i < all.size(); i++) {
			Vehicle v = all.get(i);
			if (v.id != this.id) {
				double dist = Math.sqrt(Math.pow(v.pos[0] - this.pos[0], 2) + Math.pow(v.pos[1] - this.pos[1], 2));
				if (dist >= radius1 && dist < radius2) {
					neighbours.add(v);
				}
			}
		}
		return neighbours;
	}

	double[] beschleunigungErmitteln(double[] vel_dest) {
		//Berechnet die notwendige Beschleunigung, um eine Zielgeschwindigkeit vel_dest zu erreichen
		double[] acc_dest = new double[2];

		// 1. Konstanter Geschwindigkeitsbetrag
		vel_dest = Vektorrechnung.normalize(vel_dest);
		vel_dest[0] = vel_dest[0] * max_vel;
		vel_dest[1] = vel_dest[1] * max_vel;

		// 2. acc_dest berechnen
		acc_dest[0] = vel_dest[0] - vel[0];
		acc_dest[1] = vel_dest[1] - vel[1];

		return acc_dest;
	}

//cohesion
	double[] zusammenbleiben(ArrayList<Vehicle> all) {
		ArrayList<Vehicle> neighbours;
		double[] pos_dest = new double[2];
		double[] vel_dest = new double[2];
		double[] acc_dest = new double[2];

		acc_dest[0] = 0;
		acc_dest[1] = 0;
		neighbours = nachbarErmitteln(all, rad_sep, rad_zus);

		if (neighbours.size() > 0) {
			// 1. Zielposition pos_dest berechnen
			pos_dest[0] = 0;
			pos_dest[1] = 0;
			for (int i = 0; i < neighbours.size(); i++) {
				Vehicle v = neighbours.get(i);
				pos_dest[0] = pos_dest[0] + v.pos[0];
				pos_dest[1] = pos_dest[1] + v.pos[1];
			}
			pos_dest[0] = pos_dest[0] / neighbours.size();
			pos_dest[1] = pos_dest[1] / neighbours.size();

			// 2. Zielgeschwindigkeit vel_dest berechnen
			vel_dest[0] = pos_dest[0] - pos[0];
			vel_dest[1] = pos_dest[1] - pos[1];

			// 3. Zielbeschleunigung acc_dest berechnen
			acc_dest = beschleunigungErmitteln(vel_dest);

		}
		return acc_dest;
	}

	double[] separieren(ArrayList<Vehicle> all) {
		ArrayList<Vehicle> neighbours;
		double[] vel_dest = new double[2];
		double[] acc_dest = new double[2];

		acc_dest[0] = 0;
		acc_dest[1] = 0;
		neighbours  = nachbarErmitteln(all, 0, rad_sep);

		if (neighbours.size() > 0) {
			// 1. Zielgeschwindigkeit vel_dest berechnen
			vel_dest[0] = 0;
			vel_dest[1] = 0;
			for (int i = 0; i < neighbours.size(); i++) {
				Vehicle v    = neighbours.get(i);
				double[] vel = new double[2];
				double dist;

				vel[0] = v.pos[0] - pos[0];
				vel[1] = v.pos[1] - pos[1];
				dist   = rad_sep  - Vektorrechnung.length(vel);
				if (dist < 0)System.out.println("fehler in rad");
				vel = Vektorrechnung.normalize(vel);
				vel[0] = -vel[0] * dist;
				vel[1] = -vel[1] * dist;
				
				vel_dest[0] = vel_dest[0] + vel[0];
				vel_dest[1] = vel_dest[1] + vel[1];
			}

			// 2. Zielbeschleunigung acc_dest berechnen
			acc_dest = beschleunigungErmitteln(vel_dest);
		}

		return acc_dest;
	}

	double[] ausrichten(ArrayList<Vehicle> all) {
		ArrayList<Vehicle> neighbours = new ArrayList<Vehicle>();
		double[] vel_dest = new double[2];
		double[] acc_dest = new double[2];
		acc_dest[0] = 0;
		acc_dest[1] = 0;

		neighbours = nachbarErmitteln(all, 0, rad_zus);
		for (Vehicle neighbour : neighbours) {
			double[] richtung = neighbour.vel;
			vel_dest[0] += richtung[0];
			vel_dest[1] += richtung[1];
		}

		if(neighbours.size() > 0){
			vel_dest[0] = vel_dest[0] / neighbours.size();
			vel_dest[1] = vel_dest[1] / neighbours.size();
			acc_dest = beschleunigungErmitteln(vel_dest);
		}

		return acc_dest;
	}

	double[] zufall() {
		double[] acc_dest = new double[2];
		acc_dest[0] = 0;
		acc_dest[1] = 0;

		if (Math.random() < 0.01) {
			acc_dest[0] = max_acc * Math.random();
			acc_dest[1] = max_acc * Math.random();
		}

		return acc_dest;
	}

	public double[] beschleunigung_festlegen(ArrayList<Vehicle> allVehicles) {

		double[] acc_dest  = new double[2];
		double[] acc_dest1 = new double[2];
		double[] acc_dest2 = new double[2];
		double[] acc_dest3 = new double[2];
		double f_zus = 0.15; // 0.05
		double f_sep = 0.55; // 0.55
		double f_aus = 0.3; // 0.4

		if (type == 1) {
			acc_dest = zufall();
		} else {
			acc_dest1 = zusammenbleiben(allVehicles);
			//acc_dest1 = folgen(allVehicles);
			acc_dest2 = separieren(allVehicles);
			acc_dest3 = ausrichten(allVehicles);

			acc_dest[0] = (f_zus * acc_dest1[0]) + (f_sep * acc_dest2[0] + (f_aus * acc_dest3[0]));
			acc_dest[1] = (f_zus * acc_dest1[1]) + (f_sep * acc_dest2[1] + (f_aus * acc_dest3[1]));

		}
		
		acc_dest = Vektorrechnung.truncate(acc_dest, max_acc);
		return acc_dest;
	}

	void steuern(ArrayList<Vehicle> allVehicles) {
		double[] acc_dest = beschleunigung_festlegen(allVehicles);

// 2. Neue Geschwindigkeit berechnen
		vel[0] = vel[0] + acc_dest[0];
		vel[1] = vel[1] + acc_dest[1];
		vel    = Vektorrechnung.normalize(vel);
		vel[0] = vel[0] * max_vel;
		vel[1] = vel[1] * max_vel;


		//Kollisionsberechnung

		//System.out.println("current Pos" + Arrays.toString(pos));
		//System.out.println("last Pos" + Arrays.toString(last_pos));

		// Define the velocity vector as a line with starting point (x1, y1) and ending point (x2, y2)
		double x1 = pos[0];
		double y1 = pos[1];
		double x2 = x1 + vel[0];
		double y2 = y1 + vel[1];
		Line2D velocityVector = new Line2D.Double(x1, y1, x2, y2);

		double[] newPoint = new double[2];
		newPoint[0] = pos[0] + vel[0];
		newPoint[1] = pos[1] + vel[1];

		//
		double[] velCopy = new double[]{vel[0], vel[1]};


		Line2D velocityPath = new Line2D.Double(pos[0], pos[1], newPoint[0], newPoint[1]);


		// Collsisions pruefung und änderung der richtung

		boolean flag=false;
		Double angleWall = angleInRect((int)pos[0],(int)pos[1],(int)newPoint[0],(int)newPoint[1]);
		if(angleWall!=null){
			double speed=Math.sqrt(vel[0]*vel[0]+vel[1]*vel[1]);
			double angleVehicle = Math.atan2(pos[1]-newPoint[1], pos[0]-newPoint[0]);


			angleVehicle=angleWall-(angleVehicle-angleWall);

			//System.out.println(angleVehicle);
			vel[0]=Math.cos(angleVehicle)*speed;
			vel[1]=Math.sin(angleVehicle)*speed;
			flag = true;
		}





		//check if the new position would result in another wall being passed and reduce the velocity so it does not pass the wall
		double[] newPosition = new double[]{pos[0]+vel[0], pos[1]+vel[1]};

		Line2D collisionRoute = new Line2D.Double(pos[0], pos[1], newPosition[0], newPosition[1]);

		double distance = 0;
		boolean collisionError = false;
		//unten links
		if(newPosition[0] > pos[0] && newPosition[1] > pos[0]){
			for(int x = (int) pos[0]; x<=newPosition[0]; x++){ //kleiner gleich damit wand dabei
				for(int y = (int) pos[1]; y<=newPosition[1]; y++){
					if(winkel[x][y]!=null){
						Rectangle2D rect = new Rectangle2D.Double(x, y, 1, 1);
						if(rect.intersectsLine(collisionRoute)){
							distance = collisionRoute.ptLineDist(x, y);
							collisionError = true;
							//System.out.println(distance);
							break;
						}
					}
				}
			}
			//oben rechts
		} else if (newPosition[0] > pos[0] && newPosition[1] < pos[1]) {
			for(int x = (int) pos[0]; x<=newPosition[0] - 2; x++){ //-2 da sonst rechte wand out of bounds
				for(int y = (int) pos[1]; y>=newPosition[1]; y--){
					if(winkel[x][y]!=null){
						//distance = Math.sqrt(Math.pow(x - pos[0], 2) + Math.pow(y - pos[1], 2));
						Rectangle2D rect = new Rectangle2D.Double(x, y, 1, 1);
						if(rect.intersectsLine(collisionRoute)){
							distance = collisionRoute.ptLineDist(x, y);
							collisionError = true;
							break;
						}
					}
				}
			}
		}
		//oben links
		else if (newPosition[0] < pos[0] && newPosition[1] < pos[1]) {
			for(int x = (int) pos[0]; x>=newPosition[0]; x--){
				for(int y = (int) pos[1]; y>=newPosition[1]; y--){
					if(winkel[x][y]!=null){
						Rectangle2D rect = new Rectangle2D.Double(x, y, 1, 1);
						if(rect.intersectsLine(collisionRoute)){
							distance = collisionRoute.ptLineDist(x, y);
							collisionError = true;
							break;
						}
					}
				}
			}
		}else if (newPosition[0] < pos[0] && newPosition[1] > pos[1]) {
			for(int x = (int) pos[0]; x>=newPosition[0]; x--){
				for(int y = (int) pos[1]; y<=newPosition[1]; y++){
					if(winkel[x][y]!=null){
						Rectangle2D rect = new Rectangle2D.Double(x, y, 1, 1);
						if(rect.intersectsLine(collisionRoute)){
							distance = collisionRoute.ptLineDist(x, y);
							collisionError = true;
							break;
						}

					}
				}
			}
		}
		if (distance < 0.09 && distance > 0) System.out.println(distance);

/*
		if(flag){
			vel = Vektorrechnung.normalize(vel);
			vel[0] = vel[0] * (distance / 1000);
			vel[1] = vel[1] * (distance / 1000);
			pos[0] = last_pos[0] + vel[0];
			pos[1] = last_pos[1] + vel[1];
		}else{
			pos[0] = pos[0] + vel[0];
			pos[1] = pos[1] + vel[1];
		}

		//lastCount++;
		//if(lastCount == 20){
			last_pos[0] = pos[0];
			last_pos[1] = pos[1];
			//lastCount = 0;
		//}
		*/


		if(flag){
				vel = Vektorrechnung.normalize(vel);
				vel[0] = vel[0] / 100000;
				vel[1] = vel[1] / 100000;
				pos[0] = last_pos[0] + vel[0];
				pos[1] = last_pos[1] + vel[1];
		}else{
			pos[0] = pos[0] + vel[0];
			pos[1] = pos[1] + vel[1];
		}
		if (last_pos_list.size() == 20) {
			Double[] temp = last_pos_list.poll();
			last_pos[0] = temp[0];
			last_pos[1] = temp[1];
			last_vel[0] = temp[2];
			last_vel[1] = temp[3];
		}
		last_pos_list.add(new Double[] {pos[0], pos[1], vel[0], vel[1]});
		/*
		lastCount++;
		if(lastCount == 20){ //20
			last_pos[0] = pos[0];
			last_pos[1] = pos[1];
			last_vel[0] = velCopy[0];
			last_vel[1] = velCopy[1];
			lastCount = 0;
		}
		*/




		/*
		if(flag){
			if(distance != 0.0){
				vel = Vektorrechnung.normalize(vel);
				vel[0] = vel[0] * (distance - distance / 2);
				vel[1] = vel[1] * (distance - distance / 2);
				pos[0] = last_pos[0] + vel[0];
				pos[1] = last_pos[1] + vel[1];

			}else{
				pos[0] = pos[0] + vel[0];
				pos[1] = pos[1] + vel[1];
			}
		}else{
			pos[0] = pos[0] + vel[0];
			pos[1] = pos[1] + vel[1];
		}

		 */

		//System.out.println(pos[0] + " : " + pos[1]);
		position_Umgebung_anpassen_Box();
	}
	private Double angleInRect(int x1,int y1, int x2,int y2){
		int dx=4;
		int dy=4;

		int startX=0;
		int startY=0;
		if(x1<x2){
			startX=x1;
		}else{
			startX=x2;
		}
		if(y1<y2){
			startY=y1;
		}else{
			startY=y2;
		}
		for(int x = startX; x < startX + dx && x < winkel.length; x++) { //+dx

			for (int y = startY; y < startY + dy && y < winkel[x].length; y++) { //+dy
				if(winkel[x][y]!=null){
					return winkel[x][y];
				}
			}
		}
		return null;
	}

	public void position_Umgebung_anpassen_Box() {
		if (pos[0] < 10) {
			vel[0] = Math.abs(vel[0]);
			pos[0] = pos[0] + vel[0];
		}
		if (pos[0] > Canvas.width * Simulation.pix) {
			vel[0] = -Math.abs(vel[0]);
			pos[0] = pos[0] + vel[0];
		}
		if (pos[1] < 10) {
			vel[1] = Math.abs(vel[1]);
			pos[1] = pos[1] + vel[1];
		}
		if (pos[1] > Canvas.height * Simulation.pix) {
			vel[1] = -Math.abs(vel[1]);
			pos[1] = pos[1] + vel[1];
		}
	}
	

	double[] folgen(ArrayList<Vehicle> all) {
		double[] pos_dest = new double[2];
		double[] vel_dest = new double[2];
		double[] acc_dest = new double[2];
		acc_dest[0] = 0;
		acc_dest[1] = 0;
		Vehicle v = null;

		if (type == 0) {
			for (int i = 0; i < all.size(); i++) {
				v = all.get(i);
				if (v.type == 1)
					break;
			}
			double dist = Math.sqrt(Math.pow(v.pos[0] - this.pos[0], 2) + Math.pow(v.pos[1] - this.pos[1], 2));

			if (dist < rad_zus && inFront(v)) {
				double[] pkt = new double[2];
				double[] ort1 = new double[2];
				double[] ort2 = new double[2];
				double[] ort3 = new double[2];
				pkt[0] = pos[0];
				pkt[1] = pos[1];
				ort1[0] = v.pos[0];
				ort1[1] = v.pos[1];
				ort2[0] = v.pos[0] + (rad_zus * v.vel[0]);
				ort2[1] = v.pos[1] + (rad_zus * v.vel[1]);
				ort3 = Vektorrechnung.punktVektorMINAbstand_punkt(pkt, ort1, ort2);

				vel_dest[0] = pos[0] - ort3[0];// UUU
				vel_dest[1] = pos[1] - ort3[1];// III

				vel_dest = Vektorrechnung.normalize(vel_dest);
				vel_dest[0] = vel_dest[0] * max_vel;
				vel_dest[1] = vel_dest[1] * max_vel;

				acc_dest[0] = vel_dest[0] - vel[0];
				acc_dest[1] = vel_dest[1] - vel[1];
			} else if (dist < rad_zus && !inFront(v)) {
				pos_dest[0] = v.pos[0] + v.vel[0];
				pos_dest[1] = v.pos[1] + v.vel[0];
				vel_dest[0] = pos_dest[0] - pos[0];
				vel_dest[1] = pos_dest[1] - pos[1];
				vel_dest = Vektorrechnung.normalize(vel_dest);
				vel_dest[0] = vel_dest[0] * max_vel;
				vel_dest[1] = vel_dest[1] * max_vel;
				acc_dest[0] = vel_dest[0] - vel[0];
				acc_dest[1] = vel_dest[1] - vel[1];
			} else {
				acc_dest = zusammenbleiben(all);
			}
		}

		return acc_dest;
	}

	boolean inFront(Vehicle v) {
		//
		boolean erg = false;
		double[] tmp = new double[2];
		tmp[0] = pos[0] - v.pos[0];
		tmp[1] = pos[1] - v.pos[1];

		if (Vektorrechnung.winkel(tmp, v.vel) < Math.PI / 2)
			erg = true;
		else
			erg = false;

		return erg;
	}

	
}
